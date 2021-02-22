package maninhouse.epicfight.animation.types;

import maninhouse.epicfight.capabilities.entity.LivingData;

public class DeathAnimation extends HitAnimation
{
	public DeathAnimation(int id, float convertTime, String path)
	{
		super(id, convertTime, path);
	}
	
	@Override
	public void onFinish(LivingData<?> entity, boolean isEnd)
	{
		entity.getAnimator().pause = true;
	}
}