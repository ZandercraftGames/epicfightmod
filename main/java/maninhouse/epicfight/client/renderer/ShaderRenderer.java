package maninhouse.epicfight.client.renderer;

import maninhouse.epicfight.client.events.engine.RenderEngine;
import maninhouse.epicfight.client.shader.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ShaderRenderer <S extends ShaderProgram>
{
	public static RenderEngine renderEngine;
	private final ResourceLocation textureLocation;
	protected S shader;
	
	public ShaderRenderer(ResourceLocation textureLocation)
	{
		this.textureLocation = textureLocation;
	}
	
	protected ResourceLocation getTexture()
	{
		return this.textureLocation;
	}
	
	protected static void bindTexture(ResourceLocation resourceLocation)
	{
		Minecraft.getInstance().getTextureManager().bindTexture(resourceLocation);
	}
	
	public void clearRenderer()
	{
		shader.clearShader();
	}
}