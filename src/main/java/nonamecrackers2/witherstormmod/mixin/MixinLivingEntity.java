/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.boss.wither.WitherBoss
 *  net.minecraft.world.entity.item.ItemEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  nonamecrackers2.witherstormmod.common.accessor.LivingEntityAccessor
 *  nonamecrackers2.witherstormmod.common.entity.WitherSickened
 *  nonamecrackers2.witherstormmod.common.entity.WitherStormEntity
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers
 *  nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes
 *  nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags
 *  nonamecrackers2.witherstormmod.common.util.BrainInjectionHelper
 *  nonamecrackers2.witherstormmod.common.util.PhlegmGravestoneHelper
 *  org.apache.commons.lang3.mutable.MutableBoolean
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package nonamecrackers2.witherstormmod.mixin;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.accessor.LivingEntityAccessor;
import nonamecrackers2.witherstormmod.common.entity.WitherSickened;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCriteriaTriggers;
import nonamecrackers2.witherstormmod.common.init.WitherStormModMobTypes;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;
import nonamecrackers2.witherstormmod.common.util.BrainInjectionHelper;
import nonamecrackers2.witherstormmod.common.util.PhlegmGravestoneHelper;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public abstract class MixinLivingEntity
extends Entity
implements LivingEntityAccessor {
    @Unique
    private boolean hasDeathProtection;

    private MixinLivingEntity() {
        super(null, null);
        throw new UnsupportedOperationException();
    }

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    public void constructorTail(EntityType<? extends LivingEntity> type, Level level, CallbackInfo ci) {
        BrainInjectionHelper.inject((LivingEntity)((LivingEntity)(Object)this));
    }

    @Inject(method={"dropAllDeathLoot"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;dropExperience()V")}, cancellable=true)
    public void witherstormmod$preventDrops_dropAllDeathLoot(DamageSource source, CallbackInfo ci) {
        MutableBoolean flag = new MutableBoolean();
        PhlegmGravestoneHelper.findPotentialPhlegmClusterPos((LivingEntity)((LivingEntity)(Object)this), (DamageSource)source).ifPresent(pos -> {
            List<ItemStack> items = this.captureDrops().stream().map(ItemEntity::getItem).toList();
            if (!items.isEmpty()) {
                PhlegmGravestoneHelper.spawnForEntity((LivingEntity)((LivingEntity)(Object)this), (Vec3)pos, items);
                this.captureDrops(null);
                flag.setTrue();
            }
        });
        if (flag.getValue().booleanValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"checkTotemDeathProtection"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;setHealth(F)V")})
    public void witherstormmod$setHasDeathProtection_checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> ci) {
        this.hasDeathProtection = true;
    }

    @Inject(method={"checkTotemDeathProtection"}, at={@At(value="TAIL")}, cancellable=true)
    public void witherstormmod$evolveWitherStormIfDying_checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> ci) {
        float health;
        WitherStormEntity storm;
        LivingEntity mixinLivingEntity = (LivingEntity)(Object)this;
        if (mixinLivingEntity instanceof WitherStormEntity && (storm = (WitherStormEntity)mixinLivingEntity).isCompletelyInvulnerable() && storm.getPhase() < 4 && (health = storm.getHealth() / storm.getMaxHealth()) <= 0.1f) {
            storm.evolveToPhase(4);
            storm.setHealth(storm.getMaxHealth());
            ci.setReturnValue(true);
            Entity entity = damageSource.getEntity();
            if (entity instanceof ServerPlayer) {
                ServerPlayer player = (ServerPlayer)entity;
                WitherStormModCriteriaTriggers.NEARLY_KILL_WITHER_STORM.trigger(player, storm);
            }
        }
    }

    @Inject(method={"removeAllEffects"}, at={@At(value="RETURN")})
    public void witherstormmod$resetHasDeathProtection_removeAllEffects(CallbackInfoReturnable<Boolean> ci) {
        if (!this.level().isClientSide) {
            this.hasDeathProtection = false;
        }
    }

    @Inject(method={"canAttack"}, at={@At(value="HEAD")}, cancellable=true)
    public void witherstormmod$preventCertainMobsFromAttackingSickenedMobs_canAttack(LivingEntity entity, CallbackInfoReturnable<Boolean> ci) {
        if (((Object)this) instanceof WitherBoss && (entity.getType().is(WitherStormModEntityTags.SICKENED_MOBS) || entity instanceof WitherSickened || WitherStormModMobTypes.isSickened(entity))) {
            ci.setReturnValue(false);
        }
    }

    public void setHasDeathProtection(boolean flag) {
        this.hasDeathProtection = flag;
    }

    public boolean hasDeathProtection() {
        return this.hasDeathProtection;
    }
}

