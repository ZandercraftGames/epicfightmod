package maninhouse.epicfight.client.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Shaders
{
	public static FigureShader FIGURE_SHADER;
	
	public static void loadShaders()
	{
		IResourceManager rsrcManager = Minecraft.getInstance().getResourceManager();
		FIGURE_SHADER = new FigureShader(rsrcManager);
	}
}