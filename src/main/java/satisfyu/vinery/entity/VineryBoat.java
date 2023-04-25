package satisfyu.vinery.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import satisfyu.vinery.registry.VineryEntites;

public class VineryBoat extends Boat {
	public static final EntityDataAccessor<String> WOOD_TYPE = SynchedEntityData.defineId(VineryBoat.class,
			EntityDataSerializers.STRING);

	public VineryBoat(EntityType<? extends Boat> type, Level level) {
		super(type, level);
		this.blocksBuilding = true;
	}

	public VineryBoat(Level level, double x, double y, double z) {
		this(VineryEntites.BOAT.get(), level);
		this.setPos(x, y, z);
		this.xo = x;
		this.yo = y;
		this.zo = z;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(WOOD_TYPE, "cherry");
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag p_38338_) {
		super.readAdditionalSaveData(p_38338_);
		p_38338_.getString("Type");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag p_38359_) {
		super.addAdditionalSaveData(p_38359_);
		p_38359_.putString("Type", this.getWoodType());
	}

	public String getWoodType() {
		return this.entityData.get(WOOD_TYPE);
	}

	public void setWoodType(String woodType) {
		this.entityData.set(WOOD_TYPE, woodType);
	}
}
