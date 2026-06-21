package nonamecrackers2.witherstormmod.common.blockentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PacketDistributor.TargetPoint;
import nonamecrackers2.witherstormmod.common.blockentity.inventory.SuperBeaconMenu;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.BlockClusterEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModAttributes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlockEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModRecipeTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.item.crafting.ItemCraftSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.ResummonSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.packet.GlobalSoundMessage;
import nonamecrackers2.witherstormmod.common.packet.ShakeScreenMessage;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModBlockTags;
import nonamecrackers2.witherstormmod.common.util.AttributeModifierUtil;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class SuperBeaconBlockEntity extends AbstractSuperBeaconBlockEntity implements WorldlyContainer, RecipeInput {
   public static final int MAX_ITEMS = 16;
   public static final int RESUMMON_START = 60;
   public static final int RESUMMON_TIME = 372;
   public static final int EFFECT_RADIUS = 128;
   private static final int[] COLOR = new int[]{14, 62, 232};
   private final Map<AbstractSuperBeaconBlockEntity.Color, BlockPos> connected = Maps.newHashMap();
   private NonNullList<ItemStack> items = NonNullList.create();
   private int resummonTicks;
   private boolean isDoingResummoning;
   @Nullable
   private EntityType<?> resummoningEntity;
   @Nullable
   private CompoundTag resummonNbt;
   public Vec2 shake = Vec2.ZERO;
   public Vec2 shakeO = Vec2.ZERO;
   private List<BlockClusterEntity> clusters = Lists.newArrayList();

   public SuperBeaconBlockEntity(BlockPos pos, BlockState state) {
      super((BlockEntityType<? extends AbstractSuperBeaconBlockEntity>)WitherStormModBlockEntityTypes.SUPER_BEACON.get(), pos, state);
   }

   @Override
   public void tick() {
      super.tick();
      int prevLevel = this.beaconLevel;
      this.beaconLevel = this.getBeaconLevel();
      if (prevLevel != this.beaconLevel) {
         if (this.beaconLevel > 0) {
            this.isActive = true;
            if (prevLevel == 0) {
               this.activate();
            }
         } else {
            this.isActive = false;
            this.deactivate();
         }

         this.markUpdated();
      }

      this.findNearbySupportBeacons();
      if (!this.level.isClientSide && !this.isDoingResummonAnimation()) {
         List<RecipeHolder<ResummonSuperBeaconRecipe>> recipes = this.level
            .getRecipeManager()
            .getRecipesFor(WitherStormModRecipeTypes.SUPER_BEACON_RESUMMON.get(), this, this.level);
         if (!recipes.isEmpty()) {
            ResummonSuperBeaconRecipe recipe = recipes.get(0).value();
            if (recipe.getCondition().canCraft(this)) {
               this.resummoningEntity = recipe.getResummonEntity();
               this.resummonNbt = recipe.getResummonEntityNBT();
               this.activateResummonAnimation();
               this.isDoingResummoning = true;
               this.markUpdated();
            }
         }

         List<RecipeHolder<ItemCraftSuperBeaconRecipe>> craftingRecipes = this.level
            .getRecipeManager()
            .getRecipesFor(WitherStormModRecipeTypes.SUPER_BEACON_ITEM.get(), this, this.level);
         if (!craftingRecipes.isEmpty()) {
            ItemCraftSuperBeaconRecipe recipe = craftingRecipes.get(0).value();
            if (recipe.getCondition().canCraft(this)) {
               Vec3 pos = Vec3.atCenterOf(this.getBlockPos());
               ServerLevel level = (ServerLevel)this.level;
               level.sendParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y + 2.0, pos.z, 20, 1.0, 1.0, 1.0, 0.01);
               level.sendParticles(
                  WitherStormModParticleTypes.COMMAND_BLOCK.get(), pos.x, pos.y + 2.0, pos.z, 50, 1.0, 1.0, 1.0, 0.015
               );
               this.level
                  .playSound(null, this.getBlockPos(), WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), SoundSource.BLOCKS, 10.0F, 1.0F);
               ItemStack stack = recipe.assemble(this, this.level.registryAccess());
               ItemEntity item = new ItemEntity(this.level, pos.x, pos.y + 2.0, pos.z, stack);
               item.setGlowingTag(true);
               this.level.addFreshEntity(item);
               this.items.clear();
               this.markUpdated();
            }
         }
      }

      if (this.isDoingResummoning) {
         this.resummonTicks++;
         BlockPos commandBlockPos = this.getBlockPos().above(3);
         if (this.getResummonTicks() > 60) {
            Vec3 cmdBlockVec = Vec3.atCenterOf(commandBlockPos);
            double x = cmdBlockVec.x + this.random.nextGaussian();
            double y = cmdBlockVec.y + this.random.nextGaussian();
            double z = cmdBlockVec.z + this.random.nextGaussian();
            Vec3 delta = cmdBlockVec.subtract(x, y, z).normalize().scale(0.1);
            this.level.addParticle((ParticleOptions)WitherStormModParticleTypes.COMMAND_BLOCK.get(), x, y, z, delta.x, delta.y, delta.z);
         }

         if (!this.level.isClientSide) {
            Vec3 pos = Vec3.atCenterOf(this.getBlockPos());
            if (this.getResummonTicks() == 60) {
               this.items.clear();
               if (this.level instanceof ServerLevel level) {
                  level.sendParticles(ParticleTypes.LARGE_SMOKE, pos.x, pos.y, pos.z, 20, 1.0, 1.0, 1.0, 0.01);
                  level.sendParticles(
                     WitherStormModParticleTypes.COMMAND_BLOCK.get(),
                     pos.x,
                     pos.y + 3.0,
                     pos.z,
                     50,
                     1.0,
                     1.0,
                     1.0,
                     0.015
                  );
                  this.level
                     .playSound(null, commandBlockPos, WitherStormModSoundEvents.COMMAND_BLOCK_ACTIVATES.get(), SoundSource.BLOCKS, 10.0F, 1.0F);
                  boolean flag = this.resummoningEntity == WitherStormModEntityTypes.WITHER_STORM.get();
                  if (flag) {
                     this.level
                        .playSound(null, commandBlockPos, WitherStormModSoundEvents.COMMAND_BLOCK_BUILD.get(), SoundSource.BLOCKS, 10.0F, 1.0F);
                  }

                  if (this.resummoningEntity != null && this.resummonNbt != null && !flag) {
                     Entity entity = this.resummoningEntity.spawn(level, commandBlockPos, MobSpawnType.TRIGGERED);

                     for (ServerPlayer player : this.level.getEntitiesOfClass(ServerPlayer.class, new AABB(this.getBlockPos()).inflate(100.0))) {
                        WitherStormModCriteriaTriggers.SUMMON_MOB_SUPER_BEACON.trigger(player, entity);
                     }

                     if (!this.resummonNbt.isEmpty()) {
                        CompoundTag current = entity.saveWithoutId(new CompoundTag());
                        current.merge(this.resummonNbt);
                        entity.load(current);
                     }

                     this.resummoningEntity = null;
                     this.resummonNbt = null;
                     this.resummonTicks = 0;
                     this.isDoingResummoning = false;
                  }
               }

               this.markUpdated();
            }

            if (this.resummoningEntity == WitherStormModEntityTypes.WITHER_STORM.get()) {
               if (this.getResummonTicks() > 60) {
                  if (this.getResummonTicks() % 40 == 0) {
                     this.playSound(WitherStormModSoundEvents.BOWELS_LOUD_HURT.get(), 10.0F, 1.0F);
                     WitherStormModPacketHandlers.MAIN
                        .send(
                           PacketDistributor.NEAR.with(new TargetPoint(pos.x, pos.y, pos.z, 20.0, this.level.dimension())),
                           new ShakeScreenMessage(80.0F, 4.0F)
                        );
                  }

                  int interval = Math.max(1, 372 / this.getResummonTicks());
                  if (this.getResummonTicks() % interval == 0 && this.getResummonTicks() < 352 && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                     for (int b = 0; b < 2; b++) {
                        int x = this.random.nextInt(97) - 48 + this.getBlockPos().getX();
                        int z = this.random.nextInt(97) - 48 + this.getBlockPos().getZ();
                        BlockPos currentPos = new BlockPos(x, this.getBlockPos().getY() + 3, z);

                        for (int i = 0; i < 30 && this.level.getBlockState(currentPos.below()).isAir(); i++) {
                           currentPos = currentPos.below();
                        }

                        currentPos = currentPos.below();
                        BlockClusterEntity cluster = (BlockClusterEntity)(WitherStormModEntityTypes.BLOCK_CLUSTER.get()).create(this.level);
                        cluster.populateWithRadius(
                           currentPos,
                           1.0F,
                           state -> {
                              if (state.is(WitherStormModBlockTags.WITHER_STORM_BLOCK_BLACKLIST)) {
                                 return false;
                              } else {
                                 for (AbstractSuperBeaconBlockEntity.Color color : AbstractSuperBeaconBlockEntity.Color.values()) {
                                    if (color.isValidBaseBlock(state)) {
                                       return false;
                                    }
                                 }

                                 return !state.is(WitherStormModBlockTags.WITHERED_BEACON_BASE)
                                    && !state.is(BlockTags.BEACON_BASE_BLOCKS)
                                    && !state.is(WitherStormModBlockTags.BEACONS);
                              }
                           }
                        );
                        cluster.setFadePos(this.getBlockPos());
                        cluster.setTime(200);
                        if (this.getResummonTicks() % 40 == 0) {
                           cluster.playSound(WitherStormModSoundEvents.BLOCK_CLUSTER_SHAKE.get(), 2.0F, 1.0F);
                        }

                        cluster.setRotationDelta(new Vec2((float)this.random.nextInt(20) * 0.1F / 2.0F, (float)this.random.nextInt(20) * 0.1F / 2.0F));
                        cluster.setNoGravity(true);
                        cluster.setPhysics(false);
                        this.level.addFreshEntity(cluster);
                        this.clusters.add(cluster);
                     }
                  }

                  Iterator<BlockClusterEntity> iterator = this.clusters.iterator();

                  while (iterator.hasNext()) {
                     BlockClusterEntity cluster = iterator.next();
                     if (cluster.isAlive()) {
                        if (cluster.getShakeTime() <= 0) {
                           Vec3 cmdPos = pos.add(0.0, 3.0, 0.0);
                           Vec3 delta = cmdPos.subtract(cluster.position()).normalize().scale(0.5);
                           cluster.setDeltaMovement(delta);
                           if (new AABB(cluster.blockPosition()).contains(cmdPos)) {
                              cluster.discard();
                           }
                        }
                     } else {
                        iterator.remove();
                     }
                  }
               }

               if (this.getResummonTicks() > 372) {
                  this.resummonTicks = 0;
                  this.isDoingResummoning = false;
                  this.markUpdated();

                  for (BlockClusterEntity cluster : this.clusters) {
                     cluster.discard();
                  }

                  this.level.removeBlock(this.getBlockPos(), false);

                  for (BlockPos connected : this.getConnected().values()) {
                     this.level.removeBlock(connected, false);
                  }

                  this.level.explode(null, pos.x, pos.y, pos.z, 8.0F, ExplosionInteraction.BLOCK);
                  WitherStormEntity storm = (WitherStormEntity)(WitherStormModEntityTypes.WITHER_STORM.get()).create(this.level);
                  storm.getAttribute(WitherStormModAttributes.holder(WitherStormModAttributes.EVOLUTION_SPEED))
                     .addPermanentModifier(new AttributeModifier(AttributeModifierUtil.id("resummoned_modifier"), -0.5, Operation.ADD_VALUE));
                  storm.setPhase((Integer)WitherStormModConfig.SERVER.resummonedPhase.get());
                  storm.moveTo(pos);
                  storm.playSoundToEveryone(WitherStormModSoundEvents.WITHER_STORM_EVOLVES.get(), 1.0F, 1.0F);
                  storm.getPlayDeadManager().setRecentlyRevived(true);
                  storm.setResummoned(true);

                  for (ServerPlayer player : this.level.getEntitiesOfClass(ServerPlayer.class, new AABB(this.getBlockPos()).inflate(100.0))) {
                     CriteriaTriggers.SUMMONED_ENTITY.trigger(player, storm);
                  }

                  this.level.addFreshEntity(storm);
               }
            }
         }
      }

      this.shakeO = this.shake;
      if (this.isDoingResummonAnimation()) {
         float x = Mth.cos((float)this.resummonTicks * 4.0F) * 0.1F + (this.random.nextFloat() - 0.5F) * 0.05F;
         float z = Mth.cos((float)this.resummonTicks * 3.0F) * 0.1F + (this.random.nextFloat() - 0.5F) * 0.05F;
         this.shake = new Vec2(x, z);
      }

      if (!this.level.isClientSide && this.getResummonTicks() == this.getResummonThreshold()) {
         this.playSound(WitherStormModSoundEvents.WITHERED_BEACON_ACTIVATE.get(), 1.0F, 1.0F);
         this.playSound(WitherStormModSoundEvents.TREMBLE.get(), 10.0F, 1.0F);
         Vec3 posx = Vec3.atCenterOf(this.getBlockPos());
         WitherStormModPacketHandlers.MAIN
            .send(
               PacketDistributor.NEAR.with(new TargetPoint(posx.x, posx.y, posx.z, 20.0, this.level.dimension())),
               new ShakeScreenMessage(80.0F, 10.0F)
            );
      }
   }

   @Override
   public void setRemoved() {
      super.setRemoved();
      this.clusters.forEach(c -> {
         c.setNoGravity(false);
         c.setPhysics(true);
      });
   }

   @Override
   protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
      super.loadAdditional(tag, registries);
      NonNullList<ItemStack> items = NonNullList.create();
      ListTag list = tag.getList("ResummonItems", 10);

      for (int i = 0; i < list.size(); i++) {
         CompoundTag item = list.getCompound(i);
         items.add(ItemStack.parseOptional(registries, item));
      }

      this.items = items;
      this.resummonTicks = tag.getInt("ResummonTicks");
      this.isDoingResummoning = tag.getBoolean("Resummoning");
      if (tag.contains("ResummoningEntity")) {
         String rawId = tag.getString("ResummoningEntity");
         this.resummoningEntity = (EntityType<?>)EntityType.byString(rawId).orElse(null);
      }

      if (tag.contains("ResummonNBT")) {
         this.resummonNbt = tag.getCompound("ResummonNBT");
      }
   }

   @Override
   protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
      super.saveAdditional(tag, registries);
      ListTag list = new ListTag();

      for (ItemStack stack : this.items) {
         CompoundTag item = new CompoundTag();
         list.add(stack.save(registries, item));
      }

      tag.put("ResummonItems", list);
      tag.putInt("ResummonTicks", this.resummonTicks);
      tag.putBoolean("Resummoning", this.isDoingResummoning);
      if (this.resummoningEntity != null) {
         ResourceLocation id = EntityType.getKey(this.resummoningEntity);
         tag.putString("ResummoningEntity", id.toString());
      }

      if (this.resummonNbt != null) {
         tag.put("ResummonNBT", this.resummonNbt);
      }
   }

   @Override
   protected void applyEffect(ServerLevel level) {
      AABB box = new AABB(this.getBlockPos()).inflate(128.0).expandTowards(0.0, (double)level.getHeight(), 0.0);
      level.getEntitiesOfClass(Player.class, box).forEach(player -> {
         if (Math.sqrt(player.distanceToSqr(Vec3.atCenterOf(this.getBlockPos()))) <= 128.0) {
            player.addEffect(new MobEffectInstance(this.effect, 505, this.beaconLevel - 1, true, true));
         }
      });
   }

   private void findNearbySupportBeacons() {
      AABB box = new AABB(this.getBlockPos()).inflate(5.0);
      List<BlockEntity> entities = WorldUtil.getBlockEntitiesInAABB(this.level, box);
      Iterator<BlockPos> iterator = this.connected.values().iterator();

      while (iterator.hasNext()) {
         BlockPos pos = iterator.next();
         BlockEntity entity = this.level.getBlockEntity(pos);
         if (!this.isValidSupportBeacon.test(entity)) {
            iterator.remove();
         }
      }

      for (BlockEntity entity : entities) {
         if (this.isValidSupportBeacon.test(entity) && Math.sqrt(entity.getBlockPos().distSqr(this.getBlockPos())) <= 5.0) {
            AbstractSuperBeaconBlockEntity.Color color = ((SuperSupportBeaconBlockEntity)entity).getColor();
            this.connected.putIfAbsent(color, entity.getBlockPos());
         }
      }
   }

   private int getBeaconLevel() {
      BlockPos pos = this.getBlockPos();
      int level = 0;

      for (int i = 1; i <= 4; level = i++) {
         int y = pos.getY() - i;
         if (i < this.level.getMinBuildHeight()) {
            break;
         }

         boolean flag = true;

         for (int x = pos.getX() - i; x <= pos.getX() + i && flag; x++) {
            for (int z = pos.getZ() - i; z <= pos.getZ() + i; z++) {
               BlockState state = this.level.getBlockState(new BlockPos(x, y, z));
               if (i == 1 ? !state.is(WitherStormModBlockTags.WITHERED_BEACON_BASE) : !state.is(BlockTags.BEACON_BASE_BLOCKS)) {
                  flag = false;
                  break;
               }
            }
         }

         if (!flag) {
            break;
         }
      }

      return level;
   }

   public boolean isConnected(BlockPos pos) {
      return this.connected.containsValue(pos);
   }

   @Override
   public float getThickness() {
      return 0.25F;
   }

   @Override
   public float getOuterThickness() {
      return 0.45F;
   }

   public Map<AbstractSuperBeaconBlockEntity.Color, BlockPos> getConnected() {
      return this.connected;
   }

   @Override
   public void doPowerUp(ServerPlayer player) {
      super.doPowerUp(player);
      player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA)
         .ifPresent(
            data -> {
               if (!data.hasActivatedSuperBeacon() && this.connected.size() >= AbstractSuperBeaconBlockEntity.Color.values().length) {
                  this.poweringUpAnimation = 80;
                  WitherStormModPacketHandlers.MAIN
                     .send(
                        PacketDistributor.ALL.noArg(), new GlobalSoundMessage(WitherStormModSoundEvents.WITHERED_BEACON_POWER_UP.get(), 1.0F, 1.0F)
                     );
                  this.level
                     .getEntitiesOfClass(ServerPlayer.class, new AABB(this.getBlockPos()).inflate(64.0))
                     .forEach(
                        p -> p.connection.send(new ClientboundStopSoundPacket(WitherStormModSoundEvents.WITHERED_BEACON_AMBIENT.getId(), SoundSource.BLOCKS))
                     );

                  for (BlockPos pos : this.connected.values()) {
                     if (this.level.getBlockEntity(pos) instanceof AbstractSuperBeaconBlockEntity superBeacon) {
                        superBeacon.poweringUpAnimation = 80;
                        superBeacon.markUpdated();
                        superBeacon.doActivationSequence();
                        superBeacon.activateAnim = 0.0F;
                     }
                  }

                  this.markUpdated();
                  this.doActivationSequence();
                  this.activateAnim = 0.0F;
               }
            }
         );
   }

   @Override
   protected void doPoweringUpAnimation() {
      if (!this.level.isClientSide && this.poweringUpAnimation == 40) {
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.ALL.noArg(), new ShakeScreenMessage(120.0F, 12.0F));
         if (this.level instanceof ServerLevel level) {
            Vec3 pos = Vec3.atCenterOf(this.getBlockPos());
            level.sendParticles(
               ParticleTypes.DRAGON_BREATH,
               pos.x,
               pos.y,
               pos.z,
               200,
               this.random.nextGaussian(),
               this.random.nextGaussian(),
               this.random.nextGaussian(),
               0.2
            );
         }

         this.level.getEntitiesOfClass(ServerPlayer.class, new AABB(this.getBlockPos()).inflate(64.0)).forEach(p -> {
            p.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(d -> d.setActivatedSuperBeacon(true));
            WitherStormModCriteriaTriggers.ACTIVATE_SUPER_BEACON.trigger(p, this.connected.size());
         });
      }
   }

   @Override
   public Set<Holder<MobEffect>> getValidEffects() {
      return VALID_EFFECTS;
   }

   public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
      return BaseContainerBlockEntity.canUnlock(player, this.lockKey, this.getDisplayName())
         ? new SuperBeaconMenu(id, inventory, this.data, ContainerLevelAccess.create(this.level, this.getBlockPos()), this::doPowerUp, this.getValidEffects())
         : null;
   }

   @Override
   public int[] getBeamColor() {
      return COLOR;
   }

   public void addItem(ItemStack stack) {
      this.setItem(this.getContainerSize(), stack);
   }

   public ItemStack takeItem() {
      return this.removeItem(this.getContainerSize() - 1, 1);
   }

   public int getResummonTicks() {
      return this.resummonTicks;
   }

   public Vec2 getShake(float partialTicks) {
      float x = Mth.lerp(partialTicks, this.shakeO.x, this.shake.x);
      float z = Mth.lerp(partialTicks, this.shakeO.y, this.shake.y);
      return new Vec2(x, z);
   }

   private void activateResummonAnimation() {
      this.playSound(WitherStormModSoundEvents.TREMBLE.get(), 10.0F, 1.0F);
      this.playSound(WitherStormModSoundEvents.BOWELS_LOUD_HURT.get(), 10.0F, 1.0F);
      Vec3 pos = Vec3.atCenterOf(this.getBlockPos());
      WitherStormModPacketHandlers.MAIN
         .send(
            PacketDistributor.NEAR.with(new TargetPoint(pos.x, pos.y, pos.z, 20.0, this.level.dimension())),
            new ShakeScreenMessage(80.0F, 10.0F)
         );
   }

   public boolean isDoingResummonAnimation() {
      return this.isDoingResummoning;
   }

   @Override
   public boolean isActive() {
      return super.isActive() && !this.isDoingResummonAnimation();
   }

   @Override
   protected boolean shouldDoActivatedAnim() {
      return super.shouldDoActivatedAnim() || this.getResummonTicks() > this.getResummonThreshold();
   }

   public void clearContent() {
      this.items.clear();
   }

   public int getContainerSize() {
      return this.items.size();
   }

   public int size() {
      return this.getContainerSize();
   }

   public boolean isEmpty() {
      for (ItemStack itemstack : this.items) {
         if (!itemstack.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public ItemStack getItem(int slot) {
      return slot >= 0 && slot < this.items.size() ? (ItemStack)this.items.get(slot) : ItemStack.EMPTY;
   }

   public ItemStack removeItem(int slot, int amount) {
      if (!this.items.isEmpty()) {
         ItemStack stack = ((ItemStack)this.items.get(slot)).split(amount);
         if (((ItemStack)this.items.get(slot)).isEmpty()) {
            this.items.remove(slot);
         }

         this.markUpdated();
         return stack;
      } else {
         return ItemStack.EMPTY;
      }
   }

   public ItemStack removeItemNoUpdate(int slot) {
      return ContainerHelper.takeItem(this.items, slot);
   }

   public void setItem(int slot, ItemStack stack) {
      if (slot >= 0 && slot <= 16) {
         this.items.add(slot, stack);
         this.markUpdated();
      }
   }

   public boolean stillValid(Player player) {
      return Container.stillValidBlockEntity(this, player);
   }

   public boolean canPlayerUseItems(Player player) {
      return BaseContainerBlockEntity.canUnlock(player, this.lockKey, this.getDisplayName());
   }

   public int[] getSlotsForFace(Direction directions) {
      return new int[]{this.getContainerSize()};
   }

   public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {
      return direction != Direction.DOWN && slot < 16 && !this.isDoingResummonAnimation();
   }

   public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
      return false;
   }

   @Nullable
   public EntityType<?> getResummonEntity() {
      return this.resummoningEntity;
   }
}
