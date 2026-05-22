package nonamecrackers2.witherstormmod.common.packet;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class FormidibombExplosionMessage extends Packet {
   private int entityId;
   private double x;
   private double y;
   private double z;
   private byte radius;
   private byte squish;

   public FormidibombExplosionMessage(@Nullable Entity entity, double x, double y, double z, int radius, int squish) {
      super(true);
      if (entity != null) {
         this.entityId = entity.getId();
      }

      this.x = x;
      this.y = y;
      this.z = z;
      this.radius = (byte)radius;
      this.squish = (byte)squish;
   }

   public FormidibombExplosionMessage() {
      super(false);
   }

   public int getId() {
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

   public int getRadius() {
      return this.radius;
   }

   public int getSquish() {
      return this.squish;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeInt(this.entityId);
      buffer.writeDouble(this.x);
      buffer.writeDouble(this.y);
      buffer.writeDouble(this.z);
      buffer.writeByte(this.radius);
      buffer.writeByte(this.squish);
   }

   public void decode(FriendlyByteBuf buffer) throws IllegalArgumentException, IndexOutOfBoundsException {
      this.entityId = buffer.readInt();
      this.x = buffer.readDouble();
      this.y = buffer.readDouble();
      this.z = buffer.readDouble();
      this.radius = buffer.readByte();
      this.squish = buffer.readByte();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processFormidibombExplosionMessage(this));
   }

   public String toString() {
      return "FormidibombExplosionMessage[id="
         + this.entityId
         + ", x="
         + this.x
         + ", y="
         + this.y
         + ", z="
         + this.z
         + ", radius="
         + this.radius
         + ", squish="
         + this.squish
         + "]";
   }
}
