package maninhouse.epicfight.entity.ai;

import java.util.EnumSet;
import java.util.List;

import maninhouse.epicfight.animation.types.attack.AttackAnimation;
import maninhouse.epicfight.capabilities.entity.MobData;
import maninhouse.epicfight.network.ModNetworkManager;
import maninhouse.epicfight.network.server.STCPlayAnimationTarget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;

public class AttackPatternGoal extends Goal
{
	protected final MobEntity attacker;
	protected final MobData<?> mobdata;
	protected final double minDist;
	protected final double maxDist;
	protected final List<AttackAnimation> pattern;
	protected final boolean affectHorizon;
	protected int patternCounter;
	
	public AttackPatternGoal(MobData<?> mobdata, MobEntity attacker, double minDist, double maxDIst, boolean affectHorizon, List<AttackAnimation> pattern)
	{
		this.attacker = attacker;
		this.mobdata = mobdata;
		this.minDist = minDist * minDist;
		this.maxDist = maxDIst * maxDIst;
		this.pattern = pattern;
		this.patternCounter = 0;
		this.affectHorizon = affectHorizon;
		this.setMutexFlags(EnumSet.noneOf(Flag.class));
	}
	
	@Override
    public boolean shouldExecute()
    {
		LivingEntity LivingEntity = this.attacker.getAttackTarget();
		return isValidTarget(LivingEntity) && isTargetInRange(LivingEntity);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting()
    {
    	LivingEntity LivingEntity = this.attacker.getAttackTarget();
    	return pattern.size() <= patternCounter && isValidTarget(LivingEntity) && isTargetInRange(LivingEntity);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    @Override
    public void resetTask()
    {
    	this.patternCounter %= pattern.size();
    }
    
    protected boolean canExecuteAttack()
    {
    	return !mobdata.isInaction();
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    @Override
    public void tick()
    {
        if(this.canExecuteAttack())
        {
        	AttackAnimation att = pattern.get(patternCounter++);
        	this.patternCounter %= pattern.size();
        	mobdata.getServerAnimator().playAnimation(att, 0);
        	mobdata.updateInactionState();
        	ModNetworkManager.sendToAllPlayerTrackingThisEntity(new STCPlayAnimationTarget(att.getId(), attacker.getEntityId(), 0, 
        			attacker.getAttackTarget().getEntityId()), attacker);
        }
    }
    
    protected boolean isTargetInRange(LivingEntity attackTarget)
    {
    	double targetRange = this.attacker.getDistanceSq(attackTarget.getPosX(), attackTarget.getBoundingBox().minY, attackTarget.getPosZ());
    	return targetRange <= this.maxDist && targetRange >= this.minDist && isInSameHorizontalPosition(attackTarget);
    }
    
    protected boolean isValidTarget(LivingEntity attackTarget)
    {
    	return attackTarget != null && attackTarget.isAlive() && 
    			!((attackTarget instanceof PlayerEntity) && (((PlayerEntity)attackTarget).isSpectator() || ((PlayerEntity)attackTarget).isCreative()));
    }
    
    protected boolean isInSameHorizontalPosition(LivingEntity attackTarget)
    {
    	if(affectHorizon)
    		return Math.abs(attacker.getPosY() - attackTarget.getPosY()) <= attacker.getEyeHeight();
    	
    	return true;
    }
}