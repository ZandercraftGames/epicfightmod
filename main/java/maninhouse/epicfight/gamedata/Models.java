package maninhouse.epicfight.gamedata;

import maninhouse.epicfight.client.model.ClientModel;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.model.Model;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class Models<T extends Model>
{
	@OnlyIn(Dist.CLIENT)
	public static final ClientModels LOGICAL_CLIENT;
	
	public static final ServerModels LOGICAL_SERVER;
	
	static
	{
		if(FMLEnvironment.dist == Dist.CLIENT)
		{
			LOGICAL_CLIENT = new ClientModels();
			LOGICAL_SERVER = new ServerModels();
		}
		else
		{
			LOGICAL_CLIENT = null;
			LOGICAL_SERVER = new ServerModels();
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public static class ClientModels extends Models<ClientModel>
	{
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
	
	public static class ServerModels extends Models<Model>
	{
		public ServerModels()
		{
			ENTITY_BIPED = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/biped.dae"));
			ENTITY_BIPED_SLIM_ARM = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/biped_slim_arm.dae"));
			ENTITY_ZOMBIE_CHILD = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_child.dae"));
			ENTITY_VILLAGER_ZOMBIE = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_villager.dae"));
			ENTITY_VILLAGER_ZOMBIE_BODY = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_villager_body.dae"));
			ENTITY_CREEPER = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/creeper.dae"));
			ENTITY_ENDERMAN = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/enderman.dae"));
			ENTITY_SKELETON = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/skeleton.dae"));
			ENTITY_SPIDER = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/spider.dae"));
			ENTITY_GOLEM = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/iron_golem.dae"));
			ENTITY_ILLAGER = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/illager.dae"));
			ENTITY_WITCH = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/witch.dae"));
			ENTITY_VILLAGER_ZOMBIE_CHILD = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_villager_child.dae"));
			ENTITY_VILLAGER_ZOMBIE_CHILD_BODY = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/zombie_villager_child_body.dae"));
			ENTITY_RAVAGER = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/ravager.dae"));
			ENTITY_VEX = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/vex.dae"));
			ENTITY_PIGLIN = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/piglin.dae"));
			ENTITY_PIGLIN_CHILD = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/piglin_child.dae"));
			ENTITY_HOGLIN = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/hoglin.dae"));
			ENTITY_HOGLIN_CHILD = new Model(new ResourceLocation(EpicFightMod.MODID, "models/entity/hoglin_child.dae"));
		}
	}
	
	/** 
	 * 0Root 1Thigh_R 2Leg_R 3Knee_R 4Thigh_L 5Leg_L 6Knee_L 7Torso 8Chest 9Head 10Shoulder_R 11Arm_R 12Hand_R 13Elbow_R 14Tool_R 15Shoulder_L 16Arm_L
	 * 17Hand_L 18Elbow_L 19Tool_L 
	 **/
	public T ENTITY_BIPED;
	public T ENTITY_BIPED_SLIM_ARM;
	public T ENTITY_ZOMBIE_CHILD;
	public T ENTITY_VILLAGER_ZOMBIE;
	public T ENTITY_VILLAGER_ZOMBIE_BODY;
	public T ENTITY_CREEPER;
	public T ENTITY_ENDERMAN;
	public T ENTITY_SKELETON;
	public T ENTITY_SPIDER;
	public T ENTITY_GOLEM;
	public T ENTITY_ILLAGER;
	public T ENTITY_WITCH;
	public T ENTITY_VILLAGER_ZOMBIE_CHILD;
	public T ENTITY_VILLAGER_ZOMBIE_CHILD_BODY;
	public T ENTITY_RAVAGER;
	public T ENTITY_VEX;
	public T ENTITY_PIGLIN;
	public T ENTITY_PIGLIN_CHILD;
	public T ENTITY_HOGLIN;
	public T ENTITY_HOGLIN_CHILD;
	
	public void buildArmatureData()
	{
		ENTITY_BIPED.loadArmatureData();
		ENTITY_BIPED_SLIM_ARM.loadArmatureData(ENTITY_BIPED.getArmature());
		ENTITY_VILLAGER_ZOMBIE.loadArmatureData(ENTITY_BIPED.getArmature());
		ENTITY_ZOMBIE_CHILD.loadArmatureData();
		ENTITY_VILLAGER_ZOMBIE_CHILD.loadArmatureData(ENTITY_ZOMBIE_CHILD.getArmature());
		ENTITY_CREEPER.loadArmatureData();
		ENTITY_SKELETON.loadArmatureData();
		ENTITY_ENDERMAN.loadArmatureData();
		ENTITY_SPIDER.loadArmatureData();
		ENTITY_GOLEM.loadArmatureData();
		ENTITY_RAVAGER.loadArmatureData();
		ENTITY_VEX.loadArmatureData();
		ENTITY_PIGLIN.loadArmatureData();
		ENTITY_PIGLIN_CHILD.loadArmatureData();
		ENTITY_ILLAGER.loadArmatureData(ENTITY_BIPED.getArmature());
		ENTITY_WITCH.loadArmatureData(ENTITY_BIPED.getArmature());
		ENTITY_HOGLIN.loadArmatureData();
		ENTITY_HOGLIN_CHILD.loadArmatureData();
	}
}