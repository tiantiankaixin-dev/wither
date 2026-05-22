package nonamecrackers2.witherstormmod.common.blockentity.inventory;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractSuperBeaconMenu extends AbstractContainerMenu {
   protected final ContainerLevelAccess access;
   private final ContainerData data;
   @Nullable
   private final Consumer<ServerPlayer> powerUp;
   private final Set<MobEffect> validEffects;

   public AbstractSuperBeaconMenu(MenuType<? extends AbstractSuperBeaconMenu> type, int id, Container container) {
      this(type, id, container, new SimpleContainerData(4), ContainerLevelAccess.NULL, null, ImmutableSet.of());
   }

   public AbstractSuperBeaconMenu(
      MenuType<? extends AbstractSuperBeaconMenu> type,
      int id,
      Container container,
      ContainerData data,
      ContainerLevelAccess access,
      @Nullable Consumer<ServerPlayer> powerUp,
      Set<MobEffect> validEffects
   ) {
      super(type, id);
      this.access = access;
      this.data = data;
      this.addDataSlots(data);
      this.powerUp = powerUp;
      this.validEffects = validEffects;
   }

   public void setData(int slot, int value) {
      super.setData(slot, value);
      this.broadcastChanges();
   }

   public int getLevel() {
      return this.data.get(0);
   }

   @Nullable
   public MobEffect getPrimaryEffect() {
      return MobEffect.byId(this.data.get(1));
   }

   public void updateEffects(int effectId) {
      this.data.set(1, effectId);
   }

   public void activateCooldown(int time) {
      this.data.set(3, time);
   }

   public int getCooldown() {
      return this.data.get(3);
   }

   public void setShowArea(boolean flag) {
      this.data.set(2, flag ? 1 : 0);
   }

   public boolean shouldShowArea() {
      return this.data.get(2) == 1;
   }

   public void doPowerUp(ServerPlayer player) {
      if (this.powerUp != null) {
         this.powerUp.accept(player);
      }
   }

   public Set<MobEffect> getValidEffects() {
      return this.validEffects;
   }

   public ItemStack quickMoveStack(Player player, int slot) {
      return ItemStack.EMPTY;
   }
}
