package nonamecrackers2.witherstormmod.common.entity.ai.symbiont;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SymbiontSpell;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.PlayerMotionMessage;
import nonamecrackers2.witherstormmod.common.predicate.BlockPredicateBuilder;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;

public class PulseSpell extends SymbiontSpell {
   public static final Predicate<BlockState> CAN_BE_THROWN = BlockPredicateBuilder.and()
      .isNotAir()
      .isNotAFluid()
      .isNotTag(WitherStormModBlockTags.TAINTED_BLOCKS)
      .isNotTag(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST)
      .isNotTag(WitherStormModBlockTags.SMALL_CLUSTER_BLACKLIST)
      .isNotTag(BlockTags.REPLACEABLE)
      .isNotTag(BlockTags.BEACON_BASE_BLOCKS)
      .isNotTag(WitherStormModBlockTags.RED_SUPPORT_BASE)
      .isNotTag(WitherStormModBlockTags.GREEN_SUPPORT_BASE)
      .isNotTag(WitherStormModBlockTags.AQUA_SUPPORT_BASE)
      .isNotTag(WitherStormModBlockTags.GRAY_SUPPORT_BASE)
      .isNotTag(WitherStormModBlockTags.WITHERED_BEACON_BASE)
      .build();

   public PulseSpell(WitheredSymbiontEntity symbiont, SpellType type) {
      super(symbiont, type);
   }

   @Override
   public void start(LivingEntity target) {
   }

   @Override
   public void cast(LivingEntity target) {
      List<LivingEntity> nearbyEntities = this.entity.getNearbyTargets(WitheredSymbiontEntity.PULSE_PREDICATE).toList();
      List<BlockPos> nearbyBlocks = this.getNearbyBlocks();
      this.entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), 4.0F, 2.0F);

      for (BlockPos pos : nearbyBlocks) {
         BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.entity.level());
         if (cluster != null) {
            cluster.populateWithRadius(
               pos,
               1.0F,
               blockState -> !blockState.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST)
                     && !blockState.is(WitherStormModBlockTags.SMALL_CLUSTER_BLACKLIST)
                     && !blockState.is(WitherStormModBlockTags.TAINTED_BLOCKS)
            );
            cluster.setTime(100);
            cluster.setShouldCrumble(false);
            int rotationDelta = this.entity.getRandom().nextInt(129) - 64;
            cluster.setRotationDelta(new Vec2((float)rotationDelta * 0.0625F, (float)rotationDelta * 0.0625F));
            cluster.setNoGravity(false);
            cluster.setPhysics(true);
            double deltaX = (double)(pos.getX() + this.entity.getRandom().nextInt(4)) - this.entity.getX();
            double deltaY = (double)(pos.getY() + this.entity.getRandom().nextInt(4)) - this.entity.getY();
            double deltaZ = (double)(pos.getZ() + this.entity.getRandom().nextInt(4)) - this.entity.getZ();
            Vec3 deltaMovement = new Vec3(deltaX, deltaY, deltaZ).normalize().scale(2.0);
            cluster.setDeltaMovement(deltaMovement);
            this.entity.level().addFreshEntity(cluster);
         }
      }

      for (Entity entity : nearbyEntities) {
         if (entity.isAlive() && entity != this.entity) {
            double deltaX = entity.getX() - this.entity.getX();
            double deltaY = entity.getY() + 1.0 - this.entity.getY();
            double deltaZ = entity.getZ() - this.entity.getZ();
            Vec3 deltaMovement = new Vec3(deltaX, deltaY, deltaZ).normalize().scale(3.0);
            entity.setDeltaMovement(deltaMovement);
            if (entity instanceof ServerPlayer) {
               PlayerMotionMessage message = new PlayerMotionMessage(deltaMovement);
               WitherStormModPacketHandlers.MAIN.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer)entity), message);
            }
         }
      }
   }

   @Override
   public void doCasting(LivingEntity target) {
      List<LivingEntity> nearbyEntities = this.entity.getNearbyTargets(WitheredSymbiontEntity.PULSE_PREDICATE).toList();
      Vec3 entityDelta = this.entity
         .getEyePosition(1.0F)
         .add(this.entity.getX(), this.entity.getEyeY(), this.entity.getZ())
         .normalize()
         .multiply(0.1, 0.1, 0.1);
      ((ServerLevel)this.entity.level())
         .sendParticles(
            WitherStormModParticleTypes.COMMAND_BLOCK.get(),
            this.entity.getX(),
            this.entity.getEyeY(),
            this.entity.getZ(),
            3,
            entityDelta.x(),
            entityDelta.y(),
            entityDelta.z(),
            1.0
         );

      for (Entity entity : nearbyEntities) {
         double x = entity.getX() + this.entity.getRandom().nextGaussian() * 1.0;
         double y = entity.getEyeY() + this.entity.getRandom().nextGaussian() * 1.0;
         double z = entity.getZ() + this.entity.getRandom().nextGaussian() * 1.0;
         Vec3 delta = entity.getEyePosition(1.0F).subtract(x, y, z).normalize().multiply(0.1, 0.1, 0.1);
         ((ServerLevel)this.entity.level())
            .sendParticles(WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, 0, delta.x(), delta.y(), delta.z(), 1.0);
      }

      if (this.entity.tickCount % Math.max(2, 16) == 0) {
         this.entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), 4.0F, 0.75F);
      }
   }

   private List<BlockPos> getNearbyBlocks() {
      List<BlockPos> nearbyBlocks = new ArrayList<>();
      BlockPos entityPos = this.entity.blockPosition();
      int horizontalRange = 16;
      int verticalRange = 8;
      int maxCount = 1024;
      int blockCount = 0;

      for (int x = -horizontalRange; x <= horizontalRange; x++) {
         for (int y = -verticalRange; y <= verticalRange; y++) {
            for (int z = -horizontalRange; z <= horizontalRange; z++) {
               BlockPos searchPos = entityPos.offset(x, y, z);
               if (blockCount >= maxCount) {
                  break;
               }

               if (CAN_BE_THROWN.test(this.entity.level().getBlockState(searchPos))) {
                  nearbyBlocks.add(searchPos);
                  blockCount++;
               }
            }
         }
      }

      return nearbyBlocks;
   }

   @Override
   public int getDelay(RandomSource random, float modifier) {
      return Math.max(480, random.nextInt(600)) - Mth.floor(modifier) * 10;
   }
}
