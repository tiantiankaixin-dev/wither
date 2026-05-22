package nonamecrackers2.witherstormmod.common.predicate;

import java.util.Objects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPredicateBuilder extends PredicateBuilder<BlockState, BlockPredicateBuilder> {
   private BlockPredicateBuilder(PredicateBuilder.Comparison comparison) {
      super(comparison);
   }

   public static BlockPredicateBuilder or() {
      return new BlockPredicateBuilder(PredicateBuilder.Comparison.OR);
   }

   public static BlockPredicateBuilder and() {
      return new BlockPredicateBuilder(PredicateBuilder.Comparison.AND);
   }

   public BlockPredicateBuilder isExactly(Block block) {
      return this.addTest(b -> b.is(block));
   }

   public BlockPredicateBuilder isNotExactly(Block block) {
      return this.addTest(b -> !b.is(block));
   }

   public BlockPredicateBuilder isExactly(BlockState state) {
      return this.addTest(b -> Objects.equals(state, b));
   }

   public BlockPredicateBuilder isNotExactly(BlockState state) {
      return this.addTest(b -> !Objects.equals(state, b));
   }

   public BlockPredicateBuilder isTag(TagKey<Block> tag) {
      return this.addTest(b -> b.is(tag));
   }

   public BlockPredicateBuilder isNotTag(TagKey<Block> tag) {
      return this.addTest(b -> !b.is(tag));
   }

   public BlockPredicateBuilder isAir() {
      return this.addTest(b -> b.isAir());
   }

   public BlockPredicateBuilder isNotAir() {
      return this.addTest(b -> !b.isAir());
   }

   public BlockPredicateBuilder isNotAFluid() {
      return this.addTest(b -> b.getFluidState().isEmpty());
   }

   public BlockPredicateBuilder isAFluid() {
      return this.addTest(b -> !b.getFluidState().isEmpty());
   }
}
