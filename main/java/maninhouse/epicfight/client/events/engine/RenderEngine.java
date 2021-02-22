package maninhouse.epicfight.client.events.engine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.capabilities.entity.mob.DrownedData;
import maninhouse.epicfight.capabilities.entity.mob.EvokerData;
import maninhouse.epicfight.capabilities.entity.mob.PiglinBruteData;
import maninhouse.epicfight.capabilities.entity.mob.PiglinData;
import maninhouse.epicfight.capabilities.entity.mob.PillagerData;
import maninhouse.epicfight.capabilities.entity.mob.SkeletonData;
import maninhouse.epicfight.capabilities.entity.mob.VindicatorData;
import maninhouse.epicfight.capabilities.entity.mob.WitchData;
import maninhouse.epicfight.capabilities.entity.mob.WitherSkeletonData;
import maninhouse.epicfight.capabilities.entity.mob.ZombieData;
import maninhouse.epicfight.capabilities.item.CapabilityItem;
import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninhouse.epicfight.client.gui.EntityIndicator;
import maninhouse.epicfight.client.gui.SkillBarGui;
import maninhouse.epicfight.client.input.ModKeys;
import maninhouse.epicfight.client.renderer.ModRenderTypes;
import maninhouse.epicfight.client.renderer.RenderAimHelper;
import maninhouse.epicfight.client.renderer.ShaderRenderer;
import maninhouse.epicfight.client.renderer.entity.ArmatureRenderer;
import maninhouse.epicfight.client.renderer.entity.CaveSpiderRenderer;
import maninhouse.epicfight.client.renderer.entity.CreeperRenderer;
import maninhouse.epicfight.client.renderer.entity.EndermanRenderer;
import maninhouse.epicfight.client.renderer.entity.HoglinRenderer;
import maninhouse.epicfight.client.renderer.entity.IronGolemRenderer;
import maninhouse.epicfight.client.renderer.entity.PlayerRenderer;
import maninhouse.epicfight.client.renderer.entity.RavagerRenderer;
import maninhouse.epicfight.client.renderer.entity.SimpleTexturBipedRenderer;
import maninhouse.epicfight.client.renderer.entity.SpiderRenderer;
import maninhouse.epicfight.client.renderer.entity.VexRenderer;
import maninhouse.epicfight.client.renderer.entity.ZombieVillagerRenderer;
import maninhouse.epicfight.client.renderer.item.RenderBow;
import maninhouse.epicfight.client.renderer.item.RenderCrossbow;
import maninhouse.epicfight.client.renderer.item.RenderElytra;
import maninhouse.epicfight.client.renderer.item.RenderHat;
import maninhouse.epicfight.client.renderer.item.RenderItemBase;
import maninhouse.epicfight.client.renderer.item.RenderKatana;
import maninhouse.epicfight.client.renderer.item.RenderShield;
import maninhouse.epicfight.client.renderer.item.RenderTrident;
import maninhouse.epicfight.item.KatanaItem;
import maninhouse.epicfight.item.ModItems;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.utils.game.Formulars;
import maninhouse.epicfight.utils.math.Vec3f;
import maninhouse.epicfight.utils.math.Vec4f;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@OnlyIn(Dist.CLIENT)
public class RenderEngine {
	private static final Vec3f AIMING_CORRECTION = new Vec3f(-1.5F, 0.0F, 1.25F);
	public static final ResourceLocation NULL_TEXTURE = new ResourceLocation(EpicFightMod.MODID, "textures/gui/null.png");
	public RenderAimHelper aimHelper;
	public SkillBarGui guiSkillBar = new SkillBarGui();
	private Minecraft minecraft;
	private VisibleMatrix4f projectionMatrix;
	private Map<EntityType<?>, ArmatureRenderer> entityRendererMap;
	private Map<Item, RenderItemBase> itemRendererMapByInstance;
	private Map<Class<? extends Item>, RenderItemBase> itemRendererMapByClass;
	private Field lightTextureFinder = ObfuscationReflectionHelper.findField(GameRenderer.class, "field_78513_d");
	private Field nativeImageFinder = ObfuscationReflectionHelper.findField(LightTexture.class, "field_205111_b");
	private Method setPositionFinder = ObfuscationReflectionHelper.findMethod(ActiveRenderInfo.class, "func_216775_b",
			double.class, double.class, double.class);
	private Field fixedBuffers = ObfuscationReflectionHelper.findField(RenderTypeBuffers.class, "field_228480_b_");
	
	private LightTexture lightTex;
	private NativeImage nativeImg;
	private boolean aiming;
	private int zoomOutTimer = 0;
	private int zoomCount;
	private int zoomMaxCount = 20;
	private float renderPartialTick;
	// public static ColliderOBB wantToDraw;

	public RenderEngine() {
		Events.renderEngine = this;
		RenderItemBase.renderEngine = this;
		ShaderRenderer.renderEngine = this;
		EntityIndicator.init();
		minecraft = Minecraft.getInstance();
		
		entityRendererMap = new HashMap<EntityType<?>, ArmatureRenderer>();
		itemRendererMapByInstance = new HashMap<Item, RenderItemBase>();
		itemRendererMapByClass = new HashMap<Class<? extends Item>, RenderItemBase>();
		projectionMatrix = new VisibleMatrix4f();
		
		try {
			lightTex = (LightTexture) lightTextureFinder.get(Minecraft.getInstance().gameRenderer);
			nativeImg = (NativeImage) nativeImageFinder.get(lightTex);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		SortedMap<RenderType, BufferBuilder> buffermap = null;
		
		try {
			buffermap = (SortedMap<RenderType, BufferBuilder>) fixedBuffers.get(Minecraft.getInstance().getRenderTypeBuffers());
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
		
		if(buffermap != null) {
			buffermap.put(ModRenderTypes.getEnchantedArmor(), new BufferBuilder(ModRenderTypes.getEnchantedArmor().getBufferSize()));
		}
	}

	public void buildRenderer() {
		entityRendererMap.put(EntityType.CREEPER, new CreeperRenderer());
		entityRendererMap.put(EntityType.ENDERMAN, new EndermanRenderer());
		entityRendererMap.put(EntityType.ZOMBIE, new SimpleTexturBipedRenderer<ZombieEntity, ZombieData<ZombieEntity>>(EpicFightMod.MODID + ":textures/entity/zombie.png"));
		entityRendererMap.put(EntityType.ZOMBIE_VILLAGER, new ZombieVillagerRenderer());
		entityRendererMap.put(EntityType.ZOMBIFIED_PIGLIN, new SimpleTexturBipedRenderer<ZombifiedPiglinEntity, ZombieData<ZombifiedPiglinEntity>>(EpicFightMod.MODID + ":textures/entity/zombified_piglin.png"));
		entityRendererMap.put(EntityType.HUSK, new SimpleTexturBipedRenderer<HuskEntity, ZombieData<HuskEntity>>(EpicFightMod.MODID + ":textures/entity/husk.png"));
		entityRendererMap.put(EntityType.SKELETON, new SimpleTexturBipedRenderer<SkeletonEntity, SkeletonData<SkeletonEntity>>("textures/entity/skeleton/skeleton.png"));
		entityRendererMap.put(EntityType.WITHER_SKELETON, new SimpleTexturBipedRenderer<WitherSkeletonEntity, WitherSkeletonData>("textures/entity/skeleton/wither_skeleton.png"));
		entityRendererMap.put(EntityType.STRAY, new SimpleTexturBipedRenderer<StrayEntity, SkeletonData<StrayEntity>>("textures/entity/skeleton/stray.png"));
		entityRendererMap.put(EntityType.PLAYER, new PlayerRenderer());
		entityRendererMap.put(EntityType.SPIDER, new SpiderRenderer());
		entityRendererMap.put(EntityType.CAVE_SPIDER, new CaveSpiderRenderer());
		entityRendererMap.put(EntityType.IRON_GOLEM, new IronGolemRenderer());
		entityRendererMap.put(EntityType.VINDICATOR, new SimpleTexturBipedRenderer<VindicatorEntity, VindicatorData>("textures/entity/illager/vindicator.png"));
		entityRendererMap.put(EntityType.EVOKER, new SimpleTexturBipedRenderer<EvokerEntity, EvokerData>("textures/entity/illager/evoker.png"));
		entityRendererMap.put(EntityType.WITCH, new SimpleTexturBipedRenderer<WitchEntity, WitchData>(EpicFightMod.MODID + ":textures/entity/witch.png"));
		entityRendererMap.put(EntityType.DROWNED, new SimpleTexturBipedRenderer<DrownedEntity, DrownedData>(EpicFightMod.MODID + ":textures/entity/drowned.png"));
		entityRendererMap.put(EntityType.PILLAGER, new SimpleTexturBipedRenderer<PillagerEntity, PillagerData>("textures/entity/illager/pillager.png"));
		entityRendererMap.put(EntityType.RAVAGER, new RavagerRenderer());
		entityRendererMap.put(EntityType.VEX, new VexRenderer());
		entityRendererMap.put(EntityType.PIGLIN, new SimpleTexturBipedRenderer<PiglinEntity, PiglinData>("textures/entity/piglin/piglin.png"));
		entityRendererMap.put(EntityType.field_242287_aj, new SimpleTexturBipedRenderer<PiglinBruteEntity, PiglinBruteData>(EpicFightMod.MODID + ":textures/entity/piglin_brute.png"));
		entityRendererMap.put(EntityType.HOGLIN, new HoglinRenderer("textures/entity/hoglin/hoglin.png"));
		entityRendererMap.put(EntityType.ZOGLIN, new HoglinRenderer("textures/entity/hoglin/zoglin.png"));
		
		RenderBow bowRenderer = new RenderBow();
		RenderCrossbow crossbowRenderer = new RenderCrossbow();
		RenderElytra elytraRenderer = new RenderElytra();
		RenderHat hatRenderer = new RenderHat();
		RenderKatana katanaRenderer = new RenderKatana();
		RenderShield shieldRenderer = new RenderShield();
		RenderTrident tridentRenderer = new RenderTrident();

		itemRendererMapByInstance.put(Items.AIR, new RenderItemBase());
		itemRendererMapByInstance.put(Items.BOW, bowRenderer);
		itemRendererMapByInstance.put(Items.SHIELD, shieldRenderer);
		itemRendererMapByInstance.put(Items.ELYTRA, elytraRenderer);
		itemRendererMapByInstance.put(Items.CREEPER_HEAD, hatRenderer);
		itemRendererMapByInstance.put(Items.DRAGON_HEAD, hatRenderer);
		itemRendererMapByInstance.put(Items.PLAYER_HEAD, hatRenderer);
		itemRendererMapByInstance.put(Items.ZOMBIE_HEAD, hatRenderer);
		itemRendererMapByInstance.put(Items.SKELETON_SKULL, hatRenderer);
		itemRendererMapByInstance.put(Items.WITHER_SKELETON_SKULL, hatRenderer);
		itemRendererMapByInstance.put(Items.CARVED_PUMPKIN, hatRenderer);
		itemRendererMapByInstance.put(Items.CROSSBOW, crossbowRenderer);
		itemRendererMapByInstance.put(Items.TRIDENT, tridentRenderer);
		itemRendererMapByInstance.put(ModItems.KATANA.get(), katanaRenderer);

		itemRendererMapByClass.put(BlockItem.class, hatRenderer);
		itemRendererMapByClass.put(BowItem.class, bowRenderer);
		itemRendererMapByClass.put(CrossbowItem.class, crossbowRenderer);
		itemRendererMapByClass.put(ElytraItem.class, elytraRenderer);
		itemRendererMapByClass.put(ShieldItem.class, shieldRenderer);
		itemRendererMapByClass.put(TridentItem.class, tridentRenderer);
		itemRendererMapByClass.put(KatanaItem.class, katanaRenderer);
		aimHelper = new RenderAimHelper();
	}

	public RenderItemBase getItemRenderer(Item item) {
		RenderItemBase renderItem = itemRendererMapByInstance.get(item);
		if (renderItem == null) {
			renderItem = this.findMatchingRendererByClass(item.getClass());
			if (renderItem == null) {
				renderItem = itemRendererMapByInstance.get(Items.AIR);
			}
			this.itemRendererMapByInstance.put(item, renderItem);
		}

		return renderItem;
	}

	private RenderItemBase findMatchingRendererByClass(Class<?> clazz) {
		RenderItemBase renderer = null;
		for (; clazz != null && renderer == null; clazz = clazz.getSuperclass())
			renderer = itemRendererMapByClass.getOrDefault(clazz, null);

		return renderer;
	}
	
	public void renderEntityArmatureModel(LivingEntity livingEntity, LivingData<?> entitydata, EntityRenderer<? extends Entity> renderer, IRenderTypeBuffer buffer, MatrixStack matStack, int packedLightIn, float partialTicks) {
		this.entityRendererMap.get(livingEntity.getType()).render(livingEntity, entitydata, renderer, buffer, matStack, packedLightIn, partialTicks);
	}

	public boolean isEntityContained(Entity entity) {
		return this.entityRendererMap.containsKey(entity.getType());
	}

	public void zoomIn() {
		aiming = true;
		zoomCount = zoomCount == 0 ? 1 : zoomCount;
		zoomOutTimer = 0;
	}

	public void zoomOut(int timer) {
		aiming = false;
		zoomOutTimer = timer;
	}

	public float getPartialTicks() {
		return renderPartialTick;
	}

	private void updateCameraInfo(ActiveRenderInfo info, PointOfView pov, float partialTicks) {
		Entity entity = minecraft.getRenderViewEntity();
		double posX = info.getProjectedView().x;
		double posY = info.getProjectedView().y;
		double posZ = info.getProjectedView().z;
		double entityPosX = entity.lastTickPosX + (entity.getPosX() - entity.lastTickPosX) * partialTicks;
		double entityPosY = entity.lastTickPosY + (entity.getPosY() - entity.lastTickPosY) * partialTicks
				+ entity.getEyeHeight();
		double entityPosZ = entity.lastTickPosZ + (entity.getPosZ() - entity.lastTickPosZ) * partialTicks;
		float intpol = pov == PointOfView.THIRD_PERSON_BACK ? ((float) zoomCount / (float) zoomMaxCount) : 0;
		Vec3f interpolatedCorrection = new Vec3f(AIMING_CORRECTION.x * intpol, AIMING_CORRECTION.y * intpol,
				AIMING_CORRECTION.z * intpol);
		VisibleMatrix4f rotationMatrix = ClientEngine.INSTANCE.getPlayerData().getMatrix(partialTicks);

		if (pov == PointOfView.THIRD_PERSON_FRONT)
			VisibleMatrix4f.rotate((float) Math.toRadians(180.0D), new Vec3f(0F, 1F, 0F), rotationMatrix,
					rotationMatrix);

		Vec4f rotateVec = VisibleMatrix4f.transform(rotationMatrix,
				new Vec4f(interpolatedCorrection.x, interpolatedCorrection.y, interpolatedCorrection.z, 1.0F), null);
		double d3 = Math.sqrt((rotateVec.x * rotateVec.x) + (rotateVec.y * rotateVec.y) + (rotateVec.z * rotateVec.z));
		double smallest = d3;
		double d00 = posX + rotateVec.x;
		double d11 = posY - rotateVec.y;
		double d22 = posZ + rotateVec.z;

		for (int i = 0; i < 8; ++i) {
			float f = (float) ((i & 1) * 2 - 1);
			float f1 = (float) ((i >> 1 & 1) * 2 - 1);
			float f2 = (float) ((i >> 2 & 1) * 2 - 1);
			f = f * 0.1F;
			f1 = f1 * 0.1F;
			f2 = f2 * 0.1F;
			RayTraceResult raytraceresult = minecraft.world
					.rayTraceBlocks(new RayTraceContext(new Vector3d(entityPosX + f, entityPosY + f1, entityPosZ + f2),
							new Vector3d(d00 + f + f2, d11 + f1, d22 + f2), RayTraceContext.BlockMode.COLLIDER,
							RayTraceContext.FluidMode.NONE, entity));
			if (raytraceresult != null) {
				double d7 = raytraceresult.getHitVec().distanceTo(new Vector3d(entityPosX, entityPosY, entityPosZ));
				if (d7 < smallest)
					smallest = d7;
			}
		}

		Vector3d vector = info.getProjectedView();
		float dist = d3 == 0 ? 0 : (float) (smallest / d3);

		double totalX = vector.getX() + rotateVec.x * dist;
		double totalY = vector.getY() - rotateVec.y * dist;
		double totalZ = vector.getZ() + rotateVec.z * dist;

		try {
			setPositionFinder.invoke(info, totalX, totalY, totalZ);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		FloatBuffer fb = GLAllocation.createDirectFloatBuffer(16);
		GL11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, fb);
		this.projectionMatrix.load(fb.asReadOnlyBuffer());
	}

	public VisibleMatrix4f getCurrentProjectionMatrix() {
		return this.projectionMatrix;
	}

	public Vec3f getTotalLight(int sky, int block) {
		int color = this.nativeImg.getPixelRGBA(sky, block);
		Vec4f color4f = convertColorToVector(color, false);
		return new Vec3f(color4f.x, color4f.y, color4f.z);
	}

	public Vec4f convertColorToVector(int color, boolean reverse) {
		float r = (color & 255) / 255.0F;
		color >>= 8;
		float g = (color & 255) / 255.0F;
		color >>= 8;
		float b = (color & 255) / 255.0F;

		if (reverse)
			return new Vec4f(b, g, r, 1);
		else
			return new Vec4f(r, g, b, 1);
	}

	@Mod.EventBusSubscriber(modid = EpicFightMod.MODID, value = Dist.CLIENT)
	public static class Events {
		static RenderEngine renderEngine;

		@SubscribeEvent
		public static void renderLivingEvent(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
			LivingEntity livingentity = event.getEntity();
			
			if (renderEngine.isEntityContained(livingentity)) {
				if (livingentity instanceof ClientPlayerEntity && event.getPartialRenderTick() == 1.0F) {
					return;
				}
				LivingData<?> entitydata = (LivingData<?>) livingentity.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);

				if (entitydata != null) {
					event.setCanceled(true);
					renderEngine.renderEntityArmatureModel(livingentity, entitydata, event.getRenderer(), event.getBuffers(), event.getMatrixStack(), event.getLight(), event.getPartialRenderTick());
				}
			}
			
			if (!Minecraft.getInstance().gameSettings.hideGUI) {
				for (EntityIndicator entityIndicator : EntityIndicator.ENTITY_INDICATOR_RENDERERS) {
					if (entityIndicator.shouldDraw(event.getEntity())) {
						entityIndicator.drawIndicator(event.getEntity(), event.getMatrixStack(), event.getBuffers());
					}
				}
			}
			
			/**
			 * if(wantToDraw != null) { if(wantToDraw instanceof ColliderOBB) {
			 * renderEngine.obb.doRender((ColliderOBB)wantToDraw); } }
			 **/
		}

		@SubscribeEvent
		public static void itemTooltip(ItemTooltipEvent event) {
			if (event.getPlayer() != null) {
				CapabilityItem cap = ModCapabilities.stackCapabilityGetter(event.getItemStack());
				
				if (cap != null) {
					if (ClientEngine.INSTANCE.inputController.isKeyDown(ModKeys.SPECIAL_ATTACK_TOOLTIP)) {
						if (cap.getSpecialAttack(ItemStack.EMPTY) != null) {
							event.getToolTip().clear();

							for (ITextComponent s : cap.getSpecialAttack(Minecraft.getInstance().player.getHeldItemOffhand()).getTooltip()) {
								event.getToolTip().add(s);
							}
						}
					} else {
						List<ITextComponent> tooltip = event.getToolTip();
						cap.modifyItemTooltip(event.getToolTip(), event.getPlayer().getHeldItemOffhand().isEmpty());

						for (int i = 0; i < tooltip.size(); i++) {
							if (tooltip.get(i).getString().contains("Attack Speed")) {
								ClientPlayerData entityCap = (ClientPlayerData) event.getPlayer().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
								double weaponSpeed = entityCap.getOriginalEntity().getAttribute(Attributes.ATTACK_SPEED).getBaseValue();
								
								for (AttributeModifier modifier : event.getItemStack().getAttributeModifiers(EquipmentSlotType.MAINHAND).get(Attributes.ATTACK_SPEED)) {
									weaponSpeed += modifier.getAmount();
								}
								
								double attackSpeedPanelty = Formulars.getAttackSpeedPanelty(entityCap.getWeight(), weaponSpeed);
								weaponSpeed += attackSpeedPanelty;
								tooltip.remove(i);
								tooltip.add(i, new StringTextComponent(String.format(" %.2f Attack Speed", weaponSpeed)));
							}
						}
					}
				}
			}
		}

		@SubscribeEvent
		public static void cameraSetupEvent(CameraSetup event) {
			renderEngine.renderPartialTick = (float) event.getRenderPartialTicks();
			renderEngine.updateCameraInfo(event.getInfo(), Minecraft.getInstance().gameSettings.getPointOfView(), renderEngine.renderPartialTick);

			if (renderEngine.zoomCount > 0) {
				if (renderEngine.zoomOutTimer > 0) {
					renderEngine.zoomOutTimer--;
				} else {
					renderEngine.zoomCount = renderEngine.aiming ? renderEngine.zoomCount + 1 : renderEngine.zoomCount - 1;
				}
				
				renderEngine.zoomCount = Math.min(renderEngine.zoomMaxCount, renderEngine.zoomCount);
			}
		}

		@SubscribeEvent
		public static void fogEvent(RenderFogEvent event) {
			/**
			* GlStateManager.fogMode(FogMode.LINEAR); GlStateManager.fogStart(0.0F);
			* GlStateManager.fogEnd(75.0F); GlStateManager.fogDensity(0.1F);
			 **/							 
		}

		@SubscribeEvent
		public static void renderGameOverlay(RenderGameOverlayEvent.Pre event) {
			if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
				ClientPlayerData playerdata = ClientEngine.INSTANCE.getPlayerData();

				if (playerdata != null) {
					if (Minecraft.isGuiEnabled()) {
						renderEngine.guiSkillBar.renderGui(playerdata);
					}
				}
			}
		}

		@SubscribeEvent
		public static void renderWorldLast(RenderWorldLastEvent event) {
			if (renderEngine.zoomCount > 0 && renderEngine.minecraft.gameSettings.getPointOfView() == PointOfView.THIRD_PERSON_BACK) {
				renderEngine.aimHelper.doRender(event.getMatrixStack(), event.getPartialTicks());
			}
		}
	}
}