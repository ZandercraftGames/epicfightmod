package maninhouse.epicfight.capabilities.entity;

import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import maninhouse.epicfight.utils.math.MathUtils;
import net.minecraft.entity.Entity;

public abstract class CapabilityEntity<T extends Entity>
{
	protected T orgEntity;
	private boolean isInitialized = false;
	
	public abstract void init();
	public abstract void update();
	protected abstract void updateOnClient();
	protected abstract void updateOnServer();
	
	public void postInit() {}
	
	public void setEntity(T vanillaEntity)
	{
		this.orgEntity = vanillaEntity;
		this.isInitialized = true;
	}
	
	public T getOriginalEntity()
	{
		return orgEntity;
	}
	
	public boolean isInitialized()
	{
		return this.isInitialized;
	}
	
	public boolean isRemote()
	{
		return orgEntity.world.isRemote;
	}
	
	public void aboutToDeath()
	{
		this.orgEntity = null;
	}
	
	public VisibleMatrix4f getMatrix(float partialTicks)
	{
		return MathUtils.getModelMatrixIntegrated(0, 0, 0, 0, 0, 0, orgEntity.prevRotationPitch, orgEntity.rotationPitch, orgEntity.prevRotationYaw,
				orgEntity.rotationYaw, partialTicks);
	}
	
	public abstract VisibleMatrix4f getModelMatrix(float partialTicks);
}