package nonamecrackers2.witherstormmod.client.util;

import net.minecraft.core.BlockPos;

public class SuperBeaconDistantInstance {
   public final BlockPos pos;
   public int[] color;
   public boolean active;
   public int beaconHeight;
   public float beamWidth = 0.25F;
   public float outerBeamWidth = 0.45F;

   public SuperBeaconDistantInstance(BlockPos pos, int[] color) {
      this.pos = pos;
      this.color = color;
   }
}
