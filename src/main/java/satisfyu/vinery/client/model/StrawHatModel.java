package satisfyu.vinery.client.model;
// Made with Blockbench 4.6.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import satisfyu.vinery.Vinery;

public class StrawHatModel <T extends LivingEntity> extends HumanoidModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation(Vinery.MODID, "straw_hat"), "main");

	private final ModelPart top_part;

	private final ModelPart lower_part;

	public StrawHatModel(ModelPart root) {
		super(root);
		this.top_part = root.getChild("top_part");
		this.lower_part = root.getChild("lower_part");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(LayerDefinitions.OUTER_ARMOR_DEFORMATION, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition top_part = partdefinition.addOrReplaceChild("top_part", CubeListBuilder.create().texOffs(-16, 12)
						.addBox(-8.0F, -4.00F, -8.0F, 16.0F, 0.0F, 16.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition lower_part = partdefinition.addOrReplaceChild("lower_part",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-4.0F, -8.001F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		top_part.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		lower_part.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}