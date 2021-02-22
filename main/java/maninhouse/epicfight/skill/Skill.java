package maninhouse.epicfight.skill;

import java.util.ArrayList;
import java.util.List;

import maninhouse.epicfight.animation.LivingMotion;
import maninhouse.epicfight.capabilities.entity.LivingData.EntityState;
import maninhouse.epicfight.capabilities.entity.player.PlayerData;
import maninhouse.epicfight.capabilities.entity.player.ServerPlayerData;
import maninhouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninhouse.epicfight.client.events.engine.ControllEngine;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.main.GameConstants;
import maninhouse.epicfight.network.ModNetworkManager;
import maninhouse.epicfight.network.server.STCSetSkillValue;
import maninhouse.epicfight.network.server.STCSetSkillValue.Target;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class Skill
{
	protected final SkillSlot slot;
	protected final boolean isActiveSkill;
	protected final float cooldown;
	protected final int duration;
	protected final int maxStackSize;
	
	@OnlyIn(Dist.CLIENT)
	protected ResourceLocation textureLocation;
	@OnlyIn(Dist.CLIENT)
	protected List<ITextComponent> tooltipInfo;
	
	public Skill(SkillSlot index, float cooldown, ResourceLocation textureLocation)
	{
		this(index, cooldown, 0, 1, true, textureLocation);
	}
	
	public Skill(SkillSlot index, float cooldown, int maxStack, ResourceLocation textureLocation)
	{
		this(index, cooldown, 0, maxStack, true, textureLocation);
	}
	
	public Skill(SkillSlot index, float cooldown, int duration, boolean isActiveSkill, ResourceLocation textureLocation)
	{
		this(index, cooldown, duration, 1, true, textureLocation);
	}
	
	public Skill(SkillSlot index, float cooldown, int duration, int maxStack, boolean isActiveSkill, ResourceLocation textureLocation)
	{
		this.cooldown = cooldown;
		this.duration = duration;
		this.isActiveSkill = isActiveSkill;
		this.slot = index;
		this.maxStackSize = maxStack;
		
		if(EpicFightMod.isPhysicalClient())
		{
			this.textureLocation = textureLocation;
			this.tooltipInfo = new ArrayList<ITextComponent> ();
		}
	}
	
	@OnlyIn(Dist.CLIENT)
	public Skill setTooltip(String line)
	{
		this.tooltipInfo.add(new StringTextComponent(line));
		return this;
	}
	
	public PacketBuffer gatherArguments(ClientPlayerData executer, ControllEngine controllEngine)
	{
		return null;
	}
	
	public boolean isExecutableState(PlayerData<?> executer)
	{
		EntityState playerState = executer.getEntityState();
		return !(executer.getOriginalEntity().isElytraFlying() || executer.currentMotion == LivingMotion.FALL || 
			((playerState != EntityState.FREE && playerState != EntityState.FREE_INPUT)));
	}
	
	public boolean canExecute(PlayerData<?> executer)
	{
		return true;
	}
	
	/**
	 * Gather arguments in client and send packet
	 * Process the skill execution with given arguments
	 */
	public void executeOnClient(ClientPlayerData executer, PacketBuffer args)
	{
		
	}
	
	public void executeOnServer(ServerPlayerData executer, PacketBuffer args)
	{
		this.setDurationSynchronize(executer, this.duration);
	}
	
	/**
	 * Use this method when skill is end
	 */
	public void cancelOnClient(ClientPlayerData executer, PacketBuffer args)
	{
		
	}
	
	public void execute(SkillContainer container)
	{
		container.duration = this.duration;
		container.isActivated = true;
	}
	
	public void onInitiate(SkillContainer container)
	{
		
	}
	
	public void onDeleted(SkillContainer container)
	{
		
	}
	
	public void onReset(SkillContainer container)
	{
		
	}
	
	public void setCooldown(SkillContainer container, float value)
	{
		container.cooldown = value;
		container.cooldown = Math.max(container.cooldown, 0);
		container.cooldown = Math.min(container.cooldown, this.cooldown);
		
		if(value >= this.cooldown)
		{
			if(container.stack < this.maxStackSize)
			{
				container.stack++;
				if(container.stack < this.maxStackSize)
				{
					container.cooldown = 0;
					container.prevCooldown = 0;
				}
			}
			else
			{
				container.cooldown = this.cooldown;
				container.prevCooldown = this.cooldown;
			}
		}
		else if(value == 0 && container.stack > 0)
			--container.stack;
	}
	
	public void update(SkillContainer container)
	{
		PlayerData<?> executer = container.executer;
		container.prevCooldown = container.cooldown;
		container.prevDuration = container.duration;
		
		if(container.stack < container.containingSkill.maxStackSize)
			container.setCooldown(container.cooldown + this.getRegenTimePerTick(executer) * GameConstants.ONE_TICK);
		
		if(container.isActivated)
		{
			if(container.consumeDuration)
				container.duration--;
				
			if(container.duration <= 0)
			{
				if(container.executer.isRemote())
					container.containingSkill.cancelOnClient((ClientPlayerData)executer, null);
				else
					container.containingSkill.cancelOnServer((ServerPlayerData)executer, null);
				container.isActivated = false;
				container.duration = 0;
			}
		}
	}
	
	public void cancelOnServer(ServerPlayerData executer, PacketBuffer args)
	{
		setCooldownSynchronize(executer, 0);
	}
	
	public void setCooldownSynchronize(ServerPlayerData executer, float amount)
	{
		setCooldownSynchronize(executer, this.slot, amount);
	}
	
	public void setDurationSynchronize(ServerPlayerData executer, int amount)
	{
		setDurationSynchronize(executer, this.slot, amount);
	}
	
	public void setDurationConsumeSynchronize(ServerPlayerData executer, boolean bool)
	{
		setDurationConsumeSynchronize(executer, this.slot, bool);
	}
	
	public static void setCooldownSynchronize(ServerPlayerData executer, SkillSlot slot, float amount)
	{
		if(amount > 0)
			executer.getSkill(slot).setCooldown(amount);
		else
			executer.getSkill(slot).reset(!executer.getOriginalEntity().isCreative());
		
		ModNetworkManager.sendToPlayer(new STCSetSkillValue(Target.COOLDOWN, slot.index, amount, false), executer.getOriginalEntity());
	}
	
	public static void setDurationSynchronize(ServerPlayerData executer, SkillSlot slot, int amount)
	{
		executer.getSkill(slot).setDuration(amount);
		ModNetworkManager.sendToPlayer(new STCSetSkillValue(Target.DURATION, slot.index, amount, false), executer.getOriginalEntity());
	}
	
	public static void setDurationConsumeSynchronize(ServerPlayerData executer, SkillSlot slot, boolean bool)
	{
		executer.getSkill(slot).setDurationConsume(bool);
		ModNetworkManager.sendToPlayer(new STCSetSkillValue(Target.DURATION_CONSUME, slot.index, 0, bool), executer.getOriginalEntity());
	}
	
	@OnlyIn(Dist.CLIENT)
	public List<ITextComponent> getTooltip()
	{
		return this.tooltipInfo;
	}
	
	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getTextureLocation()
	{
		return this.textureLocation;
	}
	
	public float getRegenTimePerTick(PlayerData<?> player)
	{
		return player.getWeightPaneltyDivider();
	}
	
	public SkillSlot getSlot()
	{
		return this.slot;
	}
	
	public int getMaxStack()
	{
		return this.maxStackSize;
	}
	
	public boolean isActiveSkill()
	{
		return this.isActiveSkill;
	}
}