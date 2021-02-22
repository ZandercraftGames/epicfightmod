package maninhouse.epicfight.client.shader.uniforms;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UniformMatrix extends Uniform<VisibleMatrix4f>
{
	public UniformMatrix(int programID, String name)
	{
		super(programID, name);
	}
	
	@Override
	public void loadUniformVariable(VisibleMatrix4f matrix)
	{
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		matrix.store(buffer);
		buffer.flip();
		GlStateManager.uniformMatrix4f(uniformLocation, false, buffer);
	}
}
