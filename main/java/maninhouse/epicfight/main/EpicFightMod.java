package maninhouse.epicfight.main;

import java.lang.reflect.Field;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.ProviderEntity;
import maninhouse.epicfight.capabilities.ProviderItem;
import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.client.events.ClientEvents;
import maninhouse.epicfight.client.events.RegistryClientEvent;
import maninhouse.epicfight.client.events.engine.ControllEngine;
import maninhouse.epicfight.client.events.engine.RenderEngine;
import maninhouse.epicfight.client.input.ModKeys;
import maninhouse.epicfight.client.shader.Shaders;
import maninhouse.epicfight.config.Configuration;
import maninhouse.epicfight.entity.ai.attribute.ModAttributes;
import maninhouse.epicfight.events.CapabilityEvent;
import maninhouse.epicfight.events.EntityEvents;
import maninhouse.epicfight.events.PlayerEvents;
import maninhouse.epicfight.events.RegistryEvents;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.gamedata.Skills;
import maninhouse.epicfight.item.ModItems;
import maninhouse.epicfight.network.ModNetworkManager;
import maninhouse.epicfight.particle.Particles;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod("epicfight")
public class EpicFightMod
{
	public static final String MODID = "epicfight";
	public static final String CONFIG_FILE_PATH = EpicFightMod.MODID + ".toml";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	@OnlyIn(Dist.CLIENT)
	public static Field mainThreadTaskQueue;
	
    public EpicFightMod()
    {
    	ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.CONFIG, CONFIG_FILE_PATH);
    	
    	if(isPhysicalClient())
    	{
    		Models.LOGICAL_CLIENT.buildArmatureData();
    		Models.LOGICAL_SERVER.buildArmatureData();
    		mainThreadTaskQueue = ObfuscationReflectionHelper.findField(Minecraft.class, "field_213275_aU");
    	}
    	else
    		Models.LOGICAL_SERVER.buildArmatureData();
    	
    	Animations.registerAnimations(FMLEnvironment.dist);
    	
    	Skills.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonStuff);
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EntityEvents.class);
        MinecraftForge.EVENT_BUS.register(RegistryEvents.class);
        MinecraftForge.EVENT_BUS.register(CapabilityEvent.class);
        MinecraftForge.EVENT_BUS.register(PlayerEvents.class);
        
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModAttributes.ATTRIBUTES.register(bus);
        ModItems.ITEMS.register(bus);
        Particles.PARTICLES.register(bus);
        Configuration.loadConfig(Configuration.CONFIG, FMLPaths.CONFIGDIR.get().resolve(CONFIG_FILE_PATH).toString());
    }
    
    @SuppressWarnings("unchecked")
	private void doClientStuff(final FMLClientSetupEvent event)
    {
    	new ClientEngine();
		
		try
		{
			((Queue<Runnable>) mainThreadTaskQueue.get(Minecraft.getInstance())).add(() -> {
				Shaders.loadShaders();
				Models.LOGICAL_CLIENT.buildMeshData();
				ClientEngine.INSTANCE.renderEngine.buildRenderer();
			});
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		ProviderEntity.makeMapClient();
		ModKeys.registerKeys();
		Skills.setTooltips();
		MinecraftForge.EVENT_BUS.register(ControllEngine.Events.class);
        MinecraftForge.EVENT_BUS.register(RenderEngine.Events.class);
        MinecraftForge.EVENT_BUS.register(RegistryClientEvent.class);
        MinecraftForge.EVENT_BUS.register(ClientEvents.class);
    }
    
    private void doCommonStuff(final FMLCommonSetupEvent event)
    {
    	ModCapabilities.registerCapabilities();
    	ModNetworkManager.registerPackets();
    	ProviderItem.makeMap();
    	ProviderEntity.makeMap();
    	ModAttributes.addVanillaMobAttributeMap();
    }
    
    public static boolean isPhysicalClient()
    {
    	return FMLEnvironment.dist == Dist.CLIENT;
    }
}