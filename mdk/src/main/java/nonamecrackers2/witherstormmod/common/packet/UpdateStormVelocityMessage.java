package nonamecrackers2.witherstormmod.common.packet;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class UpdateStormVelocityMessage extends DistantRendererMessage {
   private int entityId;
   private int x;
   private int y;
   private int z;

   @Deprecated
   public UpdateStormVelocityMessage(List<Integer> applicable, WitherStormEntity entity) {
      super(true, applicable);
      this.entityId = entity.getId();
      Vec3 delta = entity.getDeltaMovement();
      double x = Mth.clamp(delta.x, -3.9, 3.9);
      double y = Mth.clamp(delta.y, -3.9, 3.9);
      double z = Mth.clamp(delta.z, -3.9, 3.9);
      this.x = (int)(x * 8000.0);
      this.y = (int)(y * 8000.0);
      this.z = (int)(z * 8000.0);
   }

   public UpdateStormVelocityMessage(List<Integer> applicable, int entityId, int xa, int ya, int za) {
      super(true, applicable);
      this.entityId = entityId;
      this.x = xa;
      this.y = ya;
      this.z = za;
   }

   public UpdateStormVelocityMessage() {
      super(false, Lists.newArrayList());
   }

   public int getEntityID() {
      return this.entityId;
   }

   public double getX() {
      return (double)this.x;
   }

   public double getY() {
      return (double)this.y;
   }

   public double getZ() {
      return (double)this.z;
   }

   @Override
   public void decode(FriendlyByteBuf buffer) {
      super.decode(buffer);
      this.entityId = buffer.readVarInt();
      this.x = buffer.readShort();
      this.y = buffer.readShort();
      this.z = buffer.readShort();
   }

   @Override
   public void encode(FriendlyByteBuf buffer) {
      super.encode(buffer);
      buffer.writeVarInt(this.entityId);
      buffer.writeShort(this.x);
      buffer.writeShort(this.y);
      buffer.writeShort(this.z);
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processUpdateStormVelocityMessage(this));
   }

   public String toString() {
      return "UpdateStormVelocityMessage[id=" + this.entityId + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
   }
}
