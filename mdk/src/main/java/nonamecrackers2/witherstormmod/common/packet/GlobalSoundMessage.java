package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context
import net.minecraft.core.registries.BuiltInRegistries;
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
      buffer.writeUtf(NeoBuiltInRegistries.SOUND_EVENT.getKey(this.event).toString());
      buffer.writeFloat(this.pitch);
      buffer.writeFloat(this.volume);
   }

   public void decode(FriendlyByteBuf buffer) {
      this.event = NeoBuiltInRegistries.SOUND_EVENT.getValue(new ResourceLocation(buffer.readUtf()));
      this.pitch = buffer.readFloat();
      this.volume = buffer.readFloat();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processGlobalSoundMessage(this));
   }

   public String toString() {
      return "GlobalSoundMessage[sound_event="
         + NeoBuiltInRegistries.SOUND_EVENT.getKey(this.event).toString()
         + ", pitch="
         + this.pitch
         + ", volume="
         + this.volume
         + "]";
   }
}
