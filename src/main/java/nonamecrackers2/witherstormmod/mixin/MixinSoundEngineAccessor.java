package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({SoundEngine.class})
public interface MixinSoundEngineAccessor {
   @Accessor("queuedTickableSounds")
   List<TickableSoundInstance> witherstormmod$getQueuedTickableSounds();
}
