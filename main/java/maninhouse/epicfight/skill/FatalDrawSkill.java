package maninhouse.epicfight.skill;

import maninhouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninhouse.epicfight.gamedata.Animations;
import maninhouse.epicfight.gamedata.Skills;
import maninhouse.epicfight.network.ModNetworkManager;
import maninhouse.epicfight.network.server.STCResetBasicAttackCool;
import net.minecraft.network.PacketBuffer;

public class FatalDrawSkill extends SelectiveAttackSkill
{
	public FatalDrawSkill()
	{
		super(SkillSlot.WEAPON_SPECIAL_ATTACK, 30.0F, Skills.skillTexture("fatal_draw"),
				(executer)->{
					return executer.getOriginalEntity().isSprinting() ? 1 : 0;
				}, Animations.FATAL_DRAW, Animations.FATAL_DRAW_DASH);
	}
	
	@Override
	public void executeOnServer(ServerPlayerData executer, PacketBuffer args)
	{
		boolean isSheathed = executer.getSkill(SkillSlot.WEAPON_GIMMICK).getVariableNBT().getBoolean("sheath");
		
		if(isSheathed)
			executer.playAnimationSynchronize(this.attackAnimations[this.getAnimationInCondition(executer)], -0.666F);
		else
			executer.playAnimationSynchronize(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
		
		ModNetworkManager.sendToPlayer(new STCResetBasicAttackCool(), executer.getOriginalEntity());
	}
}