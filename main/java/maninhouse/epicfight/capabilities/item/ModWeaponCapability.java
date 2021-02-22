package maninhouse.epicfight.capabilities.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.entity.player.PlayerData;
import maninhouse.epicfight.entity.ai.attribute.ModAttributes;
import maninhouse.epicfight.particle.HitParticleType;
import maninhouse.epicfight.particle.Particles;
import maninhouse.epicfight.physics.Collider;
import maninhouse.epicfight.skill.Skill;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ModWeaponCapability extends CapabilityItem
{
	protected final Skill specialAttack;
	protected final Skill weaponGimmick;
	protected final SoundEvent smashingSound;
	protected final SoundEvent hitSound;
	protected final Collider weaponCollider;
	protected final boolean twoHanded;
	protected final boolean mainHandOnly;
	protected List<StaticAnimation> autoAttackMotions;
	protected List<StaticAnimation> autoAttackTwohandMotions;
	protected List<StaticAnimation> mountAttackMotion;
	protected Map<LivingMotion, StaticAnimation> livingMotionChangers;
	
	public ModWeaponCapability(Skill specialAttack, Skill weaponGimmick, SoundEvent smash, SoundEvent hit, Collider weaponCollider,
			double armorIgnorance, double impact, int hitEnemiesAtOnce, boolean twoHanded, boolean mainHandOnly)
	{
		this.specialAttack = specialAttack;
		this.weaponGimmick = weaponGimmick;
		this.smashingSound = smash;
		this.hitSound = hit;
		this.twoHanded = twoHanded;
		this.mainHandOnly = mainHandOnly;
		this.oneHandedStyleDamageAttribute.put(ModAttributes.IGNORE_DEFENCE, ModAttributes.getIgnoreDefenceModifier(armorIgnorance));
		this.oneHandedStyleDamageAttribute.put(ModAttributes.IMPACT, ModAttributes.getIgnoreDefenceModifier(impact));
		this.oneHandedStyleDamageAttribute.put(ModAttributes.HIT_AT_ONCE, ModAttributes.getIgnoreDefenceModifier(hitEnemiesAtOnce));
		this.weaponCollider = weaponCollider;
	}
	
	public void addLivingMotionChanger(LivingMotion livingMotion, StaticAnimation animation)
	{
		if(livingMotionChangers == null)
			livingMotionChangers = new HashMap<LivingMotion, StaticAnimation> ();
		
		livingMotionChangers.put(livingMotion, animation);
	}
	
	public void addAutoAttackCombos(StaticAnimation animation)
	{
		if(autoAttackMotions == null)
			autoAttackMotions = new ArrayList<StaticAnimation> ();
		
		autoAttackMotions.add(animation);
	}
	
	public void addTwohandAutoAttackCombos(StaticAnimation animation)
	{
		if(autoAttackTwohandMotions == null)
			autoAttackTwohandMotions = new ArrayList<StaticAnimation> ();
		
		autoAttackTwohandMotions.add(animation);
	}
	
	public void addMountAttackCombos(StaticAnimation animation)
	{
		if(mountAttackMotion == null)
			mountAttackMotion = new ArrayList<StaticAnimation> ();
		
		mountAttackMotion.add(animation);
	}
	
	@Override
	public List<StaticAnimation> getAutoAttckMotion(PlayerData<?> playerdata)
	{
		ItemStack offHandItem = playerdata.getOriginalEntity().getHeldItemOffhand();
		
		return (autoAttackMotions != null || autoAttackTwohandMotions != null) ? (this.twoHanded || (!this.canUsedOffhand() && offHandItem.isEmpty())) ?
				autoAttackTwohandMotions : autoAttackMotions : super.getAutoAttckMotion(playerdata);
	}
	
	@Override
	public List<StaticAnimation> getMountAttackMotion()
	{
		return this.mountAttackMotion;
	}
	
	@Override
	public boolean hasSpecialAttack()
	{
		return this.specialAttack != null;
	}
	
	@Override
	public Skill getSpecialAttack(ItemStack offhandItem)
	{
		return this.specialAttack;
	}
	
	@Override
	public Skill getPassiveSkill()
	{
		return this.weaponGimmick;
	}
	
	@Override
	public SoundEvent getSmashingSound()
	{
		return this.smashingSound;
	}
	
	@Override
	public SoundEvent getHitSound()
	{
		return this.hitSound;
	}
	
	@Override
	public HitParticleType getHitParticle()
	{
		return Particles.HIT_CUT.get();
	}
	
	@Override
	public Collider getWeaponCollider()
	{
		return this.weaponCollider != null ? weaponCollider : super.getWeaponCollider();
	}
	
	@Override
	public boolean canUsedOffhand()
	{
		return this.twoHanded ? false : !this.mainHandOnly;
	}
	
	public void setOneHandStyleAttribute(double armorIgnorance, double impact, int hitEnemiesAtOnce)
	{
		this.oneHandedStyleDamageAttribute.put(ModAttributes.IGNORE_DEFENCE,
				new AttributeModifier(ModAttributes.IGNORE_DEFENCE_ID, "Weapon modifier", armorIgnorance, AttributeModifier.Operation.ADDITION));
		this.oneHandedStyleDamageAttribute.put(ModAttributes.HIT_AT_ONCE,
				new AttributeModifier(ModAttributes.HIT_AT_ONCE_ID, "Weapon modifier", (double)hitEnemiesAtOnce, AttributeModifier.Operation.ADDITION));
		this.oneHandedStyleDamageAttribute.put(ModAttributes.IMPACT,
				new AttributeModifier(ModAttributes.IMPACT_ID, "Weapon modifier", impact, AttributeModifier.Operation.ADDITION));
	}
	
	public void setTwoHandStyleAttribute(double armorIgnorance, double impact, int hitEnemiesAtOnce)
	{
		if(twoHandedStyleDamageAttribute == null)
			this.twoHandedStyleDamageAttribute = new HashMap<Supplier<Attribute>, AttributeModifier>();
		
		this.twoHandedStyleDamageAttribute.put(ModAttributes.IGNORE_DEFENCE,
				new AttributeModifier(ModAttributes.IGNORE_DEFENCE_ID, "Weapon modifier", armorIgnorance, AttributeModifier.Operation.ADDITION));
		this.twoHandedStyleDamageAttribute.put(ModAttributes.HIT_AT_ONCE,
				new AttributeModifier(ModAttributes.HIT_AT_ONCE_ID, "Weapon modifier", (double)hitEnemiesAtOnce, AttributeModifier.Operation.ADDITION));
		this.twoHandedStyleDamageAttribute.put(ModAttributes.IMPACT,
				new AttributeModifier(ModAttributes.IMPACT_ID, "Weapon modifier", impact, AttributeModifier.Operation.ADDITION));
	}
	
	@Override
	public boolean isTwoHanded()
	{
		return this.twoHanded;
	}
	
	@Override
	public Map<LivingMotion, StaticAnimation> getLivingMotionChanges(PlayerData<?> player)
	{
		return this.livingMotionChangers;
	}
}