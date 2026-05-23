package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ServerChunkCache.class})
public interface MixinServerChunkCache {
   @Accessor
   DistanceManager getDistanceManager();
}
