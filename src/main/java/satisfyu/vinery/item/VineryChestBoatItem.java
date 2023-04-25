package satisfyu.vinery.item;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import satisfyu.vinery.entity.VineryBoat;
import satisfyu.vinery.entity.VineryChestBoat;
import satisfyu.vinery.registry.VineryEntites;

import java.util.List;
import java.util.function.Predicate;

public class VineryChestBoatItem extends Item {
	private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);

	private final String woodType;

	public VineryChestBoatItem(Properties p_41383_, String woodType) {
		super(p_41383_);
		this.woodType = woodType;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
		ItemStack boat = player.getItemInHand(interactionHand);
		HitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY);
		if (hitResult.getType() == HitResult.Type.MISS) {
			return InteractionResultHolder.pass(boat);
		}
		else {
			Vec3 vec3 = player.getViewVector(1.0F);
			double d0 = 5.0D;
			List<Entity> list = level.getEntities(player,
					player.getBoundingBox().expandTowards(vec3.scale(5.0D)).inflate(1.0D), ENTITY_PREDICATE);
			if (!list.isEmpty()) {
				Vec3 vec31 = player.getEyePosition();
				for (Entity entity : list) {
					AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
					if (aabb.contains(vec31)) {
						return InteractionResultHolder.pass(boat);
					}
				}
			}
			if (hitResult.getType() == HitResult.Type.BLOCK) {
				double x = hitResult.getLocation().x;
				double y = hitResult.getLocation().y;
				double z = hitResult.getLocation().z;
				VineryChestBoat vineryChestBoat = VineryEntites.CHEST_BOAT.get().create(level);
				vineryChestBoat.setPos(x, y, z);
				summonBoat(vineryChestBoat, level, hitResult, player, boat);
			}
		}
		return InteractionResultHolder.pass(boat);
	}

	private InteractionResultHolder<ItemStack> summonBoat(VineryBoat vineryBoat, Level level, HitResult hitResult, Player player, ItemStack boat) {
		vineryBoat.setWoodType(woodType);
		vineryBoat.setYRot(player.getYRot());
		if (!level.noCollision(vineryBoat, vineryBoat.getBoundingBox())) {
			return InteractionResultHolder.fail(boat);
		}
		else {
			if (!level.isClientSide) {
				level.addFreshEntity(vineryBoat);
				level.gameEvent(player, GameEvent.ENTITY_PLACE, hitResult.getLocation());
				if (!player.getAbilities().instabuild) {
					boat.shrink(1);
				}
			}
			player.awardStat(Stats.ITEM_USED.get(this));
			return InteractionResultHolder.sidedSuccess(boat, level.isClientSide);
		}
	}
}
