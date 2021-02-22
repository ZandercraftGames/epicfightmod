package maninhouse.epicfight.client.shader.uniforms;

import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.systems.RenderSystem;

import maninhouse.epicfight.utils.math.Vec4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UniformVector4f extends Uniform<Vec4f>
{	
	public UniformVector4f(int programID, String name)
	{
		super(programID, name);
	}
	
	@Override
	public void loadUniformVariable(Vec4f vector4f)
	{
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
		GL20.glUniform4f(uniformLocation, vector4f.x, vector4f.y, vector4f.z, vector4f.w);
	}
}