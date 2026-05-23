package nonamecrackers2.witherstormmod.common.blockentity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.FireworkRocketItem.Shape;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;

public class FireworkBundleBlockEntity extends BlockEntity {
   private static final int FUSE_TIME = 100;
   private static final int LAUNCH_TIME = 500;
   private final RandomSource random = RandomSource.create();
   private int fuse;
   private int launchDuration;

   public FireworkBundleBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType)WitherStormModBlockEntityTypes.FIREWORK_BUNDLE.get(), pos, state);
   }

   protected void saveAdditional(CompoundTag tag) {
      super.saveAdditional(tag);
      tag.putInt("Fuse", this.fuse);
      tag.putInt("LaunchDuration", this.launchDuration);
   }

   public void load(CompoundTag tag) {
      super.load(tag);
      this.fuse = tag.getInt("Fuse");
      this.launchDuration = tag.getInt("LaunchDuration");
   }

   public void beginFuse() {
      if (this.fuse == 0 && this.launchDuration == 0) {
         this.fuse = 100;
         this.level.playSound(null, this.getBlockPos(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS);
      }
   }

   public boolean isActivated() {
      return this.fuse > 0 || this.launchDuration > 0;
   }

   public static void serverTick(Level level, BlockPos pos, BlockState state, FireworkBundleBlockEntity entity) {
      if (entity.fuse > 0) {
         if (level instanceof ServerLevel serverLevel) {
            Vec3 vec = Vec3.atCenterOf(pos);
            serverLevel.sendParticles(ParticleTypes.SMOKE, vec.x, vec.y, vec.z, 1, 0.0, 0.0, 0.0, 0.0);
         }

         entity.fuse--;
         if (entity.fuse == 0) {
            entity.launchDuration = 500;
         }
      }

      if (entity.launchDuration > 0) {
         entity.launchDuration--;
         if (entity.launchDuration == 0) {
            level.removeBlock(pos, false);
         } else if (entity.random.nextInt(3) == 0) {
            Vec3 vec = Vec3.atCenterOf(pos).add(entity.random.nextDouble() - 0.5, 0.6, entity.random.nextDouble() - 0.5);
            ItemStack stack = createRandomFireworkItem(entity.random);
            FireworkRocketEntity rocket = new FireworkRocketEntity(level, vec.x, vec.y, vec.z, stack);
            Vec3 delta = rocket.getDeltaMovement().add((entity.random.nextDouble() - 0.5) * 0.05, 0.0, (entity.random.nextDouble() - 0.5) * 0.05);
            rocket.setDeltaMovement(delta);
            level.addFreshEntity(rocket);
         }
      }
   }

   private static ItemStack createRandomFireworkItem(RandomSource random) {
      ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
      ListTag list = new ListTag();
      CompoundTag tag = new CompoundTag();
      tag.putBoolean("Flicker", random.nextBoolean());
      tag.putBoolean("Trail", random.nextBoolean());
      List<Integer> colors = Lists.newArrayList();
      int size = random.nextInt(5) + 1;

      for (int i = 0; i < size; i++) {
         DyeColor color = (DyeColor)Util.getRandom(DyeColor.values(), random);
         colors.add(color.getFireworkColor());
      }

      tag.putIntArray("Colors", colors);
      tag.putByte("Type", (byte)((Shape)Util.getRandom(Shape.values(), random)).getId());
      list.add(tag);
      CompoundTag fireworks = stack.getOrCreateTagElement("Fireworks");
      fireworks.putByte("Flight", (byte)(random.nextInt(1) + 2));
      fireworks.put("Explosions", list);
      return stack;
   }
}
