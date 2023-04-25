package satisfyu.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import satisfyu.vinery.block.entity.WineBoxEntity;
import satisfyu.vinery.util.ClientUtil;

public class WineBoxRenderer implements BlockEntityRenderer<WineBoxEntity> {
	public WineBoxRenderer(BlockEntityRendererProvider.Context context) { }

	@Override
	public void render(WineBoxEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		pPoseStack.pushPose();
		ClientUtil.applyAngle(pPoseStack, pBlockEntity.getBlockState());
		pPoseStack.translate(0.35F, 0.6F, -0.35F);
		pPoseStack.scale(0.7F, 0.7F, 0.7F);
		ItemStack stack = pBlockEntity.getItem(0);
		if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
			pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
			pPoseStack.mulPose(Vector3f.YN.rotationDegrees(90.0F));
			ClientUtil.renderBlockFromItem(blockItem, pPoseStack, pBufferSource, pBlockEntity);
		}
		pPoseStack.popPose();
	}
}