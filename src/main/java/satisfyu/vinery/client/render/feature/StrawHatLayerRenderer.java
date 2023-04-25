package satisfyu.vinery.client.render.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.client.model.StrawHatModel;
import satisfyu.vinery.item.WinemakerArmorItem;

public class StrawHatLayerRenderer <T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	private final StrawHatModel<T> model;

	public StrawHatLayerRenderer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet) {
		super(renderLayerParent);
		model = new StrawHatModel<>(entityModelSet.bakeLayer(StrawHatModel.LAYER_LOCATION));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if ((entity instanceof Player player && player.getInventory().getArmor(3)
				.getItem() instanceof WinemakerArmorItem) || (entity instanceof ArmorStand armorStand
				&& armorStand.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof WinemakerArmorItem)) {
			poseStack.pushPose();
			if (entity.isCrouching()) {
				poseStack.translate(0, 0.3, 0);
			}
			this.getParentModel().copyPropertiesTo(model);
			float f = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - Mth.lerp(partialTicks, entity.yBodyRotO,
					entity.yBodyRot);
			float f1 = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
			poseStack.scale(1.002F, 1.002F, 1.002F);
			poseStack.mulPose(Vector3f.YP.rotationDegrees(netHeadYaw));
			poseStack.mulPose(Vector3f.XP.rotationDegrees(headPitch));
			VertexConsumer consumer = buffer.getBuffer(
					RenderType.entityCutout(new ResourceLocation(Vinery.MODID, "textures/armor/straw_hat.png")));
			this.model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
			this.model.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,
					1.0F);
			poseStack.popPose();
		}
	}
}
