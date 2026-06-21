package nonamecrackers2.witherstormmod.common.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Packet {
   protected static final Logger LOGGER = LogManager.getLogger();
   protected boolean isValid;

   public Packet(boolean valid) {
      this.isValid = valid;
   }

   public boolean isMessageValid() {
      return this.isValid;
   }

   protected abstract void encode(FriendlyByteBuf buffer);

   protected abstract void decode(FriendlyByteBuf buffer);

   public static <T extends Packet> void encodeCheck(T message, FriendlyByteBuf buffer) {
      if (message.isMessageValid()) {
         message.encode(buffer);
      } else {
         LOGGER.error("Attempted to encode invalid packet {}", message.getClass().getName());
      }
   }

   public static <T extends Packet> T decode(Supplier<T> supplier, FriendlyByteBuf buffer) {
      T message = supplier.get();

      try {
         message.decode(buffer);
         message.isValid = true;
      } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
         LOGGER.error("Failed to decode packet {}", message.getClass().getName(), exception);
      }

      return message;
   }

   public abstract Runnable getProcessor(LegacyNetworkEvent.Context context);

   protected static Runnable client(Runnable runnable) {
      return runnable;
   }
}
