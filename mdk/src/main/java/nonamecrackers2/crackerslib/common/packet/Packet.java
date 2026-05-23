package nonamecrackers2.crackerslib.common.packet;

import io.netty.handler.codec.DecoderException;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context;
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

   protected abstract void encode(FriendlyByteBuf var1);

   protected abstract void decode(FriendlyByteBuf var1);

   public static <T extends Packet> void encodeCheck(T packet, FriendlyByteBuf buffer) {
      if (packet.isValid) {
         packet.encode(buffer);
      }
   }

   public static <T extends Packet> T decode(Supplier<T> blank, FriendlyByteBuf buffer) {
      T message = (T)blank.get();

      try {
         message.decode(buffer);
      } catch (IndexOutOfBoundsException | DecoderException | IllegalArgumentException var4) {
         LOGGER.warn("Exception while reading " + message.toString() + "; " + var4);
         var4.printStackTrace();
         return message;
      }

      message.isValid = true;
      return message;
   }

   public abstract Runnable getProcessor(Context var1);

   protected static Runnable client(Runnable processor) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> processor);
   }
}
