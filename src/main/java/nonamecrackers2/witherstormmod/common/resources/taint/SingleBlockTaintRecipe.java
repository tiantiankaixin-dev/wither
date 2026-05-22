package nonamecrackers2.witherstormmod.common.resources.taint;

import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;

public class SingleBlockTaintRecipe extends TaintRecipe {
   private final Block block;

   public SingleBlockTaintRecipe(Block block, MobEffect effect, BlockState replacement, List<Property<?>> propertiesToCopy) {
      super(effect, replacement, propertiesToCopy);
      this.block = block;
   }

   @Override
   public boolean canConvertBlock(BlockState state) {
      return state.is(this.block);
   }

   @Override
   public String getName() {
      return ForgeRegistries.BLOCKS.getKey(this.block).getPath();
   }

   @Override
   public void serializeFrom(JsonObject object) {
      object.addProperty("block", ForgeRegistries.BLOCKS.getKey(this.block).toString());
   }

   public Block getBlock() {
      return this.block;
   }

   public int compareTo(TaintRecipe o) {
      return o instanceof TagBasedTaintRecipe ? 1 : 0;
   }
}
