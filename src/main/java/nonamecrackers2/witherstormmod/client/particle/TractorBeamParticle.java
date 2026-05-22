package nonamecrackers2.witherstormmod.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.api.common.entity.WitherStormBase;
import nonamecrackers2.witherstormmod.common.particle.TractorBeamParticleOptions;
import nonamecrackers2.witherstormmod.common.util.TractorBeamHelper;

public class TractorBeamParticle extends SimpleAnimatedParticle {
   private final int storm;
   private final int head;
   private Vec3 pos;

   public TractorBeamParticle(int storm, int head, ClientLevel world, double x, double y, double z, double dX, double dY, double dZ, SpriteSet sprite) {
      super(world, x, y, z, sprite, 0.0F);
      this.storm = storm;
      this.head = head;
      this.xd = dX;
      this.yd = dY;
      this.zd = dZ;
      this.friction = 1.0F;
      this.lifetime = 20 + this.random.nextInt(30);
      this.quadSize = 0.1F;
      this.hasPhysics = false;
      this.pos = new Vec3(this.x, this.y, this.z);
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_LIT;
   }

   public void tick() {
      super.tick();
      this.pos = new Vec3(this.x, this.y, this.z);
      if (!this.canExist()) {
         this.remove();
      }
   }

   public <T extends LivingEntity & WitherStormBase> boolean canExist() {
      Entity entity = this.level.getEntity(this.storm);
      if (entity instanceof LivingEntity && entity instanceof WitherStormBase) {
         T storm = (T)entity;
         if (TractorBeamHelper.isInsideTractorBeam(this.pos, storm, 4.0, this.head)) {
            return true;
         }
      }

      return false;
   }

   public static class Factory implements ParticleProvider<TractorBeamParticleOptions> {
      private final SpriteSet sprites;

      public Factory(SpriteSet sprites) {
         this.sprites = sprites;
      }

      public Particle createParticle(TractorBeamParticleOptions options, ClientLevel world, double x, double y, double z, double dX, double dY, double dZ) {
         TractorBeamParticle particle = new TractorBeamParticle(options.storm(), options.head(), world, x, y, z, dX, dY, dZ, this.sprites);
         particle.pickSprite(this.sprites);
         return particle;
      }
   }
}
