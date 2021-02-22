package maninhouse.epicfight.client.renderer.layer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.capabilities.item.ArmorCapability;
import maninhouse.epicfight.capabilities.item.VanillaArmorCapability;
import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.client.model.ClientModel;
import maninhouse.epicfight.client.renderer.ModRenderTypes;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@SuppressWarnings("unchecked")
@OnlyIn(Dist.CLIENT)
public class WearableItemLayer<E extends LivingEntity, T extends LivingData<E>> extends Layer<E, T> {
	private static Map<String, ResourceLocation> ARMOR_MAP;
	private static Map<ResourceLocation, ClientModel> ARMOR_MODEL_MAP;

	static {
		Field fld = ObfuscationReflectionHelper.findField(BipedArmorLayer.class, "field_177191_j");
		fld.setAccessible(true);

		try {
			ARMOR_MAP = (Map<String, ResourceLocation>) fld.get(null);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		ARMOR_MODEL_MAP = new HashMap<ResourceLocation, ClientModel>();
	}
	
	private EquipmentSlotType slot;
	
	public WearableItemLayer(EquipmentSlotType slotType) {
		this.slot = slotType;
	}
	
	private void renderArmor(MatrixStack matStack, IRenderTypeBuffer buf, int packedLightIn, boolean hasEffect,
			ClientModel model, float r, float g, float b, ResourceLocation armorResource, VisibleMatrix4f[] poses) {
		IVertexBuilder ivertexbuilder = ModRenderTypes.getArmorVertexBuilder(buf, ModRenderTypes.getAnimatedArmorModel(armorResource), hasEffect);
		model.draw(matStack, ivertexbuilder, packedLightIn, r, g, b, 1.0F, poses);
	}
	
	@Override
	public void renderLayer(T entitydata, E entityliving, MatrixStack matrixStackIn, IRenderTypeBuffer buffer, int packedLightIn, VisibleMatrix4f[] poses) {
		ItemStack stack = entityliving.getItemStackFromSlot(this.slot);
		Item item = stack.getItem();
		
		matrixStackIn.push();
		if(this.slot == EquipmentSlotType.HEAD && entityliving instanceof ZombieVillagerEntity) {
			matrixStackIn.translate(0.0D, 0.1D, 0.0D);
		}
		
		if (item instanceof ArmorItem) {
			ClientModel model;
			ArmorItem armorItem = (ArmorItem) stack.getItem();

			if (ARMOR_MODEL_MAP.containsKey(armorItem.getRegistryName())) {
				model = ARMOR_MODEL_MAP.get(armorItem.getRegistryName());
			} else {
				VanillaArmorCapability cap = (VanillaArmorCapability) stack
						.getCapability(ModCapabilities.CAPABILITY_ITEM, null).orElse(null);
				if (cap == null) {
					model = ArmorCapability.getBipedArmorModel(armorItem.getEquipmentSlot(), entityliving.isChild());
				} else {
					model = cap.getEquipModel(armorItem.getEquipmentSlot(), entityliving.isChild());
				}
				
				if (model != null) {
					ARMOR_MODEL_MAP.put(armorItem.getRegistryName(), model);
				}
			}
			
			boolean hasEffect = stack.hasEffect();
			if (armorItem instanceof IDyeableArmorItem) {
				int i = ((IDyeableArmorItem) armorItem).getColor(stack);
				float r = (float) (i >> 16 & 255) / 255.0F;
				float g = (float) (i >> 8 & 255) / 255.0F;
				float b = (float) (i & 255) / 255.0F;
				this.renderArmor(matrixStackIn, buffer, packedLightIn, hasEffect, model, r, g, b,
						this.getArmorTexture(stack, entityliving, armorItem.getEquipmentSlot(), null), poses);
				this.renderArmor(matrixStackIn, buffer, packedLightIn, hasEffect, model, 1.0F, 1.0F, 1.0F,
						this.getArmorTexture(stack, entityliving, armorItem.getEquipmentSlot(), "overlay"), poses);
			} else {
				this.renderArmor(matrixStackIn, buffer, packedLightIn, hasEffect, model, 1.0F, 1.0F, 1.0F,
						this.getArmorTexture(stack, entityliving, armorItem.getEquipmentSlot(), null), poses);
			}
		} else {
			if (item != Items.AIR) {
				ClientEngine.INSTANCE.renderEngine.getItemRenderer(stack.getItem()).renderItemOnHead(stack, entitydata,
						buffer, matrixStackIn, packedLightIn);
			}
		}
		
		matrixStackIn.pop();
	}

	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		ArmorItem item = (ArmorItem) stack.getItem();
		String texture = item.getArmorMaterial().getName();
		String domain = "minecraft";
		int idx = texture.indexOf(':');

		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}

		String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture,
				(slot == EquipmentSlotType.LEGS ? 2 : 1), type == null ? "" : String.format("_%s", type));
		s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = (ResourceLocation) ARMOR_MAP.get(s1);
		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			ARMOR_MAP.put(s1, resourcelocation);
		}

		return resourcelocation;
	}
}