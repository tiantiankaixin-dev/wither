package nonamecrackers2.witherstormmod.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class PhlegmBlockParticle extends SimpleAnimatedParticle {
   public PhlegmBlockParticle(ClientLevel world, double x, double y, double z, double dX, double dY, double dZ, SpriteSet sprite) {
      super(world, x, y, z, sprite, 0.0F);
      this.xd = dX;
      this.yd = dY;
      this.zd = dZ;
      this.lifetime = 45;
      this.quadSize = 0.1F * this.random.nextFloat();
      this.hasPhysics = false;
   }

   public ParticleRenderType getRenderType() {
      return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
   }

   public static class Factory implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprites;

      public Factory(SpriteSet sprites) {
         this.sprites = sprites;
      }

      public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double dX, double dY, double dZ) {
         PhlegmBlockParticle particle = new PhlegmBlockParticle(world, x, y, z, dX, dY, dZ, this.sprites);
         particle.pickSprite(this.sprites);
         return particle;
      }
   }
}
