package maninhouse.epicfight.client.shader;

import maninhouse.epicfight.client.shader.uniforms.UniformMatrix;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BasicShader extends ShaderProgram
{	
	private final UniformMatrix modelMatrix;
	private final UniformMatrix viewMatrix;
	private final UniformMatrix projectionMatrix;
	
	public BasicShader(IResourceManager resourceManager)
	{
		super(resourceManager, new ResourceLocation(EpicFightMod.MODID, "shaders/basicv.glsl"), new ResourceLocation(EpicFightMod.MODID, "shaders/basicf.glsl"));
		super.bindShader();
		this.modelMatrix = new UniformMatrix(programID, "modelMatrix");
		this.viewMatrix = new UniformMatrix(programID, "viewMatrix");
		this.projectionMatrix = new UniformMatrix(programID, "projectionMatrix");
	}
	
	@Override
	public void bindAttributes()
	{
		super.bindAttribute("in_position");
		super.bindAttribute("in_normal");
		super.bindAttribute("in_textureCoord");
	}
	
	/**
	 * @param 1 model, 2 view, 3 projection
	 */
	@Override
	public void loadUniforms(Object... objects)
	{
		modelMatrix.loadUniformVariable((VisibleMatrix4f) objects[0]);
		viewMatrix.loadUniformVariable((VisibleMatrix4f) objects[1]);
		projectionMatrix.loadUniformVariable((VisibleMatrix4f) objects[2]);
	}
}