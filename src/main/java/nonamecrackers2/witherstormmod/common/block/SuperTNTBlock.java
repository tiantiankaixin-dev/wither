package nonamecrackers2.witherstormmod.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.PushReaction;
import nonamecrackers2.witherstormmod.common.entity.SuperTNTEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class SuperTNTBlock extends TntBlock {
   public SuperTNTBlock(Properties properties) {
      super(properties);
   }

   public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
      if (!world.isClientSide) {
         SuperTNTEntity entity = new SuperTNTEntity(
            world, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, explosion.getIndirectSourceEntity()
         );
         entity.setFuse(world.random.nextInt(entity.getFuse() / 4) + entity.getFuse() / 8);
         world.addFreshEntity(entity);
      }
   }

   public void onCaughtFire(BlockState state, Level world, BlockPos pos, Direction face, LivingEntity igniter) {
      if (!world.isClientSide) {
         SuperTNTEntity entity = new SuperTNTEntity(world, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, igniter);
         world.addFreshEntity(entity);
         world.playSound(
            null,
            entity.getX(),
            entity.getY(),
            entity.getZ(),
            WitherStormModSoundEvents.SUPER_TNT_PRIMED.get(),
            SoundSource.BLOCKS,
            1.0F,
            1.0F
         );
      }
   }

   public PushReaction getPistonPushReaction(BlockState state) {
      return PushReaction.BLOCK;
   }
}
