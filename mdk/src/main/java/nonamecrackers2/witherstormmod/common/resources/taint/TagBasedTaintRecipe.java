package nonamecrackers2.witherstormmod.common.resources.taint;

import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

public class TagBasedTaintRecipe extends TaintRecipe {
   private final TagKey<Block> tag;

   public TagBasedTaintRecipe(TagKey<Block> tag, MobEffect effect, BlockState replacement, List<Property<?>> propertiesToCopy) {
      super(effect, replacement, propertiesToCopy);
      this.tag = tag;
   }

   @Override
   public boolean canConvertBlock(BlockState state) {
      return state.is(this.tag);
   }

   @Override
   public String getName() {
      return this.tag.location().getPath();
   }

   @Override
   public void serializeFrom(JsonObject object) {
      object.addProperty("block", "#" + this.tag.location().toString());
   }

   public TagKey<Block> getTag() {
      return this.tag;
   }

   public int compareTo(TaintRecipe o) {
      return o instanceof SingleBlockTaintRecipe ? -1 : 0;
   }
}
