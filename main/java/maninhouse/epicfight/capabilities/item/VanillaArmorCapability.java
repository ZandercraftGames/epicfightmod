package maninhouse.epicfight.capabilities.item;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.client.model.ClientModel;
import maninhouse.epicfight.entity.ai.attribute.ModAttributes;
import maninhouse.epicfight.gamedata.Models;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class VanillaArmorCapability extends ArmorCapability
{
	private final EquipmentSlotType armorType;
	private final float weight;
	
	public VanillaArmorCapability(ArmorItem item)
	{
		super(null, null);
		this.armorType = item.getEquipmentSlot();
		
		if(item.getArmorMaterial() instanceof ArmorMaterial)
		{
			switch((ArmorMaterial)item.getArmorMaterial())
			{
			case LEATHER:
				this.weight = item.getDamageReduceAmount();
				break;
			case GOLD:
				this.weight = item.getDamageReduceAmount() * 2.5F;
				break;
			case CHAIN:
				this.weight = item.getDamageReduceAmount() * 2.5F;
				break;
			case IRON:
				this.weight = item.getDamageReduceAmount() * 3F;
				break;
			case DIAMOND:
				this.weight = item.getDamageReduceAmount() * 3F;
				break;
			case NETHERITE:
				this.weight = item.getDamageReduceAmount() * 3.2F;
				break;
			default:
				this.weight = 0F;
			}
		}
		else
			this.weight = 0;
	}

	@Override
	public void modifyItemTooltip(List<ITextComponent> itemTooltip, boolean isOffhandEmpty)
	{
		itemTooltip.add(1, new StringTextComponent(TextFormatting.BLUE + " +" + (int)this.weight + " Weight"));
	}
	
	@Override
	public ClientModel getEquipModel(EquipmentSlotType slot, boolean smallsize)
	{
		switch(slot)
		{
		case HEAD:
			return smallsize ? Models.LOGICAL_CLIENT.ITEM_HELMET_FOR_CHILD : Models.LOGICAL_CLIENT.ITEM_HELMET;
		case CHEST:
			return smallsize ? Models.LOGICAL_CLIENT.ITEM_CHESTPLATE_FOR_CHILD : Models.LOGICAL_CLIENT.ITEM_CHESTPLATE;
		case LEGS:
			return smallsize ? Models.LOGICAL_CLIENT.ITEM_LEGGINS_FOR_CHILD : Models.LOGICAL_CLIENT.ITEM_LEGGINS;
		case FEET:
			return smallsize ? Models.LOGICAL_CLIENT.ITEM_BOOTS_FOR_CHILD : Models.LOGICAL_CLIENT.ITEM_BOOTS;
		default:
			return null;
		}
	}
	
	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, LivingData<?> entitydata)
    {
		Multimap<Attribute, AttributeModifier> map = HashMultimap.<Attribute, AttributeModifier>create();
		if (entitydata != null && equipmentSlot == this.armorType)
			map.put(ModAttributes.WEIGHT.get(), new AttributeModifier(ARMOR_MODIFIERS[equipmentSlot.getIndex()], "Armor modifier", (double)this.weight, Operation.ADDITION));
		
        return map;
    }
}