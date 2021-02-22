package maninhouse.epicfight.config;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.electronwill.nightconfig.core.AbstractCommentedConfig;
import com.google.common.collect.Lists;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.capabilities.item.ModWeaponCapability;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Colliders;
import maninhouse.epicfight.gamedata.Skills;
import maninhouse.epicfight.gamedata.Sounds;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class CapabilityConfig
{
	public static final List<CustomWeaponConfig> CUSTOM_WEAPON_LISTS = Lists.<CustomWeaponConfig>newArrayList();
	
	public static void init(ForgeConfigSpec.Builder config, Map<String, Object> dynamicConfigMap)
	{
		String keyName = "custom_weaponry";
		
		if(dynamicConfigMap.get(keyName) != null)
		{
			List<Map.Entry<String, Object>> entries = new LinkedList<>(((AbstractCommentedConfig)dynamicConfigMap.get(keyName)).valueMap().entrySet());
		    Collections.sort(entries, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));
			for(Map.Entry<String, Object> entry : entries)
			{
				if(entry.getKey() == "sample")
					continue;
				ConfigValue<String> registryName = config.define(String.format("%s.%s.registry_name", keyName, entry.getKey()), "broken_item");
				ConfigValue<WeaponType> weaponType = config.defineEnum(String.format("%s.%s.weapon_type", keyName, entry.getKey()), WeaponType.SWORD);
				ConfigValue<Double> impactOnehand;
				ConfigValue<Double> armorIgnoranceOnehand;
				ConfigValue<Integer> hitAtOnceOnehand;
				ConfigValue<Double> impactTwohand;
				ConfigValue<Double> armorIgnoranceTwohand;
				ConfigValue<Integer> hitAtOnceTwohand;
				
				boolean containOnehand = ((AbstractCommentedConfig)entry.getValue()).contains("onehand");
				boolean containTwohand = ((AbstractCommentedConfig)entry.getValue()).contains("twohand");
				
				if(!(containOnehand || containTwohand))
				{
					impactOnehand = config.define(String.format("%s.%s.impact", keyName, entry.getKey()), 0.5D);
					armorIgnoranceOnehand = config.define(String.format("%s.%s.armor_ignorance", keyName, entry.getKey()), 0.0D);
					hitAtOnceOnehand = config.define(String.format("%s.%s.hit_at_once", keyName, entry.getKey()), 1);
					impactTwohand = config.define(String.format("%s.%s.impact", keyName, entry.getKey()), 0.5D);
					armorIgnoranceTwohand = config.define(String.format("%s.%s.armor_ignorance", keyName, entry.getKey()), 0.0D);
					hitAtOnceTwohand = config.define(String.format("%s.%s.hit_at_once", keyName, entry.getKey()), 1);
				}
				else
				{
					impactOnehand = config.define(String.format("%s.%s.onehand.impact", keyName, entry.getKey()), 0.5D);
					armorIgnoranceOnehand = config.define(String.format("%s.%s.onehand.armor_ignorance", keyName, entry.getKey()), 0.0D);
					hitAtOnceOnehand = config.define(String.format("%s.%s.onehand.hit_at_once", keyName, entry.getKey()), 1);
					impactTwohand = config.define(String.format("%s.%s.twohand.impact", keyName, entry.getKey()), 0.5D);
					armorIgnoranceTwohand = config.define(String.format("%s.%s.twohand.armor_ignorance", keyName, entry.getKey()), 0.0D);
					hitAtOnceTwohand = config.define(String.format("%s.%s.twohand.hit_at_once", keyName, entry.getKey()), 1);
				}
				
				CUSTOM_WEAPON_LISTS.add(new CustomWeaponConfig(
						registryName, weaponType, impactOnehand, armorIgnoranceOnehand, hitAtOnceOnehand, impactTwohand, armorIgnoranceTwohand, hitAtOnceTwohand
				));
			}
		}
	}
	
	public static class CustomWeaponConfig
	{
		private ConfigValue<String> registryName;
		private ConfigValue<WeaponType> weaponType;
		private ConfigValue<Double> impactOnehand;
		private ConfigValue<Double> armorIgnoranceOnehand;
		private ConfigValue<Integer> hitAtOnceOnehand;
		private ConfigValue<Double> impactTwohand;
		private ConfigValue<Double> armorIgnoranceTwohand;
		private ConfigValue<Integer> hitAtOnceTwohand;
		
		public CustomWeaponConfig(ConfigValue<String> registryName, ConfigValue<WeaponType> weaponType,
				ConfigValue<Double> impactOnehand, ConfigValue<Double> armorIgnoranceOnehand, ConfigValue<Integer> hitAtOnceOnehand,
				ConfigValue<Double> impactTwohand, ConfigValue<Double> armorIgnoranceTwohand, ConfigValue<Integer> hitAtOnceTwohand)
		{
			this.registryName = registryName;
			this.weaponType = weaponType;
			this.impactOnehand = impactOnehand;
			this.armorIgnoranceOnehand = armorIgnoranceOnehand;
			this.hitAtOnceOnehand = hitAtOnceOnehand;
			this.impactTwohand = impactTwohand;
			this.armorIgnoranceTwohand = armorIgnoranceTwohand;
			this.hitAtOnceTwohand = hitAtOnceTwohand;
		}
		
		public String getRegistryName()
		{
			return this.registryName.get();
		}
		
		public WeaponType getWeaponType()
		{
			return this.weaponType.get();
		}
		
		public Double getImpactOnehand()
		{
			return this.impactOnehand.get();
		}
		
		public Double getArmorIgnoranceOnehand()
		{
			return this.armorIgnoranceOnehand.get();
		}
		
		public Integer getHitAtOnceOnehand()
		{
			return this.hitAtOnceOnehand.get();
		}
		
		public Double getImpactTwohand()
		{
			return this.impactTwohand.get();
		}
		
		public Double getArmorIgnoranceTwohand()
		{
			return this.armorIgnoranceTwohand.get();
		}
		
		public Integer getHitAtOnceTwohand()
		{
			return this.hitAtOnceTwohand.get();
		}
	}
	
	public static enum WeaponType
	{
		AXE(()->{
			ModWeaponCapability cap = new ModWeaponCapability(Skills.GUILLOTINE_AXE, null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.tools, 0.0, 0.0, 1, false, false);
			cap.addAutoAttackCombos(Animations.AXE_AUTO1);
			cap.addAutoAttackCombos(Animations.AXE_AUTO2);
			cap.addAutoAttackCombos(Animations.AXE_DASH);
	    	cap.addMountAttackCombos(Animations.SWORD_MOUNT_ATTACK);
			return cap;
		}),
		FIST(()->{
			ModWeaponCapability cap = new ModWeaponCapability(null, null, Sounds.WHOOSH, Sounds.BLUNT_HIT, Colliders.fist, 0.0, 0.0, 1, false, false);
			cap.addAutoAttackCombos(Animations.FIST_AUTO_1);
			cap.addAutoAttackCombos(Animations.FIST_AUTO_2);
			cap.addAutoAttackCombos(Animations.FIST_AUTO_3);
			cap.addAutoAttackCombos(Animations.FIST_DASH);
			return cap;
		}),
		HOE(()->{
			ModWeaponCapability cap = new ModWeaponCapability(null, null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.tools, 0.0, 0.0, 1, false, false);
			cap.addAutoAttackCombos(Animations.TOOL_AUTO_1);
			cap.addAutoAttackCombos(Animations.TOOL_AUTO_2);
			cap.addAutoAttackCombos(Animations.TOOL_DASH);
	    	cap.addMountAttackCombos(Animations.SWORD_MOUNT_ATTACK);
			return cap;
		}),
		PICKAXE(()->{
			ModWeaponCapability cap = new ModWeaponCapability(null, null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.tools, 0.0, 0.0, 1, false, false);
			cap.addAutoAttackCombos(Animations.AXE_AUTO1);
			cap.addAutoAttackCombos(Animations.AXE_AUTO2);
			cap.addAutoAttackCombos(Animations.AXE_DASH);
	    	cap.addMountAttackCombos(Animations.SWORD_MOUNT_ATTACK);
			return cap;
		}),
		SHOVEL(()->{
			ModWeaponCapability cap = new ModWeaponCapability(null, null, Sounds.WHOOSH, Sounds.BLUNT_HIT, Colliders.tools, 0.0, 0.0, 1, false, false);
			cap.addAutoAttackCombos(Animations.AXE_AUTO1);
			cap.addAutoAttackCombos(Animations.AXE_AUTO2);
			cap.addAutoAttackCombos(Animations.AXE_DASH);
	    	cap.addMountAttackCombos(Animations.SWORD_MOUNT_ATTACK);
			return cap;
		}),
		SWORD(()->{
			ModWeaponCapability cap = new ModWeaponCapability(Skills.SWEEPING_EDGE, null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.sword, 0.0, 0.0, 1, false, false);
			cap.addAutoAttackCombos(Animations.SWORD_AUTO_1);
			cap.addAutoAttackCombos(Animations.SWORD_AUTO_2);
			cap.addAutoAttackCombos(Animations.SWORD_AUTO_3);
			cap.addAutoAttackCombos(Animations.SWORD_DASH);
	    	cap.addMountAttackCombos(Animations.SWORD_MOUNT_ATTACK);
			return cap;
		}),
		SPEAR(()->{
			ModWeaponCapability cap = new ModWeaponCapability(Skills.SLAUGHTER_STANCE, null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.spearNarrow, 0.0, 0.0, 1, false, true);
			cap.addAutoAttackCombos(Animations.SPEAR_ONEHAND_AUTO);
			cap.addAutoAttackCombos(Animations.SPEAR_DASH);
			cap.addTwohandAutoAttackCombos(Animations.SPEAR_TWOHAND_AUTO_1);
			cap.addTwohandAutoAttackCombos(Animations.SPEAR_TWOHAND_AUTO_2);
			cap.addTwohandAutoAttackCombos(Animations.SPEAR_DASH);
			cap.addMountAttackCombos(Animations.SPEAR_MOUNT_ATTACK);
			cap.addLivingMotionChanger(LivingMotion.RUNNING, Animations.BIPED_RUN_HELDING_WEAPON);
			return cap;
		}),
		GREATSWORD(()->{
			ModWeaponCapability cap = new ModWeaponCapability(Skills.GIANT_WHIRLWIND, null, Sounds.WHOOSH_BIG, Sounds.BLADE_HIT, Colliders.greatSword, 0.0, 0.0, 1, true, true);
			cap.addTwohandAutoAttackCombos(Animations.GREATSWORD_AUTO_1);
			cap.addTwohandAutoAttackCombos(Animations.GREATSWORD_AUTO_2);
			cap.addTwohandAutoAttackCombos(Animations.GREATSWORD_DASH);
			cap.addLivingMotionChanger(LivingMotion.IDLE, Animations.BIPED_IDLE_MASSIVE_HELD);
			cap.addLivingMotionChanger(LivingMotion.WALKING, Animations.BIPED_WALK_MASSIVE_HELD);
			cap.addLivingMotionChanger(LivingMotion.RUNNING, Animations.BIPED_RUN_MASSIVE_HELD);
	    	cap.addLivingMotionChanger(LivingMotion.JUMPING, Animations.BIPED_JUMP_MASSIVE_HELD);
	    	cap.addLivingMotionChanger(LivingMotion.KNEELING, Animations.BIPED_KNEEL_MASSIVE_HELD);
	    	cap.addLivingMotionChanger(LivingMotion.SNEAKING, Animations.BIPED_SNEAK_MASSIVE_HELD);
			return cap;
		}),
		KATANA(()->{
			ModWeaponCapability cap = new ModWeaponCapability(Skills.SWEEPING_EDGE, null, Sounds.WHOOSH, Sounds.BLADE_HIT, Colliders.katana, 0.0, 0.0, 1, false, true);
			cap.addTwohandAutoAttackCombos(Animations.KATANA_AUTO_1);
			cap.addTwohandAutoAttackCombos(Animations.KATANA_AUTO_2);
			cap.addTwohandAutoAttackCombos(Animations.KATANA_AUTO_3);
			cap.addTwohandAutoAttackCombos(Animations.SWORD_DASH);
			cap.addMountAttackCombos(Animations.SWORD_MOUNT_ATTACK);
			cap.addLivingMotionChanger(LivingMotion.IDLE, Animations.BIPED_IDLE_UNSHEATHING);
			cap.addLivingMotionChanger(LivingMotion.WALKING, Animations.BIPED_WALK_UNSHEATHING);
			cap.addLivingMotionChanger(LivingMotion.RUNNING, Animations.BIPED_RUN_UNSHEATHING);
			return cap;
		});
		
		Supplier<ModWeaponCapability> capabilitySupplier;
		
		WeaponType(Supplier<ModWeaponCapability> capabilitySupplier)
		{
			this.capabilitySupplier = capabilitySupplier;
		}
		
		public ModWeaponCapability get()
		{
			return this.capabilitySupplier.get();
		}
	}
}