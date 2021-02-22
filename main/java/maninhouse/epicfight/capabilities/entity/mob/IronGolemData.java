package maninhouse.epicfight.capabilities.entity.mob;

import java.util.Iterator;
import java.util.Set;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.client.animation.AnimatorClient;
import maninhouse.epicfight.entity.ai.AttackPatternGoal;
import maninhouse.epicfight.entity.ai.AttackPatternPercentGoal;
import maninhouse.epicfight.entity.ai.ChasingGoal;
import maninhouse.epicfight.entity.ai.attribute.ModAttributes;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.gamedata.Sounds;
import maninhouse.epicfight.model.Model;
import maninhouse.epicfight.utils.game.IExtendedDamageSource.StunType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;

public class IronGolemData extends BipedMobData<IronGolemEntity>
{
	private int deathTimerExt;
	
	public IronGolemData()
	{
		super(Faction.VILLAGER);
	}
	
	@Override
	public void init()
	{
		super.init();
		
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
            
            if (inner instanceof MoveTowardsTargetGoal)
            {
            	toRemove = inner;
            	break;
            }
        }
        
        if(toRemove != null)
        	orgEntity.goalSelector.removeGoal(toRemove);
		
		this.orgEntity.entityCollisionReduction = 0.2F;
	}
	
	@Override
	protected void initAttributes()
	{
		super.initAttributes();
		this.orgEntity.getAttribute(ModAttributes.HIT_AT_ONCE.get()).setBaseValue(4.0D);
		this.orgEntity.getAttribute(ModAttributes.IMPACT.get()).setBaseValue(10.0D);
	}
	
	@Override
	protected void initAnimator(AnimatorClient animatorClient)
	{
		super.initAnimator(animatorClient);
		animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.GOLEM_IDLE);
		animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.GOLEM_WALK);
		animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.GOLEM_DEATH);
		animatorClient.setCurrentLivingMotionsToDefault();
	}
	
	@Override
	public void updateMotion()
	{
		super.commonCreatureUpdateMotion();
	}
	
	@Override
	public void update()
	{
		if(orgEntity.getHealth() <= 0.0F)
		{
			orgEntity.rotationPitch = 0;
			
			if(orgEntity.deathTime > 1 && this.deathTimerExt < 20)
			{
				deathTimerExt++;
				orgEntity.deathTime--;
			}
		}
		
		super.update();
	}

	@Override
	public void setAIAsUnarmed()
	{
		orgEntity.goalSelector.addGoal(0, new AttackPatternPercentGoal(this, this.orgEntity, 0.0D, 1.5D, 0.3F, true, MobAttackPatterns.GOLEM_PATTERN1));
		orgEntity.goalSelector.addGoal(0, new AttackPatternPercentGoal(this, this.orgEntity, 1.0D, 2.5D, 0.15F, true, MobAttackPatterns.GOLEM_PATTERN2));
		orgEntity.goalSelector.addGoal(0, new AttackPatternGoal(this, this.orgEntity, 0.0D, 2.0D, true, MobAttackPatterns.GOLEM_PATTERN3));
		orgEntity.goalSelector.addGoal(1, new ChasingGoal(this, this.orgEntity, 1.0D, false));
	}
	
	@Override
	public void setAIAsArmed()
	{
		this.setAIAsUnarmed();
	}
	
	@Override
	public SoundEvent getWeaponHitSound(Hand hand)
	{
		return Sounds.BLUNT_HIT_HARD;
	}
	
	@Override
	public SoundEvent getSwingSound(Hand hand)
	{
		return Sounds.WHOOSH_BIG;
	}
	
	@Override
	public float getDamageToEntity(Entity targetEntity, Hand hand)
	{
		return (float)(7 + this.orgEntity.getRNG().nextInt(15));
	}
	
	@Override
	public StaticAnimation getHitAnimation(StunType stunType)
	{
		return null;
	}
	
	@Override
	public <M extends Model> M getEntityModel(Models<M> modelDB)
	{
		return modelDB.ENTITY_GOLEM;
	}
}