package maninhouse.epicfight.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import maninhouse.epicfight.client.ClientEngine;
import maninhouse.epicfight.client.renderer.ModRenderTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TargetIndicator extends EntityIndicator
{
	@Override
	public boolean shouldDraw(LivingEntity entityIn)
	{
		if(entityIn != ClientEngine.INSTANCE.getPlayerData().getAttackTarget())
			return false;
		else if(entityIn.isInvisible() || !entityIn.isAlive() || entityIn == Minecraft.getInstance().player.getRidingEntity())
			return false;
		else if(entityIn.getDistanceSq(Minecraft.getInstance().getRenderViewEntity()) >= 400)
			return false;
		else if(entityIn instanceof PlayerEntity)
		{
			PlayerEntity playerIn = (PlayerEntity) entityIn;
			if(playerIn.isSpectator())
				return false;
		}
		
		return true;
	}
	
	@Override
	public void drawIndicator(LivingEntity entityIn, MatrixStack matStackIn, IRenderTypeBuffer bufferIn)
	{
		Matrix4f mvMatrix = super.getMVMatrix(matStackIn, entityIn, 0.0F, entityIn.getHeight() + 0.45F, 0.0F, true, false);
		this.drawTexturedModalRect2DPlane(mvMatrix, bufferIn.getBuffer(ModRenderTypes.getEntityIndicator(BATTLE_ICON)),
				-0.1F, -0.1F, 0.1F, 0.1F, 65, 2, 91, 36);
	}
}