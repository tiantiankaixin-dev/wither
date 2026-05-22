package nonamecrackers2.witherstormmod.common.entity;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.packet.FormidibombExplosionMessage;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.util.IFormidibomb;
import org.jetbrains.annotations.NotNull;

public class FormidibombEntity extends PrimedTnt implements IFormidibomb {
   private static final EntityDataAccessor<Optional<BlockState>> BLOCK_STATE = SynchedEntityData.defineId(
      FormidibombEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE
   );
   private static final EntityDataAccessor<Integer> START_FUSE = SynchedEntityData.defineId(FormidibombEntity.class, EntityDataSerializers.INT);
   @Nullable
   private LivingEntity owner;
   private int airTime;

   public FormidibombEntity(EntityType<? extends FormidibombEntity> type, Level world) {
      super(type, world);
      this.initiateFuse(1200);
   }

   public FormidibombEntity(
      Level world, double x, double y, double z, @Nullable LivingEntity owner, @Nullable IFormidibomb previous, @Nullable BlockState state
   ) {
      this((EntityType<? extends FormidibombEntity>)WitherStormModEntityTypes.FORMIDIBOMB.get(), world);
      this.setPos(x, y, z);
      double d0 = world.random.nextDouble() * (float) (Math.PI * 2);
      this.setDeltaMovement(-Math.sin(d0) * 0.02, 0.2, -Math.cos(d0) * 0.02);
      this.initiateFuse(1200);
      this.xo = x;
      this.yo = y;
      this.zo = z;
      this.owner = owner;
      if (previous != null && previous.getStartFuse() > 0) {
         this.copyFrom(previous);
      }

      this.setBlockState(state);
   }

   protected void defineSynchedData() {
      super.defineSynchedData();
      this.entityData.define(BLOCK_STATE, Optional.empty());
      this.entityData.define(START_FUSE, 0);
   }

   protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
      super.readAdditionalSaveData(compound);
      if (compound.contains("StartFuse")) {
         this.setStartFuse(compound.getInt("StartFuse"));
      }

      if (compound.contains("State")) {
         this.setBlockState(NbtUtils.readBlockState(this.level().holderLookup(Registries.BLOCK), compound.getCompound("State")));
      }
   }

   protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
      super.addAdditionalSaveData(compound);
      compound.putInt("StartFuse", this.getStartFuse());
      compound.put("State", NbtUtils.writeBlockState(this.getBlockState()));
   }

   public void tick() {
      super.tick();
      int startFuse = this.getStartFuse();
      int currentFuse = this.getFuse();
      float fuseProgress = 1.0F - (float)currentFuse / (float)startFuse;
      float initalRadius = 3.0F;
      float initalSpeed = 0.1F;
      float radius;
      float speed;
      if (fuseProgress < 0.5F) {
         speed = initalSpeed * (1.0F - 2.0F * fuseProgress);
         radius = initalRadius * (1.0F - fuseProgress);
      } else {
         speed = initalSpeed * 8.0F * (fuseProgress - 0.5F);
         radius = 1.5F;
      }

      for (int i = 0; i < 6; i++) {
         double x = ((double)this.random.nextFloat() * 2.0 - 1.0) * (double)radius;
         double y = ((double)this.random.nextFloat() * 2.0 - 1.0) * (double)radius;
         double z = ((double)this.random.nextFloat() * 2.0 - 1.0) * (double)radius;
         Vec3 start = this.position().add(x, y, z);
         Vec3 delta = new Vec3(x, y, z).scale((double)speed);
         if (fuseProgress > 0.5F) {
            delta = delta.reverse();
         }

         this.level()
            .addParticle(
               (ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(),
               start.x,
               start.y + 0.5,
               start.z,
               -delta.x,
               -delta.y,
               -delta.z
            );
      }

      if (this.onGround()) {
         this.airTime = 0;
      } else {
         this.airTime++;
      }
   }

   @Override
   public int getFuseLife() {
      return this.getFuse();
   }

   @Override
   public void setLifeFuse(int fuse) {
      this.setFuse(fuse);
   }

   public void initiateFuse(int fuse) {
      this.setLifeFuse(fuse);
      this.setStartFuse(fuse);
   }

   @Override
   public int getStartFuse() {
      return (Integer)this.entityData.get(START_FUSE);
   }

   @Override
   public void setStartFuse(int fuse) {
      this.entityData.set(START_FUSE, fuse);
   }

   @Override
   public LivingEntity getFormidibombOwner() {
      return this.owner;
   }

   @Override
   public void setFormidibombOwner(LivingEntity entity) {
      this.owner = entity;
   }

   public BlockState getBlockState() {
      return ((Optional<BlockState>)this.entityData.get(BLOCK_STATE)).orElse(((Block)WitherStormModBlocks.FORMIDIBOMB.get()).defaultBlockState());
   }

   public void setBlockState(@Nullable BlockState state) {
      this.entityData.set(BLOCK_STATE, Optional.ofNullable(state));
   }

   public ItemStack getPickedResult(HitResult target) {
      ItemStack stack = new ItemStack((ItemLike)WitherStormModItems.FORMIDIBOMB.get());
      CompoundTag compound = stack.getOrCreateTag();
      compound.putInt("Fuse", this.getFuse());
      compound.putInt("StartFuse", this.getStartFuse());
      return stack;
   }

   protected void explode() {
      explode(this.level(), this.getOwner(), 48 + this.level().random.nextInt(9), 3, this.getX(), this.getY(), this.getZ());
      WitherStormModPacketHandlers.MAIN
         .send(
            PacketDistributor.NEAR.with(TargetPoint.p(this.getX(), this.getY(), this.getZ(), 100.0, this.level().dimension())),
            new ShakeScreenMessage(480.0F, 24.0F)
         );
   }

   @Override
   public Vec3 getPosition() {
      return this.position();
   }

   @Override
   public boolean isStillAlive() {
      return this.isAlive();
   }

   public int getAirTime() {
      return this.airTime;
   }

   @NotNull
   public Packet<ClientGamePacketListener> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   public static void explode(Level world, @Nullable Entity entity, int radius, int squish, double x, double y, double z) {
      world.getProfiler().push("formidibomb_explosion");
      ExplosionDamageCalculator explosionContext = (ExplosionDamageCalculator)(entity == null
         ? new ExplosionDamageCalculator()
         : new EntityBasedExplosionDamageCalculator(entity));
      Explosion explosion = new Explosion(
         world, entity, formidibomb(world.registryAccess(), entity), explosionContext, x, y, z, (float)radius, true, BlockInteraction.DESTROY
      );
      if (world.isClientSide) {
         int poofCount = Math.max(4500, world.random.nextInt(5001));

         for (int i = 0; i < poofCount; i++) {
            double speedX = world.random.nextGaussian() * 0.5;
            double speedY = world.random.nextGaussian() * 0.5;
            double speedZ = world.random.nextGaussian() * 0.5;
            double deltaX = world.random.nextGaussian() * 4.0;
            double deltaY = world.random.nextGaussian() * 4.0;
            double deltaZ = world.random.nextGaussian() * 4.0;
            world.addParticle(ParticleTypes.POOF, x + deltaX, y + deltaY, z + deltaZ, speedX, speedY, speedZ);
         }

         int explosionCount = Math.max(50, world.random.nextInt(76));

         for (int i = 0; i < explosionCount; i++) {
            double deltaX = world.random.nextGaussian() * 12.0;
            double deltaY = world.random.nextGaussian() * 12.0;
            double deltaZ = world.random.nextGaussian() * 12.0;
            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, x + deltaX, y + deltaY, z + deltaZ, 0.0, 0.0, 0.0);
         }
      }

      if (world.isClientSide) {
         SoundEvent event = WitherStormModSoundEvents.FORMIDIBOMB_EXPLOSION.get();
         if (!(Boolean)WitherStormModConfig.CLIENT.earRingingEffects.get()) {
            event = WitherStormModSoundEvents.FORMIDIBOMB_EXPLOSION_QUIET.get();
         }

         world.playLocalSound(x, y, z, event, SoundSource.BLOCKS, 16.0F, 1.0F, false);
      }

      world.playSound(null, x, y, z, WitherStormModSoundEvents.TREMBLE.get(), SoundSource.BLOCKS, 32.0F, 1.0F);
      if (!world.isClientSide) {
         FormidibombExplosionMessage message = new FormidibombExplosionMessage(entity, x, y, z, radius, squish);
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(world::dimension), message);
         float diameter = (float)radius * 2.0F;
         int minX = Mth.floor(x - (double)diameter - 1.0);
         int maxX = Mth.floor(x + (double)diameter + 1.0);
         int minY = Mth.floor(y - (double)diameter - 1.0);
         int maxY = Mth.floor(y + (double)diameter + 1.0);
         int minZ = Mth.floor(z - (double)diameter - 1.0);
         int maxZ = Mth.floor(z + (double)diameter + 1.0);
         AABB explosionArea = new AABB((double)minX, (double)minY, (double)minZ, (double)maxX, (double)maxY, (double)maxZ);

         for (WitherStormEntity storm : world.getEntitiesOfClass(WitherStormEntity.class, explosionArea.inflate(200.0))) {
            if (storm.canBeFormidibombed(true)) {
               storm.explode();
            }
         }

         ObjectArrayList<Pair<ItemStack, BlockPos>> items = new ObjectArrayList();

         for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
               for (int k = -radius; k < radius; k++) {
                  if (Mth.sqrt((float)(i * i + j * j * squish + k * k)) < (float)radius) {
                     int random = world.random.nextInt(2);

                     for (int l = -random; l <= random; l++) {
                        BlockPos pos = BlockPos.containing((double)i + x, (double)j + y - (double)l, (double)k + z);
                        BlockState state = world.getBlockState(pos);
                        FluidState fluid = world.getFluidState(pos);
                        if (!state.is(Blocks.AIR)) {
                           BlockPos imutable = pos.immutable();
                           if (state.canDropFromExplosion(world, pos, explosion) && world instanceof ServerLevel) {
                              BlockEntity tile = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
                              Builder context = new Builder((ServerLevel)world)
                                 .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                                 .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                                 .withOptionalParameter(LootContextParams.BLOCK_ENTITY, tile)
                                 .withOptionalParameter(LootContextParams.THIS_ENTITY, entity)
                                 .withParameter(LootContextParams.EXPLOSION_RADIUS, (float)radius);
                              state.getDrops(context).forEach(item -> addBlockDrops(items, item, imutable));
                           }

                           float resistance = (float)radius * (0.7F + world.random.nextFloat() * 0.6F);
                           Optional<Float> blockResistance = explosionContext.getBlockExplosionResistance(explosion, world, pos, state, fluid);
                           if (blockResistance.isPresent()) {
                              resistance -= (blockResistance.get() + 0.3F) * (WitherStormModConfig.SERVER.lowerBlockResistance.get() ? 0.01F : 0.3F);
                           }

                           if (resistance > 0.0F && explosionContext.shouldBlockExplode(explosion, world, pos, state, resistance)) {
                              state.onBlockExploded(world, pos, explosion);
                              if (world.random.nextInt(3) == 0
                                 && world.getBlockState(pos).is(Blocks.AIR)
                                 && world.getBlockState(pos.below()).isSolidRender(world, pos.below())) {
                                 world.setBlockAndUpdate(pos, BaseFireBlock.getState(world, pos));
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         List<Entity> entitiesToExplode = world.getEntities(null, explosionArea);
         ForgeEventFactory.onExplosionDetonate(world, explosion, entitiesToExplode, (double)diameter);
         Vec3 vector = new Vec3(x, y, z);

         for (Entity toExplode : entitiesToExplode) {
            if (!toExplode.ignoreExplosion()) {
               double distance = Math.sqrt(toExplode.distanceToSqr(vector)) / (double)diameter;
               if (distance <= 1.0) {
                  double relativeX = toExplode.getX() - x;
                  double relativeY = (toExplode instanceof PrimedTnt ? toExplode.getY() : toExplode.getEyeY()) - y;
                  double relativeZ = toExplode.getZ() - z;
                  double sqrtPos = Math.sqrt(relativeX * relativeX + relativeY * relativeY + relativeZ * relativeZ);
                  if (sqrtPos != 0.0) {
                     relativeX /= sqrtPos;
                     relativeY /= sqrtPos;
                     relativeZ /= sqrtPos;
                     double seenPercent = (double)Explosion.getSeenPercent(vector, toExplode);
                     double explosionPower = (1.0 - distance) * seenPercent;
                     toExplode.hurt(
                        formidibomb(world.registryAccess(), entity),
                        (float)((int)((explosionPower * explosionPower + explosionPower) / 2.0 * 7.0 * (double)diameter + 1.0))
                     );
                     double explosionPowerModifiable = explosionPower;
                     if (toExplode instanceof LivingEntity) {
                        explosionPowerModifiable = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)toExplode, explosionPower);
                     }

                     toExplode.setDeltaMovement(
                        toExplode.getDeltaMovement()
                           .add(
                              relativeX * (explosionPowerModifiable + (double)radius),
                              relativeY * (explosionPowerModifiable + (double)radius),
                              relativeZ * (explosionPowerModifiable + (double)radius)
                           )
                     );
                  }
               }
            }
         }

         ObjectListIterator var62 = items.iterator();

         while (var62.hasNext()) {
            Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>)var62.next();
            Block.popResource(world, (BlockPos)pair.getSecond(), (ItemStack)pair.getFirst());
         }
      }

      world.getProfiler().pop();
   }

   private static void addBlockDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> items, ItemStack stack, BlockPos pos) {
      int i = items.size();

      for (int j = 0; j < i; j++) {
         Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>)items.get(j);
         ItemStack pairStack = (ItemStack)pair.getFirst();
         if (ItemEntity.areMergable(pairStack, stack)) {
            ItemStack merged = ItemEntity.merge(pairStack, stack, 16);
            items.set(j, Pair.of(merged, (BlockPos)pair.getSecond()));
            if (stack.isEmpty()) {
               return;
            }
         }
      }

      items.add(Pair.of(stack, pos));
   }

   public static DamageSource formidibomb(RegistryAccess access, @Nullable Entity entity) {
      return entity != null
         ? WitherStormModDamageTypes.source(access, WitherStormModDamageTypes.PLAYER_FORMIDIBOMB, entity)
         : WitherStormModDamageTypes.source(access, WitherStormModDamageTypes.FORMIDIBOMB);
   }
}
