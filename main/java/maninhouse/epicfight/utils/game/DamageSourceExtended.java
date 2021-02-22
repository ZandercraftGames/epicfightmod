package maninhouse.epicfight.utils.game;

import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.entity.LivingData;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSource;

public class DamageSourceExtended extends EntityDamageSource implements IExtendedDamageSource
{
	private float impact;
	private float armorIgnore;
	private boolean isSureStrike;
	private StunType stunType;
	private DamageType damageType;
	private final int id;
	
	public DamageSourceExtended(String damageTypeIn, Entity damageSourceEntityIn, StunType stunType, DamageType damageType, int id)
	{
		super(damageTypeIn, damageSourceEntityIn);
		
		LivingData<?> entityCap = (LivingData<?>) damageSourceEntityIn.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		
		this.stunType = stunType;
		this.damageType = damageType;
		this.impact = entityCap.getImpact();
		this.armorIgnore = (float) entityCap.getDefenceIgnore();
		this.id = id;
	}
	
	@Override
	public void setImpact(float amount)
	{
		this.impact = amount;
	}
	
	@Override
	public void setArmorIgnore(float amount)
	{
		this.armorIgnore = amount;
	}
	
	@Override
	public void setSureStrike(boolean value)
	{
		this.isSureStrike = value;
	}
	
	@Override
	public void setStunType(StunType stunType)
	{
		this.stunType = stunType;
	}
	
	@Override
	public float getImpact()
	{
		return impact;
	}
	
	@Override
	public float getArmorIgnoreRatio()
	{
		return armorIgnore * 0.01F;
	}
	
	@Override
	public StunType getStunType()
	{
		return stunType;
	}
	
	@Override
	public DamageType getExtDamageType()
	{
		return damageType;
	}

	@Override
	public boolean isSureStrike()
	{
		return isSureStrike;
	}

	@Override
	public Entity getOwner()
	{
		return super.getTrueSource();
	}

	@Override
	public String getType()
	{
		return super.damageType;
	}

	@Override
	public int getSkillId()
	{
		return this.id;
	}
}