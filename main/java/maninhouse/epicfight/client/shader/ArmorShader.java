package maninhouse.epicfight.client.shader;

import org.lwjgl.opengl.GL20;

import maninhouse.epicfight.client.shader.uniforms.UniformInt;
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
public class ArmorShader extends ArmatureShader
{
	private final UniformInt overlay;
	private final UniformVector4f ovelayColor;
	private final UniformInt texture1;
	private final UniformInt texture2;
	
	private final UniformInt glint;
	private final UniformInt texture3;
	private final UniformVector4f glintColor;
	private final UniformMatrix textureTransform1;
	private final UniformMatrix textureTransform2;
	
	public ArmorShader(IResourceManager resourceManager, int vertexShaderId, int jointNumber)
	{
		super(vertexShaderId, loadShader(resourceManager, new ResourceLocation(EpicFightMod.MODID, "shaders/armorf.glsl"), GL20.GL_FRAGMENT_SHADER), jointNumber);
		this.texture1 = new UniformInt(programID, "texture2D");
		this.texture2 = new UniformInt(programID, "overlay2D");
		this.texture3 = new UniformInt(programID, "glint2D");
		
		this.overlay = new UniformInt(programID, "overlay");
		this.ovelayColor = new UniformVector4f(programID, "overlayColor");
		
		this.glint = new UniformInt(programID, "glint");
		this.glintColor = new UniformVector4f(programID, "glintColor");
		this.textureTransform1 = new UniformMatrix(this.programID, "glintTransform1");
		this.textureTransform2 = new UniformMatrix(this.programID, "glintTransform2");
	}
	
	/**
	 * @param 1 model, 2 view, 3 projection
	 */
	public void loadUniforms(int overlay, int glint, Object... objects)
	{
		super.loadUniforms(objects);
		
		texture1.loadUniformVariable(0);
		texture2.loadUniformVariable(6);
		texture3.loadUniformVariable(7);
		this.overlay.loadUniformVariable(overlay);
		
		if(overlay > 0)
		{
			ovelayColor.loadUniformVariable((Vec4f) objects[10]);
		}
		
		this.glint.loadUniformVariable(glint);
		if(glint > 0)
		{
			int i = 10+overlay;
			glintColor.loadUniformVariable((Vec4f) objects[i]);
			textureTransform1.loadUniformVariable((VisibleMatrix4f) objects[++i]);
			textureTransform2.loadUniformVariable((VisibleMatrix4f) objects[++i]);
		}
	}
}