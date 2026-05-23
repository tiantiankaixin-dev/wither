package nonamecrackers2.witherstormmod.mixin;

import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({LayerDefinition.class})
public interface IMixinLayerDefinition {
   @Accessor
   MeshDefinition getMesh();
}
