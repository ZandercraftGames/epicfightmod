package maninhouse.epicfight.client.shader;

import org.lwjgl.opengl.GL20;

import maninhouse.epicfight.client.shader.uniforms.UniformInt;
import maninhouse.epicfight.main.EpicFightMod;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ShiningEyeShader extends ArmatureShader
{
	private final UniformInt texture1;
	private final UniformInt texture2;
	
	public ShiningEyeShader(IResourceManager resourceManager, int vertexShaderId, int jointNumber)
	{
		super(vertexShaderId, loadShader(resourceManager,
				new ResourceLocation(EpicFightMod.MODID, "shaders/eyef.glsl"), GL20.GL_FRAGMENT_SHADER), jointNumber);
		
		this.texture1 = new UniformInt(programID, "texture2D");
		this.texture2 = new UniformInt(programID, "eyeTexture2D");
	}
	
	@Override
	public void loadUniforms(Object... objects)
	{
		super.loadUniforms(objects);
		texture1.loadUniformVariable(0);
		texture2.loadUniformVariable(7);
	}
}