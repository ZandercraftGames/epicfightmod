package maninhouse.epicfight.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;

import maninhouse.epicfight.capabilities.item.KatanaCapability;
import maninhouse.epicfight.main.EpicFightMod;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class KatanaItem extends WeaponItem
{
	@OnlyIn(Dist.CLIENT)
	private List<ITextComponent> tooltipExpand;
	
	public KatanaItem(Item.Properties build)
	{
		super(build);
		if(EpicFightMod.isPhysicalClient())
		{
			tooltipExpand = new ArrayList<ITextComponent> ();
			tooltipExpand.add(new StringTextComponent(""));
			tooltipExpand.add(new StringTextComponent("If you don't act for 5 second, it will go to the sheathing state. In sheathing state, you can reinforce the next attack"));
		}
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
		return toRepair.getItem() == Items.IRON_BARS;
    }
	
	@Override
	public boolean canHarvestBlock(BlockState blockIn)
    {
        return false;
    }
    
    @Override
    public void setWeaponCapability()
    {
    	capability = new KatanaCapability();
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack)
    {
    	if (slot == EquipmentSlotType.MAINHAND)
        {
    		Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    	    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 5.0D, Operation.ADDITION));
    	    builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.0D, Operation.ADDITION));
    	    return builder.build();
        }
        
        return super.getAttributeModifiers(slot, stack);
    }
	
	@OnlyIn(Dist.CLIENT)@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		for(ITextComponent txtComp : tooltipExpand)
		{
			tooltip.add(txtComp);
		}
	}
}