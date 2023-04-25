package satisfyu.vinery.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import satisfyu.vinery.client.model.StrawHatModel;

import java.util.function.Consumer;

public class StrawHatItem extends WinemakerArmorItem {
	public StrawHatItem(ArmorMaterial material, EquipmentSlot slot, Properties settings) {
		super(material, slot, settings);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ArmorRender.INSTANCE);
	}

	private static final class ArmorRender implements IClientItemExtensions {
		public static final ArmorRender INSTANCE = new ArmorRender();

		@Override
		public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
			EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
			ModelPart root = modelSet.bakeLayer(StrawHatModel.LAYER_LOCATION);
			HumanoidModel<?> model = new StrawHatModel(root);
			return model;
		}
	}
}
