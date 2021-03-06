package maninhouse.epicfight.network.server;

import java.util.function.Supplier;

import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.client.capabilites.entity.ClientPlayerData;
import maninhouse.epicfight.skill.SkillSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class STCModifySkillVariable
{
	private String nbtName;
	private Object value;
	private int index;
	private int type;
	
	public STCModifySkillVariable()
	{
		this.nbtName = "";
		this.value = null;
	}
	
	public STCModifySkillVariable(VariableType type, SkillSlot slot, String nbtName, Object value)
	{
		this(type.id, slot.getIndex(), nbtName, value);
	}
	
	public STCModifySkillVariable(int type, int slot, String nbtName, Object value)
	{
		this.type = type;
		this.index = slot;
		this.nbtName = nbtName;
		this.value = value;
	}
	
	public static STCModifySkillVariable fromBytes(PacketBuffer buf)
	{
		Object value = null;
		int type = buf.readInt();
		
		switch(type)
		{
		case 0:
			value = buf.readBoolean();
			break;
		case 1:
			value = buf.readInt();
			break;
		case 2:
			value = buf.readFloat();
			break;
		default:
			break;
		}
		
		return new STCModifySkillVariable(type, buf.readInt(), buf.readString(), value);
	}
	
	public static void toBytes(STCModifySkillVariable msg, PacketBuffer buf)
	{
		buf.writeInt(msg.type);
		
		switch(msg.type)
		{
		case 0:
			buf.writeBoolean((boolean)msg.value);
			break;
		case 1:
			buf.writeInt((int)msg.value);
			break;
		case 2:
			buf.writeFloat((float)msg.value);
			break;
		default:
			break;
		}
		
		buf.writeInt(msg.index);
		buf.writeString(msg.nbtName);
	}
	
	public static void handle(STCModifySkillVariable msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			ClientPlayerData playerdata = (ClientPlayerData) Minecraft.getInstance().player.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
			
			if(playerdata != null)
			{
				CompoundNBT nbt = playerdata.getSkill(msg.index).getVariableNBT();
				
				switch(msg.type)
				{
				case 0:
					nbt.putBoolean(msg.nbtName, (boolean)msg.value);
					break;
				case 1:
					nbt.putInt(msg.nbtName, (int)msg.value);
					break;
				case 2:
					nbt.putFloat(msg.nbtName, (float)msg.value);
					break;
				default:
					break;
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
	
	public static enum VariableType
	{
		BOOLEAN(0), INTEGER(1), FLOAT(2);
		
		final int id;
		
		VariableType(int id)
		{
			this.id = id;
		}
	}
}