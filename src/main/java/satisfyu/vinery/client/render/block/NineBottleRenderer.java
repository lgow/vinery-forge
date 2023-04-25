package satisfyu.vinery.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import satisfyu.vinery.block.entity.NineWineBottleStorageBlockEntity;
import satisfyu.vinery.util.ClientUtil;

public class NineBottleRenderer implements BlockEntityRenderer<NineWineBottleStorageBlockEntity> {
	public NineBottleRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(NineWineBottleStorageBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
		ClientUtil.applyAngle(pPoseStack, pBlockEntity.getBlockState());
		pPoseStack.translate(-0.13, 0.335, 0.125);
		pPoseStack.scale(0.9f, 0.9f, 0.9f);
		NonNullList<ItemStack> itemStacks = pBlockEntity.getInventory();
		for (int i = 0; i < itemStacks.size(); i++) {
			ItemStack stack = itemStacks.get(i);
			if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
				pPoseStack.pushPose();
				int line = i >= 6 ? 3 : i >= 3 ? 2 : 1;
				float x;
				float y;
				if (line == 1) {
					x = -0.35f * i;
					y = 0;
				}
				else if (line == 2) {
					x = -0.35f * (i - 3);
					y = -0.33f;
				}
				else {
					x = -0.35f * (i - 6);
					y = -0.66f;
				}
				pPoseStack.translate(x, y, 0f);
				pPoseStack.mulPose(Vector3f.XN.rotationDegrees(90f));
				ClientUtil.renderBlockFromItem(blockItem, pPoseStack, pBufferSource, pBlockEntity);
				pPoseStack.popPose();
			}
		}
	}
}
