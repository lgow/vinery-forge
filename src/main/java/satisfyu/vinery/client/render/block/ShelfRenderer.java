package satisfyu.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import satisfyu.vinery.block.entity.ShelfEntity;
import satisfyu.vinery.util.ClientUtil;

public class ShelfRenderer implements BlockEntityRenderer<ShelfEntity> {
	public ShelfRenderer(BlockEntityRendererProvider.Context context) { }

	@Override
	public boolean shouldRender(ShelfEntity pBlockEntity, Vec3 pCameraPos) {
		return BlockEntityRenderer.super.shouldRender(pBlockEntity, pCameraPos);
	}

	@Override
	public void render(ShelfEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		ClientUtil.applyAngle(pPoseStack, pBlockEntity.getBlockState());
		pPoseStack.translate(-0.4, 0.5, 0.25);
		pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90f));
		pPoseStack.scale(0.5f, 0.5f, 0.5f);
		NonNullList<ItemStack> itemStacks = pBlockEntity.getInventory();
		for (int i = 0; i < itemStacks.size(); i++) {
			ItemStack stack = itemStacks.get(i);
			if (!stack.isEmpty()) {
				pPoseStack.pushPose();
				pPoseStack.translate(0f, 0f, 0.2f * i);
				pPoseStack.mulPose(Vector3f.YN.rotationDegrees(22.5f));
				ClientUtil.renderItem(stack, pPoseStack, pBufferSource, pBlockEntity);
				pPoseStack.popPose();
			}
		}
	}
}
