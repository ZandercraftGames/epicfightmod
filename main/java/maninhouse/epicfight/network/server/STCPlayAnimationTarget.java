package maninhouse.epicfight.network.server;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCPlayAnimationTarget extends STCPlayAnimation
{
	protected int targetId;
	
	public STCPlayAnimationTarget()
	{
		super();
		this.targetId = 0;
	}
	
	public STCPlayAnimationTarget(int animation, int entityId, float modifyTime, int targetId)
	{
		this(animation, entityId, modifyTime, false, targetId);
	}
	
	public STCPlayAnimationTarget(int animation, int entityId, float modifyTime, boolean mixLayer, int targetId)
	{
		super(animation, entityId, modifyTime, mixLayer);
		this.targetId = targetId;
	}
	
	@Override
	public void onArrive()
	{
		super.onArrive();
		
		Entity entity = Minecraft.getInstance().player.world.getEntityByID(entityId);
		Entity target = Minecraft.getInstance().player.world.getEntityByID(targetId);
		
		if(entity instanceof MobEntity && target instanceof LivingEntity)
		{
			MobEntity entityliving = (MobEntity) entity;
			entityliving.setAttackTarget((LivingEntity) target);
		}
	}
	
	public static STCPlayAnimationTarget fromBytes(PacketBuffer buf)
	{
		return new STCPlayAnimationTarget(buf.readInt(), buf.readInt(), buf.readFloat(), buf.readBoolean(), buf.readInt());
	}
	
	public static void toBytes(STCPlayAnimationTarget msg, PacketBuffer buf)
	{
		buf.writeInt(msg.animationId);
		buf.writeInt(msg.entityId);
		buf.writeFloat(msg.modifyTime);
		buf.writeBoolean(msg.mixLayer);
		buf.writeInt(msg.targetId);
	}
	
	public static void handle(STCPlayAnimationTarget msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(()->{
			msg.onArrive();
		});
		ctx.get().setPacketHandled(true);
	}
}