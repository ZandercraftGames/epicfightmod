package maninhouse.epicfight.client.model;

import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.main.EpicFightMod;
import net.minecraft.util.ResourceLocation;

public class ClientModels extends Models<ClientModel>
{
	public static final ClientModels LOGICAL_CLIENT = new ClientModels();
	
	public final ClientModel ITEM_HELMET;
	public final ClientModel ITEM_CHESTPLATE;
	public final ClientModel ITEM_LEGGINS;
	public final ClientModel ITEM_LEGGINS_CLOTH;
	public final ClientModel ITEM_BOOTS;
	public final ClientModel ITEM_HELMET_FOR_CHILD;
	public final ClientModel ITEM_CHESTPLATE_FOR_CHILD;
	public final ClientModel ITEM_LEGGINS_FOR_CHILD;
	public final ClientModel ITEM_BOOTS_FOR_CHILD;
	
	public ClientModels()
	{
		ENTITY_BIPED = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/biped.dae"));
		ENTITY_BIPED_SLIM_ARM = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/biped_slim_arm.dae"));
		ENTITY_ZOMBIE_CHILD = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_child.dae"));
		ENTITY_VILLAGER_ZOMBIE = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_villager.dae"));
		ENTITY_VILLAGER_ZOMBIE_BODY = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_villager_body.dae"));
		ENTITY_CREEPER = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/creeper.dae"));
		ENTITY_ENDERMAN = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/enderman.dae"));
		ENTITY_SKELETON = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/skeleton.dae"));
		ENTITY_SPIDER = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/spider.dae"));
		ENTITY_GOLEM = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/iron_golem.dae"));
		ENTITY_ILLAGER = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/illager.dae"));
		ENTITY_WITCH = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/witch.dae"));
		ENTITY_VILLAGER_ZOMBIE_CHILD = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_villager_child.dae"));
		ENTITY_VILLAGER_ZOMBIE_CHILD_BODY = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_villager_child_body.dae"));
		ENTITY_RAVAGER = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/ravager.dae"));
		ENTITY_VEX = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/vex.dae"));
		ENTITY_PIGLIN = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/piglin.dae"));
		ENTITY_PIGLIN_CHILD = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/piglin_child.dae"));
		ENTITY_HOGLIN = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/hoglin.dae"));
		ENTITY_HOGLIN_CHILD = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/entity/hoglin_child.dae"));
		
		ITEM_HELMET = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_helmet.dae"));
		ITEM_CHESTPLATE = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_chestplate.dae"));
		ITEM_LEGGINS = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_leggins.dae"));
		ITEM_LEGGINS_CLOTH = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_leggins_cloth.dae"));
		ITEM_BOOTS = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_boots.dae"));
		ITEM_HELMET_FOR_CHILD = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_helmet_child.dae"));
		ITEM_CHESTPLATE_FOR_CHILD = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_chestplate_child.dae"));
		ITEM_LEGGINS_FOR_CHILD = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_leggins_child.dae"));
		ITEM_BOOTS_FOR_CHILD = new ClientModel(new ResourceLocation(EpicFightMod.MODID, "models/item/armor/armor_boots_child.dae"));
	}
	
	public void buildMeshData()
	{
		ENTITY_BIPED.loadMeshData();
		ENTITY_BIPED_SLIM_ARM.loadMeshData();
		ENTITY_CREEPER.loadMeshData();
		ENTITY_SKELETON.loadMeshData();
		ENTITY_ZOMBIE_CHILD.loadMeshData();
		ENTITY_VILLAGER_ZOMBIE.loadMeshData();
		ENTITY_VILLAGER_ZOMBIE_BODY.loadMeshData();
		ENTITY_ENDERMAN.loadMeshData();
		ENTITY_SPIDER.loadMeshData();
		ENTITY_GOLEM.loadMeshData();
		ENTITY_WITCH.loadMeshData();
		ENTITY_VILLAGER_ZOMBIE_CHILD.loadMeshData();
		ENTITY_VILLAGER_ZOMBIE_CHILD_BODY.loadMeshData();
		ENTITY_RAVAGER.loadMeshData();
		ENTITY_VEX.loadMeshData();
		ENTITY_PIGLIN.loadMeshData();
		ENTITY_PIGLIN_CHILD.loadMeshData();
		ENTITY_ILLAGER.loadMeshData();
		ENTITY_HOGLIN.loadMeshData();
		ENTITY_HOGLIN_CHILD.loadMeshData();
		
		ITEM_HELMET.loadMeshData();
		ITEM_CHESTPLATE.loadMeshData();
		ITEM_LEGGINS.loadMeshData();
		ITEM_LEGGINS_CLOTH.loadMeshData();
		ITEM_BOOTS.loadMeshData();
		ITEM_HELMET_FOR_CHILD.loadMeshData();
		ITEM_CHESTPLATE_FOR_CHILD.loadMeshData();
		ITEM_LEGGINS_FOR_CHILD.loadMeshData();
		ITEM_BOOTS_FOR_CHILD.loadMeshData();
	}
}