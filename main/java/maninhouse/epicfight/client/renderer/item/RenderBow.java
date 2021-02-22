package maninhouse.epicfight.client.renderer.item;

import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import maninhouse.epicfight.utils.math.Vec3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderBow extends RenderShootableWeapon
{
	public RenderBow()
	{
		correctionMatrix = new VisibleMatrix4f();
		
		VisibleMatrix4f.rotate((float)Math.toRadians(-90), new Vec3f(1,0,0), correctionMatrix, correctionMatrix);
		VisibleMatrix4f.rotate((float)Math.toRadians(-10), new Vec3f(0,0,1), correctionMatrix, correctionMatrix);
		VisibleMatrix4f.translate(new Vec3f(0.06F,0.1F,0), correctionMatrix, correctionMatrix);
	}
}