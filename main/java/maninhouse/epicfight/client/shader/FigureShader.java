package maninhouse.epicfight.client.shader;

import maninhouse.epicfight.client.shader.uniforms.UniformMatrix;
import maninhouse.epicfight.client.shader.uniforms.UniformVector4f;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import maninhouse.epicfight.utils.math.Vec4f;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FigureShader extends ShaderProgram
{	
	private final UniformMatrix viewMatrix;
	private final UniformMatrix projectionMatrix;
	private final UniformVector4f color;
	
	public FigureShader(IResourceManager resourceManager)
	{
		super(resourceManager, new ResourceLocation(EpicFightMod.MODID, "shaders/figurev.glsl"),
				new ResourceLocation(EpicFightMod.MODID, "shaders/figuref.glsl"));
		super.bindShader();
		this.viewMatrix = new UniformMatrix(programID, "viewMatrix");
		this.projectionMatrix = new UniformMatrix(programID, "projectionMatrix");
		this.color = new UniformVector4f(programID, "color");
	}
	
	@Override
	public void bindAttributes()
	{
		super.bindAttribute("in_position");
	}
	
	/**
	 * @param Viewmatrix, Projectionmatrix, color;
	 */
	@Override
	public void loadUniforms(Object... objects)
	{
		viewMatrix.loadUniformVariable((VisibleMatrix4f) objects[0]);
		projectionMatrix.loadUniformVariable((VisibleMatrix4f) objects[1]);
		color.loadUniformVariable((Vec4f) objects[2]);
	}
}
