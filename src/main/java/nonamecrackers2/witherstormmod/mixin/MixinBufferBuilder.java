package nonamecrackers2.witherstormmod.mixin;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BufferBuilder.class})
public interface MixinBufferBuilder {
   @Accessor("buffer")
   ByteBuffer witherstormmod$getBuffer();

   @Accessor("buffer")
   void witherstormmod$setBuffer(@Nullable ByteBuffer var1);
}
