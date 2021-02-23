package maninhouse.epicfight.client.renderer.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.client.model.ClientModels;
import maninhouse.epicfight.utils.math.MathUtils;
import maninhouse.epicfight.utils.math.Vec3f;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.ElytraModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderElytra extends RenderItemBase {
	private final ElytraModel<LivingEntity> modelElytra;
	private static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
	
	public RenderElytra() {
		super();
		modelElytra = new ElytraModel<>();
	}
	
	@Override
	public void renderItemOnHead(ItemStack stack, LivingData<?> itemHolder, IRenderTypeBuffer buffer, MatrixStack viewMatrixStack, int packedLight) {
		LivingEntity entity = itemHolder.getOriginalEntity();
		float partialTicks = renderEngine.getPartialTicks();
		VisibleMatrix4f modelMatrix = new VisibleMatrix4f();
		VisibleMatrix4f.scale(new Vec3f(-0.9F, -0.9F, 0.9F), modelMatrix, modelMatrix);
		VisibleMatrix4f.translate(new Vec3f(0F, -0.5F, 0.125F), modelMatrix, modelMatrix);
		VisibleMatrix4f.mul(itemHolder.getEntityModel(ClientModels.LOGICAL_CLIENT).getArmature().findJointById(8).getAnimatedTransform(), modelMatrix, modelMatrix);
		VisibleMatrix4f transpose = VisibleMatrix4f.transpose(modelMatrix, null);
		MathUtils.translateStack(viewMatrixStack, modelMatrix);
		MathUtils.rotateStack(viewMatrixStack, transpose);
		
		float f = MathUtils.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float f1 = MathUtils.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float f2 = f1 - f;
		float f7 = entity.getPitch(partialTicks);
		
		this.modelElytra.isChild = entity.isChild();
        this.modelElytra.setRotationAngles(entity, entity.limbSwing, entity.limbSwingAmount, entity.ticksExisted, f2, f7);
	    IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexBuilder(buffer, RenderType.getArmorCutoutNoCull(TEXTURE_ELYTRA), false, stack.hasEffect());
	    this.modelElytra.render(viewMatrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
}
