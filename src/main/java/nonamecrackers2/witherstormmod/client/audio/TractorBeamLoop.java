package nonamecrackers2.witherstormmod.client.audio;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;

public class TractorBeamLoop<T extends LivingEntity> extends AbstractTickableSoundInstance implements IForceStoppableSound {
   public static double DISTANCE_REQUIRED = 30.0;
   protected final T entity;
   protected final LocalPlayer player;
   private double prevX;
   private double prevY;
   private double prevZ;
   @Nullable
   private final Predicate<T> shouldStop;

   public TractorBeamLoop(T entity, Predicate<T> shouldStop) {
      super(WitherStormModSoundEvents.WITHER_STORM_TRACTOR_BEAM.get(), SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
      this.entity = entity;
      Minecraft mc = Minecraft.getInstance();
      this.player = mc.player;
      this.looping = true;
      this.delay = 0;
      this.volume = 0.0F;
      this.shouldStop = shouldStop;
   }

   public TractorBeamLoop(T entity) {
      this(entity, null);
   }

   public void tick() {
      this.prevX = this.x;
      this.prevY = this.y;
      this.prevZ = this.z;
      Vec3 closest = this.calculateClosestPoint();
      if (closest != null) {
         this.x = Mth.lerp(0.1, this.prevX, closest.x);
         this.y = Mth.lerp(0.1, this.prevY, closest.y);
         this.z = Mth.lerp(0.1, this.prevZ, closest.z);
         this.calculateVolume();
      }

      double distance = Math.sqrt(this.player.distanceToSqr(closest));
      if (this.shouldStop(distance)) {
         this.stop();
      }
   }

   public void setPos(Vec3 vec) {
      this.prevX = vec.x;
      this.prevY = vec.y;
      this.prevZ = vec.z;
      this.x = vec.x;
      this.y = vec.y;
      this.z = vec.z;
   }

   protected Vec3 calculateClosestPoint() {
      return TractorBeamHelper.calculateClosestPoint(this.player, this.entity);
   }

   protected float getDistance(Vec3 origin) {
      float x = (float)(origin.x - this.player.getX());
      float y = (float)(origin.y - this.player.getY());
      float z = (float)(origin.z - this.player.getZ());
      return Mth.sqrt(x * x + y * y + z * z);
   }

   @Override
   public void forceStop() {
      this.stop();
   }

   public T getEntity() {
      return this.entity;
   }

   public boolean shouldStop(double distance) {
      return (this.entity instanceof WitherStormBase storm ? storm.isDeadOrPlayingDead() : this.entity.isDeadOrDying())
         || !this.entity.isAlive()
         || distance > DISTANCE_REQUIRED
         || this.shouldStop != null && this.shouldStop.test(this.entity);
   }

   public void calculateVolume() {
      this.volume = Math.max(0.0F, 0.3F - this.getDistance(new Vec3(this.x, this.y, this.z)) / 60.0F);
   }

   public boolean canStartSilent() {
      return true;
   }
}
