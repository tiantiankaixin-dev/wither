package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ModelPart.class})
public interface IMixinModelPart {
   @Accessor
   Map<String, ModelPart> getChildren();

   @Accessor
   List<Cube> getCubes();
}
