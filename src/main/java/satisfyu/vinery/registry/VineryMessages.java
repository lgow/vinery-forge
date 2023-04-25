package satisfyu.vinery.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import satisfyu.vinery.Vinery;
import satisfyu.vinery.networking.FlowerBoxClientSyncS2CPacket;
import satisfyu.vinery.networking.ItemStackSyncS2CPacket;
import satisfyu.vinery.networking.TallFlowerPotClientSyncS2CPacket;

public class VineryMessages {
	private static SimpleChannel INSTANCE;

	private static int packetId = 0;

	private static int id() {
		return packetId++;
	}

	public static void register() {
		SimpleChannel net = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Vinery.MODID, "messages"))
				.networkProtocolVersion(() -> "1.0").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true)
				.simpleChannel();
		INSTANCE = net;
		net.messageBuilder(ItemStackSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(
				ItemStackSyncS2CPacket::new).encoder(ItemStackSyncS2CPacket::toBytes).consumerMainThread(
				ItemStackSyncS2CPacket::handle).add();
		net.messageBuilder(FlowerBoxClientSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(
				FlowerBoxClientSyncS2CPacket::new).encoder(FlowerBoxClientSyncS2CPacket::toBytes).consumerMainThread(
				FlowerBoxClientSyncS2CPacket::handle).add();
		net.messageBuilder(TallFlowerPotClientSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT).decoder(
				TallFlowerPotClientSyncS2CPacket::new).encoder(TallFlowerPotClientSyncS2CPacket::toBytes).consumerMainThread(
				TallFlowerPotClientSyncS2CPacket::handle).add();
	}

	public static <MSG> void sendToServer(MSG msg) {
		INSTANCE.sendToServer(msg);
	}

	public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
		INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
	}

	public static <MSG> void sendToClients(MSG msg) {
		INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
	}
}
