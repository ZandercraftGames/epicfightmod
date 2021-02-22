package maninhouse.epicfight.client.shader.uniforms;

import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UniformInt extends Uniform<Integer>
{	
	public UniformInt(int programID, String name)
	{
		super(programID, name);
	}

	@Override
	public void loadUniformVariable(Integer uniform)
	{
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
		GL20.glUniform1i(uniformLocation, uniform);
	}
}