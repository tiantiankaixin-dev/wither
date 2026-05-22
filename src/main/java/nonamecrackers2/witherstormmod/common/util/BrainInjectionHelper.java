package nonamecrackers2.witherstormmod.common.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.ai.behavior.VillagerCalmDownFromWitherStorm;
import nonamecrackers2.witherstormmod.common.entity.ai.behavior.WitherStormPanicTrigger;
import nonamecrackers2.witherstormmod.common.init.WitherStormModActivities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMemoryTypes;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSensorTypes;
import nonamecrackers2.witherstormmod.mixin.IMixinBrain;

public class BrainInjectionHelper {
   public static void inject(LivingEntity entity) {
      if ((Boolean)WitherStormModConfig.COMMON.injectCustomAiBehavior.get() && entity.getType().equals(EntityType.VILLAGER)) {
         Villager villager = (Villager)entity;
         Brain<Villager> brain = villager.getBrain();
         if (brain != null) {
            addMemoryType(villager, (MemoryModuleType<?>)WitherStormModMemoryTypes.NEAREST_WITHER_STORM.get());
            addSensorType(villager, (SensorType)WitherStormModSensorTypes.WITHER_STORM_SENSOR.get());
            addToActivity(villager, Activity.CORE, ImmutableList.of(Pair.of(-1, new WitherStormPanicTrigger())));
            brain.addActivityAndRemoveMemoryWhenStopped(
               (Activity)WitherStormModActivities.WITHER_STORM_PANIC.get(),
               0,
               ImmutableList.<net.minecraft.world.entity.ai.behavior.BehaviorControl<? super net.minecraft.world.entity.npc.Villager>>of(
                  new VillagerCalmDownFromWitherStorm(),
                  SetWalkTargetAwayFrom.entity((MemoryModuleType)WitherStormModMemoryTypes.NEAREST_WITHER_STORM.get(), 0.75F, 300, true),
                  RandomStroll.stroll(1.0F),
                  new RunOne(
                     ImmutableList.<com.mojang.datafixers.util.Pair<? extends net.minecraft.world.entity.ai.behavior.BehaviorControl<? super net.minecraft.world.entity.npc.Villager>, Integer>>of(
                        Pair.of(SetEntityLookTarget.create(EntityType.VILLAGER, 8.0F), 2),
                        Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2),
                        Pair.of(new DoNothing(30, 60), 8)
                     )
                  )
               ),
               (MemoryModuleType)WitherStormModMemoryTypes.NEAREST_WITHER_STORM.get()
            );
         }
      }
   }

   private static <E extends LivingEntity> void addToActivity(
      E entity, Activity activity, ImmutableList<? extends Pair<Integer, ? extends Behavior<? super E>>> behaviors
   ) {
      @SuppressWarnings("unchecked") Brain<E> brain = (Brain<E>)entity.getBrain();
      IMixinBrain<E> mixinBrain = (IMixinBrain<E>)brain;
      Set<Pair<MemoryModuleType<?>, MemoryStatus>> previousRequirements = mixinBrain.getActivityRequirements().get(activity);
      Set<MemoryModuleType<?>> previousMemoriesToErase = mixinBrain.getActivityMemoriesToEraseWhenStopped().getOrDefault(activity, Sets.newHashSet());
      brain.addActivityAndRemoveMemoriesWhenStopped(activity, behaviors, previousRequirements, previousMemoriesToErase);
   }

   private static <E extends LivingEntity> IMixinBrain<E> toMixinBrain(E entity) {
      return (IMixinBrain<E>)entity.getBrain();
   }

   private static <E extends LivingEntity> void addMemoryType(E entity, MemoryModuleType<?> type) {
      IMixinBrain<E> brain = toMixinBrain(entity);
      brain.getMemories().put(type, Optional.empty());
   }

   private static <E extends LivingEntity> void addSensorType(E entity, SensorType<? extends Sensor<? super E>> type) {
      IMixinBrain<E> brain = toMixinBrain(entity);
      Sensor<? super E> sensor = type.create();
      brain.getSensors().put(type, sensor);

      for (MemoryModuleType<?> memory : sensor.requires()) {
         brain.getMemories().put(memory, Optional.empty());
      }
   }
}
