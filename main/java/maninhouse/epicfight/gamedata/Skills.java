package maninhouse.epicfight.gamedata;

import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.skill.DodgeSkill;
import maninhouse.epicfight.skill.FatalDrawSkill;
import maninhouse.epicfight.skill.KatanaPassive;
import maninhouse.epicfight.skill.SelectiveAttackSkill;
import maninhouse.epicfight.skill.Skill;
import maninhouse.epicfight.skill.SkillSlot;
import maninhouse.epicfight.skill.SpecialAttackSkill;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class Skills
{
	public static Skill ROLL;
	public static Skill GUILLOTINE_AXE;
	public static Skill SWEEPING_EDGE;
	public static Skill DANCING_EDGE;
	public static Skill SLAUGHTER_STANCE;
	public static Skill GIANT_WHIRLWIND;
	public static Skill FATAL_DRAW;
	public static Skill KATANA_GIMMICK;
	
	public static void init()
	{
		ROLL = new DodgeSkill(SkillSlot.DODGE, 2.0F, skillTexture("roll"), Animations.BIPED_ROLL_FORWARD, Animations.BIPED_ROLL_BACKWARD);
		SWEEPING_EDGE = new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 30.0F, skillTexture("spinattack"), Animations.SWEEPING_EDGE);
		DANCING_EDGE = new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 30.0F, skillTexture("dancing_edge"), Animations.DANCING_EDGE);
		GUILLOTINE_AXE = new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 20.0F, skillTexture("guillotine_axe"), Animations.GUILLOTINE_AXE);
		SLAUGHTER_STANCE = new SelectiveAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 40.0F, skillTexture("slaughter_stance"),
				(executer)->{
					return executer.getOriginalEntity().getHeldItemOffhand().isEmpty() ? 1 : 0;
				}, Animations.SPEAR_THRUST, Animations.SPEAR_SLASH);
		GIANT_WHIRLWIND = new SpecialAttackSkill(SkillSlot.WEAPON_SPECIAL_ATTACK, 100.0F, skillTexture("giant_whirlwind"), Animations.GIANT_WHIRLWIND);
		FATAL_DRAW = new FatalDrawSkill();
		KATANA_GIMMICK = new KatanaPassive();
	}
	
	public static void setTooltips()
	{
		GUILLOTINE_AXE
				.setTooltip(TextFormatting.WHITE + "Guillotine Axe" + TextFormatting.AQUA + "[20]").setTooltip("")
				.setTooltip(TextFormatting.DARK_GRAY + "Heft the axe a moment then smash the target in front").setTooltip("")
				.setTooltip(TextFormatting.DARK_RED + "250%" + TextFormatting.DARK_GRAY + " damage")
				.setTooltip(TextFormatting.GOLD + "40%" + TextFormatting.DARK_GRAY + " armor ignore")
				.setTooltip(TextFormatting.DARK_GRAY + "hit" + TextFormatting.WHITE + " 1 " + TextFormatting.DARK_GRAY + "enemies");
		
		SWEEPING_EDGE
				.setTooltip(TextFormatting.WHITE + "Sweeping Edge" + TextFormatting.AQUA + "[30]").setTooltip("")
				.setTooltip(TextFormatting.DARK_GRAY + "Spin the sword and cut down enemies.").setTooltip("")
				.setTooltip(TextFormatting.DARK_RED + "200%" + TextFormatting.DARK_GRAY + " damage")
				.setTooltip(TextFormatting.GOLD + "25%" + TextFormatting.DARK_GRAY + " armor ignore")
				.setTooltip(TextFormatting.DARK_GRAY + "hit" + TextFormatting.WHITE + " 3 " + TextFormatting.DARK_GRAY + "enemies");
		
		DANCING_EDGE
				.setTooltip(TextFormatting.WHITE + "Dancing Edge" + TextFormatting.AQUA + "[30]").setTooltip("")
				.setTooltip(TextFormatting.DARK_GRAY + "Charge forward and swing your sword three times quickly.").setTooltip("")
				.setTooltip(TextFormatting.DARK_RED + "100%" + TextFormatting.DARK_GRAY + " damage")
				.setTooltip(TextFormatting.GOLD + "1.5" + TextFormatting.DARK_GRAY + " impact")
				.setTooltip(TextFormatting.DARK_GRAY + "hit" + TextFormatting.WHITE + " 3 " + TextFormatting.DARK_GRAY + "enemies");
		
		SLAUGHTER_STANCE
				.setTooltip(TextFormatting.WHITE + "Slaughter Stance" + TextFormatting.AQUA + "[40]").setTooltip("")
				.setTooltip(TextFormatting.DARK_GRAY + "When used with two hands, it cuts enemies in wide range, and when used with one hand, it pierces enemies in front three times.").setTooltip("")
				.setTooltip(TextFormatting.DARK_RED + "125%/100%" + TextFormatting.DARK_GRAY + " damage")
				.setTooltip(TextFormatting.GOLD + "2.0/0.5" + TextFormatting.DARK_GRAY + " impact")
				.setTooltip(TextFormatting.DARK_GRAY + "hit" + TextFormatting.WHITE + " 8/1 " + TextFormatting.DARK_GRAY + "enemies");
		
		GIANT_WHIRLWIND
				.setTooltip(TextFormatting.WHITE + "Giant Whirlwind" + TextFormatting.AQUA + "[100]").setTooltip("")
				.setTooltip(TextFormatting.DARK_GRAY + "Turns massively three times and cuts down enemies.").setTooltip("")
				.setTooltip(TextFormatting.DARK_RED + "100%" + TextFormatting.DARK_GRAY + " damage")
				.setTooltip(TextFormatting.GOLD + "4.8" + TextFormatting.DARK_GRAY + " impact")
				.setTooltip(TextFormatting.DARK_GRAY + "hit" + TextFormatting.WHITE + " 5 " + TextFormatting.DARK_GRAY + "enemies");
		
		FATAL_DRAW
				.setTooltip(TextFormatting.WHITE + "Fatal Draw" + TextFormatting.AQUA + "[50]").setTooltip("")
				.setTooltip(TextFormatting.DARK_GRAY + "Draw a sword at an invisible speed and cut down enemies. when it used during you're running, it dashes at very high speed and slice enemies in your path.").setTooltip("")
				.setTooltip(TextFormatting.DARK_RED + "200%" + TextFormatting.DARK_GRAY + " damage")
				.setTooltip(TextFormatting.GOLD + "70%" + TextFormatting.DARK_GRAY + " armor ignore")
				.setTooltip(TextFormatting.DARK_GRAY + "hit" + TextFormatting.WHITE + " 8 " + TextFormatting.DARK_GRAY + "enemies")
				.setTooltip(TextFormatting.DARK_GRAY + "make enemies" + TextFormatting.WHITE + " HOLD");
	}
	
	public static ResourceLocation skillTexture(String name)
	{
		return new ResourceLocation(EpicFightMod.MODID, "textures/gui/skills/" + name + ".png");
	}
}