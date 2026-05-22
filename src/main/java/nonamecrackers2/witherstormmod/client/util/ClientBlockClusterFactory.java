package nonamecrackers2.witherstormmod.client.util;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import nonamecrackers2.witherstormmod.client.entity.ClientBlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;

public class ClientBlockClusterFactory {
   public static ClientBlockClusterEntity make(SpawnEntity packet, Level level) {
      return new ClientBlockClusterEntity((EntityType<? extends BlockClusterEntity>)WitherStormModEntityTypes.BLOCK_CLUSTER.get(), level);
   }
}
