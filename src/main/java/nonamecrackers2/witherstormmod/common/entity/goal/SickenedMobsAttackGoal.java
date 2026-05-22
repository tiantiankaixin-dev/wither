package nonamecrackers2.witherstormmod.common.entity.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import nonamecrackers2.witherstormmod.common.entity.TentacleEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormHeadEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public class SickenedMobsAttackGoal extends NearestAttackableTargetGoal<Mob> {
   public SickenedMobsAttackGoal(Mob mob) {
      super(
         mob,
         Mob.class,
         10,
         true,
         false,
         entity -> !(entity instanceof WitherSickened)
               && !(entity instanceof WitheredSymbiontEntity)
               && !(entity instanceof WitherStormEntity)
               && !(entity instanceof WitherStormHeadEntity)
               && !(entity instanceof TentacleEntity)
               && !(entity instanceof WitherBoss)
               && !(entity instanceof WitherSkeleton)
               && !(entity instanceof Creeper)
               && !(entity instanceof EnderMan)
               && (
                  entity instanceof AbstractVillager
                     || entity instanceof AbstractGolem
                     || entity instanceof Monster
                     || entity instanceof Slime
                     || entity instanceof Bat
                     || entity instanceof Animal
                     || entity instanceof NeutralMob
                     || entity instanceof Player
               )
      );
   }
}
