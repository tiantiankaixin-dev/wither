package nonamecrackers2.witherstormmod.client.event;

import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RegisterColorHandlersEvent.Block;
import nonamecrackers2.witherstormmod.common.block.WireBlock;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import org.joml.Vector3f;

public class WitherStormModRegisterBlockColors {
   public static void registerBlockColors(Block event) {
      event.register((state, world, pos, i) -> {
         Vector3f color = ((WireBlock)WitherStormModBlocks.TAINTED_DUST.get()).getColor();
         return Mth.color(color.x(), color.y(), color.z());
      }, new net.minecraft.world.level.block.Block[]{(net.minecraft.world.level.block.Block)WitherStormModBlocks.TAINTED_DUST.get()});
   }
}
