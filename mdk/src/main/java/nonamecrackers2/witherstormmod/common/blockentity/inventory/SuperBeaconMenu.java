package nonamecrackers2.witherstormmod.common.blockentity.inventory;

import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMenuTypes;

public class SuperBeaconMenu extends AbstractSuperBeaconMenu {
   public SuperBeaconMenu(int id, Container container) {
      super((MenuType<? extends AbstractSuperBeaconMenu>)WitherStormModMenuTypes.SUPER_BEACON.get(), id, container);
   }

   public SuperBeaconMenu(
      int id, Container container, ContainerData data, ContainerLevelAccess access, @Nullable Consumer<ServerPlayer> powerUp, Set<MobEffect> validEffects
   ) {
      super((MenuType<? extends AbstractSuperBeaconMenu>)WitherStormModMenuTypes.SUPER_BEACON.get(), id, container, data, access, powerUp, validEffects);
   }

   public boolean stillValid(Player player) {
      return stillValid(this.access, player, (Block)WitherStormModBlocks.SUPER_BEACON.get());
   }
}
