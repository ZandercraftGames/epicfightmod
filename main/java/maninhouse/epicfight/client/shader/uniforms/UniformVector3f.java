package maninhouse.epicfight.client.shader.uniforms;

import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.systems.RenderSystem;

import maninhouse.epicfight.utils.math.Vec3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UniformVector3f extends Uniform<Vec3f>
{
	public UniformVector3f(int programID, String name)
	{
		super(programID, name);
	}

	@Override
	public void loadUniformVariable(Vec3f vector3f)
	{
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
		GL20.glUniform3f(uniformLocation, vector3f.x, vector3f.y, vector3f.z);
	}
}