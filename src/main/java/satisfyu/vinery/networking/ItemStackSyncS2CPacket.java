package satisfyu.vinery.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import satisfyu.vinery.block.entity.StorageBlockEntity;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ItemStackSyncS2CPacket {
	private final BlockPos pos;

	private final ArrayList<ItemStack> inventory;

	public ItemStackSyncS2CPacket(BlockPos blockPos, ArrayList<ItemStack> inventory) {
		this.pos = blockPos;
		this.inventory = inventory;
	}

	public ItemStackSyncS2CPacket(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		this.inventory = buf.readCollection(ArrayList::new, FriendlyByteBuf::readItem);
	}

	public void toBytes(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeCollection(inventory, FriendlyByteBuf::writeItem);
	}

	public boolean handle(Supplier<NetworkEvent.Context> supplier) {
		NetworkEvent.Context context = supplier.get();
		context.enqueueWork(() -> {
			if (Minecraft.getInstance().level.getBlockEntity(pos) instanceof StorageBlockEntity blockEntity) {
				NonNullList nonNullList = NonNullList.create();
				nonNullList.addAll(inventory);
				blockEntity.setInventory(nonNullList);
			}
		});
		return true;
	}
}
