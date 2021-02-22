package maninhouse.epicfight.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import maninhouse.epicfight.animation.Joint;
import maninhouse.epicfight.capabilities.entity.mob.HoglinData;
import maninhouse.epicfight.model.Armature;
import maninhouse.epicfight.utils.math.Vec3f;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HoglinRenderer extends ArmatureRenderer<HoglinEntity, HoglinData> {
	private static final VisibleMatrix4f CORRECTION = new VisibleMatrix4f().rotate((float)Math.toRadians(-30.0), new Vec3f(1, 0, 0));
	private static final VisibleMatrix4f REVERSE = new VisibleMatrix4f().rotate((float)Math.toRadians(30.0), new Vec3f(1, 0, 0));
	
	private final ResourceLocation textureLocation;
	
	public HoglinRenderer(String texturePath) {
		this.textureLocation = new ResourceLocation(texturePath);
	}
	
	@Override
	protected void transformJoint(int jointId, Armature modelArmature, VisibleMatrix4f mat) {
		Joint joint = modelArmature.findJointById(jointId);
		VisibleMatrix4f.mul(joint.getAnimatedTransform(), CORRECTION, joint.getAnimatedTransform());
        VisibleMatrix4f.mul(joint.getAnimatedTransform(), mat, joint.getAnimatedTransform());
        VisibleMatrix4f.mul(joint.getAnimatedTransform(), REVERSE, joint.getAnimatedTransform());
	}
	
	@Override
	protected void applyRotations(MatrixStack matStack, Armature armature, HoglinEntity entityIn, HoglinData entitydata, float partialTicks) {
        super.applyRotations(matStack, armature, entityIn, entitydata, partialTicks);
        transformJoint(1, armature, entitydata.getHeadMatrix(partialTicks));
	}
	
	@Override
	protected ResourceLocation getEntityTexture(HoglinEntity entityIn) {
		return textureLocation;
	}
}