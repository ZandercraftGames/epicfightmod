package maninhouse.epicfight.capabilities.entity.mob;

import java.util.List;
import java.util.Optional;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.animation.types.attack.AttackAnimation;
import maninhouse.epicfight.capabilities.entity.MobData;
import maninhouse.epicfight.client.animation.AnimatorClient;
import maninhouse.epicfight.entity.ai.attribute.ModAttributes;
import maninhouse.epicfight.entity.ai.brain.BrainRemodeler;
import maninhouse.epicfight.entity.ai.brain.task.AttackWithPatternTask;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.gamedata.Sounds;
import maninhouse.epicfight.model.Model;
import maninhouse.epicfight.utils.game.IExtendedDamageSource.StunType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.SupplementedTask;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;

public class HoglinData extends MobData<HoglinEntity>
{
	@Override
	public void init()
	{
		super.init();
		
		List<AttackAnimation> pattern = this.orgEntity.isChild() ? MobAttackPatterns.HOGLIN_CHILD_PATTERN : MobAttackPatterns.HOGLIN_PATTERN;
		
		BrainRemodeler.replaceTask(this.orgEntity.getBrain(), Activity.FIGHT, 13, SupplementedTask.class, new AttackWithPatternTask(this, pattern, 0.0D, 1.5D));
		BrainRemodeler.removeTask(this.orgEntity.getBrain(), Activity.FIGHT, 14, SupplementedTask.class);
	}
	
	@Override
	protected void initAnimator(AnimatorClient animatorClient)
	{
		animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.HOGLIN_IDLE);
		animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.HOGLIN_WALK);
		
		if(this.orgEntity.isChild())
			animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.HOGLIN_DEATH_CHILD);
		else
			animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.HOGLIN_DEATH);
		
		animatorClient.setCurrentLivingMotionsToDefault();
	}
	
	@Override
	protected void initAttributes()
	{
		super.initAttributes();
		this.orgEntity.getAttribute(ModAttributes.HIT_AT_ONCE.get()).setBaseValue(4.0F);
		this.orgEntity.getAttribute(ModAttributes.IMPACT.get()).setBaseValue(5.0F);
	}
	
	@Override
	public void updateMotion()
	{
		super.commonCreatureUpdateMotion();
	}
	
	@Override
	public <M extends Model> M getEntityModel(Models<M> modelDB)
	{
		return this.orgEntity.isChild() ? modelDB.ENTITY_HOGLIN_CHILD : modelDB.ENTITY_HOGLIN;
	}
	
	@Override
	public StaticAnimation getHitAnimation(StunType stunType)
	{
		return null;
	}
	
	@Override
	public SoundEvent getWeaponHitSound(Hand hand)
	{
		return this.orgEntity.isChild() ? Sounds.BLUNT_HIT : Sounds.BLUNT_HIT_HARD;
	}
	
	@Override
	public SoundEvent getSwingSound(Hand hand)
	{
		return this.orgEntity.isChild() ? Sounds.WHOOSH : Sounds.WHOOSH_BIG;
	}
	
	@Override
	public LivingEntity getAttackTarget()
	{
		Optional<LivingEntity> opt = this.orgEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
		return opt.orElse(null);
	}
}