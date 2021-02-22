package maninhouse.epicfight.capabilities.entity.mob;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.client.animation.AnimatorClient;
import maninhouse.epicfight.entity.ai.AttackPatternGoal;
import maninhouse.epicfight.entity.ai.ChasingGoal;
import maninhouse.epicfight.entity.ai.RangeAttackMobGoal;
import maninhouse.epicfight.gamedata.Animations;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.item.Items;

public class DrownedData extends ZombieData<DrownedEntity>
{
	@Override
	protected void initAnimator(AnimatorClient animator)
	{
		animator.addLivingAnimation(LivingMotion.IDLE, Animations.ZOMBIE_IDLE);
		animator.addLivingAnimation(LivingMotion.WALKING, Animations.ZOMBIE_WALK);
		animator.addLivingAnimation(LivingMotion.FALL, Animations.BIPED_FALL);
		animator.addLivingAnimation(LivingMotion.MOUNT, Animations.BIPED_MOUNT);
		animator.addLivingAnimation(LivingMotion.DEATH, Animations.BIPED_DEATH);
		animator.setCurrentLivingMotionsToDefault();
	}
	
	@Override
	public void setAIAsUnarmed()
	{
		orgEntity.goalSelector.addGoal(1, new DrownedChasingGoal(this, this.orgEntity, 1.0D, false));
		orgEntity.goalSelector.addGoal(0, new AttackPatternGoal(this, this.orgEntity, 0.0D, 1.5D, true, MobAttackPatterns.ZOMBIE_NORAML));
	}
	
	@Override
	public void setAIAsArmed()
	{
		if(this.orgEntity.getHeldItemMainhand().getItem() == Items.TRIDENT)
		{
			orgEntity.goalSelector.addGoal(0, new AttackPatternGoal(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_SPEAR));
			orgEntity.goalSelector.addGoal(0, new TridentAttackGoal(this, this.orgEntity));
		}
		else
		{
			orgEntity.goalSelector.addGoal(0, new AttackPatternGoal(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.BIPED_ARMED_ONEHAND));
			orgEntity.goalSelector.addGoal(1, new DrownedChasingGoal(this, this.orgEntity, 1.0D, false));
		}
	}
	
	static class DrownedChasingGoal extends ChasingGoal
	{
		private final DrownedEntity drownedEntity;
		
		public DrownedChasingGoal(DrownedData mobdata, DrownedEntity host, double speedIn, boolean useLongMemory)
		{
			super(mobdata, host, speedIn, useLongMemory);
			this.drownedEntity = host;
		}
		
		@Override
		public boolean shouldExecute()
	    {
	        return super.shouldExecute() && this.drownedEntity.shouldAttack(this.drownedEntity.getAttackTarget());
	    }
		
		@Override
		public boolean shouldContinueExecuting()
		{
			return super.shouldContinueExecuting() && this.drownedEntity.shouldAttack(this.drownedEntity.getAttackTarget());
	    }
	}
	
	class TridentAttackGoal extends RangeAttackMobGoal
	{
		private final DrownedEntity hostEntity;
		
	    public TridentAttackGoal(DrownedData entitydata, IRangedAttackMob hostEntity)
	    {
	    	super(hostEntity, entitydata, Animations.BIPED_MOB_THROW, 1.0, 40, 10.0F, 13);
	        this.hostEntity = (DrownedEntity)hostEntity;
	    }
	    
	    @Override
	    public boolean shouldExecute()
	    {
	       return super.shouldExecute();
	    }
	    
	    @Override
	    public void startExecuting()
	    {
	       super.startExecuting();
	       this.hostEntity.setAggroed(true);
	    }
	    
	    @Override
	    public void resetTask()
	    {
	       super.resetTask();
	       this.hostEntity.setAggroed(false);
	    }
	}
}