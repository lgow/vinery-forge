package satisfyu.vinery.util;

import com.google.gson.JsonArray;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class VineryUtils {
	public static boolean matchesRecipe(Container inventory, NonNullList<Ingredient> recipe, int startIndex, int endIndex) {
		final List<ItemStack> validStacks = new ArrayList<>();
		for (int i = startIndex; i <= endIndex; i++) {
			final ItemStack stackInSlot = inventory.getItem(i);
			if (!stackInSlot.isEmpty()) { validStacks.add(stackInSlot); }
		}
		for (Ingredient entry : recipe) {
			boolean matches = false;
			for (ItemStack item : validStacks) {
				if (entry.test(item)) {
					matches = true;
					validStacks.remove(item);
					break;
				}
			}
			if (!matches) {
				return false;
			}
		}
		return true;
	}

	public static NonNullList<Ingredient> deserializeIngredients(JsonArray json) {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		for (int i = 0; i < json.size(); i++) {
			Ingredient ingredient = Ingredient.fromJson(json.get(i));
			if (!ingredient.isEmpty()) {
				ingredients.add(ingredient);
			}
		}
		return ingredients;
	}

	public static boolean isIndexInRange(int index, int startInclusive, int endInclusive) {
		return index >= startInclusive && index <= endInclusive;
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[] { shape, Shapes.empty() };
		int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.joinUnoptimized(buffer[1],
					Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX), BooleanOp.OR));
			buffer[0] = buffer[1];
			buffer[1] = Shapes.empty();
		}
		return buffer[0];
	}

	public static Optional<Pair<Float, Float>> getRelativeHitCoordinatesForBlockFace(BlockHitResult blockHitResult, Direction direction, Direction[] unAllowedDirections) {
		Direction direction2 = blockHitResult.getDirection();
		if (Arrays.stream(unAllowedDirections).toList().contains(direction2)) { return Optional.empty(); }
		if (direction != direction2 && direction2 != Direction.UP && direction2 != Direction.DOWN) {
			return Optional.empty();
		}
		else {
			BlockPos blockPos = blockHitResult.getBlockPos().relative(direction2);
			Vec3 vec3 = blockHitResult.getLocation().subtract(blockPos.getX(), blockPos.getY(),
					blockPos.getZ());
			float d = (float) vec3.x;
			float f = (float) vec3.z;
			float y = (float) vec3.y;
			if (direction2 == Direction.UP || direction2 == Direction.DOWN) { direction2 = direction; }
			return switch (direction2) {
				case NORTH -> Optional.of(new Pair<>((float) (1.0 - d), y));
				case SOUTH -> Optional.of(new Pair<>(d, y));
				case WEST -> Optional.of(new Pair<>(f, y));
				case EAST -> Optional.of(new Pair<>((float) (1.0 - f), y));
				case DOWN, UP -> Optional.empty();
			};
		}
	}

	public static boolean isFDLoaded() {
		return ModList.get().isLoaded("farmersdelight");
	}
}
