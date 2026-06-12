package nonamecrackers2.crackerslib.common.packet;

import io.netty.handler.codec.DecoderException;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Base packet class bridging the old crackerslib packet API to NeoForge 1.21.1.
 * Subclasses keep their encode/decode/getProcessor pattern.
 * Registration and dispatching is handled by PacketUtil.
 */
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
      T message = blank.get();

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

   /**
    * Returns a Runnable that processes this packet.
    * The IPayloadContext replaces the old NetworkEvent.Context.
    */
   public abstract Runnable getProcessor(IPayloadContext context);

   /**
    * Utility: run something only on the client physical side.
    */
   protected static Runnable client(Runnable processor) {
      return () -> {
         if (FMLEnvironment.dist == Dist.CLIENT) {
            processor.run();
         }
      };
   }
}
