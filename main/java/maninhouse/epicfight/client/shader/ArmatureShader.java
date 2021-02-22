package maninhouse.epicfight.client.shader;

import maninhouse.epicfight.client.shader.uniforms.UniformFloat;
import maninhouse.epicfight.client.shader.uniforms.UniformInt;
import maninhouse.epicfight.client.shader.uniforms.UniformMatrix;
import maninhouse.epicfight.client.shader.uniforms.UniformMatrixArray;
import maninhouse.epicfight.client.shader.uniforms.UniformVector3f;
import maninhouse.epicfight.main.EpicFightMod;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import maninhouse.epicfight.utils.math.Vec3f;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ArmatureShader extends ShaderProgram
{	
	private final UniformMatrix modelMatrix;
	private final UniformMatrix viewMatrix;
	private final UniformMatrix projectionMatrix;
	private final UniformVector3f lightColor;
	private final UniformMatrixArray jointTransforms;
	private final UniformVector3f fogColor;
	private final UniformFloat fogMin;
	private final UniformFloat fogMax;
	private final UniformFloat fogDensity;
	private final UniformInt fogOper;
	
	public ArmatureShader(int vertexId, int fragementId, int jointNumber)
	{
		super(vertexId, fragementId);
		super.bindShader();
		
		this.modelMatrix = new UniformMatrix(programID, "modelMatrix");
		this.viewMatrix = new UniformMatrix(programID, "viewMatrix");
		this.projectionMatrix = new UniformMatrix(programID, "projectionMatrix");
		this.lightColor = new UniformVector3f(programID, "lightColor");
		this.jointTransforms = new UniformMatrixArray(programID, "jointTransforms", jointNumber);
		this.fogColor = new UniformVector3f(programID, "fogColor");
		this.fogMin = new UniformFloat(programID, "fogMin");
		this.fogMax = new UniformFloat(programID, "fogMax");
		this.fogDensity = new UniformFloat(programID, "fogDensity");
		this.fogOper = new UniformInt(programID, "fogOperation");
	}
	
	public ArmatureShader(IResourceManager resourceManager, int jointNumber)
	{
		super(resourceManager, new ResourceLocation(EpicFightMod.MODID, "shaders/armaturev.glsl"),
				new ResourceLocation(EpicFightMod.MODID, "shaders/armaturef.glsl"));
		super.bindShader();
		
		this.modelMatrix = new UniformMatrix(programID, "modelMatrix");
		this.viewMatrix = new UniformMatrix(programID, "viewMatrix");
		this.projectionMatrix = new UniformMatrix(programID, "projectionMatrix");
		this.lightColor = new UniformVector3f(programID, "lightColor");
		this.jointTransforms = new UniformMatrixArray(programID, "jointTransforms", jointNumber);
		this.fogColor = new UniformVector3f(programID, "fogColor");
		this.fogMin = new UniformFloat(programID, "fogMin");
		this.fogMax = new UniformFloat(programID, "fogMax");
		this.fogDensity = new UniformFloat(programID, "fogDensity");
		this.fogOper = new UniformInt(programID, "fogOperation");
	}
	
	@Override
	public void bindAttributes()
	{
		super.bindAttribute("in_position");
		super.bindAttribute("in_normal");
		super.bindAttribute("in_textureCoord");
		super.bindAttribute("in_jointIndices");
		super.bindAttribute("in_weights");
	}
	
	/**
	 * @param 0 model, 1 view, 2 projection
	 */
	@Override
	public void loadUniforms(Object... objects)
	{
		modelMatrix.loadUniformVariable((VisibleMatrix4f) objects[0]);
		viewMatrix.loadUniformVariable((VisibleMatrix4f) objects[1]);
		projectionMatrix.loadUniformVariable((VisibleMatrix4f) objects[2]);
		lightColor.loadUniformVariable((Vec3f) objects[3]);
		jointTransforms.loadUniformVariable((VisibleMatrix4f[]) objects[4]);
		fogColor.loadUniformVariable((Vec3f) objects[5]);
		fogMin.loadUniformVariable((Float) objects[6]);
		fogMax.loadUniformVariable((Float) objects[7]);
		fogDensity.loadUniformVariable((Float) objects[8]);
		fogOper.loadUniformVariable((Integer) objects[9]);
	}
}