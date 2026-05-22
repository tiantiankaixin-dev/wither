package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({PartDefinition.class})
public interface IMixinPartDefinition {
   @Accessor
   List<CubeDefinition> getCubes();

   @Accessor
   PartPose getPartPose();
}
