package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class UpdateEffectInstanceMessage extends Packet {
   private int entityId;
   private byte effectId;
   private byte amplifier;
   private int duration;
   private boolean showDuration;

   public UpdateEffectInstanceMessage(int entityId, MobEffectInstance effect, boolean showDuration) {
      super(true);
      this.entityId = entityId;
      this.effectId = (byte)(MobEffect.getId(effect.getEffect()) & 0xFF);
      this.amplifier = (byte)(effect.getAmplifier() & 0xFF);
      if (effect.getDuration() > 32767) {
         this.duration = 32767;
      } else {
         this.duration = effect.getDuration();
      }

      this.showDuration = showDuration;
   }

   public UpdateEffectInstanceMessage() {
      super(false);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public byte getEffectID() {
      return this.effectId;
   }

   public byte getAmplifier() {
      return this.amplifier;
   }

   public int getDuration() {
      return this.duration;
   }

   public boolean shouldShowDuration() {
      return this.showDuration;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.entityId);
      buffer.writeByte(this.effectId);
      buffer.writeByte(this.amplifier);
      buffer.writeVarInt(this.duration);
   }

   public void decode(FriendlyByteBuf buffer) {
      this.entityId = buffer.readVarInt();
      this.effectId = buffer.readByte();
      this.amplifier = buffer.readByte();
      this.duration = buffer.readVarInt();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processUpdateEffectInstanceMessage(this));
   }

   public String toString() {
      return "UpdateEffectInstanceMessage[id="
         + this.entityId
         + ", effectId="
         + this.effectId
         + ", amplifer="
         + this.amplifier
         + ", duration="
         + this.duration
         + ", showDuration="
         + this.showDuration
         + "]";
   }
}
