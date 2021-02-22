package maninhouse.epicfight.capabilities.entity.mob;

import java.lang.reflect.Field;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.entity.MobData;
import maninhouse.epicfight.client.animation.AnimatorClient;
import maninhouse.epicfight.entity.ai.AttackPatternGoal;
import maninhouse.epicfight.entity.ai.AttackPatternPercentGoal;
import maninhouse.epicfight.entity.ai.ChasingGoal;
import maninhouse.epicfight.entity.ai.attribute.ModAttributes;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.gamedata.Sounds;
import maninhouse.epicfight.model.Model;
import maninhouse.epicfight.utils.game.IExtendedDamageSource;
import maninhouse.epicfight.utils.game.IExtendedDamageSource.StunType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class RavagerData extends MobData<RavagerEntity>
{
	static Field stunTime;
	
	static
	{
		stunTime= ObfuscationReflectionHelper.findField(RavagerEntity.class, "field_213692_bA");
		stunTime.setAccessible(true);
	}
	
	public RavagerData()
	{
		super(Faction.ILLAGER);
	}
	
	@Override
	public void init()
	{
		super.init();
		this.orgEntity.entityCollisionReduction = 0.2F;
	}
	
	@Override
	protected void initAttributes()
	{
		super.initAttributes();
		this.orgEntity.getAttribute(ModAttributes.HIT_AT_ONCE.get()).setBaseValue(8.0D);
		this.orgEntity.getAttribute(ModAttributes.IMPACT.get()).setBaseValue(10.0D);
	}
	
	@Override
	protected void initAnimator(AnimatorClient animatorClient)
	{
		animatorClient.addLivingAnimation(LivingMotion.IDLE, Animations.RAVAGER_IDLE);
		animatorClient.addLivingAnimation(LivingMotion.WALKING, Animations.RAVAGER_WALK);
		animatorClient.addLivingAnimation(LivingMotion.DEATH, Animations.RAVAGER_DEATH);
		animatorClient.setCurrentLivingMotionsToDefault();
	}
	
	@Override
	public void updateMotion()
	{
		super.commonCreatureUpdateMotion();
	}
	
	@Override
	protected void initAI()
	{
		super.initAI();
		
		orgEntity.goalSelector.addGoal(1, new ChasingGoal(this, this.orgEntity, 1.0D, false));
		orgEntity.goalSelector.addGoal(0, new AttackPatternPercentGoal(this, this.orgEntity, 0.0D, 2.25D, 0.1F, true, MobAttackPatterns.RAVAGER_PATTERN2));
        orgEntity.goalSelector.addGoal(1, new AttackPatternGoal(this, this.orgEntity, 1.0D, 2.4D, true, MobAttackPatterns.RAVAGER_PATTERN1));
	}
	
	@Override
	public boolean hurtEntity(Entity hitTarget, IExtendedDamageSource source, float amount)
	{
		boolean succed = hitTarget.attackEntityFrom((DamageSource)source, amount);
		
		if(!succed)
		{
			try {
				if(stunTime.getInt(this.orgEntity) > 0)
				{
					this.playAnimationSynchronize(Animations.RAVAGER_STUN, 0.0F);
				}
			}
			catch(IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}
		
		return succed;
	}
	
	@Override
	public StaticAnimation getHitAnimation(StunType stunType)
	{
		return null;
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
	public <M extends Model> M getEntityModel(Models<M> modelDB)
	{
		return modelDB.ENTITY_RAVAGER;
	}
}