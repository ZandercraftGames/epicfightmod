package maninhouse.epicfight.capabilities.item;

import java.util.ArrayList;
import java.util.List;

import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.particle.HitParticleType;
import maninhouse.epicfight.particle.Particles;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.TieredItem;

public abstract class MaterialItemCapability extends CapabilityItem
{
	protected IItemTier itemTier;
	
	protected static List<StaticAnimation> toolAttackMotion;
	protected static List<StaticAnimation> mountAttackMotion;
	
	static
	{
		toolAttackMotion = new ArrayList<StaticAnimation> ();
		toolAttackMotion.add(Animations.TOOL_AUTO_1);
		toolAttackMotion.add(Animations.TOOL_AUTO_2);
		toolAttackMotion.add(Animations.TOOL_DASH);
		mountAttackMotion = new ArrayList<StaticAnimation> ();
		mountAttackMotion.add(Animations.SWORD_MOUNT_ATTACK);
	}
	
	public MaterialItemCapability(Item item)
	{
		super(item);
		
		this.itemTier = ((TieredItem)item).getTier();
		
		if(EpicFightMod.isPhysicalClient())
		{
			loadClientThings();
		}
		
		registerAttribute();
	}
	
	@Override
	public List<StaticAnimation> getMountAttackMotion()
	{
		return mountAttackMotion;
	}
	
	@Override
	public HitParticleType getHitParticle()
	{
		return Particles.HIT_CUT.get();
	}
}