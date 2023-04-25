package satisfyu.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import satisfyu.vinery.block.entity.TallFlowerPotBlockEntity;

import static satisfyu.vinery.util.ClientUtil.renderBlock;

public class TallFlowerPotRenderer implements BlockEntityRenderer<TallFlowerPotBlockEntity> {
	public TallFlowerPotRenderer(BlockEntityRendererProvider.Context ctx) { }

	@Override
	public void render(TallFlowerPotBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		if (!pBlockEntity.hasLevel()) { return; }
		Item item = pBlockEntity.getFlower().getItem();
		pPoseStack.pushPose();
		if (item instanceof BlockItem) {
			BlockState state = ((BlockItem) item).getBlock().defaultBlockState();
			pPoseStack.translate(0f, 0.4f, 0f);
			renderBlock(state, pPoseStack, pBufferSource, pBlockEntity);
			state = ((BlockItem) item).getBlock().defaultBlockState().setValue(TallFlowerBlock.HALF, DoubleBlockHalf.UPPER);
			pPoseStack.translate(0f, 1f, 0f);
			renderBlock(state, pPoseStack, pBufferSource, pBlockEntity);
		}
		pPoseStack.popPose();
	}
}
