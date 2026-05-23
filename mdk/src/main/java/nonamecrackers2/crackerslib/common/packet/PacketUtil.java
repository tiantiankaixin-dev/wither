package nonamecrackers2.crackerslib.common.packet;

import com.google.common.collect.Maps;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.NetworkDirection;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: SimpleChannel removed, use IPayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketUtil {
   @Nullable
   private static Throwable lastException;
   private static final Map<SimpleChannel, AtomicInteger> CURRENT_IDS = Maps.newHashMap();
   private static final Logger LOGGER = LogManager.getLogger();

   public static <T extends Packet> void registerToClient(SimpleChannel channel, Class<T> clazz) {
      channel.registerMessage(
         CURRENT_IDS.computeIfAbsent(channel, c -> new AtomicInteger()).incrementAndGet(),
         clazz,
         Packet::encodeCheck,
         buffer -> Packet.decode(
            () -> {
               try {
                  return clazz.getDeclaredConstructor().newInstance();
               } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException var2) {
                  LOGGER.error("Failed to create blank packet from class {}", clazz);
                  var2.printStackTrace();
                  return null;
               }
            },
            buffer
         ),
         PacketUtil::receiveClientMessage,
         Optional.of(NetworkDirection.PLAY_TO_CLIENT)
      );
   }

   public static <T extends Packet> void registerToServer(SimpleChannel channel, Class<T> clazz) {
      channel.registerMessage(
         CURRENT_IDS.computeIfAbsent(channel, c -> new AtomicInteger()).incrementAndGet(),
         clazz,
         Packet::encodeCheck,
         buffer -> Packet.decode(
            () -> {
               try {
                  return clazz.getDeclaredConstructor().newInstance();
               } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | InstantiationException var2) {
                  LOGGER.error("Failed to create blank packet from class {}", clazz);
                  var2.printStackTrace();
                  return null;
               }
            },
            buffer
         ),
         PacketUtil::receiveServerMessage,
         Optional.of(NetworkDirection.PLAY_TO_SERVER)
      );
   }

   private static <T extends Packet> void receiveClientMessage(T message, Supplier<Context> supplier) {
      Context context = supplier.get();
      LogicalSide sideReceived = context.getDirection().getReceptionSide();
      context.setPacketHandled(true);
      if (sideReceived != LogicalSide.CLIENT) {
         LOGGER.warn(message.toString() + " was received on the wrong side: " + sideReceived);
      } else if (!message.isMessageValid()) {
         LOGGER.warn(message.toString() + " was invalid");
      } else {
         context.enqueueWork(message.getProcessor(context)).handle((v, e) -> {
            if (e != null) {
               if (lastException == null || !lastException.getClass().equals(e.getClass())) {
                  LOGGER.error("Failed to process packet {}: {}", message, e);
                  e.printStackTrace();
               }

               lastException = e;
            }

            return (Void)v;
         });
      }
   }

   private static <T extends Packet> void receiveServerMessage(T message, Supplier<Context> supplier) {
      Context context = supplier.get();
      LogicalSide sideReceived = context.getDirection().getReceptionSide();
      context.setPacketHandled(true);
      if (sideReceived != LogicalSide.SERVER) {
         LOGGER.warn(message.toString() + " was received on the wrong side: " + sideReceived);
      } else if (!message.isMessageValid()) {
         LOGGER.warn(message.toString() + " was invalid");
      } else {
         ServerPlayer player = context.getSender();
         if (player == null) {
            LOGGER.warn("The sending player is not present when " + message.toString() + " was received");
         } else {
            context.enqueueWork(message.getProcessor(context)).handle((v, e) -> {
               if (e != null) {
                  if (lastException == null || !lastException.getClass().equals(e.getClass())) {
                     LOGGER.error("Failed to process packet {}: {}", message, e);
                     e.printStackTrace();
                  }

                  lastException = e;
               }

               return (Void)v;
            });
         }
      }
   }
}
