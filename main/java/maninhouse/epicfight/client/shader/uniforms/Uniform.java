package maninhouse.epicfight.client.shader.uniforms;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class Uniform<T>
{
	protected int uniformLocation;
	
	public Uniform(int programID, String name)
	{
		uniformLocation = GlStateManager.getUniformLocation(programID, name);
		if(uniformLocation == -1)
		{
			System.err.println("No uniform variable called " + name);
		}
	}
	
	public abstract void loadUniformVariable(T uniform);
}