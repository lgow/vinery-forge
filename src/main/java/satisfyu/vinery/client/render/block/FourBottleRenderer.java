package satisfyu.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import satisfyu.vinery.block.entity.FourWineBottleStorageBlockEntity;
import satisfyu.vinery.util.ClientUtil;

public class FourBottleRenderer implements BlockEntityRenderer<FourWineBottleStorageBlockEntity> {
	public FourBottleRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(FourWineBottleStorageBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		ClientUtil.applyAngle(pPoseStack, pBlockEntity.getBlockState());
		pPoseStack.translate(-0.13, 0.335, 0.125);
		pPoseStack.scale(0.9f, 0.9f, 0.9f);
		NonNullList<ItemStack> itemStacks = pBlockEntity.getInventory();
		for (int i = 0; i < itemStacks.size(); i++) {
			ItemStack stack = itemStacks.get(i);
			if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
				pPoseStack.pushPose();
				if (i == 0) {
					pPoseStack.translate(-0.35f, 0, 0f);
				}
				else if (i == 1) {
					pPoseStack.translate(0, -0.33f, 0f);
				}
				else if (i == 2) {
					pPoseStack.translate(-0.7f, -0.33f, 0f);
				}
				else if (i == 3) {
					pPoseStack.translate(-0.35f, -0.66f, 0f);
				}
				else {
					pPoseStack.popPose();
					continue;
				}
				pPoseStack.mulPose(Vector3f.XN.rotationDegrees(90f));
				ClientUtil.renderBlockFromItem(blockItem, pPoseStack, pBufferSource, pBlockEntity);
				pPoseStack.popPose();
			}
		}
	}
}
