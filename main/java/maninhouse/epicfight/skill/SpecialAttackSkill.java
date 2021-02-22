package maninhouse.epicfight.skill;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.animation.types.StaticAnimation;
import maninhouse.epicfight.capabilities.entity.LivingData.EntityState;
import maninhouse.epicfight.capabilities.entity.player.PlayerData;
import maninhouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninhouse.epicfight.capabilities.item.CapabilityItem;
import maninhouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninhouse.epicfight.network.ModNetworkManager;
import maninhouse.epicfight.network.client.CTSExecuteSkill;
import maninhouse.epicfight.network.server.STCResetBasicAttackCool;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

public class SpecialAttackSkill extends Skill
{
	protected final StaticAnimation attackAnimation;
	
	public SpecialAttackSkill(SkillSlot index, float restriction, ResourceLocation textureLocation, StaticAnimation animation)
	{
		super(index, restriction, textureLocation);
		this.attackAnimation = animation;
	}
	
	public SpecialAttackSkill(SkillSlot index, float restriction, int duration, ResourceLocation textureLocation, StaticAnimation animation)
	{
		super(index, restriction, duration, true, textureLocation);
		this.attackAnimation = animation;
	}
	
	@Override
	public void executeOnClient(ClientPlayerData executer, PacketBuffer args)
	{
		ModNetworkManager.sendToServer(new CTSExecuteSkill(this.slot.getIndex(), true, args));
	}
	
	@Override
	public void executeOnServer(ServerPlayerData executer, PacketBuffer args)
	{
		executer.playAnimationSynchronize(attackAnimation, 0);
		ModNetworkManager.sendToPlayer(new STCResetBasicAttackCool(), executer.getOriginalEntity());
	}
	
	@Override
	public float getRegenTimePerTick(PlayerData<?> player)
	{
		return 0;
	}
	
	@Override
	public boolean canExecute(PlayerData<?> executer)
	{
		CapabilityItem item = executer.getHeldItemCapability(Hand.MAIN_HAND);
		if(item != null)
		{
			Skill skill = item.getSpecialAttack(executer.getOriginalEntity().getHeldItemOffhand());
			return skill == this && executer.getOriginalEntity().getRidingEntity() == null;
		}
		
		return false;
	}
	
	@Override
	public boolean isExecutableState(PlayerData<?> executer)
	{
		EntityState playerState = executer.getEntityState();
		return !(executer.getOriginalEntity().isElytraFlying() || executer.currentMotion == LivingMotion.FALL || 
			((playerState != EntityState.POST_DELAY && playerState != EntityState.FREE && playerState != EntityState.FREE_INPUT)));
	}
}