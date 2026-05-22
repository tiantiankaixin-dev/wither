package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm;

import java.util.Collections;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements.Type;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.IItemHandler;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.capability.PlayerWitherStormData;
import nonamecrackers2.witherstormmod.common.capability.WitherStormBowelsManager;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.item.FormidibombItem;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModItemTags;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class SymbiontSummoningManager {
   protected final WitherStormEntity entity;
   protected int timeTillCanSummonSymbiont;

   public SymbiontSummoningManager(WitherStormEntity entity) {
      this.entity = entity;
   }

   public void tick() {
      if (this.timeTillCanSummonSymbiont > 0) {
         this.timeTillCanSummonSymbiont--;
      }

      if ((Boolean)WitherStormModConfig.SERVER.canSummonSymbiont.get()) {
         int delay = Mth.clamp((Integer)WitherStormModConfig.SERVER.minimumSpawnCheckInterval.get(), 1, 240) * 20;
         if (this.entity.tickCount % delay * (this.entity.getRandom().nextInt(3) + 1) == 0) {
            List<Player> players = this.entity.level().getNearbyPlayers(TargetingConditions.DEFAULT, null, this.entity.getSearchBox());
            Collections.sort(players, (entity, entity1) -> Mth.floor(entity.distanceTo(this.entity) - entity1.distanceTo(this.entity)));
            Player player = null;

            for (Player playerToCheck : players) {
               if (this.playerApplicable(playerToCheck)) {
                  player = playerToCheck;
                  break;
               }
            }

            if (this.canSummonSymbiont() && player != null) {
               this.summonSymbiont(player);
            }
         }
      }
   }

   protected boolean canSummonSymbiont() {
      boolean flag = true;
      if (this.entity.isDeadOrPlayingDead() || !this.entity.isAlive()) {
         flag = false;
      }

      if (this.entity.getPhase() < 5 || this.entity.getConsumedEntities() < this.entity.getConsumptionAmountForPhase(5)) {
         flag = false;
      }

      AABB searchBox = this.entity.getSearchBox().inflate(50.0);

      for (Entity entity : WorldUtil.getPerformantEntitiesOfClass((ServerLevel)this.entity.level(), Entity.class, searchBox)) {
         if (entity.isAlive()) {
            if (entity instanceof FormidibombEntity formidibomb) {
               if (formidibomb.getStartFuse() > 0) {
                  flag = false;
                  break;
               }
            } else if (entity instanceof WitheredSymbiontEntity) {
               flag = false;
               break;
            }
         }
      }

      if (this.entity.isAttractingFormidibomb()) {
         flag = false;
      }

      if (this.timeTillCanSummonSymbiont > 0) {
         flag = false;
      }

      if (this.entity.hasRecentlyBeenRevived()) {
         flag = false;
      }

      CommandBlockEntity commandBlock = this.entity.getBowelsCommandBlock();
      if (commandBlock != null && commandBlock.getHealth() < commandBlock.getMaxHealth()) {
         flag = false;
      }

      ServerLevel bowels = WitherStormMod.bowels((ServerLevel)this.entity.level());
      WitherStormBowelsManager manager = (WitherStormBowelsManager)bowels.getCapability(WitherStormModCapabilities.BOWELS_MANAGER).orElse(null);
      if (manager != null) {
         WitherStormBowelsManager.BowelsInstance instance = this.entity.getBowelsInstance();
         if (instance != null) {
            AABB box = new AABB(instance.getPos()).inflate(50.0);

            for (Player entityx : bowels.getEntitiesOfClass(Player.class, box)) {
               if (manager.getInstanceFromEntity(entityx) == instance) {
                  flag = false;
                  break;
               }
            }
         }
      }

      return flag;
   }

   protected boolean playerApplicable(Player player) {
      if (EntitySelector.NO_SPECTATORS.test(player) && EntitySelector.LIVING_ENTITY_STILL_ALIVE.test(player)) {
         if (player.position().subtract(this.entity.position()).horizontalDistance() > this.entity.getAttributeValue(Attributes.FOLLOW_RANGE)) {
            return false;
         } else {
            IItemHandler handler = (IItemHandler)player.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
            if (handler != null) {
               for (int i = 0; i < handler.getSlots(); i++) {
                  ItemStack stack = handler.getStackInSlot(i);
                  if (stack.is((Item)WitherStormModItems.COMMAND_BLOCK_BOOK.get())) {
                     return false;
                  }

                  if (stack.getItem() instanceof FormidibombItem) {
                     FormidibombItem item = (FormidibombItem)stack.getItem();
                     if (item.getStartFuse(stack) > 0) {
                        return false;
                     }
                  } else if (this.entity.getPhase() > 5 && stack.is(WitherStormModItemTags.COMMAND_BLOCK_TOOLS)) {
                     return false;
                  }
               }
            }

            PlayerWitherStormData data = (PlayerWitherStormData)player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).orElse(null);
            if (data != null) {
               if (data.hasKilledSymbiontRecently()) {
                  return false;
               }

               if (!data.hasChangedPhase(this.entity) && data.hasRecentlySummonedSymbiont(this.entity)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public void summonSymbiont(Player player) {
      float angle = -((float)Math.atan2(player.getX() - this.entity.getX(), player.getZ() - this.entity.getZ()));
      float spawnX = Mth.cos(angle) * 30.0F + (float)this.entity.getX();
      float spawnZ = Mth.sin(angle) * 30.0F + (float)this.entity.getZ();
      double y = this.entity.getEyeY();

      for (int i = 0; i < 10; i++) {
         int randomX = Mth.floor(spawnX) + (int)(10.0 * this.entity.getRandom().nextGaussian()) + 5;
         int randomZ = Mth.floor(spawnZ) + (int)(10.0 * this.entity.getRandom().nextGaussian()) + 5;
         int radius = 5;
         Integer highest = null;
         BlockPos pos = null;

         for (int ax = -radius; ax <= radius; ax++) {
            for (int az = -radius; az <= radius; az++) {
               int x = ax + randomX;
               int z = az + randomZ;
               int height = player.level().getHeight(Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
               if (highest == null || height > highest) {
                  highest = height;
                  pos = new BlockPos(x, highest, z);
               }
            }
         }

         BlockState state = this.entity.level().getBlockState(pos);
         if (state.isFaceSturdy(this.entity.level(), pos, Direction.UP) && state.isValidSpawn(this.entity.level(), pos, this.entity.getType())) {
            VoxelShape shape = state.getCollisionShape(this.entity.level(), pos);
            if (!shape.isEmpty()) {
               y = shape.max(Axis.Y) + (double)pos.getY();
            }

            if (NaturalSpawner.isSpawnPositionOk(
               Type.ON_GROUND,
               this.entity.level(),
               BlockPos.containing((double)pos.getX() + 0.5, y, (double)pos.getZ() + 0.5),
               WitherStormModEntityTypes.WITHERED_SYMBIONT.get()
            )) {
               WitheredSymbiontEntity entity = (WitheredSymbiontEntity)(WitherStormModEntityTypes.WITHERED_SYMBIONT.get())
                  .create(this.entity.level());
               entity.setPos((double)pos.getX() + 0.5, y, (double)pos.getZ() + 0.5);
               double deltaX = player.getX() - entity.getX();
               double deltaY = player.getEyeY() - entity.getEyeY();
               double deltaZ = player.getZ() - entity.getZ();
               double sqrt = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
               float yRot = (float)(Mth.atan2(deltaZ, deltaX) * 180.0F / (float)Math.PI) - 90.0F;
               float xRot = (float)(-(Mth.atan2(deltaY, sqrt) * 180.0F / (float)Math.PI));
               entity.yBodyRot = yRot;
               entity.setYRot(yRot);
               entity.setXRot(xRot);
               entity.setOwner(this.entity);
               this.entity.level().addFreshEntity(entity);
               ServerLevel world = (ServerLevel)this.entity.level();

               for (Player nearbyPlayers : world.getNearbyPlayers(TargetingConditions.DEFAULT, null, this.entity.getSearchBox())) {
                  CriteriaTriggers.SUMMONED_ENTITY.trigger((ServerPlayer)nearbyPlayers, entity);
               }

               ForgeEventFactory.onFinalizeSpawn(entity, world, world.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.TRIGGERED, null, null);
               entity.spawnAnim();
               world.sendParticles(
                  WitherStormModParticleTypes.COMMAND_BLOCK.get(),
                  entity.getX(),
                  entity.getEyeY(),
                  entity.getZ(),
                  20,
                  this.entity.getRandom().nextGaussian(),
                  this.entity.getRandom().nextGaussian(),
                  this.entity.getRandom().nextGaussian(),
                  0.2
               );
               world.sendParticles(
                  ParticleTypes.LARGE_SMOKE,
                  entity.getX(),
                  entity.getEyeY(),
                  entity.getZ(),
                  20,
                  this.entity.getRandom().nextGaussian(),
                  this.entity.getRandom().nextGaussian(),
                  this.entity.getRandom().nextGaussian(),
                  0.01
               );
               this.timeTillCanSummonSymbiont = Mth.clamp((Integer)WitherStormModConfig.SERVER.witherStormSummoningDelay.get(), 1, 20) * 1200
                  + this.entity.getRandom().nextInt(12000);
               player.getCapability(WitherStormModCapabilities.PLAYER_WITHER_STORM_DATA).ifPresent(data -> data.markSummonedSymbiont(this.entity));
               this.entity.playSound(WitherStormModSoundEvents.COMMAND_BLOCK_SUMMON.get(), 15.0F, 1.0F);
               entity.playSound(WitherStormModSoundEvents.WITHERED_SYMBIONT_SPAWN.get(), 12.0F, 1.0F);
               break;
            }
         }
      }
   }

   public int getSummoningDelay() {
      return this.timeTillCanSummonSymbiont;
   }

   public void setSummoningDelay(int delay) {
      this.timeTillCanSummonSymbiont = delay;
   }
}
