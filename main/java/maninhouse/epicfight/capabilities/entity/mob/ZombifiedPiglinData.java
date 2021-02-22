package maninhouse.epicfight.capabilities.entity.mob;

import io.netty.buffer.ByteBuf;
import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.client.animation.AnimatorClient;
import maninhouse.epicfight.entity.ai.AttackPatternGoal;
import maninhouse.epicfight.entity.ai.ChasingGoal;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.model.Model;
import maninhouse.epicfight.network.ModNetworkManager;
import maninhouse.epicfight.network.client.CTSReqSpawnInfo;
import maninhouse.epicfight.network.server.STCLivingMotionChange;
import maninhouse.epicfight.network.server.STCMobInitialSetting;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.util.DamageSource;

public class ZombifiedPiglinData extends BipedMobData<ZombifiedPiglinEntity>
{
	public ZombifiedPiglinData()
	{
		super(Faction.NATURAL);
	}
	
	@Override
	public void postInit()
	{
		super.postInit();
		
		if(this.isRemote())
		{
			AnimatorClient animator = this.getClientAnimator();
			
			if(this.orgEntity.isChild())
			{
				animator.addLivingAnimation(LivingMotion.DEATH, Animations.BABY_DEATH);
			}
			else
			{
				animator.addLivingAnimation(LivingMotion.DEATH, Animations.PIGLIN_DEATH);
			}
			
			ModNetworkManager.sendToServer(new CTSReqSpawnInfo(this.orgEntity.getEntityId()));
		}
	}
	
	@Override
	protected void initAnimator(AnimatorClient animatorClient)
	{
		super.initAnimator(animatorClient);
		animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.PIGLIN_IDLE_ZOMBIE);
		animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.PIGLIN_WALK_ZOMBIE);
		animatorClient.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
		animatorClient.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
		animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
		animatorClient.setCurrentLivingMotionsToDefault();
	}
	
	@Override
	public STCMobInitialSetting sendInitialInformationToClient()
	{
		STCMobInitialSetting packet = new STCMobInitialSetting(this.orgEntity.getEntityId());
        ByteBuf buf = packet.getBuffer();
        buf.writeBoolean(this.orgEntity.canPickUpLoot());
		return packet;
	}
	
	@Override
	public void clientInitialSettings(ByteBuf buf)
	{
		AnimatorClient animator = this.getClientAnimator();
		
		if(buf.readBoolean())
		{
			animator.addLivingAnimation(LivingMotion.IDLE, Animations.BIPED_IDLE);
			animator.addLivingAnimation(LivingMotion.WALKING, Animations.BIPED_WALK);
		}
		
		if(orgEntity.isChild())
		{
			animator.addLivingAnimation(LivingMotion.IDLE, Animations.ZOMBIE_IDLE);
			animator.addLivingAnimation(LivingMotion.WALKING, Animations.BABY_RUN);
		}
	}
	
	@Override
	public void updateMotion()
	{
		super.commonCreatureUpdateMotion();
	}
	
	@Override
	public void setAIAsArmed()
	{
        orgEntity.goalSelector.addGoal(1, new EntityAIPigmanChase(this, this.orgEntity));
        orgEntity.goalSelector.addGoal(0, new AttackPatternGoal(this, this.orgEntity, 0.0D, 1.5D, true, MobAttackPatterns.BIPED_ARMED_ONEHAND));
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource damageSource, float amount)
	{
		if(damageSource.getTrueSource() instanceof ZombifiedPiglinEntity)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public <M extends Model> M getEntityModel(Models<M> modelDB)
	{
		return this.orgEntity.isChild() ? modelDB.ENTITY_PIGLIN_CHILD : modelDB.ENTITY_PIGLIN;
	}
	
	static class EntityAIPigmanChase extends ChasingGoal
	{
		boolean angry;
		
		public EntityAIPigmanChase(BipedMobData<?> entitydata, MobEntity creature)
		{
			super(entitydata, creature, 1.35D, false, Animations.PIGLIN_CHASE_ZOMBIE, Animations.PIGLIN_WALK_ZOMBIE);
		}
		
		@Override
		public void tick()
		{
			super.tick();
			
			if(!((ZombifiedPiglinEntity)attacker).isAggressive())
			{
				if(this.angry)
				{
					STCLivingMotionChange msg = new STCLivingMotionChange(attacker.getEntityId(), 1);
					msg.setMotions(LivingMotion.WALKING);
					msg.setAnimations(walkingAnimation);
					ModNetworkManager.sendToAllPlayerTrackingThisEntity(msg, attacker);
					this.angry = false;
				}
			}
			else
			{
				if(!this.angry)
				{
					STCLivingMotionChange msg = new STCLivingMotionChange(attacker.getEntityId(), 1);
					msg.setMotions(LivingMotion.WALKING);
					msg.setAnimations(chasingAnimation);
					ModNetworkManager.sendToAllPlayerTrackingThisEntity(msg, attacker);
					this.angry = true;
				}
			}
		}
		
		@Override
	    public void startExecuting()
	    {
	        super.startExecuting();
	        this.angry = true;
	    }
		
		@Override
	    public void resetTask()
	    {
	        super.resetTask();
	        this.angry = false;
	    }
	}
}