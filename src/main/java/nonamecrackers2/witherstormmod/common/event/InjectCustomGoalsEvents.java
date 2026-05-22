package nonamecrackers2.witherstormmod.common.event;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.entity.goal.AvoidWitherStormGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.FightSickenedMobsGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.NearestAttackingWitherStormGoal;
import nonamecrackers2.witherstormmod.common.entity.goal.PhantomOrbitWitherStormGoal;
import nonamecrackers2.witherstormmod.common.predicate.EntityPredicateBuilder;
import nonamecrackers2.witherstormmod.mixin.IMixinBrain;

public class InjectCustomGoalsEvents {
   public static final Predicate<LivingEntity> CAN_RUN_AWAY_FROM_WITHER_STORM = ((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.and()
                  .isNotInstanceOf(WitherSickened.class))
               .isNotInstanceOf(WitherSkeleton.class))
            .addTest(
               ((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.or()
                                       .isInstanceOf(Zombie.class))
                                    .isInstanceOf(Spider.class))
                                 .isInstanceOf(AbstractSkeleton.class))
                              .isInstanceOf(Creeper.class))
                           .isInstanceOf(AbstractPiglin.class))
                        .isInstanceOf(Pillager.class))
                     .isInstanceOf(Animal.class))
                  .build()
            ))
         .addTest(e -> ((IMixinBrain)((LivingEntity)e).getBrain()).getCoreActivities().isEmpty()))
      .build();
   public static final Predicate<LivingEntity> CAN_ATTACK_WITHER_STORM_BACK = ((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.and()
               .isNotInstanceOf(WitherSickened.class))
            .isNotInstanceOf(WitherSkeleton.class))
         .addTest(
            ((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.or().isInstanceOf(Creeper.class)).isInstanceOf(AbstractSkeleton.class))
               .build()
         ))
      .build();
   public static final Predicate<LivingEntity> CAN_ATTACK_SICKENED_MOBS = ((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.and()
               .isNotInstanceOf(WitherSickened.class))
            .isNotInstanceOf(WitherSkeleton.class))
         .addTest(
            ((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)((EntityPredicateBuilder)EntityPredicateBuilder.or()
                                                         .isInstanceOf(Shulker.class))
                                                      .isInstanceOf(Evoker.class))
                                                   .isInstanceOf(Vex.class))
                                                .isInstanceOf(Witch.class))
                                             .isInstanceOf(Ravager.class))
                                          .isInstanceOf(Llama.class))
                                       .isInstanceOf(Vindicator.class))
                                    .isInstanceOf(ZombifiedPiglin.class))
                                 .isInstanceOf(AbstractPiglin.class))
                              .isInstanceOf(Hoglin.class))
                           .isInstanceOf(Silverfish.class))
                        .isInstanceOf(Blaze.class))
                     .isInstanceOf(Wolf.class))
                  .isInstanceOf(Bee.class))
               .build()
         ))
      .build();

   @SubscribeEvent
   public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
      Level world = event.getLevel();
      if (!world.isClientSide && (Boolean)WitherStormModConfig.COMMON.injectCustomAiBehavior.get()) {
         Entity entity = event.getEntity();
         if (!((List)WitherStormModConfig.COMMON.injectAiMobBlacklist.get()).contains(entity.getEncodeId())) {
            if (entity.getType().equals(EntityType.PHANTOM)
               && entity instanceof Phantom phantom
               && (Boolean)WitherStormModConfig.COMMON.phantomsOrbitWitherStorm.get()) {
               phantom.goalSelector.removeAllGoals(g -> g instanceof PhantomOrbitWitherStormGoal);
               phantom.goalSelector.addGoal(1, new PhantomOrbitWitherStormGoal(phantom));
            }

            if (entity instanceof PathfinderMob mob) {
               if (CAN_RUN_AWAY_FROM_WITHER_STORM.test(mob)) {
                  mob.goalSelector.removeAllGoals(g -> g instanceof AvoidWitherStormGoal);
                  mob.goalSelector.addGoal(0, new AvoidWitherStormGoal(mob, 300.0F, 1.55, 1.55));
               }

               if (CAN_ATTACK_WITHER_STORM_BACK.test(mob)) {
                  mob.targetSelector.removeAllGoals(g -> g instanceof NearestAttackingWitherStormGoal);
                  mob.targetSelector.addGoal(0, new NearestAttackingWitherStormGoal(mob, 10));
               }

               if (CAN_ATTACK_SICKENED_MOBS.test(mob)) {
                  mob.targetSelector.removeAllGoals(g -> g instanceof FightSickenedMobsGoal);
                  mob.targetSelector.addGoal(-1, new FightSickenedMobsGoal(mob));
               }
            }
         }
      }
   }
}
