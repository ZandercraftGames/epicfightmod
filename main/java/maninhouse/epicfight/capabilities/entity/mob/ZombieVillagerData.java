package maninhouse.epicfight.capabilities.entity.mob;

import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.model.Model;
import net.minecraft.entity.monster.ZombieVillagerEntity;

public class ZombieVillagerData extends ZombieData<ZombieVillagerEntity>
{
	@Override
	public <M extends Model> M getEntityModel(Models<M> modelDB)
	{
		return this.orgEntity.isChild() ? modelDB.ENTITY_VILLAGER_ZOMBIE_CHILD : modelDB.ENTITY_VILLAGER_ZOMBIE;
	}
}