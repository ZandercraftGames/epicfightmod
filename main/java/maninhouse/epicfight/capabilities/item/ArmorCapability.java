package maninhouse.epicfight.capabilities.item;

import java.util.UUID;

import maninhouse.epicfight.client.model.ClientModel;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.gamedata.Models.ClientModels;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.model.Model;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArmorCapability extends CapabilityItem
{
	protected static final UUID[] ARMOR_MODIFIERS = new UUID[] {UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
	
	@OnlyIn(Dist.CLIENT)
	protected Model equipModel;
	@OnlyIn(Dist.CLIENT)
	protected ResourceLocation equipTexture;
	
	public ArmorCapability(Model equipModel, ResourceLocation equipTexture)
	{
		if(EpicFightMod.isPhysicalClient())
		{
			this.equipModel = equipModel;
			this.equipTexture = equipTexture;
		}
	}
	
	public Model getEquipModel(EquipmentSlotType slot, boolean smallsize)
	{
		return equipModel;
	}
	
	public static ClientModel getBipedArmorModel(EquipmentSlotType slot, boolean isBaby)
	{
		ClientModels modelDB = Models.LOGICAL_CLIENT;
		
		switch(slot)
		{
		case HEAD:
			return isBaby ? modelDB.ITEM_HELMET_FOR_CHILD : modelDB.ITEM_HELMET;
		case CHEST:
			return isBaby ? modelDB.ITEM_CHESTPLATE_FOR_CHILD : modelDB.ITEM_CHESTPLATE;
		case LEGS:
			return isBaby ? modelDB.ITEM_LEGGINS_FOR_CHILD : modelDB.ITEM_LEGGINS;
		case FEET:
			return isBaby ? modelDB.ITEM_BOOTS_FOR_CHILD : modelDB.ITEM_BOOTS;
		default:
			return null;
		}
	}
}
