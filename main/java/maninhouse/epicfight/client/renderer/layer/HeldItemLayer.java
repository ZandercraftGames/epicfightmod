package maninhouse.epicfight.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.capabilities.item.CapabilityItem;
import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.client.events.engine.RenderEngine;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HeldItemLayer<E extends LivingEntity, T extends LivingData<E>> extends Layer<E, T> {
	@Override
	public void renderLayer(T entitydata, E entityliving, MatrixStack matrixStackIn, IRenderTypeBuffer buffer, int packedLightIn, VisibleMatrix4f[] poses) {
		ItemStack mainHandStack = entitydata.getOriginalEntity().getHeldItemMainhand();
		RenderEngine renderEngine = ClientEngine.INSTANCE.renderEngine;
		matrixStackIn.push();
		
		if (mainHandStack.getItem() != Items.AIR) {
			if (entitydata.getOriginalEntity().getRidingEntity() != null) {
				CapabilityItem itemCap = entitydata.getHeldItemCapability(Hand.MAIN_HAND);
				if (itemCap != null && !itemCap.canUseOnMount()) {
					renderEngine.getItemRenderer(mainHandStack.getItem()).renderItemBack(mainHandStack, entitydata, buffer, matrixStackIn, packedLightIn);
					return;
				}
			}
			renderEngine.getItemRenderer(mainHandStack.getItem()).renderItemInHand(mainHandStack, entitydata, Hand.MAIN_HAND, buffer, matrixStackIn, packedLightIn);
		}
		matrixStackIn.pop();
		matrixStackIn.push();
		ItemStack offHandStack = entitydata.getOriginalEntity().getHeldItemOffhand();
		
		if (offHandStack.getItem() != Items.AIR) {
			CapabilityItem cap = entitydata.getHeldItemCapability(Hand.MAIN_HAND);
			if (cap != null) {
				if (cap.canCompatibleWith(offHandStack)) {
					renderEngine.getItemRenderer(offHandStack.getItem()).renderItemInHand(offHandStack, entitydata, Hand.OFF_HAND, buffer, matrixStackIn, packedLightIn);
				}
			} else {
				renderEngine.getItemRenderer(offHandStack.getItem()).renderItemInHand(offHandStack, entitydata, Hand.OFF_HAND, buffer, matrixStackIn, packedLightIn);
			}
		}
		matrixStackIn.pop();
	}
}