package satisfyu.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import satisfyu.vinery.block.FlowerBoxBlock;
import satisfyu.vinery.block.entity.FlowerBoxBlockEntity;
import satisfyu.vinery.util.ClientUtil;

public class FlowerBoxRenderer implements BlockEntityRenderer<FlowerBoxBlockEntity> {
	public FlowerBoxRenderer(BlockEntityRendererProvider.Context context) {
	}

	public static void applyBlockAngle(PoseStack matrices, BlockState state) {
		switch (state.getValue(FlowerBoxBlock.FACING)) {
			case EAST -> {
				matrices.translate(-0.5f, 0f, 1f);
				matrices.mulPose(Vector3f.YP.rotationDegrees(90));
			}
			case SOUTH -> {
				matrices.translate(1f, 0f, 1f);
				matrices.mulPose(Vector3f.YP.rotationDegrees(180));
			}
			case WEST -> {
				matrices.translate(1.5f, 0f, 0f);
				matrices.mulPose(Vector3f.YP.rotationDegrees(270));
			}
		}
	}

	@Override
	public void render(FlowerBoxBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (!pBlockEntity.hasLevel()) { return; }
		BlockState blockState = pBlockEntity.getBlockState();
		if (blockState.getBlock() instanceof FlowerBoxBlock) {
			pPoseStack.pushPose();
			applyBlockAngle(pPoseStack, blockState);
			pPoseStack.translate(-0.25, 0.25, 0.25);
			if (!pBlockEntity.isSlotEmpty(0)) {
				BlockState state1 = Block.byItem(pBlockEntity.getFlower(0).getItem()).defaultBlockState();
				ClientUtil.renderBlock(state1, pPoseStack, pBufferSource, pBlockEntity);
			}
			pPoseStack.translate(0.5, 0, 0);
			if (!pBlockEntity.isSlotEmpty(1)) {
				BlockState state2 = Block.byItem(pBlockEntity.getFlower(1).getItem()).defaultBlockState();
				ClientUtil.renderBlock(state2, pPoseStack, pBufferSource, pBlockEntity);
			}
			pPoseStack.popPose();
		}
	}
}
