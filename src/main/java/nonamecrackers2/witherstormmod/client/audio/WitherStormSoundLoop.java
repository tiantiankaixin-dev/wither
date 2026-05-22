package nonamecrackers2.witherstormmod.client.audio;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class WitherStormSoundLoop extends FadingSoundLoop implements IForceStoppableSound {
   public Vec3 pos;
   public Vec3 prevPos;
   public final SoundEvent soundevent;
   protected Optional<WitherStormEntity> entity;

   public WitherStormSoundLoop(@Nullable WitherStormEntity entity, Vec3 pos, SoundEvent event) {
      super(event, SoundSource.AMBIENT);
      this.pos = pos;
      this.prevPos = pos;
      this.x = pos.x;
      this.y = pos.y;
      this.z = pos.z;
      this.soundevent = event;
      this.entity = Optional.ofNullable(entity);
   }

   public WitherStormSoundLoop(@Nonnull WitherStormEntity entity, SoundEvent event) {
      this(entity, entity.position(), event);
   }

   public WitherStormSoundLoop(Vec3 pos, SoundEvent event) {
      this(null, pos, event);
   }

   public Vec3 getPos() {
      return this.pos;
   }

   public SoundEvent getSoundEvent() {
      return this.soundevent;
   }

   @Override
   public void tick() {
      LocalPlayer player = Minecraft.getInstance().player;
      double distance = Math.sqrt(player.distanceToSqr(this.pos));
      if (distance < 1000.0) {
         float dampenAmount;
         if ((Boolean)WitherStormModConfig.SERVER.occludeSoundsUnderground.get()) {
            boolean cave = !WorldUtil.isInAnOpenArea(player);
            dampenAmount = cave ? 30.0F + Mth.clamp((float)(-player.getY()) + 40.0F, 0.0F, 20.0F) : 15.0F;
         } else {
            dampenAmount = 15.0F;
         }

         BlockHitResult ray = player.level().clip(new ClipContext(this.pos, player.position(), Block.COLLIDER, Fluid.ANY, null));
         if (ray.getType() == Type.BLOCK && this.dampen < dampenAmount) {
            this.dampen++;
         } else if (ray.getType() == Type.BLOCK && this.dampen > dampenAmount) {
            this.dampen--;
         } else if (ray.getType() == Type.MISS && this.dampen > 0.0F) {
            this.dampen--;
         }
      } else {
         this.dampen = 0.0F;
      }

      this.x = this.pos.x;
      this.y = this.pos.y;
      this.z = this.pos.z;
      this.entity.ifPresent(entity -> {
         if (entity.isDeadOrDying() || !entity.isAddedToWorld()) {
            this.stopSound();
         }
      });
      super.tick();
   }

   @Override
   public void forceStop() {
      this.stop();
   }

   @Override
   protected int getFadeTime() {
      return 40;
   }
}
