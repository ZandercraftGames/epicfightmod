package maninhouse.epicfight.client.shader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ShaderProgram
{	
	protected final int programID;
	protected final int vertexShaderID;
	protected final int geometryShaderID;
	protected final int fragmentShaderID;
	private int attribCount = 0;
	
	public ShaderProgram(int vertexId, int fragmentId)
	{
		this.vertexShaderID = vertexId;
		this.fragmentShaderID = fragmentId;	
		this.programID = GL20.glCreateProgram();
		this.geometryShaderID = 0;
	}
	
	public ShaderProgram(IResourceManager resourceManager, ResourceLocation vertex, ResourceLocation fragment)
	{
		this.vertexShaderID = loadShader(resourceManager, vertex, GL20.GL_VERTEX_SHADER);
		this.fragmentShaderID = loadShader(resourceManager, fragment, GL20.GL_FRAGMENT_SHADER);	
		this.programID = GlStateManager.createProgram();
		this.geometryShaderID = 0;
	}
	
	public ShaderProgram(IResourceManager resourceManager, ResourceLocation vertex, ResourceLocation geometry, ResourceLocation fragment)
	{
		this.vertexShaderID = loadShader(resourceManager, vertex, GL20.GL_VERTEX_SHADER);
		this.geometryShaderID = loadShader(resourceManager, geometry, GL32.GL_GEOMETRY_SHADER);
		this.fragmentShaderID = loadShader(resourceManager, fragment, GL20.GL_FRAGMENT_SHADER);
		this.programID = GL20.glCreateProgram();
	}
	
	public void bindShader()
	{
		GlStateManager.attachShader(programID, vertexShaderID);
		GlStateManager.attachShader(programID, fragmentShaderID);
		
		if(geometryShaderID != 0)
		{
			GlStateManager.attachShader(programID, geometryShaderID);
		}
		
		bindAttributes();
		GlStateManager.linkProgram(programID);
		GL20.glValidateProgram(programID);
	}
	
	protected abstract void bindAttributes();
	public abstract void loadUniforms(Object... objects);
	
	public void setMultiOutput(int attachment, String outName)
	{
		GL30.glBindFragDataLocation(programID, attachment, outName);
	}
	
	public void start()
	{
		GlStateManager.useProgram(programID);
	}
	
	public void stop()
	{
		GlStateManager.useProgram(0);
	}
	
	public void clearShader()
	{
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GlStateManager.deleteShader(vertexShaderID);
		GlStateManager.deleteShader(fragmentShaderID);
		GlStateManager.deleteProgram(programID);
		
		if(geometryShaderID != 0)
		{
			GL20.glDetachShader(programID, geometryShaderID);
			GlStateManager.deleteShader(geometryShaderID);
		}
	}
	
	protected void bindAttribute(String variableName)
	{
		GL20.glBindAttribLocation(programID, attribCount++, variableName);
	}
	
	protected static int loadShader(IResourceManager resourceManager, ResourceLocation resourceLocation, int type)
	{
        BufferedInputStream bufferedinputstream;
        CharSequence seq = null;
        
		try
		{
			bufferedinputstream = new BufferedInputStream(resourceManager.getResource(resourceLocation).getInputStream());
			seq = CharBuffer.wrap(toCharSequence(bufferedinputstream));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        
        int shaderID = GlStateManager.createShader(type);
        GlStateManager.shaderSource(shaderID, seq);
        GlStateManager.compileShader(shaderID);
        
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS ) == GL11.GL_FALSE)
        {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
        }
        
        return shaderID;
	}
	
	protected static byte[] toByteArray(BufferedInputStream stream) throws IOException
    {
        byte[] abyte;
        
        try
        {
            abyte = IOUtils.toByteArray((InputStream)stream);
        }
        finally
        {
            stream.close();
        }

        return abyte;
    }
	
	protected static char[] toCharSequence(BufferedInputStream stream) throws IOException
    {
        char[] abyte;
        
        try
        {
            abyte = IOUtils.toCharArray((InputStream)stream, Charset.defaultCharset());
        }
        finally
        {
            stream.close();
        }

        return abyte;
    }
	
	int getVertexShaderId()
	{
		return vertexShaderID;
	}
}