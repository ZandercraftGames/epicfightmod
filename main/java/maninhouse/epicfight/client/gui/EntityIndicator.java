package maninhouse.epicfight.client.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.utils.math.Vec3f;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EntityIndicator extends ModIngameGui
{
	public static final List<EntityIndicator> ENTITY_INDICATOR_RENDERERS = Lists.newArrayList();
	public static final ResourceLocation BATTLE_ICON = new ResourceLocation(EpicFightMod.MODID, "textures/gui/battle_icons.png");
	
	public static void init()
	{
		new TargetIndicator();
		new HealthBarIndicator();
	}
	
	public void drawTexturedModalRect2DPlane(Matrix4f matrix, IVertexBuilder vertexBuilder, 
			float minX, float minY, float maxX, float maxY, float minTexU, float minTexV, float maxTexU, float maxTexV)
    {
		this.drawTexturedModalRect3DPlane(matrix, vertexBuilder, minX, minY, this.getBlitOffset(), maxX, maxY, this.getBlitOffset(), minTexU, minTexV, maxTexU, maxTexV);
    }
	
	public void drawTexturedModalRect3DPlane(Matrix4f matrix, IVertexBuilder vertexBuilder, 
			float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float minTexU, float minTexV, float maxTexU, float maxTexV)
    {
        float cor = 0.00390625F;
        
        vertexBuilder.pos(matrix, minX, minY, maxZ).tex((minTexU * cor), (maxTexV) * cor).endVertex();
        vertexBuilder.pos(matrix, maxX, minY, maxZ).tex((maxTexU * cor), (maxTexV) * cor).endVertex();
        vertexBuilder.pos(matrix, maxX, maxY, minZ).tex((maxTexU * cor), (minTexV) * cor).endVertex();
        vertexBuilder.pos(matrix, minX, maxY, minZ).tex((minTexU * cor), (minTexV) * cor).endVertex();
    }
	
	public EntityIndicator()
	{
		EntityIndicator.ENTITY_INDICATOR_RENDERERS.add(this);
	}
	
	public Matrix4f getMVMatrix(MatrixStack matStackIn, LivingEntity entityIn, float correctionX, float correctionY, float correctionZ, boolean lockRotation, boolean setupProjection)
	{
		float partialTicks = ClientEngine.INSTANCE.renderEngine.getPartialTicks();
		
		float posX = (float)MathHelper.lerp((double)partialTicks, entityIn.lastTickPosX, entityIn.getPosX());
		float posY = (float)MathHelper.lerp((double)partialTicks, entityIn.lastTickPosY, entityIn.getPosY());
		float posZ = (float)MathHelper.lerp((double)partialTicks, entityIn.lastTickPosZ, entityIn.getPosZ());
		matStackIn.push();
		matStackIn.translate(-posX, -posY, -posZ);
		matStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
		
		return this.getMVMatrix(matStackIn, posX + correctionX, posY + correctionY, posZ + correctionZ, lockRotation, setupProjection);
	}
	
	public Matrix4f getMVMatrix(MatrixStack matStackIn, float posX, float posY, float posZ, boolean lockRotation, boolean setupProjection)
	{
		VisibleMatrix4f viewMatrix = VisibleMatrix4f.importMatrix(matStackIn.getLast().getMatrix());
		VisibleMatrix4f finalMatrix = new VisibleMatrix4f();
		finalMatrix.translate(new Vec3f(-posX, posY, -posZ));
		matStackIn.pop();
		if(lockRotation)
		{
			finalMatrix.m00 = viewMatrix.m00;
			finalMatrix.m01 = viewMatrix.m10;
			finalMatrix.m02 = viewMatrix.m20;
			finalMatrix.m10 = viewMatrix.m01;
			finalMatrix.m11 = viewMatrix.m11;
			finalMatrix.m12 = viewMatrix.m21;
			finalMatrix.m20 = viewMatrix.m02;
			finalMatrix.m21 = viewMatrix.m12;
			finalMatrix.m22 = viewMatrix.m22;
		}
		VisibleMatrix4f.mul(viewMatrix, finalMatrix, finalMatrix);
		if(setupProjection)
			VisibleMatrix4f.mul(ClientEngine.INSTANCE.renderEngine.getCurrentProjectionMatrix(), finalMatrix, finalMatrix);
		
		return VisibleMatrix4f.exportMatrix(finalMatrix);
	}
	
	public abstract void drawIndicator(LivingEntity entityIn, MatrixStack matStackIn, IRenderTypeBuffer ivertexBuilder);
	public abstract boolean shouldDraw(LivingEntity entityIn);
}