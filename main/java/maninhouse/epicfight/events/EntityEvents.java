package maninhouse.epicfight.events;

import java.util.List;

import com.google.common.collect.Lists;

import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.entity.CapabilityEntity;
import maninhouse.epicfight.capabilities.entity.IRangedAttackMobCapability;
import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.capabilities.entity.mob.BipedMobData;
import maninhouse.epicfight.capabilities.entity.mob.EndermanData;
import maninhouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninhouse.epicfight.capabilities.item.CapabilityItem;
import maninhouse.epicfight.effects.ModEffects;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.network.ModNetworkManager;
import maninhouse.epicfight.network.client.CTSPlayAnimation;
import maninhouse.epicfight.network.server.STCPlayAnimation;
import maninhouse.epicfight.network.server.STCPotion;
import maninhouse.epicfight.network.server.STCPotion.Action;
import maninhouse.epicfight.utils.game.IExtendedDamageSource;
import maninhouse.epicfight.utils.game.IExtendedDamageSource.StunType;
import maninhouse.epicfight.utils.game.IndirectDamageSourceExtended;
import net.minecraft.command.arguments.EntityAnchorArgument.Type;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.CombatRules;
import net.minecraft.util.Hand;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=EpicFightMod.MODID)
public class EntityEvents
{
	private static List<CapabilityEntity<?>> unInitializedEntitiesClient = Lists.<CapabilityEntity<?>>newArrayList();
	private static List<CapabilityEntity<?>> unInitializedEntitiesServer = Lists.<CapabilityEntity<?>>newArrayList();
	
	@SubscribeEvent
	public static void spawnEvent(EntityJoinWorldEvent event)
	{
		CapabilityEntity entitydata = event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		if(entitydata != null && event.getEntity().ticksExisted == 0)
		{
			entitydata.setEntity(event.getEntity());
			entitydata.init();
			if(entitydata.isRemote())
				unInitializedEntitiesClient.add(entitydata);
			else
				unInitializedEntitiesServer.add(entitydata);
		}
	}
	
	@SubscribeEvent
	public static void updateEvent(LivingUpdateEvent event)
	{
		LivingData<?> entitydata = (LivingData<?>) event.getEntityLiving().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		if(entitydata != null && entitydata.getOriginalEntity() != null)
		{
			entitydata.update();
		}
	}
	
	@SubscribeEvent
	public static void knockBackEvent(LivingKnockBackEvent event)
	{
		event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void hurtEvent(LivingHurtEvent event)
	{
		IExtendedDamageSource extSource = null;
		Entity trueSource = event.getSource().getTrueSource();
		
		if(trueSource != null)
		{
			if(event.getSource() instanceof IExtendedDamageSource)
			{
				extSource = (IExtendedDamageSource) event.getSource();
			}
			else if(event.getSource() instanceof IndirectEntityDamageSource)
			{
				CapabilityEntity<?> attackerdata = trueSource.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
				
				if(attackerdata != null)
				{
					if(attackerdata instanceof IRangedAttackMobCapability)
					{
						extSource = ((IRangedAttackMobCapability)attackerdata).getRangedDamageSource(event.getSource().getImmediateSource());
					}
					else if(event.getSource().damageType == "arrow")
					{
						extSource = new IndirectDamageSourceExtended("arrow", trueSource, event.getSource().getImmediateSource(), StunType.SHORT);
					}
				}
			}
			
			if(extSource != null)
			{
				float totalDamage = event.getAmount();
				float ignoreDamage = event.getAmount() * extSource.getArmorIgnoreRatio();
				float calculatedDamage = ignoreDamage;
				
				LivingEntity hitEntity = event.getEntityLiving();
				
			    if(hitEntity.isPotionActive(Effects.RESISTANCE))
			    {
			    	int i = (hitEntity.getActivePotionEffect(Effects.RESISTANCE).getAmplifier() + 1) * 5;
			        int j = 25 - i;
			        float f = calculatedDamage * (float)j;
			        float f1 = calculatedDamage;
			        calculatedDamage = Math.max(f / 25.0F, 0.0F);
			        float f2 = f1 - calculatedDamage;
			        if (f2 > 0.0F && f2 < 3.4028235E37F)
			        {
			        	if (hitEntity instanceof ServerPlayerEntity)
			        		((ServerPlayerEntity)hitEntity).addStat(Stats.DAMAGE_RESISTED, Math.round(f2 * 10.0F));
			        	else if(event.getSource().getTrueSource() instanceof ServerPlayerEntity)
			                ((ServerPlayerEntity)event.getSource().getTrueSource()).addStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f2 * 10.0F));
			        }
			    }
			    if(calculatedDamage > 0.0F)
			    {
			    	int k = EnchantmentHelper.getEnchantmentModifierDamage(hitEntity.getArmorInventoryList(), event.getSource());
			        if(k > 0)
			        	calculatedDamage = CombatRules.getDamageAfterMagicAbsorb(calculatedDamage, (float)k);
			    }
			    
			    float absorpAmount = hitEntity.getAbsorptionAmount() - calculatedDamage;
			    hitEntity.setAbsorptionAmount(Math.max(absorpAmount, 0.0F));
		        float realHealthDamage = Math.max(-absorpAmount, 0.0F);
		        if (realHealthDamage > 0.0F && realHealthDamage < 3.4028235E37F && event.getSource().getTrueSource() instanceof ServerPlayerEntity)
		        	((ServerPlayerEntity)event.getSource().getTrueSource()).addStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(realHealthDamage * 10.0F));
		        
		        if(absorpAmount < 0.0F)
		        {
		        	hitEntity.setHealth(hitEntity.getHealth() + absorpAmount);
		        	LivingData<?> attacker = (LivingData<?>)trueSource.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
					if(attacker != null)
						attacker.gatherDamageDealt((IExtendedDamageSource)event.getSource(), calculatedDamage);
		        }
		        
				event.setAmount(totalDamage - ignoreDamage);
				
				if(event.getAmount() > 0.0F)
				{
					LivingData<?> hitEntityData = (LivingData<?>)hitEntity.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
					
					if(hitEntityData != null)
					{
						StaticAnimation hitAnimation = null;
						float extendStunTime = 0;
						float knockBackAmount = 0;
						float weightReduction = 40.0F / (float)hitEntityData.getWeight();
						
						switch(extSource.getStunType())
						{
						case SHORT:
							if(!hitEntity.isPotionActive(ModEffects.STUN_IMMUNITY) && (hitEntityData.getStunResistance() == 0 || extSource.isSureStrike()))
							{
								int i = EnchantmentHelper.getKnockbackModifier((LivingEntity)trueSource);
								float totalStunTime = (float) ((0.25F + extSource.getImpact() * 0.1F + 0.1F * i) * weightReduction);
								if(!extSource.isSureStrike())
									totalStunTime *= (1.0F - hitEntityData.getStunTimeTimeReduction());
								
								if(totalStunTime >= 0.2F)
								{
									extendStunTime = totalStunTime - 0.1F;
									boolean flag = totalStunTime >= 0.83F;
									StunType stunType = flag ? StunType.LONG : StunType.SHORT;
									extendStunTime = flag ? 0 : extendStunTime;
									hitAnimation = hitEntityData.getHitAnimation(stunType);
									knockBackAmount = totalStunTime;
								}
							}
							break;
						case LONG:
							hitAnimation = hitEntity.isPotionActive(ModEffects.STUN_IMMUNITY) ? null : hitEntityData.getHitAnimation(StunType.LONG);
							knockBackAmount = (extSource.getImpact() * 0.25F) * weightReduction;
							break;
						case HOLD:
							hitAnimation = hitEntityData.getHitAnimation(StunType.SHORT);
							extendStunTime = extSource.getImpact() * 0.1F;
							break;
						}
						
						if(hitAnimation != null)
						{
							if(!(hitEntity instanceof PlayerEntity))
								hitEntity.lookAt(Type.FEET, trueSource.getPositionVec());
							hitEntityData.setStunTimeReduction();
							hitEntityData.getAnimator().playAnimation(hitAnimation, extendStunTime);
							ModNetworkManager.sendToAllPlayerTrackingThisEntity(new STCPlayAnimation(hitAnimation.getId(), hitEntity.getEntityId(), extendStunTime), hitEntity);
							if(hitEntity instanceof ServerPlayerEntity)
								ModNetworkManager.sendToPlayer(new STCPlayAnimation(hitAnimation.getId(), hitEntity.getEntityId(), extendStunTime), (ServerPlayerEntity)hitEntity);
						}
						
						hitEntityData.knockBackEntity(trueSource, knockBackAmount);
					}
				}
			}
		}
		
		if(event.getEntityLiving().isHandActive() && event.getEntityLiving().getActiveItemStack().getItem() == Items.SHIELD)
		{
			if(event.getEntityLiving() instanceof PlayerEntity)
			{
				event.getEntityLiving().world.playSound((PlayerEntity)event.getEntityLiving(), event.getEntityLiving().getPosition(), SoundEvents.ITEM_SHIELD_BLOCK,
						event.getEntityLiving().getSoundCategory(), 1.0F, 0.8F + event.getEntityLiving().getRNG().nextFloat() * 0.4F);
			}
		}
	}
	
	@SubscribeEvent
	public static void damageEvent(LivingDamageEvent event)
	{
		Entity trueSource = event.getSource().getTrueSource();
		if(event.getSource() instanceof IExtendedDamageSource)
		{
			if(trueSource != null)
			{
				LivingData<?> attacker = (LivingData<?>)trueSource.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
				if(attacker!=null)
					attacker.gatherDamageDealt((IExtendedDamageSource)event.getSource(), event.getAmount());
			}
		}
	}
	
	@SubscribeEvent
	public static void attackEvent(LivingAttackEvent event)
	{
		LivingData<?> entitydata = (LivingData<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		if(entitydata != null && !event.getEntity().world.isRemote && event.getEntityLiving().getHealth() > 0.0F)
		{
			if(!entitydata.attackEntityFrom(event.getSource(), event.getAmount()))
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public static void deathEvent(LivingDeathEvent event)
	{
		LivingData<?> entitydata = (LivingData<?>)event.getEntityLiving().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		if(entitydata != null)
			entitydata.getAnimator().playDeathAnimation();
	}
	
	@SubscribeEvent
	public static void arrowHitEvent(ProjectileImpactEvent.Arrow event)
	{
		if(event.getRayTraceResult() instanceof EntityRayTraceResult)
		{
			EntityRayTraceResult rayresult = ((EntityRayTraceResult)event.getRayTraceResult());
			
			if(rayresult.getEntity() != null && event.getArrow().func_234616_v_() != null)
			{
				if(rayresult.getEntity().equals(event.getArrow().func_234616_v_().getRidingEntity()))
					event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public static void equipChangeEvent(LivingEquipmentChangeEvent event)
	{
		if(event.getFrom().getItem() == event.getTo().getItem())
			return;
		
		LivingData<?> entitycap = (LivingData<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		if(entitycap != null && entitycap.getOriginalEntity() != null)
		{
			if(event.getSlot() == EquipmentSlotType.MAINHAND)
			{
				CapabilityItem fromCap = ModCapabilities.stackCapabilityGetter(event.getFrom());
				CapabilityItem toCap = ModCapabilities.stackCapabilityGetter(event.getTo());
				entitycap.cancelUsingItem();
				
				if(fromCap != null)
					event.getEntityLiving().getAttributeManager().removeModifiers(fromCap.getAttributeModifiers(event.getSlot(), entitycap));
				if(toCap != null)
					event.getEntityLiving().getAttributeManager().reapplyModifiers(toCap.getAttributeModifiers(event.getSlot(), entitycap));
				
				if(entitycap instanceof ServerPlayerData)
				{
					ServerPlayerData playercap = (ServerPlayerData)entitycap;
					playercap.onHeldItemChange(toCap, event.getTo(), Hand.MAIN_HAND);
				}
			}
			else if(event.getSlot() == EquipmentSlotType.OFFHAND)
			{
				entitycap.cancelUsingItem();
				
				if(entitycap instanceof ServerPlayerData)
				{
					ServerPlayerData playercap = (ServerPlayerData)entitycap;
					CapabilityItem toCap = event.getTo().isEmpty() ? null : entitycap.getHeldItemCapability(Hand.MAIN_HAND);
					playercap.onHeldItemChange(toCap, event.getTo(), Hand.OFF_HAND);
				}
			}
			else if(event.getSlot().getSlotType() == EquipmentSlotType.Group.ARMOR)
			{
				CapabilityItem fromCap = ModCapabilities.stackCapabilityGetter(event.getFrom());
				CapabilityItem toCap = ModCapabilities.stackCapabilityGetter(event.getTo());
				
				if(fromCap != null)
					event.getEntityLiving().getAttributeManager().removeModifiers(fromCap.getAttributeModifiers(event.getSlot(), entitycap));
				if(toCap != null)
					event.getEntityLiving().getAttributeManager().reapplyModifiers(toCap.getAttributeModifiers(event.getSlot(), entitycap));
				
				if(event.getEntityLiving() instanceof ServerPlayerEntity)
				{
					ServerPlayerData playercap = (ServerPlayerData) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
					playercap.onArmorSlotChanged(fromCap, toCap, event.getSlot());
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void effectAddEvent(PotionAddedEvent event)
	{
		if(!event.getEntity().world.isRemote)
			ModNetworkManager.sendToAll(new STCPotion(event.getPotionEffect().getPotion(), Action.Active, event.getEntity().getEntityId()));
	}
	
	@SubscribeEvent
	public static void effectRemoveEvent(PotionRemoveEvent event)
	{
		if(!event.getEntity().world.isRemote)
			ModNetworkManager.sendToAll(new STCPotion(event.getPotionEffect().getPotion(), Action.Remove, event.getEntity().getEntityId()));
	}
	
	@SubscribeEvent
	public static void effectExpiryEvent(PotionExpiryEvent event)
	{
		if(!event.getEntity().world.isRemote)
			ModNetworkManager.sendToAll(new STCPotion(event.getPotionEffect().getPotion(), Action.Remove, event.getEntity().getEntityId()));
	}
	
	@SubscribeEvent
	public static void mountEvent(EntityMountEvent event)
	{
		CapabilityEntity<?> mountEntity = event.getEntityMounting().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		if(!event.getWorldObj().isRemote && mountEntity instanceof BipedMobData && mountEntity.getOriginalEntity() != null)
		{
			if(event.getEntityBeingMounted() instanceof MobEntity)
			{
				((BipedMobData<?>)mountEntity).onMount(event.isMounting(), event.getEntityBeingMounted());
			}
		}
	}
	
	@SubscribeEvent
	public static void targetEvent(LivingSetAttackTargetEvent event)
	{
		if(event.getEntityLiving().equals(event.getTarget()))
		{
			event.getEntityLiving().setRevengeTarget(null);
			
			if(event.getEntityLiving() instanceof MobEntity)
			{
				((MobEntity)event.getEntityLiving()).setAttackTarget(null);
			}
		}
	}
	
	@SubscribeEvent
	public static void tpEvent(EnderTeleportEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		if(event.getEntityLiving() instanceof EndermanEntity)
		{
			EndermanEntity enderman = (EndermanEntity)entity;
			EndermanData endermandata = (EndermanData) enderman.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
			
			if(endermandata != null)
			{
				if(endermandata.isInaction())
				{
					for (Entity collideEntity : enderman.world.getEntitiesWithinAABB(Entity.class, enderman.getBoundingBox().grow(0.2D, 0.2D, 0.2D)))
	                {
	                    if (collideEntity instanceof ProjectileEntity)
	                    {
	                    	return;
	                    }
	                }
					
					event.setCanceled(true);
				}
				else if(endermandata.isRaging())
				{
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void jumpEvent(LivingJumpEvent event)
	{
		LivingData<?> entitydata = (LivingData<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		if(entitydata != null && entitydata.isRemote())
		{
			if(!entitydata.isInaction() && !event.getEntity().isInWater())
			{
				StaticAnimation jumpAnimation = entitydata.getClientAnimator().getJumpAnimation();
				entitydata.getAnimator().playAnimation(jumpAnimation, 0);
				ModNetworkManager.sendToServer(new CTSPlayAnimation(jumpAnimation.getId(), 0, true, false));
			}
		}
	}
	
	@SubscribeEvent
	public static void fallEvent(LivingFallEvent event)
	{
		LivingData<?> entitydata = (LivingData<?>) event.getEntity().getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		if(entitydata != null && !entitydata.isInaction())
		{
			float distance = event.getDistance();
			
			if(distance > 5.0F)
			{
				entitydata.getAnimator().playAnimation(Animations.BIPED_LAND_DAMAGE, 0);
			}
		}
	}
	
	@SubscribeEvent
	public static void changeDimensionEvent(PlayerEvent.PlayerChangedDimensionEvent event)
	{
		PlayerEntity player = event.getPlayer();
		ServerPlayerData playerData = (ServerPlayerData) player.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		playerData.modifiLivingMotions(playerData.getHeldItemCapability(Hand.MAIN_HAND));
	}
	
	@SubscribeEvent
	public static void tickClientEvent(TickEvent.ClientTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			for(CapabilityEntity<?> cap : unInitializedEntitiesClient)
			{
				cap.postInit();
			}
			unInitializedEntitiesClient.clear();
		}
	}
	
	@SubscribeEvent
	public static void tickServerEvent(TickEvent.ServerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			for(CapabilityEntity<?> cap : unInitializedEntitiesServer)
			{
				cap.postInit();
			}
			unInitializedEntitiesServer.clear();
		}
	}
}