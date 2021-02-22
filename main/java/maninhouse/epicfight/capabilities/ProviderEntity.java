package maninhouse.epicfight.capabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import maninhouse.epicfight.capabilities.entity.CapabilityEntity;
import maninhouse.epicfight.capabilities.entity.mob.CaveSpiderData;
import maninhouse.epicfight.capabilities.entity.mob.CreeperData;
import maninhouse.epicfight.capabilities.entity.mob.DrownedData;
import maninhouse.epicfight.capabilities.entity.mob.EndermanData;
import maninhouse.epicfight.capabilities.entity.mob.EvokerData;
import maninhouse.epicfight.capabilities.entity.mob.HoglinData;
import maninhouse.epicfight.capabilities.entity.mob.IronGolemData;
import maninhouse.epicfight.capabilities.entity.mob.PiglinBruteData;
import maninhouse.epicfight.capabilities.entity.mob.PiglinData;
import maninhouse.epicfight.capabilities.entity.mob.PillagerData;
import maninhouse.epicfight.capabilities.entity.mob.RavagerData;
import maninhouse.epicfight.capabilities.entity.mob.SkeletonData;
import maninhouse.epicfight.capabilities.entity.mob.SpiderData;
import maninhouse.epicfight.capabilities.entity.mob.StrayData;
import maninhouse.epicfight.capabilities.entity.mob.VexData;
import maninhouse.epicfight.capabilities.entity.mob.VindicatorData;
import maninhouse.epicfight.capabilities.entity.mob.WitchData;
import maninhouse.epicfight.capabilities.entity.mob.WitherSkeletonData;
import maninhouse.epicfight.capabilities.entity.mob.ZoglinData;
import maninhouse.epicfight.capabilities.entity.mob.ZombieData;
import maninhouse.epicfight.capabilities.entity.mob.ZombieVillagerData;
import maninhouse.epicfight.capabilities.entity.mob.ZombifiedPiglinData;
import maninhouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninhouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninhouse.epicfight.client.capabilites.entity.RemoteClientPlayerData;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZoglinEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

public class ProviderEntity implements ICapabilityProvider, NonNullSupplier<CapabilityEntity<?>>
{
	private static final Map<Class<? extends Entity>, Supplier<CapabilityEntity<?>>> capabilityMap = new HashMap<Class<? extends Entity>, Supplier<CapabilityEntity<?>>> ();
	
	public static void makeMap()
	{
		capabilityMap.put(ServerPlayerEntity.class, ServerPlayerData::new);
		capabilityMap.put(ZombieEntity.class, ZombieData<ZombieEntity>::new);
		capabilityMap.put(CreeperEntity.class, CreeperData::new);
		capabilityMap.put(EndermanEntity.class, EndermanData::new);
		capabilityMap.put(SkeletonEntity.class, SkeletonData<SkeletonEntity>::new);
		capabilityMap.put(WitherSkeletonEntity.class, WitherSkeletonData::new);
		capabilityMap.put(StrayEntity.class, StrayData::new);
		capabilityMap.put(ZombifiedPiglinEntity.class, ZombifiedPiglinData::new);
		capabilityMap.put(ZombieVillagerEntity.class, ZombieVillagerData::new);
		capabilityMap.put(HuskEntity.class, ZombieData<HuskEntity>::new);
		capabilityMap.put(SpiderEntity.class, SpiderData::new);
		capabilityMap.put(CaveSpiderEntity.class, CaveSpiderData::new);
		capabilityMap.put(IronGolemEntity.class, IronGolemData::new);
		capabilityMap.put(VindicatorEntity.class, VindicatorData::new);
		capabilityMap.put(EvokerEntity.class, EvokerData::new);
		capabilityMap.put(WitchEntity.class, WitchData::new);
		capabilityMap.put(DrownedEntity.class, DrownedData::new);
		capabilityMap.put(PillagerEntity.class, PillagerData::new);
		capabilityMap.put(RavagerEntity.class, RavagerData::new);
		capabilityMap.put(VexEntity.class, VexData::new);
		capabilityMap.put(PiglinEntity.class, PiglinData::new);
		capabilityMap.put(PiglinBruteEntity.class, PiglinBruteData::new);
		capabilityMap.put(HoglinEntity.class, HoglinData::new);
		capabilityMap.put(ZoglinEntity.class, ZoglinData::new);
	}
	
	public static void makeMapClient()
	{
		capabilityMap.put(ClientPlayerEntity.class, ClientPlayerData::new);
		capabilityMap.put(RemoteClientPlayerEntity.class, RemoteClientPlayerData<RemoteClientPlayerEntity>::new);
	}
	
	private CapabilityEntity<?> capability;
	private LazyOptional<CapabilityEntity<?>> optional = LazyOptional.of(this);
	
	public ProviderEntity(Entity entity)
	{
		if(capabilityMap.containsKey(entity.getClass()))
		{
			capability = capabilityMap.get(entity.getClass()).get();
		}
	}
	
	public boolean hasCapability()
	{
		return capability != null;
	}
	
	@Override
	public CapabilityEntity<?> get()
	{
		return this.capability;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return cap == ModCapabilities.CAPABILITY_ENTITY ? optional.cast() :  LazyOptional.empty();
	}
}