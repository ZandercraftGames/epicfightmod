package maninhouse.epicfight.client.shader.uniforms;

import org.lwjgl.opengl.GL20;

import com.mojang.blaze3d.systems.RenderSystem;

import maninhouse.epicfight.utils.math.Vec2f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UniformVector2f extends Uniform<Vec2f>
{
	public UniformVector2f(int programID, String name)
	{
		super(programID, name);
	}

	@Override
	public void loadUniformVariable(Vec2f vector2f)
	{
		RenderSystem.assertThread(RenderSystem::isOnRenderThread);
		GL20.glUniform2f(uniformLocation, vector2f.x, vector2f.y);
	}
}