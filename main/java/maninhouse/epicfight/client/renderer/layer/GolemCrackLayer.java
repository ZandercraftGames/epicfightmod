package maninhouse.epicfight.client.renderer.layer;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import maninhouse.epicfight.capabilities.entity.mob.IronGolemData;
import maninhouse.epicfight.client.renderer.ModRenderTypes;
import maninhouse.epicfight.gamedata.Models;
import maninhouse.epicfight.utils.math.VisibleMatrix4f;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GolemCrackLayer extends Layer<IronGolemEntity, IronGolemData> {
	private static final Map<IronGolemEntity.Cracks, ResourceLocation> CRACK_MAP = ImmutableMap.of(
			IronGolemEntity.Cracks.LOW, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_low.png"),
			IronGolemEntity.Cracks.MEDIUM, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_medium.png"),
			IronGolemEntity.Cracks.HIGH, new ResourceLocation("textures/entity/iron_golem/iron_golem_crackiness_high.png"));
	
	@Override
	public void renderLayer(IronGolemData entitydata, IronGolemEntity entityGolem, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int packedLightIn, VisibleMatrix4f[] poses) {
		
		IronGolemEntity.Cracks crack = entityGolem.func_226512_l_();
		if(crack != IronGolemEntity.Cracks.NONE) {
			IVertexBuilder ivertexbuilder = bufferIn.getBuffer(ModRenderTypes.getEntityCutoutNoCull(CRACK_MAP.get(crack)));
			entitydata.getEntityModel(Models.LOGICAL_CLIENT).draw(matrixStackIn, ivertexbuilder, packedLightIn, 1.0F, 1.0F, 1.0F, 1.0F, poses);
		}
	}
}