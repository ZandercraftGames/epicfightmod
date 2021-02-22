package maninhouse.epicfight.utils.game;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface IExtendedDamageSource
{
	public static DamageSourceExtended causePlayerDamage(PlayerEntity player, StunType stunType, DamageType damageType, int id)
    {
        return new DamageSourceExtended("player", player, stunType, damageType, id);
    }
	
	public static DamageSourceExtended causeMobDamage(LivingEntity mob, StunType stunType, DamageType damageType, int id)
    {
        return new DamageSourceExtended("mob", mob, stunType, damageType, id);
    }
	
	public static DamageSourceExtended getFrom(IExtendedDamageSource original)
	{
		return new DamageSourceExtended(original.getType(), original.getOwner(), original.getStunType(), original.getExtDamageType(), original.getSkillId());
	}
	
	public void setImpact(float amount);
	public void setArmorIgnore(float amount);
	public void setSureStrike(boolean value);
	public void setStunType(StunType stunType);
	public float getImpact();
	public float getArmorIgnoreRatio();
	public int getSkillId();
	public StunType getStunType();
	public DamageType getExtDamageType();
	public boolean isSureStrike();
	public Entity getOwner();
	public String getType();
	
	public static enum StunType
	{
		SHORT, LONG, HOLD
	}
	
	public static enum DamageType
	{
		PHYSICAL, MAGIC
	}
}