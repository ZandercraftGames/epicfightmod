package maninhouse.epicfight.entity.ai.attribute;

import java.util.UUID;

import maninhouse.epicfight.main.EpicFightMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.AttributeModifierMap.MutableAttribute;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.monster.AbstractSkeletonEntity;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModAttributes
{
	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, EpicFightMod.MODID);
	
    public static final RegistryObject<Attribute> MAX_STUN_ARMOR = ATTRIBUTES.register("stun_armor", () -> new RangedAttribute("stun armor", 0.0D, 0.0D, 1024.0D).setShouldWatch(true));
    public static final RegistryObject<Attribute> WEIGHT = ATTRIBUTES.register("weight", () -> new RangedAttribute("+weight", 0.0D, 0.0D, 1024.0).setShouldWatch(true));
    public static final RegistryObject<Attribute> HIT_AT_ONCE = ATTRIBUTES.register("hit_at_once", () -> new RangedAttribute("+hit %.0f enemies", 1.0D, 1.0D, 1024.0).setShouldWatch(true));
	public static final RegistryObject<Attribute> IGNORE_DEFENCE = ATTRIBUTES.register("ignore_defence", () -> new RangedAttribute("+ignore defence %.0f%%", 0.0D, 0.0D, 100.0D).setShouldWatch(true));
	public static final RegistryObject<Attribute> IMPACT = ATTRIBUTES.register("impact", () -> new RangedAttribute("+impact %.1f", 0.0D, 0.0D, 1024.0).setShouldWatch(true));
	public static final RegistryObject<Attribute> OFFHAND_ATTACK_DAMAGE = ATTRIBUTES.register("offhand_attack_damage", () -> new RangedAttribute("offhand attack damage", 1.0D, 0.0D, 2048.0D));
	public static final RegistryObject<Attribute> OFFHAND_ATTACK_SPEED = ATTRIBUTES.register("offhand_attack_speed", () -> new RangedAttribute("offhand attack speed", 4.0D, 0.0D, 1024.0D).setShouldWatch(true));
	public static final UUID IGNORE_DEFENCE_ID = UUID.fromString("b0a7436e-5734-11eb-ae93-0242ac130002");
	public static final UUID HIT_AT_ONCE_ID = UUID.fromString("b0a745b2-5734-11eb-ae93-0242ac130002");
	public static final UUID IMPACT_ID = UUID.fromString("b0a746ac-5734-11eb-ae93-0242ac130002");
	public static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
	public static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
	
    public static void addVanillaMobAttributeMap()
    {
		GlobalEntityTypeAttributes.put(EntityType.CAVE_SPIDER, normal(CaveSpiderEntity.registerAttributes()));
		GlobalEntityTypeAttributes.put(EntityType.CREEPER, normal(CreeperEntity.registerAttributes()));
		GlobalEntityTypeAttributes.put(EntityType.DROWNED, normal(ZombieEntity.func_234342_eQ_()));
		GlobalEntityTypeAttributes.put(EntityType.ENDERMAN, withStunArmor(EndermanEntity.func_234287_m_()));
		GlobalEntityTypeAttributes.put(EntityType.EVOKER, normal(EvokerEntity.func_234289_eI_()));
		GlobalEntityTypeAttributes.put(EntityType.HUSK, normal(ZombieEntity.func_234342_eQ_()));
		GlobalEntityTypeAttributes.put(EntityType.IRON_GOLEM, normal(IronGolemEntity.func_234200_m_()));
		GlobalEntityTypeAttributes.put(EntityType.PIGLIN, normal(PiglinEntity.func_234420_eI_()));
		GlobalEntityTypeAttributes.put(EntityType.field_242287_aj, withStunArmor(PiglinBruteEntity.func_242344_eS()));
		GlobalEntityTypeAttributes.put(EntityType.PILLAGER, normal(PillagerEntity.func_234296_eI_()));
		GlobalEntityTypeAttributes.put(EntityType.PLAYER, player(PlayerEntity.func_234570_el_()));
		GlobalEntityTypeAttributes.put(EntityType.RAVAGER, normal(RavagerEntity.func_234297_m_()));
		GlobalEntityTypeAttributes.put(EntityType.SKELETON, normal(AbstractSkeletonEntity.registerAttributes()));
		GlobalEntityTypeAttributes.put(EntityType.SPIDER, normal(SpiderEntity.func_234305_eI_()));
		GlobalEntityTypeAttributes.put(EntityType.STRAY, normal(AbstractSkeletonEntity.registerAttributes()));
		GlobalEntityTypeAttributes.put(EntityType.VEX, normal(VexEntity.func_234321_m_()));
		GlobalEntityTypeAttributes.put(EntityType.VINDICATOR, normal(VindicatorEntity.func_234322_eI_()));
		GlobalEntityTypeAttributes.put(EntityType.WITCH, normal(WitchEntity.func_234323_eI_()));
		GlobalEntityTypeAttributes.put(EntityType.WITHER_SKELETON, withStunArmor(AbstractSkeletonEntity.registerAttributes()));
    	GlobalEntityTypeAttributes.put(EntityType.ZOMBIE, normal(ZombieEntity.func_234342_eQ_()));
    	GlobalEntityTypeAttributes.put(EntityType.ZOMBIE_VILLAGER, normal(ZombieEntity.func_234342_eQ_()));
    	GlobalEntityTypeAttributes.put(EntityType.ZOMBIFIED_PIGLIN, normal(ZombifiedPiglinEntity.func_234352_eU_()));
    	GlobalEntityTypeAttributes.put(EntityType.HOGLIN, normal(ZombifiedPiglinEntity.func_234352_eU_()));
    	GlobalEntityTypeAttributes.put(EntityType.ZOGLIN, normal(ZombifiedPiglinEntity.func_234352_eU_()));
    }
    
    private static AttributeModifierMap normal(MutableAttribute original)
	{
		return original.createMutableAttribute(ModAttributes.WEIGHT.get())
				.createMutableAttribute(ModAttributes.HIT_AT_ONCE.get())
				.createMutableAttribute(ModAttributes.IGNORE_DEFENCE.get())
				.createMutableAttribute(ModAttributes.IMPACT.get())
				.create();
	}
    
    private static AttributeModifierMap withStunArmor(MutableAttribute original)
	{
		return original.createMutableAttribute(ModAttributes.WEIGHT.get())
				.createMutableAttribute(ModAttributes.MAX_STUN_ARMOR.get())
				.createMutableAttribute(ModAttributes.HIT_AT_ONCE.get())
				.createMutableAttribute(ModAttributes.IGNORE_DEFENCE.get())
				.createMutableAttribute(ModAttributes.IMPACT.get())
				.create();
	}
    
    private static AttributeModifierMap player(MutableAttribute original)
	{
		return original.createMutableAttribute(ModAttributes.WEIGHT.get())
				.createMutableAttribute(ModAttributes.MAX_STUN_ARMOR.get())
				.createMutableAttribute(ModAttributes.HIT_AT_ONCE.get())
				.createMutableAttribute(ModAttributes.IGNORE_DEFENCE.get())
				.createMutableAttribute(ModAttributes.IMPACT.get())
				.createMutableAttribute(ModAttributes.OFFHAND_ATTACK_DAMAGE.get())
				.createMutableAttribute(ModAttributes.OFFHAND_ATTACK_SPEED.get())
				.create();
	}
    
    public static AttributeModifier getIgnoreDefenceModifier(double value)
	{
		return new AttributeModifier(ModAttributes.IGNORE_DEFENCE_ID, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}
    
    public static AttributeModifier getHitAtOnceModifier(int value)
	{
    	return new AttributeModifier(ModAttributes.HIT_AT_ONCE_ID, EpicFightMod.MODID + ":weapon_modifier", (double)value, AttributeModifier.Operation.ADDITION);
	}
    
    public static AttributeModifier getImpactModifier(double value)
	{
    	return new AttributeModifier(ModAttributes.IMPACT_ID, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}
    
    public static AttributeModifier getAttackDamageModifier(double value)
	{
    	return new AttributeModifier(ATTACK_DAMAGE_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}
    
    public static AttributeModifier getAttackSpeedModifier(double value)
	{
    	return new AttributeModifier(ATTACK_SPEED_MODIFIER, EpicFightMod.MODID + ":weapon_modifier", value, AttributeModifier.Operation.ADDITION);
	}
}