package maninhouse.epicfight.client.renderer.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import maninhouse.epicfight.capabilities.entity.LivingData;
import maninhouse.epicfight.client.events.engine.RenderEngine;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.utils.math.MathUtils;
import maninhouse.epicfight.utils.math.Vec3f;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderItemBase {
	protected VisibleMatrix4f correctionMatrix;
	
	protected static final VisibleMatrix4f BACK_COORECTION;
	public static RenderEngine renderEngine;
	
	static {
		BACK_COORECTION = new VisibleMatrix4f();
		VisibleMatrix4f.translate(new Vec3f(0.5F, 1, 0.1F), BACK_COORECTION, BACK_COORECTION);
		VisibleMatrix4f.rotate((float)Math.toRadians(130), new Vec3f(0, 0, 1), BACK_COORECTION, BACK_COORECTION);
		VisibleMatrix4f.rotate((float)Math.toRadians(100), new Vec3f(0, 1, 0), BACK_COORECTION, BACK_COORECTION);
	}
	
	public RenderItemBase() {
		correctionMatrix = new VisibleMatrix4f();
		VisibleMatrix4f.rotate((float)Math.toRadians(-80), new Vec3f(1,0,0), correctionMatrix, correctionMatrix);
		VisibleMatrix4f.translate(new Vec3f(0,0.1F,0), correctionMatrix, correctionMatrix);
	}
	
	public void renderItemInHand(ItemStack stack, LivingData<?> itemHolder, Hand hand, IRenderTypeBuffer buffer, MatrixStack viewMatrixStack, int packedLight) {
		VisibleMatrix4f modelMatrix = this.getCorrectionMatrix(stack, itemHolder, hand);
		String heldingHand = hand == Hand.MAIN_HAND ? "Tool_R" : "Tool_L";
		VisibleMatrix4f jointTransform = itemHolder.getEntityModel(Models.LOGICAL_CLIENT).getArmature().findJointByName(heldingHand).getAnimatedTransform();
		VisibleMatrix4f.mul(jointTransform, modelMatrix, modelMatrix);
		VisibleMatrix4f transpose = VisibleMatrix4f.transpose(modelMatrix, null);
		
		MathUtils.translateStack(viewMatrixStack, modelMatrix);
		MathUtils.rotateStack(viewMatrixStack, transpose);
		
		Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(itemHolder.getOriginalEntity(), stack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, false, viewMatrixStack, buffer, packedLight);
		GlStateManager.enableDepthTest();
	}
	
	public void renderItemBack(ItemStack stack, LivingData<?> itemHolder, IRenderTypeBuffer buffer, MatrixStack viewMatrixStack, int packedLight) {
		VisibleMatrix4f modelMatrix = new VisibleMatrix4f(BACK_COORECTION);
		VisibleMatrix4f.mul(itemHolder.getEntityModel(Models.LOGICAL_CLIENT).getArmature().findJointById(0).getAnimatedTransform(), modelMatrix, modelMatrix);
		VisibleMatrix4f transpose = VisibleMatrix4f.transpose(modelMatrix, null);
		
		MathUtils.translateStack(viewMatrixStack, modelMatrix);
		MathUtils.rotateStack(viewMatrixStack, transpose);
		
        Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLight, OverlayTexture.NO_OVERLAY, viewMatrixStack, buffer);
	}
	
	public void renderItemOnHead(ItemStack stack, LivingData<?> itemHolder, IRenderTypeBuffer buffer, MatrixStack viewMatrixStack, int packedLight) {
		VisibleMatrix4f modelMatrix = new VisibleMatrix4f();
		VisibleMatrix4f.translate(new Vec3f(0F, 0.2F, 0F), modelMatrix, modelMatrix);
		VisibleMatrix4f.mul(itemHolder.getEntityModel(Models.LOGICAL_CLIENT).getArmature().findJointById(9).getAnimatedTransform(), modelMatrix, modelMatrix);
		VisibleMatrix4f.scale(new Vec3f(0.6F, 0.6F, 0.6F), modelMatrix, modelMatrix);
		VisibleMatrix4f transpose = VisibleMatrix4f.transpose(modelMatrix, null);
		MathUtils.translateStack(viewMatrixStack, modelMatrix);
		MathUtils.rotateStack(viewMatrixStack, transpose);
		
		Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(itemHolder.getOriginalEntity(), stack, ItemCameraTransforms.TransformType.HEAD, false, viewMatrixStack, buffer, packedLight);
	}
	
	public VisibleMatrix4f getCorrectionMatrix(ItemStack stack, LivingData<?> itemHolder, Hand hand) {
		return new VisibleMatrix4f(correctionMatrix);
	}
}