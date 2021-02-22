package maninhouse.epicfight.capabilities.entity.mob;

import java.util.Iterator;
import java.util.Set;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.entity.MobData;
import maninhouse.epicfight.client.animation.AnimatorClient;
import maninhouse.epicfight.entity.ai.AttackPatternGoal;
import maninhouse.epicfight.entity.ai.AttackPatternPercentGoal;
import maninhouse.epicfight.entity.ai.ChasingGoal;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.model.Model;
import maninhouse.epicfight.utils.game.IExtendedDamageSource.StunType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public class SpiderData<T extends SpiderEntity> extends MobData<T>
{
	public SpiderData()
	{
		super(Faction.NATURAL);
	}
	
	@Override
	protected void initAI()
	{
		super.initAI();
        
		Set<PrioritizedGoal> goals = null;
		try {
			goals = (Set<PrioritizedGoal>)goalSelectorSeeker.get(this.orgEntity.goalSelector);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace(); }
		
		Iterator<PrioritizedGoal> iterator = goals.iterator();
		Goal toRemove = null;
		
        while (iterator.hasNext())
        {
        	PrioritizedGoal goal = iterator.next();
            Goal inner = goal.getGoal();
            
            if (inner instanceof LeapAtTargetGoal)
            {
            	toRemove = inner;
            	break;
            }
        }
        
        if(toRemove != null)
        	orgEntity.goalSelector.removeGoal(toRemove);
        
        orgEntity.goalSelector.addGoal(1, new ChasingGoal(this, this.orgEntity, 1.0D, false));
        orgEntity.goalSelector.addGoal(1, new AttackPatternPercentGoal(this, this.orgEntity, 0.0D, 2.0D, 0.5F, true, MobAttackPatterns.SPIDER_PATTERN));
        orgEntity.goalSelector.addGoal(0, new AttackPatternGoal(this, this.orgEntity, 0.0D, 2.5D, true, MobAttackPatterns.SPIDER_JUMP_PATTERN));
	}
	
	@Override
	public void postInit()
	{
		super.postInit();
	}
	
	@Override
	protected void initAnimator(AnimatorClient animatorClient)
	{
		animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.SPIDER_DEATH);
		animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.SPIDER_IDLE);
		animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.SPIDER_CRAWL);
		animatorClient.setCurrentLivingMotionsToDefault();
	}

	@Override
	public void updateMotion()
	{
		super.commonCreatureUpdateMotion();
	}
	
	@Override
	public StaticAnimation getHitAnimation(StunType stunType)
	{
		return Animations.SPIDER_HIT;
	}

	@Override
	public SoundEvent getSwingSound(Hand hand)
	{
		return SoundEvents.ENTITY_SPIDER_HURT;
	}
	
	@Override
	public SoundEvent getWeaponHitSound(Hand hand)
	{
		return super.getWeaponHitSound(hand);
	}
	
	@Override
	public <M extends Model> M getEntityModel(Models<M> modelDB)
	{
		return modelDB.ENTITY_SPIDER;
	}
}