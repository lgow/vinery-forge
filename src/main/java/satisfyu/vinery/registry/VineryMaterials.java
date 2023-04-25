package satisfyu.vinery.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class VineryMaterials {
	public static final ArmorMaterial VINEMAKER_ARMOR = new ArmorMaterial() {
		@Override
		public int getDurabilityForSlot(@NotNull EquipmentSlot slot) {
			return ArmorMaterials.LEATHER.getDurabilityForSlot(slot);
		}

		@Override
		public int getDefenseForSlot(@NotNull EquipmentSlot slot) {
			return ArmorMaterials.LEATHER.getDefenseForSlot(slot);
		}

		@Override
		public int getEnchantmentValue() {
			return ArmorMaterials.LEATHER.getEnchantmentValue();
		}

		@Override
		public @NotNull SoundEvent getEquipSound() {
			return ArmorMaterials.LEATHER.getEquipSound();
		}

		@Override
		public @NotNull Ingredient getRepairIngredient() {
			return ArmorMaterials.LEATHER.getRepairIngredient();
		}

		@Override
		public @NotNull String getName() {
			return "vinemaker";
		}

		@Override
		public float getToughness() {
			return ArmorMaterials.LEATHER.getToughness();
		}

		@Override
		public float getKnockbackResistance() {
			return ArmorMaterials.LEATHER.getKnockbackResistance();
		}
	};
}
