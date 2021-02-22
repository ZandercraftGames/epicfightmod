package maninhouse.epicfight.item;

import javax.annotation.Nullable;

import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.item.ModWeaponCapability;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public abstract class WeaponItem extends Item
{
	protected ModWeaponCapability capability;
	protected LazyOptional<ModWeaponCapability> optional = LazyOptional.of(()->capability);
	
	public WeaponItem(Item.Properties builder)
	{
		super(builder);
		this.setWeaponCapability();
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        stack.damageItem(1, attacker, (entity)->{ entity.sendBreakAnimation(EquipmentSlotType.HEAD); });
        return true;
    }
	
	public abstract void setWeaponCapability();
	
	@Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new ICapabilityProvider()
        {
			@Override
			public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
			{
				return cap == ModCapabilities.CAPABILITY_ITEM ? optional.cast() : LazyOptional.empty();
			}
        };
    }
}