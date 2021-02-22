package maninhouse.epicfight.animation.types.attack;

import javax.annotation.Nullable;

import maninhouse.epicfight.animation.Pose;
import maninhouse.epicfight.animation.types.LinkAnimation;
import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.capabilities.entity.LivingData.EntityState;
import maninhouse.epicfight.capabilities.entity.player.PlayerData;
import maninhouse.epicfight.physics.Collider;
import net.minecraft.util.Hand;

public class AAAnimation extends TargetTraceAnimation
{
	protected final float basisSpeed;
	
	public AAAnimation(int id, float convertTime, float antic, float contact, float recovery, float basisSpeed, @Nullable Collider collider, String index, String path)
	{
		this(id, convertTime, antic, antic, contact, recovery, basisSpeed, collider, index, path);
	}
	
	public AAAnimation(int id, float convertTime, float antic, float preDelay, float contact, float recovery, float basisSpeed, @Nullable Collider collider, String index, String path)
	{
		super(id, convertTime, antic, preDelay, contact, recovery, false, collider, index, path);
		this.basisSpeed = basisSpeed;
	}
	
	public AAAnimation(int id, float convertTime, float antic, float contact, float recovery, float basisSpeed, Hand hand, @Nullable Collider collider,  String index, String path)
	{
		super(id, convertTime, antic, antic, contact, recovery, false, hand, collider, index, path);
		this.basisSpeed = basisSpeed;
	}

	@Override
	public float getPlaySpeed(LivingData<?> entitydata)
	{
		if(entitydata instanceof PlayerData<?>)
		{
			PlayerData<?> playerdata = (PlayerData<?>)entitydata;
			if(this.getState(entitydata.getAnimator().getPlayer().getElapsedTime()) == EntityState.CONTACT)
				return 1.0F;
			else
				return (float)(playerdata.getAttackSpeed() / basisSpeed);
		}
		else
			return 1.0F;
	}
	
	@Override
	public void getLinkAnimation(Pose pose1, float timeModifier, LivingData<?> entitydata, LinkAnimation dest)
	{
		float extTime = Math.max(this.convertTime + timeModifier, 0);
		
		if(entitydata instanceof PlayerData<?>)
		{
			PlayerData<?> playerdata = (PlayerData<?>) entitydata;
			extTime *= (float)(basisSpeed / playerdata.getAttackSpeed());
		}
		
		extTime = Math.max(extTime - this.convertTime, 0);
		super.getLinkAnimation(pose1, extTime, entitydata, dest);
	}
}