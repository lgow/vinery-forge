package satisfyu.vinery.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import satisfyu.vinery.entity.VineryBoat;
import satisfyu.vinery.util.VineryIdentifier;
import satisfyu.vinery.util.VineryMth;

public class VineryBoatRenderer extends EntityRenderer<VineryBoat> {
	private final BoatModel boatModel;

	private final ResourceLocation texture;

	public VineryBoatRenderer(EntityRendererProvider.Context p_234563_, boolean p_234564_) {
		super(p_234563_);
		this.shadowRadius = 0.8F;
		this.boatModel = createBoatModel(p_234563_, p_234564_);
		this.texture = new VineryIdentifier(createLocation(p_234564_));
	}

	public static ModelLayerLocation createBoatModelname() {
		return new ModelLayerLocation(new VineryIdentifier("boat"), "main");
	}

	public static ModelLayerLocation createChestBoatModelname() {
		return new ModelLayerLocation(new VineryIdentifier("chest_boat"), "main");
	}

	private BoatModel createBoatModel(EntityRendererProvider.Context context, boolean chest) {
		ModelLayerLocation modelLayerLocation = chest ? createChestBoatModelname() : createBoatModelname();
		ModelPart modelPart = context.bakeLayer(modelLayerLocation);
		return new BoatModel(modelPart, chest);
	}

	private String createLocation(boolean hasChest) {
		return hasChest ? "textures/entity/chest_boat/cherry.png" : "textures/entity/boat/cherry.png";
	}

	@Override
	public void render(VineryBoat boat, float boatYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferSource, int light) {
		stack.pushPose();
		stack.translate(0.0F, 0.375F, 0.0F);
		stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - boatYaw));
		float f = (float) boat.getHurtTime() - partialTicks;
		float f1 = boat.getDamage() - partialTicks;
		if (f1 < 0.0F) {
			f1 = 0.0F;
		}
		if (f > 0.0F) {
			stack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float) boat.getHurtDir()));
		}
		float f2 = boat.getBubbleAngle(partialTicks);
		if (!Mth.equal(f2, 0.0F)) {
			stack.mulPose(
					VineryMth.getQuaterion(boat.getBubbleAngle(partialTicks) * (Mth.PI / 180.0F), 1.0F, 0.0F, 1.0F));
		}
		stack.scale(-1.0F, -1.0F, 1.0F);
		stack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
		boatModel.setupAnim(boat, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
		VertexConsumer vertexConsumer = bufferSource.getBuffer(boatModel.renderType(texture));
		boatModel.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		if (!boat.isUnderWater()) {
			VertexConsumer vertexConsumer1 = bufferSource.getBuffer(RenderType.waterMask());
			boatModel.waterPatch().render(stack, vertexConsumer1, light, OverlayTexture.NO_OVERLAY);
		}
		stack.popPose();
		super.render(boat, boatYaw, partialTicks, stack, bufferSource, light);
	}

	@Override
	public ResourceLocation getTextureLocation(VineryBoat p_114482_) {
		return texture;
	}
}
