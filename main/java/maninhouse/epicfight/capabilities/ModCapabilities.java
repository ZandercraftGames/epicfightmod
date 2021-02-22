package maninhouse.epicfight.capabilities;

import maninhouse.epicfight.capabilities.entity.CapabilityEntity;
import maninhouse.epicfight.capabilities.item.CapabilityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class ModCapabilities
{
	@CapabilityInject(CapabilityEntity.class)
	public static final Capability<CapabilityEntity> CAPABILITY_ENTITY = null;
	@CapabilityInject(CapabilityItem.class)
    public static final Capability<CapabilityItem> CAPABILITY_ITEM = null;
	
	public static void registerCapabilities()
	{
		CapabilityManager.INSTANCE.register(CapabilityItem.class, new IStorage<CapabilityItem>()
        {
			@Override
			public INBT writeNBT(Capability<CapabilityItem> capability, CapabilityItem instance, Direction side)
			{
				return null;
			}

			@Override
			public void readNBT(Capability<CapabilityItem> capability, CapabilityItem instance, Direction side, INBT nbt)
			{
			}
        }, () -> null);
		
		CapabilityManager.INSTANCE.register(CapabilityEntity.class, new IStorage<CapabilityEntity>()
        {
			@Override
			public INBT writeNBT(Capability<CapabilityEntity> capability, CapabilityEntity instance, Direction side)
			{
				return null;
			}

			@Override
			public void readNBT(Capability<CapabilityEntity> capability, CapabilityEntity instance, Direction side, INBT nbt)
			{
			}
        }, () -> null);
	}
	
	public static CapabilityItem stackCapabilityGetter(ItemStack stack)
	{
		return stack.isEmpty() ? null : stack.getCapability(CAPABILITY_ITEM, null).orElse(null);
	}
}