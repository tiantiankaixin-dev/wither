package nonamecrackers2.witherstormmod.client.util;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import nonamecrackers2.witherstormmod.client.entity.ClientBlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;

public class ClientBlockClusterFactory {
   public static BlockClusterEntity make(EntityType<BlockClusterEntity> entityType, Level level) {
      if (level.isClientSide) {
         return new ClientBlockClusterEntity(entityType, level);
      }

      return new BlockClusterEntity(entityType, level);
   }
}
