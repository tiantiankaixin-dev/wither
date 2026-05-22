package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({StructurePiecesBuilder.class})
public interface IMixinStructurePiecesBuilder {
   @Accessor
   List<StructurePiece> getPieces();
}
