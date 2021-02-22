package maninhouse.epicfight.client.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;

import maninhouse.epicfight.animation.Joint;
import maninhouse.epicfight.capabilities.ModCapabilities;
import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HierarchicalParticle extends SpriteTexturedParticle
{
	private LivingData<?> entitydata;
	private int jointKey;
	
	protected HierarchicalParticle(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
	{
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);
		
		Entity e = worldIn.getEntityByID((int)Double.doubleToLongBits(xSpeedIn));
		this.entitydata = (LivingData<?>) e.getCapability(ModCapabilities.CAPABILITY_ENTITY, null).orElse(null);
		this.jointKey = (int) Double.doubleToLongBits(ySpeedIn);
		
		//this.entitydata.hierarchicalParticles.add(this);
	}
	
	@Override
	public void tick()
	{
		if(this.isExpired)
		{
			//this.entitydata.hierarchicalParticles.remove(this);
			this.entitydata = null;
		}
	}
	
	@Override
	public void renderParticle(IVertexBuilder buffer, ActiveRenderInfo entityIn, float partialTicks)
	{
		this.entitydata.getClientAnimator().setPoseToModel();
		Joint joint = this.entitydata.getEntityModel(Models.LOGICAL_CLIENT).getArmature().findJointById(this.jointKey);
		VisibleMatrix4f jointTransform = VisibleMatrix4f.mul(joint.getAnimatedTransform(), this.entitydata.getModelMatrix(ClientEngine.INSTANCE.renderEngine.getPartialTicks()), null);
		this.posX = jointTransform.m30;
		this.posY = jointTransform.m31;
		this.posZ = jointTransform.m32;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		super.renderParticle(buffer, entityIn, partialTicks);
	}
	
	@Override
	public IParticleRenderType getRenderType()
	{
		return HitParticle.HIT_PARTICLE_TYPE;
	}
}