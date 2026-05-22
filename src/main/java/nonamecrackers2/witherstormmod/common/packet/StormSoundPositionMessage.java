package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class StormSoundPositionMessage extends Packet {
   private int entityId;
   private byte phase;
   private double x;
   private double y;
   private double z;

   public StormSoundPositionMessage(int id, double x, double y, double z, byte phase) {
      super(true);
      this.entityId = id;
      this.phase = phase;
      this.x = x;
      this.y = y;
      this.z = z;
      this.phase = phase;
   }

   public StormSoundPositionMessage() {
      super(false);
   }

   public int getEntityID() {
      return this.entityId;
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

   public byte getPhase() {
      return this.phase;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.entityId);
      buffer.writeDouble(this.x);
      buffer.writeDouble(this.y);
      buffer.writeDouble(this.z);
      buffer.writeByte(this.phase);
   }

   public void decode(FriendlyByteBuf buffer) {
      this.entityId = buffer.readVarInt();
      this.x = buffer.readDouble();
      this.y = buffer.readDouble();
      this.z = buffer.readDouble();
      this.phase = buffer.readByte();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processStormSoundPositionMessage(this));
   }

   public String toString() {
      return "StormSoundPositionMessage[id=" + this.entityId + ", phase=" + this.phase + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
   }
}
