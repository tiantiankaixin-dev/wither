package nonamecrackers2.witherstormmod.common.world.tainting;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.init.WitherStormModParticleTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.resources.BlockTainting;
import nonamecrackers2.witherstormmod.common.resources.MobConversions;
import nonamecrackers2.witherstormmod.common.resources.taint.MobConversion;
import nonamecrackers2.witherstormmod.common.resources.taint.TaintRecipe;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldTainting {
   private static final Logger LOGGER = LogManager.getLogger("witherstormmod/WorldTainting");
   @Nullable
   public static WorldTainting instance;
   private final BlockTainting blockTainting = new BlockTainting();
   private final MobConversions mobConversions = new MobConversions();

   private void addReloadListeners(Consumer<PreparableReloadListener> addListener) {
      addListener.accept(this.blockTainting);
      addListener.accept(this.mobConversions);
   }

   public Map<ResourceLocation, TaintRecipe> getBlockTaintingRecipes() {
      return this.blockTainting.getRecipes();
   }

   public Map<ResourceLocation, MobConversion> getMobConversions() {
      return this.mobConversions.getConversions();
   }

   public Optional<MobConversion> getMobConversionFor(EntityType<?> type) {
      return this.getMobConversions().values().stream().filter(c -> c.from().equals(type)).findFirst();
   }

   @Nullable
   public EntityType<?> getOriginalTypeFromConvertedType(EntityType<?> converted) {
      return this.getMobConversions().values().stream().filter(c -> c.to().equals(converted)).findFirst().orElseThrow().from();
   }

   public boolean canConvertBlock(BlockPos pos, Level level) {
      for (TaintRecipe recipe : this.getBlockTaintingRecipes().values()) {
         if (recipe.canConvertBlock(level.getBlockState(pos))) {
            return true;
         }
      }

      return false;
   }

   public int convertBlocks(BoundingBox box, Level level) {
      return this.convertBlocks(box, level, (Predicate<TaintRecipe>)(r -> true));
   }

   public int convertBlocks(BoundingBox box, Level level, MobEffect effect) {
      return this.convertBlocks(box, level, (Predicate<TaintRecipe>)(r -> r.effect() == effect));
   }

   public int convertBlocks(BoundingBox box, Level level, Potion potion) {
      return this.convertBlocks(box, level, (Predicate<TaintRecipe>)(r -> r.canConvertWithPotion(potion)));
   }

   public int convertBlocks(BoundingBox box, Level level, Predicate<TaintRecipe> canConvert) {
      MutableInt count = new MutableInt();
      BlockPos.betweenClosedStream(box).forEach(pos -> {
         for (TaintRecipe recipe : getInstance().getBlockTaintingRecipes().values()) {
            BlockState state = level.getBlockState(pos);
            if (canConvert.test(recipe) && recipe.canConvertBlock(state)) {
               BlockState replacement = recipe.replacement();

               for (Property<?> property : recipe.propertiesToCopy()) {
                  replacement = copyProperty(property, state, replacement);
               }

               if (level.setBlock(pos, replacement, 3, 0)) {
                  count.increment();
                  break;
               }
            }
         }
      });
      return count.getValue();
   }

   public boolean convertBlock(BlockPos pos, Level level) {
      for (TaintRecipe recipe : getInstance().getBlockTaintingRecipes().values()) {
         BlockState state = level.getBlockState(pos);
         if (recipe.canConvertBlock(state)) {
            BlockState replacement = recipe.replacement();

            for (Property<?> property : recipe.propertiesToCopy()) {
               replacement = copyProperty(property, state, replacement);
            }

            return level.setBlock(pos, replacement, 3);
         }
      }

      return false;
   }

   private static <T extends Comparable<T>> BlockState copyProperty(Property<T> property, BlockState from, BlockState to) {
      return from.hasProperty(property) ? (BlockState)to.setValue(property, from.getValue(property)) : to;
   }

   public boolean canConvertMob(Entity entity, boolean fromWitherSickness) {
      if (entity instanceof Mob) {
         MutableBoolean result = new MutableBoolean();
         this.getMobConversionFor(entity.getType())
            .ifPresent(conversion -> result.setValue(conversion.canBeConvertedFromWitherSickness() || !fromWitherSickness));
         return result.getValue();
      } else {
         return false;
      }
   }

   public boolean convertMob(Mob mob, boolean fromWitherSickness) {
      EntityType<?> type = mob.getType();
      MutableBoolean result = new MutableBoolean();
      this.getMobConversionFor(type)
         .ifPresent(
            conversion -> {
               if (conversion.canBeConvertedFromWitherSickness() || !fromWitherSickness) {
                  CompoundTag data = mob.serializeNBT();

                  try {
                     @SuppressWarnings("unchecked") Mob converted = (Mob)mob.convertTo((EntityType<? extends Mob>)conversion.to(), true);
                     if (converted != null) {
                        copyExtraData(mob, converted);
                        if (converted instanceof WitherSickened sickened) {
                           sickened.getData().setOriginal(type, data);
                           sickened.convertFrom(mob);
                        }

                        converted.playSound(WitherStormModSoundEvents.MOB_INFECTED.get());
                        ServerLevel level = (ServerLevel)converted.level();
                        AABB box = converted.getBoundingBox();
                        level.sendParticles(
                           WitherStormModParticleTypes.PHLEGM.get(),
                           box.getCenter().x,
                           box.getCenter().y,
                           box.getCenter().z,
                           10,
                           box.getXsize() / 2.0,
                           box.getYsize() / 2.0,
                           box.getZsize() / 2.0,
                           0.05
                        );
                        result.setTrue();
                     }
                  } catch (ClassCastException var9) {
                     LOGGER.warn(
                        "Invalid mob conversion! Cannot convert to {} since it is not a valid mob. Please fix your mob conversion: {}",
                        conversion.to(),
                        conversion
                     );
                  }
               }
            }
         );
      return result.getValue();
   }

   public static void copyExtraData(Mob from, Mob to) {
      to.yBodyRot = from.yBodyRot;
      to.yHeadRot = from.yHeadRot;
   }

   public static void initializeOrAddListeners(Consumer<PreparableReloadListener> addListener) {
      if (instance == null) {
         instance = new WorldTainting();
      }

      instance.addReloadListeners(addListener);
   }

   public static WorldTainting getInstance() {
      return Objects.requireNonNull(instance, "WorldTainting has not been initialized yet");
   }
}
