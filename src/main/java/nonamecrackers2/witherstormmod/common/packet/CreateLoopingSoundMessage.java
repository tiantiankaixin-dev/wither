package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class CreateLoopingSoundMessage extends Packet {
   private int entityId;
   private byte phase;
   private double x;
   private double y;
   private double z;

   public CreateLoopingSoundMessage(WitherStormEntity entity) {
      super(true);
      this.entityId = entity.getId();
      this.phase = (byte)entity.getPhase();
      this.x = entity.getX();
      this.y = entity.getEyeY();
      this.z = entity.getZ();
   }

   public CreateLoopingSoundMessage() {
      super(false);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public byte getPhase() {
      return this.phase;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.entityId);
      buffer.writeByte(this.phase);
      buffer.writeDouble(this.x);
      buffer.writeDouble(this.y);
      buffer.writeDouble(this.z);
   }

   public void decode(FriendlyByteBuf buffer) throws IllegalArgumentException, IndexOutOfBoundsException {
      this.entityId = buffer.readVarInt();
      this.phase = buffer.readByte();
      this.x = buffer.readDouble();
      this.y = buffer.readDouble();
      this.z = buffer.readDouble();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processCreateLoopingSoundMessage(this));
   }

   public String toString() {
      return "CreateLoopingSoundMessage[id=" + this.entityId + ", phase=" + this.phase + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
   }
}
