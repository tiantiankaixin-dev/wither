package nonamecrackers2.witherstormmod.common.item;

import java.util.function.Consumer;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import nonamecrackers2.witherstormmod.client.util.TaintedCarvedPumpkinExtensions;

public class TaintedCarvedPumpkinItem extends BlockItem {
   public TaintedCarvedPumpkinItem(Block block, Properties properties) {
      super(block, properties);
   }

   public void initializeClient(Consumer<IClientItemExtensions> consumer) {
      consumer.accept(TaintedCarvedPumpkinExtensions.INSTANCE);
   }

   public boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
      return true;
   }
}
