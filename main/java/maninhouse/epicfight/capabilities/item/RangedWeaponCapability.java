package maninhouse.epicfight.capabilities.item;

import java.util.HashMap;
import java.util.Map;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.entity.player.PlayerData;
import net.minecraft.item.Item;

public class RangedWeaponCapability extends CapabilityItem
{
	protected Map<LivingMotion, StaticAnimation> rangeAnimationSet;
	
	public RangedWeaponCapability(Item item, StaticAnimation reload, StaticAnimation aiming, StaticAnimation shot)
	{
		super(item);
		this.rangeAnimationSet = new HashMap<LivingMotion, StaticAnimation> ();
		if(reload != null)
			this.rangeAnimationSet.put(LivingMotion.RELOADING, reload);
		if(aiming != null)
			this.rangeAnimationSet.put(LivingMotion.AIMING, aiming);
		if(shot != null)
			this.rangeAnimationSet.put(LivingMotion.SHOTING, shot);
	}
	
	@Override
	public Map<LivingMotion, StaticAnimation> getLivingMotionChanges(PlayerData<?> playerdata)
	{
		return rangeAnimationSet;
	}
	
	@Override
	public boolean canUseOnMount()
	{
		return true;
	}
}
