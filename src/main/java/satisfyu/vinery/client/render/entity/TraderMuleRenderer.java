package satisfyu.vinery.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import satisfyu.vinery.client.ClientModEvents;
import satisfyu.vinery.client.model.TraderMuleModel;
import satisfyu.vinery.entity.TraderMuleEntity;
import satisfyu.vinery.util.VineryIdentifier;

public class TraderMuleRenderer <T extends TraderMuleEntity> extends MobRenderer<T, TraderMuleModel<T>> {
	private static final ResourceLocation TEXTURE = new VineryIdentifier("textures/entity/trader_mule.png");

	public TraderMuleRenderer(EntityRendererProvider.Context context) {
		super(context, new TraderMuleModel<T>(context.bakeLayer(ClientModEvents.TRADER_MULE_LAYER_LOCATION)), 0.7f);
	}

	@Override
	public ResourceLocation getTextureLocation(TraderMuleEntity cowEntity) {
		return TEXTURE;
	}
}
