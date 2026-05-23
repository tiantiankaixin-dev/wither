package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT
import net.neoforged.neoforge.network.PacketDistributor;
// TODO_MIG[REMOVED_IMPORT]: // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.init.WitherStormModClientCapabilities;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.entity.FormidibombEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.PlayDeadManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.AdditionalHead;
import nonamecrackers2.witherstormmod.common.init.WitherStormModPacketHandlers;

public class WitherStormBodyController extends BodyRotationControl {
   protected final WitherStormEntity storm;
   protected float maxRotSpeed;
   protected boolean hasWanted;
   protected double wantedX;
   protected double wantedZ;
   protected int headStableTime;
   protected float lastStableYHeadRot;

   public WitherStormBodyController(WitherStormEntity storm) {
      super(storm);
      this.storm = storm;
   }

   public void clientTick() {
      if (!this.storm.level().isClientSide) {
         if (!this.storm.isNoAi() && !this.storm.isDeadOrPlayingDead()) {
            boolean ultimateTargetInUseBySegment = this.storm.getUltimateTarget() == null
               ? false
               : this.storm.targetInUseBySegment(this.storm.getUltimateTarget());
            boolean ultimateTargetInUseByHeads = this.storm.getUltimateTarget() == null
               ? false
               : this.storm.alreadyATarget(this.storm.getUltimateTarget(), false);
            FormidibombEntity formidibomb = this.storm.getFormidibomb();
            boolean formidibombNearby = this.storm.isNearbyTickingFormidibomb();
            if (this.storm.getPhase() > 3) {
               if (!ultimateTargetInUseBySegment && !ultimateTargetInUseByHeads && !formidibombNearby && this.storm.shouldRotateTowardsUltimateTarget()) {
                  Vec3 pos = this.storm.getUltimateTargetPos();
                  if (pos != null) {
                     this.setBodyAt(pos.x(), pos.z(), ((Double)WitherStormModConfig.SERVER.rotationSpeed.get()).floatValue());
                  }

                  if (this.hasWanted) {
                     this.storm.yBodyRot = this.rotateTowards(this.storm.yBodyRot, this.getYRotD(), this.maxRotSpeed);
                     this.rotateHeadIfNecessary();
                  }
               } else if (formidibombNearby) {
                  this.setBodyAt(formidibomb.getX(), formidibomb.getZ(), 0.1F);
                  if (this.hasWanted) {
                     this.storm.yBodyRot = this.rotateTowards(this.storm.yBodyRot, this.getYRotD(), this.maxRotSpeed);
                     this.rotateHeadIfNecessary();
                  }
               }
            } else {
               boolean flag = true;

               for (AdditionalHead head : this.storm.getHeadManager().getOtherHeads()) {
                  if (head.getTarget() != null) {
                     flag = false;
                     break;
                  }
               }

               if (flag && this.storm.getTarget() == null && this.isMoving()) {
                  Vec3 posx = this.storm.getUltimateTargetPos();
                  if (posx != null) {
                     this.setBodyAt(posx.x(), posx.z(), 5.0F);
                  }

                  if (this.hasWanted) {
                     this.storm.yBodyRot = this.rotateTowards(this.storm.yBodyRot, this.getYRotD(), this.maxRotSpeed);
                     this.rotateHeadIfNecessary();
                  }
               } else {
                  super.clientTick();
               }
            }
         }

         if (this.storm.canFallOnBack()) {
            if (this.storm.getPlayDeadManager().getState() == PlayDeadManager.State.PLAYING_DEAD) {
               if (this.storm.xBodyRot < 90.0F) {
                  this.storm.xBodyRot = this.storm.xBodyRot + this.storm.xBodyRot * 0.04F + 0.05F;
               }

               if (this.storm.xBodyRot > 90.0F) {
                  this.storm.xBodyRot = 90.0F;
                  this.storm.onFallOnBack();
               }
            } else if (!this.storm.isPlayingDead()) {
               WitherStormEntity var8 = this.storm;
               var8.xBodyRot = var8.xBodyRot + (-this.storm.xBodyRot * 0.015F - 0.02F);
               if (this.storm.xBodyRot < 0.0F) {
                  this.storm.xBodyRot = 0.0F;
               }
            }
         }

         WitherStormBodyController.UpdateBodyRotMessage message = new WitherStormBodyController.UpdateBodyRotMessage(this.storm);
         ResourceKey<Level> dimension = this.storm.level().dimension();
         WitherStormModPacketHandlers.MAIN.send(PacketDistributor.DIMENSION.with(() -> dimension), message);
      }
   }

   private void setBodyAt(double x, double z, float speed) {
      this.wantedX = x;
      this.wantedZ = z;
      this.maxRotSpeed = speed;
      this.hasWanted = true;
   }

   protected void rotateHeadIfNecessary() {
      this.storm.yHeadRot = Mth.rotateIfNecessary(this.storm.yHeadRot, this.storm.yBodyRot, (float)this.storm.getMaxHeadYRot());
   }

   protected float rotateTowards(float yRot, float newYRot, float speed) {
      float f = Mth.degreesDifference(yRot, newYRot);
      float f1 = Mth.clamp(f, -speed, speed);
      return yRot + f1;
   }

   protected float getYRotD() {
      double d0 = this.wantedX - this.storm.getX();
      double d1 = this.wantedZ - this.storm.getZ();
      return (float)(Mth.atan2(d1, d0) * 180.0F / (float)Math.PI) - 90.0F;
   }

   protected boolean isMoving() {
      double d0 = this.storm.getX() - this.storm.xo;
      double d1 = this.storm.getZ() - this.storm.zo;
      return d0 * d0 + d1 * d1 > 2.5000003E-7F;
   }

   public static class UpdateBodyRotMessage extends Packet {
      private float yBodyRot;
      private float xBodyRot;
      private int entityId;

      private UpdateBodyRotMessage(WitherStormEntity entity) {
         super(true);
         this.yBodyRot = entity.yBodyRot;
         this.xBodyRot = entity.xBodyRot;
         this.entityId = entity.getId();
      }

      public UpdateBodyRotMessage() {
         super(false);
      }

      public void decode(FriendlyByteBuf buffer) {
         this.yBodyRot = buffer.readFloat();
         this.xBodyRot = buffer.readFloat();
         this.entityId = buffer.readVarInt();
      }

      public void encode(FriendlyByteBuf buffer) {
         buffer.writeFloat(this.yBodyRot);
         buffer.writeFloat(this.xBodyRot);
         buffer.writeVarInt(this.entityId);
      }

      public Runnable getProcessor(Context context) {
         return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> processMessage(this));
      }

      public String toString() {
         return "UpdateBodyRotMessage[yBodyRot=" + this.yBodyRot + ", xBodyRot=" + this.xBodyRot + "]";
      }

      public static void processMessage(WitherStormBodyController.UpdateBodyRotMessage message) {
         Minecraft mc = Minecraft.getInstance();
         ClientLevel world = mc.level;
         WitherStormEntity entity = (WitherStormEntity)mc.level.getEntity(message.entityId);
         if (entity != null) {
            entity.lerpBodyRotationTo(message.xBodyRot, message.yBodyRot, 3);
         }

         world.getCapability(WitherStormModClientCapabilities.DISTANT_RENDERER).ifPresent(distantRenderer -> {
            WitherStormEntity distantEntity = distantRenderer.get(message.entityId);
            if (distantEntity != null) {
               distantEntity.lerpBodyRotationTo(message.xBodyRot, message.yBodyRot, 3);
            }
         });
      }
   }
}
