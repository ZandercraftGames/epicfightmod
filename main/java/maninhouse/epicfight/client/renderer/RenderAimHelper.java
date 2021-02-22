package maninhouse.epicfight.client.renderer;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import maninhouse.epicfight.utils.math.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderAimHelper
{
	public void doRender(MatrixStack matStackIn, float partialTicks)
	{
		Entity entity = Minecraft.getInstance().getRenderViewEntity();
		RayTraceResult ray = entity.pick(200.D, partialTicks, false);
		Vector3d vec3 = ray.getHitVec();
		Vec3f pos1 = new Vec3f((float) MathHelper.lerp((double)partialTicks, entity.lastTickPosX, entity.getPosX()),
							   (float) MathHelper.lerp((double)partialTicks, entity.lastTickPosY, entity.getPosY()) + entity.getEyeHeight(),
							   (float) MathHelper.lerp((double)partialTicks, entity.lastTickPosZ, entity.getPosZ()));
		Vec3f pos2 = new Vec3f((float) vec3.x, (float) vec3.y, (float) vec3.z);
		
		float length = Vec3f.sub(pos2, pos1, null).length();
		
		GlStateManager.enableBlend();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_POINT_SMOOTH);
		GL11.glLineWidth(1.0F);
		
		ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
		Vector3d projectedView = renderInfo.getProjectedView();
		matStackIn.push();
		matStackIn.translate(-projectedView.x, -projectedView.y, -projectedView.z);
		matStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
		Matrix4f matrix = matStackIn.getLast().getMatrix();
		
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		bufferBuilder.pos(matrix, pos1.x, pos1.y, pos1.z).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
		bufferBuilder.pos(matrix, pos2.x, pos2.y, pos2.z).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
		bufferBuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferBuilder);
		
		float ratio = Math.min(50.0F, length);
		ratio = (50.1F - ratio) / 50.0F;
		GL11.glPointSize(ratio * 10.0F);
		bufferBuilder.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
		bufferBuilder.pos(matrix, pos2.x, pos2.y, pos2.z).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
		bufferBuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferBuilder);
		
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
		bufferBuilder.pos(-1000, 100, -1.0F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
		bufferBuilder.pos(-1000, -100, -1.0F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
		bufferBuilder.pos(1000, -100, -1.0F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
		bufferBuilder.pos(1000, 100, -1.0F).color(0.0F, 1.0F, 0.0F, 1.0F).endVertex();
		bufferBuilder.finishDrawing();
		WorldVertexBufferUploader.draw(bufferBuilder);
		
		matStackIn.pop();
		GlStateManager.disableBlend();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_POINT_SMOOTH);
	}
}
