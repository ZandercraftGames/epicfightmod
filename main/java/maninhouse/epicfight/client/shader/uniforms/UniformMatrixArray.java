package maninhouse.epicfight.client.shader.uniforms;

import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class UniformMatrixArray extends Uniform<VisibleMatrix4f[]>
{
	private UniformMatrix[] matrixArray;
	
	public UniformMatrixArray(int programID, String name, int size)
	{
		super(programID, name);
		
		matrixArray = new UniformMatrix[size];
		for(int i = 0; i < matrixArray.length; i++)
		{
			matrixArray[i] = new UniformMatrix(programID, name + "["+i+"]");
		}
	}
	
	@Override
	public void loadUniformVariable(VisibleMatrix4f[] matrix)
	{
		for(int i = 0; i < matrix.length; i++)
		{
			matrixArray[i].loadUniformVariable(matrix[i]);
		}
	}
}