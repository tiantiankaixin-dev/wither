package nonamecrackers2.witherstormmod.mixin;

import java.util.Map;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({EntityModelSet.class})
public interface IMixinEntityModelSet {
   @Accessor
   Map<ModelLayerLocation, LayerDefinition> getRoots();
}
