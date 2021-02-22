package maninhouse.epicfight.skill;

import java.util.function.Function;

import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninhouse.epicfight.network.ModNetworkManager;
import maninhouse.epicfight.network.server.STCResetBasicAttackCool;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class SelectiveAttackSkill extends SpecialAttackSkill
{
	protected final StaticAnimation[] attackAnimations;
	protected final Function<ServerPlayerData, Integer> selector;
	
	public SelectiveAttackSkill(SkillSlot index, float restriction, ResourceLocation textureLocation, Function<ServerPlayerData, Integer> func, StaticAnimation... animations)
	{
		super(index, restriction, textureLocation, null);
		this.attackAnimations = animations;
		this.selector = func;
	}
	
	public SelectiveAttackSkill(SkillSlot index, float restriction, int duration, ResourceLocation textureLocation, Function<ServerPlayerData, Integer> func, StaticAnimation... animations)
	{
		super(index, restriction, duration, textureLocation, null);
		this.attackAnimations = animations;
		this.selector = func;
	}
	
	@Override
	public void executeOnServer(ServerPlayerData executer, PacketBuffer args)
	{
		executer.playAnimationSynchronize(this.attackAnimations[this.getAnimationInCondition(executer)], 0);
		ModNetworkManager.sendToPlayer(new STCResetBasicAttackCool(), executer.getOriginalEntity());
	}
	
	public int getAnimationInCondition(ServerPlayerData executer)
	{
		return selector.apply(executer);
	}
}