package maninhouse.epicfight.config;

import java.io.File;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import maninhouse.epicfight.config.CapabilityConfig.WeaponType;
import maninhouse.epicfight.main.EpicFightMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber(modid=EpicFightMod.MODID)
public class Configuration
{
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;
	
	static
	{
		CommentedFileConfig file = CommentedFileConfig.builder(new File(FMLPaths.CONFIGDIR.get().resolve(EpicFightMod.CONFIG_FILE_PATH).toString())).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		
		String keyName = "custom_weaponry";
		if(file.valueMap().get(keyName) == null)
		{
			BUILDER.define(String.format("%s.%s.registry_name", keyName, "sample"), "epicfight:registryname");
			BUILDER.defineEnum(String.format("%s.%s.weapon_type", keyName, "sample"), WeaponType.SWORD);
			BUILDER.define(String.format("%s.%s.impact", keyName, "sample"), 0.5D);
			BUILDER.define(String.format("%s.%s.armor_ignorance", keyName, "sample"), 0.0D);
			BUILDER.define(String.format("%s.%s.hit_at_once", keyName, "sample"), 1);
		}
		
		CapabilityConfig.init(BUILDER, file.valueMap());
		CONFIG = BUILDER.build();
	}
	
	public static void loadConfig(ForgeConfigSpec config, String path)
	{
		CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
		EpicFightMod.LOGGER.info("Load Configuration File");
	}
}