package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class GlobalSoundMessage extends Packet {
   private SoundEvent event;
   private float pitch;
   private float volume;

   public GlobalSoundMessage(SoundEvent event, float volume, float pitch) {
      super(true);
      this.event = event;
      this.pitch = pitch;
      this.volume = volume;
   }

   public GlobalSoundMessage() {
      super(false);
   }

   public SoundEvent getSoundEvent() {
      return this.event;
   }

   public float getPitch() {
      return this.pitch;
   }

   public float getVolume() {
      return this.volume;
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeUtf(BuiltInRegistries.SOUND_EVENT.getKey(this.event).toString());
      buffer.writeFloat(this.pitch);
      buffer.writeFloat(this.volume);
   }

   public void decode(FriendlyByteBuf buffer) {
      this.event = BuiltInRegistries.SOUND_EVENT.get(new ResourceLocation(buffer.readUtf()));
      this.pitch = buffer.readFloat();
      this.volume = buffer.readFloat();
   }

   public Runnable getProcessor(IPayloadContext context) {
      return () -> client(() -> WitherStormModMessageHandlerClient.processGlobalSoundMessage(this));
   }

   public String toString() {
      return "GlobalSoundMessage[sound_event="
         + BuiltInRegistries.SOUND_EVENT.getKey(this.event).toString()
         + ", pitch="
         + this.pitch
         + ", volume="
         + this.volume
         + "]";
   }
}
