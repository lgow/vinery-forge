package satisfyu.vinery.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import satisfyu.vinery.registry.VineryEntites;
import satisfyu.vinery.registry.VineryItems;

import javax.annotation.Nullable;

public class VineryChestBoat extends VineryBoat implements HasCustomInventoryScreen, ContainerEntity {
	private NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);

	@Nullable private ResourceLocation lootTable;

	private long lootTableSeed;

	public VineryChestBoat(EntityType<? extends Boat> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	public VineryChestBoat(Level level, double x, double y, double z) {
		this(VineryEntites.BOAT.get(), level);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	protected float getSinglePassengerXOffset() {
		return 0.15F;
	}

	@Override
	protected int getMaxPassengers() {
		return 1;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_38338_) {
		super.readAdditionalSaveData(p_38338_);
		this.readChestVehicleSaveData(p_38338_);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_38359_) {
		super.addAdditionalSaveData(p_38359_);
		this.addChestVehicleSaveData(p_38359_);
	}

	@Override
	protected void destroy(DamageSource p_219862_) {
		super.destroy(p_219862_);
		this.chestVehicleDestroyed(p_219862_, this.level, this);
	}

	@Override
	public void remove(RemovalReason p_146834_) {
		if (!this.level.isClientSide && p_146834_.shouldDestroy()) {
			Containers.dropContents(this.level, this, this);
		}
		super.remove(p_146834_);
	}

	@Override
	public InteractionResult interact(Player p_38330_, InteractionHand p_38331_) {
		return this.canAddPassenger(p_38330_) && !p_38330_.isSecondaryUseActive() ? super.interact(p_38330_, p_38331_)
				: this.interactWithChestVehicle(this::gameEvent, p_38330_);
	}

	@Override
	public void openCustomInventoryScreen(Player p_217023_) {
		p_217023_.openMenu(this);
		if (!p_217023_.level.isClientSide) {
			this.gameEvent(GameEvent.CONTAINER_OPEN, p_217023_);
			PiglinAi.angerNearbyPiglins(p_217023_, true);
		}
	}

	@Override
	public Item getDropItem() {
		return VineryItems.CHERRY_BOAT.get();
	}

	@Override
	public void clearContent() {
		this.clearChestVehicleContent();
	}

	@Override
	public int getContainerSize() {
		return 27;
	}

	@Override
	public ItemStack getItem(int p_18941_) {
		return this.getChestVehicleItem(p_18941_);
	}

	@Override
	public ItemStack removeItem(int p_18942_, int p_18943_) {
		return this.removeChestVehicleItem(p_18942_, p_18943_);
	}

	@Override
	public ItemStack removeItemNoUpdate(int p_18951_) {
		return this.removeChestVehicleItemNoUpdate(p_18951_);
	}

	@Override
	public void setItem(int p_18944_, ItemStack p_18945_) {
		this.setChestVehicleItem(p_18944_, p_18945_);
	}

	@Override
	public SlotAccess getSlot(int p_146919_) {
		return this.getChestVehicleSlot(p_146919_);
	}

	@Override
	public void setChanged() { }

	@Override
	public boolean stillValid(Player p_18946_) {
		return this.isChestVehicleStillValid(p_18946_);
	}

	@org.jetbrains.annotations.Nullable
	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		if (this.lootTable != null && player.isSpectator()) {
			return null;
		}
		else {
			this.unpackLootTable(inventory.player);
			return ChestMenu.threeRows(id, inventory, this);
		}
	}

	public void unpackLootTable(@Nullable Player player) {
		this.unpackChestVehicleLootTable(player);
	}

	@Override
	@Nullable
	public ResourceLocation getLootTable() {
		return this.lootTable;
	}

	@Override
	public void setLootTable(@Nullable ResourceLocation lootTable) {
		this.lootTable = lootTable;
	}

	@Override
	public long getLootTableSeed() {
		return this.lootTableSeed;
	}

	@Override
	public void setLootTableSeed(long lootTableSeed) {
		this.lootTableSeed = lootTableSeed;
	}

	@Override
	public NonNullList<ItemStack> getItemStacks() {
		return this.itemStacks;
	}

	@Override
	public void clearItemStacks() {
		this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
	}
}
