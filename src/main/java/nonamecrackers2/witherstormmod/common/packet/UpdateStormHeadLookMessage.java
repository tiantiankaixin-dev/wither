package nonamecrackers2.witherstormmod.common.packet;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class UpdateStormHeadLookMessage extends DistantRendererMessage {
   private int entityId;
   private byte yHeadRot;

   @Deprecated
   public UpdateStormHeadLookMessage(List<Integer> applicable, WitherStormEntity entity, byte yHeadRot) {
      super(true, applicable);
      this.entityId = entity.getId();
      this.yHeadRot = yHeadRot;
   }

   public UpdateStormHeadLookMessage(List<Integer> applicable, int id, byte yHeadRot) {
      super(true, applicable);
      this.entityId = id;
      this.yHeadRot = yHeadRot;
   }

   public UpdateStormHeadLookMessage() {
      super(false, Lists.newArrayList());
   }

   public int getEntityID() {
      return this.entityId;
   }

   public byte getYRot() {
      return this.yHeadRot;
   }

   @Override
   public void decode(FriendlyByteBuf buffer) {
      super.decode(buffer);
      this.entityId = buffer.readVarInt();
      this.yHeadRot = buffer.readByte();
   }

   @Override
   public void encode(FriendlyByteBuf buffer) {
      super.encode(buffer);
      buffer.writeVarInt(this.entityId);
      buffer.writeByte(this.yHeadRot);
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processUpdateStormHeadLookMessage(this));
   }

   public String toString() {
      return "UpdateStormHeadLookMessage[id=" + this.entityId + ", yHeadRot=" + this.yHeadRot + "]";
   }
}
