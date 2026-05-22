package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.model.geom.builders.CubeDefinition;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.UVPair;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({CubeDefinition.class})
public interface IMixinCubeDefinition {
   @Accessor
   Vector3f getOrigin();

   @Accessor
   Vector3f getDimensions();

   @Accessor
   CubeDeformation getGrow();

   @Accessor
   boolean getMirror();

   @Accessor
   UVPair getTexCoord();

   @Accessor
   UVPair getTexScale();
}
