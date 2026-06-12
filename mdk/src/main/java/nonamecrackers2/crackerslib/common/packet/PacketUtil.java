package nonamecrackers2.crackerslib.common.packet;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Bridge utility: registers legacy Packet subclasses as NeoForge 1.21.1 CustomPacketPayloads.
 * Each Packet subclass is wrapped in a PacketPayload record for network transport.
 */
public class PacketUtil {
   @Nullable
   private static Throwable lastException;
   private static final Logger LOGGER = LogManager.getLogger();

   /**
    * Register a packet that travels server → client.
    */
   public static <T extends Packet> void registerToClient(PayloadRegistrar registrar, String modId, String name, Class<T> clazz) {
      CustomPacketPayload.Type<PacketPayload<T>> type = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(modId, name));
      StreamCodec<FriendlyByteBuf, PacketPayload<T>> codec = StreamCodec.of(
         (buf, payload) -> Packet.encodeCheck(payload.packet(), buf),
         buf -> new PacketPayload<>(Packet.decode(instantiate(clazz), buf), type)
      );
      registrar.playToClient(type, codec, (payload, ctx) -> handlePacket(payload.packet(), ctx));
   }

   /**
    * Register a packet that travels client → server.
    */
   public static <T extends Packet> void registerToServer(PayloadRegistrar registrar, String modId, String name, Class<T> clazz) {
      CustomPacketPayload.Type<PacketPayload<T>> type = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(modId, name));
      StreamCodec<FriendlyByteBuf, PacketPayload<T>> codec = StreamCodec.of(
         (buf, payload) -> Packet.encodeCheck(payload.packet(), buf),
         buf -> new PacketPayload<>(Packet.decode(instantiate(clazz), buf), type)
      );
      registrar.playToServer(type, codec, (payload, ctx) -> handlePacket(payload.packet(), ctx));
   }

   private static <T extends Packet> void handlePacket(T message, IPayloadContext ctx) {
      if (!message.isMessageValid()) {
         LOGGER.warn(message.toString() + " was invalid");
         return;
      }
      ctx.enqueueWork(message.getProcessor(ctx)).handle((v, e) -> {
         if (e != null) {
            if (lastException == null || !lastException.getClass().equals(e.getClass())) {
               LOGGER.error("Failed to process packet {}: {}", message, e);
               e.printStackTrace();
            }
            lastException = e;
         }
         return v;
      });
   }

   private static <T extends Packet> Supplier<T> instantiate(Class<T> clazz) {
      return () -> {
         try {
            return clazz.getDeclaredConstructor().newInstance();
         } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException ex) {
            LOGGER.error("Failed to create blank packet from class {}", clazz);
            ex.printStackTrace();
            return null;
         }
      };
   }

   /**
    * Wrapper record that makes any legacy Packet a CustomPacketPayload.
    */
   public record PacketPayload<T extends Packet>(T packet, CustomPacketPayload.Type<PacketPayload<T>> payloadType) implements CustomPacketPayload {
      @Override
      public Type<? extends CustomPacketPayload> type() {
         return payloadType;
      }
   }
}
