package maninhouse.epicfight.utils.math;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class MathUtils {
	public static VisibleMatrix4f getModelMatrixIntegrated(float prevPosX, float posX, float prevPosY, float posY,
			float prevPosZ, float posZ, float prevPitch, float pitch, float prevYaw, float yaw, float partialTick) {
		VisibleMatrix4f modelMatrix = new VisibleMatrix4f().setIdentity();
		Vec3f entityPosition = new Vec3f(-(prevPosX + (posX - prevPosX) * partialTick),
				((prevPosY + (posY - prevPosY) * partialTick)), -(prevPosZ + (posZ - prevPosZ) * partialTick));

		VisibleMatrix4f.translate(entityPosition, modelMatrix, modelMatrix);

		float pitchDegree = interpolateRotation(prevPitch, pitch, partialTick);
		float yawDegree = interpolateRotation(prevYaw, yaw, partialTick);

		VisibleMatrix4f.rotate((float) -Math.toRadians(yawDegree), new Vec3f(0, 1, 0), modelMatrix, modelMatrix);
		VisibleMatrix4f.rotate((float) -Math.toRadians(pitchDegree), new Vec3f(1, 0, 0), modelMatrix, modelMatrix);

		return modelMatrix;
	}

	public static Vector3d getVectorForRotation(float pitch, float yaw) {
		float f = pitch * ((float) Math.PI / 180F);
		float f1 = -yaw * ((float) Math.PI / 180F);
		float f2 = MathHelper.cos(f1);
		float f3 = MathHelper.sin(f1);
		float f4 = MathHelper.cos(f);
		float f5 = MathHelper.sin(f);

		return new Vector3d((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
	}

	public static float interpolateRotation(float par1, float par2, float par3) {
		float f = 0;

		for (f = par2 - par1; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return par1 + par3 * f;
	}

	public static float getInterpolatedRotation(float par1, float par2, float par3) {
		float f = 0;

		for (f = par2 - par1; f < -180.0F; f += 360.0F) {
			;
		}

		while (f >= 180.0F) {
			f -= 360.0F;
		}

		return par3 * f;
	}

	public static void translateStack(MatrixStack mStack, VisibleMatrix4f mat) {
		Vector3f vector = getTranslationFromMatrix(mat);
		mStack.translate(vector.getX(), vector.getY(), vector.getZ());
	}

	public static void rotateStack(MatrixStack mStack, VisibleMatrix4f mat) {
		mStack.rotate(getQuaternionFromMatrix(mat));
	}
	
	public static void scaleStack(MatrixStack mStack, VisibleMatrix4f mat) {
		Vector3f vector = getScaleFromMatrix(mat);
		mStack.scale(vector.getX(), vector.getY(), vector.getZ());
	}

	public static double getAngleBetween(Entity e1, Entity e2) {
		Vector3d a = e1.getLookVec();
		Vector3d b = new Vector3d(e2.getPosX() - e1.getPosX(), e2.getPosY() - e1.getPosY(), e2.getPosZ() - e1.getPosZ())
				.normalize();
		double cosTheta = (a.x * b.x + a.y * b.y + a.z * b.z);
		return Math.acos(cosTheta);
	}

	private static Vector3f getTranslationFromMatrix(VisibleMatrix4f mat) {
		return new Vector3f(mat.m30, mat.m31, mat.m32);
	}

	private static Quaternion getQuaternionFromMatrix(VisibleMatrix4f mat) {
		float w, x, y, z;
		float diagonal = mat.m00 + mat.m11 + mat.m22;

		if (diagonal > 0) {
			float w4 = (float) (Math.sqrt(diagonal + 1.0F) * 2.0F);
			w = w4 * 0.25F;
			x = (mat.m21 - mat.m12) / w4;
			y = (mat.m02 - mat.m20) / w4;
			z = (mat.m10 - mat.m01) / w4;
		} else if ((mat.m00 > mat.m11) && (mat.m00 > mat.m22)) {
			float x4 = (float) (Math.sqrt(1.0F + mat.m00 - mat.m11 - mat.m22) * 2F);
			w = (mat.m21 - mat.m12) / x4;
			x = x4 * 0.25F;
			y = (mat.m01 + mat.m10) / x4;
			z = (mat.m02 + mat.m20) / x4;
		} else if (mat.m11 > mat.m22) {
			float y4 = (float) (Math.sqrt(1.0F + mat.m11 - mat.m00 - mat.m22) * 2F);
			w = (mat.m02 - mat.m20) / y4;
			x = (mat.m01 + mat.m10) / y4;
			y = y4 * 0.25F;
			z = (mat.m12 + mat.m21) / y4;
		} else {
			float z4 = (float) (Math.sqrt(1.0F + mat.m22 - mat.m00 - mat.m11) * 2F);
			w = (mat.m10 - mat.m01) / z4;
			x = (mat.m02 + mat.m20) / z4;
			y = (mat.m12 + mat.m21) / z4;
			z = z4 * 0.25F;
		}
		
		Quaternion quat = new Quaternion(x, y, z, w);
		quat.normalize();
		return quat;
	}
	
	private static Vector3f getScaleFromMatrix(VisibleMatrix4f mat) {
		Vec3f a = new Vec3f(mat.m00, mat.m10, mat.m20);
		Vec3f b = new Vec3f(mat.m01, mat.m11, mat.m21);
		Vec3f c = new Vec3f(mat.m02, mat.m12, mat.m22);
		return new Vector3f(a.length(), b.length(), c.length());
	}
}