package maninhouse.epicfight.network.server;

import java.lang.reflect.Field;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCResetBasicAttackCool
{
	private static Field cooldownSeeker = ObfuscationReflectionHelper.findField(LivingEntity.class, "field_184617_aD");
	
	public static STCResetBasicAttackCool fromBytes(PacketBuffer buf)
	{
		STCResetBasicAttackCool msg = new STCResetBasicAttackCool();
		return msg;
	}
	
	public static void toBytes(STCResetBasicAttackCool msg, PacketBuffer buf)
	{
		
	}
	
	public static void handle(STCResetBasicAttackCool msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
			
			try
			{
				cooldownSeeker.setInt(clientPlayer, 10000);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				e.printStackTrace();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}