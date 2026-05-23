package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.neoforged.api.distmarker.Dist;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class UpdateDamagingProjectileMessage extends Packet {
   private int entityId;
   private double xPower;
   private double yPower;
   private double zPower;

   public UpdateDamagingProjectileMessage(AbstractHurtingProjectile entity) {
      super(true);
      this.entityId = entity.getId();
      this.xPower = entity.xPower;
      this.yPower = entity.yPower;
      this.zPower = entity.zPower;
   }

   public int getEntityId() {
      return this.entityId;
   }

   public double getXPower() {
      return this.xPower;
   }

   public double getYPower() {
      return this.yPower;
   }

   public double getZPower() {
      return this.zPower;
   }

   public UpdateDamagingProjectileMessage() {
      super(false);
   }

   public void decode(FriendlyByteBuf buffer) {
      this.entityId = buffer.readVarInt();
      this.xPower = buffer.readDouble();
      this.yPower = buffer.readDouble();
      this.zPower = buffer.readDouble();
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.entityId);
      buffer.writeDouble(this.xPower);
      buffer.writeDouble(this.yPower);
      buffer.writeDouble(this.zPower);
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processUpdateDamagingProjectileMessage(this));
   }

   public String toString() {
      return "UpdateDamagingProjectileMessage[id=" + this.entityId + ", xPower=" + this.xPower + ", yPower=" + this.yPower + ", zPower=" + this.zPower + "]";
   }
}
