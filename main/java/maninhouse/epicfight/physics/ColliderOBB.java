package maninhouse.epicfight.physics;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import maninhouse.epicfight.client.renderer.ModRenderTypes;
import maninhouse.epicfight.utils.math.MathUtils;
import maninhouse.epicfight.utils.math.Vec3f;
import maninhouse.epicfight.utils.math.Vec4f;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ColliderOBB extends Collider
{
	private final Vec3f[] modelVertex;
	private final Vec3f[] modelNormal;
	
	private Vec3f[] rotatedVertex;
	private Vec3f[] rotatedNormal;
	
	/**
	 * make 3d obb
	 * @param pos1 left_back
	 * @param pos2 left_front
	 * @param pos3 right_front
	 * @param pos4 right_back
	 * @param modelCenter central position
	 */
	public ColliderOBB(float posX, float posY, float posZ, float center_x, float center_y, float center_z)
	{
		super(new Vec3f(center_x, center_y, center_z), null);
		
		float xLength = Math.abs(posX - center_x);
		float yLength = Math.abs(posY - center_y);
		float zLength = Math.abs(posZ - center_z);
		
		float maxLength = Math.max(xLength, Math.max(yLength, zLength));
		
		hitboxAABB = new AxisAlignedBB(maxLength, maxLength, maxLength, -maxLength, -maxLength, -maxLength);
		
		modelVertex = new Vec3f[4];
		modelNormal = new Vec3f[3];
		rotatedVertex = new Vec3f[4];
		rotatedNormal = new Vec3f[3];
		
		this.modelVertex[0] = new Vec3f(posX, posY, -posZ);
		this.modelVertex[1] = new Vec3f(posX, posY, posZ);
		this.modelVertex[2] = new Vec3f(-posX, posY, posZ);
		this.modelVertex[3] = new Vec3f(-posX, posY, -posZ);
		this.modelNormal[0] = new Vec3f(1,0,0);
		this.modelNormal[1] = new Vec3f(0,1,0);
		this.modelNormal[2] = new Vec3f(0,0,-1);
		
		this.rotatedVertex[0] = new Vec3f();
		this.rotatedVertex[1] = new Vec3f();
		this.rotatedVertex[2] = new Vec3f();
		this.rotatedVertex[3] = new Vec3f();
		this.rotatedNormal[0] = new Vec3f();
		this.rotatedNormal[1] = new Vec3f();
		this.rotatedNormal[2] = new Vec3f();
	}
	
	/**
	 * make 2d obb
	 * @param pos1 left
	 * @param pos2 right
	 * @param modelCenter central position
	 */
	public ColliderOBB(AxisAlignedBB entityCallAABB, float pos1_x, float pos1_y, float pos1_z, float pos2_x, float pos2_y, float pos2_z, 
			float norm1_x, float norm1_y, float norm1_z, float norm2_x, float norm2_y, float norm2_z, float center_x, float center_y, float center_z)
	{
		super(new Vec3f(center_x, center_y, center_z), entityCallAABB);
		
		modelVertex = new Vec3f[2];
		modelNormal = new Vec3f[2];
		rotatedVertex = new Vec3f[2];
		rotatedNormal = new Vec3f[2];
		
		this.modelVertex[0] = new Vec3f(pos1_x, pos1_y, pos1_z);
		this.modelVertex[1] = new Vec3f(pos2_x, pos2_y, pos2_z);
		this.modelNormal[0] = new Vec3f(norm1_x,norm1_y,norm1_z);
		this.modelNormal[1] = new Vec3f(norm2_x,norm2_y,norm2_z);
		
		this.rotatedVertex[0] = new Vec3f();
		this.rotatedVertex[1] = new Vec3f();
		this.rotatedNormal[0] = new Vec3f();
		this.rotatedNormal[1] = new Vec3f();
	}
	
	/**
	 * make obb from aabb
	 * @param aabbCopy
	 */
	public ColliderOBB(AxisAlignedBB aabbCopy)
	{
		super(null, null);
		modelVertex = null;
		modelNormal = null;
		
		float xSize = (float) (aabbCopy.maxX - aabbCopy.minX) / 2;
		float ySize = (float) (aabbCopy.maxY - aabbCopy.minY) / 2;
		float zSize = (float) (aabbCopy.maxZ - aabbCopy.minZ) / 2;
		
		worldCenter = new Vec3f(-((float)aabbCopy.minX + xSize), (float)aabbCopy.minY + ySize, -((float)aabbCopy.minZ + zSize));
		rotatedVertex = new Vec3f[4];
		rotatedNormal = new Vec3f[3];
		
		this.rotatedVertex[0] = new Vec3f(-xSize, ySize, -zSize);
		this.rotatedVertex[1] = new Vec3f(-xSize, ySize, zSize);
		this.rotatedVertex[2] = new Vec3f(xSize, ySize, zSize);
		this.rotatedVertex[3] = new Vec3f(xSize, ySize, -zSize);
		this.rotatedNormal[0] = new Vec3f(1,0,0);
		this.rotatedNormal[1] = new Vec3f(0,1,0);
		this.rotatedNormal[2] = new Vec3f(0,0,1);
	}
	
	/**
	 * Transform every elements of this Bounding Box
	 **/
	@Override
	public void transform(VisibleMatrix4f mat)
	{
		Vec4f tempVector = new Vec4f(0,0,0,1);
		VisibleMatrix4f rotationMatrix = new VisibleMatrix4f(mat);
		rotationMatrix.m30 = 0;
		rotationMatrix.m31 = 0;
		rotationMatrix.m32 = 0;
		
		for(int i = 0; i < modelVertex.length; i ++)
		{
			tempVector.x = modelVertex[i].x;
			tempVector.y = modelVertex[i].y;
			tempVector.z = modelVertex[i].z;
			VisibleMatrix4f.transform(rotationMatrix, tempVector, tempVector);
			rotatedVertex[i].x = tempVector.x;
			rotatedVertex[i].y = tempVector.y;
			rotatedVertex[i].z = tempVector.z;
		}
		
		for(int i = 0; i < modelNormal.length; i ++)
		{
			tempVector.x = modelNormal[i].x;
			tempVector.y = modelNormal[i].y;
			tempVector.z = modelNormal[i].z;
			VisibleMatrix4f.transform(rotationMatrix, tempVector, tempVector);
			rotatedNormal[i].x = tempVector.x;
			rotatedNormal[i].y = tempVector.y;
			rotatedNormal[i].z = tempVector.z;
		}
		
		super.transform(mat);
	}
	
	public boolean isCollideWith(ColliderOBB opponent)
	{
		Vec3f toOpponent = Vec3f.sub(opponent.worldCenter, this.worldCenter, null);
		
		for(Vec3f seperateAxis : this.rotatedNormal)
		{
			if(!collisionDetection(seperateAxis, toOpponent, this, opponent))
			{
				return false;
			}
		}
		
		for(Vec3f seperateAxis : opponent.rotatedNormal)
		{
			if(!collisionDetection(seperateAxis, toOpponent, this, opponent))
			{
				return false;
			}
		}
		
		/** Below code detects whether the each line of obb is collide but it is disabled for better performance
		for(Vector3f norm1 : this.rotatedNormal)
		{
			for(Vector3f norm2 : opponent.rotatedNormal)
			{
				Vector3f seperateAxis = Vector3f.cross(norm1, norm2, null);
				
				if(seperateAxis.x + seperateAxis.y + seperateAxis.z == 0)
				{
					continue;
				}
				
				if(!collisionLogic(seperateAxis, toOpponent, this, opponent))
				{
					return false;
				}
			}
		}
		 **/
		
		return true;
	}
	
	@Override
	public boolean isCollideWith(Entity entity)
	{
		ColliderOBB obb = new ColliderOBB(entity.getBoundingBox());
		
		return isCollideWith(obb);
	}
	
	private static boolean collisionDetection(Vec3f seperateAxis, Vec3f toOpponent, ColliderOBB box1, ColliderOBB box2)
	{
		Vec3f maxProj1 = null, maxProj2 = null, distance;
		float maxDot1 = -1, maxDot2 = -1;
		distance = Vec3f.dot(seperateAxis, toOpponent) > 0 ? toOpponent : new Vec3f(-toOpponent.x, -toOpponent.y, -toOpponent.z);
		
		for(Vec3f vertexVector : box1.rotatedVertex)
		{
			Vec3f temp = Vec3f.dot(seperateAxis, vertexVector) > 0 ? vertexVector : new Vec3f(-vertexVector.x, -vertexVector.y, -vertexVector.z);
			float dot = Vec3f.dot(seperateAxis, temp);
			if(dot > maxDot1)
			{
				maxDot1 = dot;
				maxProj1 = temp;
			}
		}
		
		for(Vec3f vertexVector : box2.rotatedVertex)
		{
			Vec3f temp = Vec3f.dot(seperateAxis, vertexVector) > 0 ? vertexVector : new Vec3f(-vertexVector.x, -vertexVector.y, -vertexVector.z);
			float dot = Vec3f.dot(seperateAxis, temp);
			if(dot > maxDot2)
			{
				maxDot2 = dot;
				maxProj2 = temp;
			}
		}
		
		if(getProjectedScale(seperateAxis, distance) >= getProjectedScale(seperateAxis, maxProj1) + getProjectedScale(seperateAxis, maxProj2))
		{
			return false;
		}
		return true;
	}
	
	private static float getProjectedScale(Vec3f normal, Vec3f projecting)
	{
		float dot = Vec3f.dot(normal, projecting);
		float normalScale = 1 / ((normal.x * normal.x) + (normal.y * normal.y) + (normal.z * normal.z));
		Vec3f projVec = new Vec3f(dot * normal.x * normalScale, dot * normal.y * normalScale, dot * normal.z * normalScale);
		
		return (float) Math.sqrt((projVec.x * projVec.x) + (projVec.y * projVec.y) + (projVec.z * projVec.z)); 
	}
	
	@OnlyIn(Dist.CLIENT) @Override
	public void draw(MatrixStack matrixStackIn, IRenderTypeBuffer buffer, VisibleMatrix4f pose, float partialTicks)
	{
		IVertexBuilder vertexBuilder = buffer.getBuffer(ModRenderTypes.getBoundingBox());
		MathUtils.translateStack(matrixStackIn, pose);
        MathUtils.rotateStack(matrixStackIn, pose);
        MathUtils.scaleStack(matrixStackIn, pose);
        Matrix4f matrix = matrixStackIn.getLast().getMatrix();
		
        Vec3f vec = this.modelVertex[1];
        float maxX = vec.x;
        float maxY = vec.y;
        float maxZ = vec.z;
        
        vertexBuilder.pos(matrix, maxX, maxY, maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, maxY, -maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, -maxX, maxY, -maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, -maxX, maxY, maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, maxY, maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, -maxY, maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, -maxY, -maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, maxY, -maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, -maxY, -maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, -maxX, -maxY, -maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, maxY, -maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, -maxY, -maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, -maxY, maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, maxY, maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, maxX, -maxY, maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexBuilder.pos(matrix, -maxX, -maxY, maxZ).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
	}
}