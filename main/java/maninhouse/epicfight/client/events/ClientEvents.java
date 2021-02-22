package maninhouse.epicfight.client.events;

import com.mojang.datafixers.util.Pair;

import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.item.CapabilityItem;
import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninhouse.epicfight.main.EpicFightMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseClickedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.MouseReleasedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = EpicFightMod.MODID, value = Dist.CLIENT)
public class ClientEvents
{
	private static final Pair<ResourceLocation, ResourceLocation> OFFHAND_TEXTURE
		= Pair.of(PlayerContainer.LOCATION_BLOCKS_TEXTURE, PlayerContainer.EMPTY_ARMOR_SLOT_SHIELD);
	
	@SubscribeEvent
	public static void mouseClickEvent(MouseClickedEvent.Pre event)
	{
		if(event.getGui() instanceof ContainerScreen)
		{
			Slot slotUnderMouse = ((ContainerScreen)event.getGui()).getSlotUnderMouse();
			
			if(slotUnderMouse != null)
			{
				CapabilityItem cap = Minecraft.getInstance().player.inventory.getItemStack().getCapability(ModCapabilities.CAPABILITY_ITEM, null).orElse(null);
				
				if(cap != null && !cap.canUsedOffhand())
				{
					if(slotUnderMouse.getBackground() != null && slotUnderMouse.getBackground().equals(OFFHAND_TEXTURE))
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void mouseReleaseEvent(MouseReleasedEvent.Pre event)
	{
		if(event.getGui() instanceof ContainerScreen)
		{
			Slot slotUnderMouse = ((ContainerScreen)event.getGui()).getSlotUnderMouse();
			
			if(slotUnderMouse != null)
			{
				CapabilityItem cap = Minecraft.getInstance().player.inventory.getItemStack().getCapability(ModCapabilities.CAPABILITY_ITEM, null).orElse(null);
				
				if(cap != null && !cap.canUsedOffhand())
				{
					if(slotUnderMouse.getBackground() != null && slotUnderMouse.getBackground().equals(OFFHAND_TEXTURE))
					{
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void clientRespawnEvent(ClientPlayerNetworkEvent.RespawnEvent event)
	{
		ClientPlayerData oldOne = (ClientPlayerData)event.getOldPlayer().getCapability(ModCapabilities.CAPABILITY_ENTITY).orElse(null);
		
		if(oldOne != null)
		{
			ClientPlayerData newOne = (ClientPlayerData)event.getNewPlayer().getCapability(ModCapabilities.CAPABILITY_ENTITY).orElse(null);
			ClientEngine.INSTANCE.setPlayerData(newOne);
			ClientEngine.INSTANCE.inputController.setGamePlayer(newOne);
			newOne.setEntity(event.getNewPlayer());
			newOne.init();
			newOne.initFromOldOne(oldOne);
			event.getNewPlayer().ticksExisted = event.getOldPlayer().ticksExisted;
		}
	}
}