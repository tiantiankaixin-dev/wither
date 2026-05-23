package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class PlayAdditionalLoopingSoundMessage extends Packet {
   private int entityId;
   private SoundEvent event;
   private double x;
   private double y;
   private double z;

   public PlayAdditionalLoopingSoundMessage(WitherStormEntity entity, SoundEvent event) {
      super(true);
      this.entityId = entity.getId();
      this.event = event;
      this.x = entity.getX();
      this.y = entity.getEyeY();
      this.z = entity.getZ();
   }

   public PlayAdditionalLoopingSoundMessage() {
      super(false);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public SoundEvent getSound() {
      return this.event;
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
      buffer.writeRegistryId(NeoBuiltInRegistries.SOUND_EVENT, this.event);
      buffer.writeDouble(this.x);
      buffer.writeDouble(this.y);
      buffer.writeDouble(this.z);
   }

   public void decode(FriendlyByteBuf buffer) throws IllegalArgumentException, IndexOutOfBoundsException {
      this.entityId = buffer.readVarInt();
      this.event = buffer.readRegistryId();
      this.x = buffer.readDouble();
      this.y = buffer.readDouble();
      this.z = buffer.readDouble();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processPlayAdditionalLoopingSoundMessage(this));
   }

   public String toString() {
      return "PlayAdditionalLoopingSoundMessage[id=" + this.entityId + ", event=" + this.event + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
   }
}
