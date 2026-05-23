package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.WitherStormSegmentEntity;
import nonamecrackers2.witherstormmod.common.serializer.WitherStormModDataSerializers;
import nonamecrackers2.witherstormmod.common.util.HeadConfiguration;

public class HeadManager {
   public static final int TOTAL_HEADS = 3;
   public static final List<EntityDataAccessor<Boolean>> HEAD_ROARS = createDataAccessors(
      () -> WitherStormEntity.registerDataAccessor(EntityDataSerializers.BOOLEAN, () -> false)
   );
   public static final List<EntityDataAccessor<Boolean>> HEADS_BITING = createDataAccessors(
      () -> WitherStormEntity.registerDataAccessor(EntityDataSerializers.BOOLEAN, () -> false)
   );
   public static final List<EntityDataAccessor<Optional<Vec3>>> TARGETS = createDataAccessors(
      () -> WitherStormEntity.registerDataAccessor(WitherStormModDataSerializers.OPTIONAL_VECTOR_3D, Optional::empty)
   );
   public static final List<EntityDataAccessor<Integer>> HURT_HEAD_TIME = createDataAccessors(
      () -> WitherStormEntity.registerDataAccessor(EntityDataSerializers.INT, () -> 0)
   );
   public static final List<EntityDataAccessor<Integer>> INJURE_ATTEMPT_COOLDOWN = createDataAccessors(
      () -> WitherStormEntity.registerDataAccessor(EntityDataSerializers.INT, () -> 0)
   );
   public static final List<EntityDataAccessor<Integer>> LOOK_STEPS = createDataAccessors(
      () -> WitherStormEntity.registerDataAccessor(EntityDataSerializers.INT, () -> 3)
   );
   public static final EntityDataAccessor<Integer> INJURY_TIME = WitherStormEntity.registerDataAccessor(EntityDataSerializers.INT, () -> 320);
   private static final EntityDataAccessor<Boolean> OTHER_HEADS_DISABLED = WitherStormEntity.registerDataAccessor(EntityDataSerializers.BOOLEAN, () -> false);
   private final List<HeadConfiguration> configurations;
   private final WitherStormEntity storm;
   private final Int2ObjectMap<WitherStormHead> heads = new Int2ObjectOpenHashMap();

   public HeadManager(WitherStormEntity storm, List<HeadConfiguration> configurations) {
      this.storm = storm;
      if (configurations.isEmpty()) {
         throw new IllegalArgumentException("Head configurations cannot be empty!");
      } else {
         this.configurations = configurations;
         this.heads.put(0, new MainHead(storm, 0));

         for (int i = 1; i < storm.getTotalHeads(); i++) {
            this.heads.put(i, new AdditionalHead(storm, i));
         }
      }
   }

   private static <T> List<EntityDataAccessor<T>> createDataAccessors(Supplier<EntityDataAccessor<T>> registrar) {
      Builder<EntityDataAccessor<T>> list = ImmutableList.builderWithExpectedSize(3);

      for (int i = 0; i < 3; i++) {
         list.add(registrar.get());
      }

      return list.build();
   }

   public void update(int phase) {
      if (phase > 3) {
         this.setHeadInjuryTime(720);
      } else {
         this.setHeadInjuryTime(180);
      }

      this.heads.forEach((i, head) -> head.update(phase));
   }

   public void baseTick() {
      this.heads.forEach((i, head) -> head.baseTick(this.findCurrentConfig()));
   }

   public void tick() {
      this.heads.forEach((i, head) -> head.tick());
   }

   public void aiStep() {
      this.heads.forEach((i, head) -> head.doAi());
   }

   public void customServerAiStep() {
      this.heads.forEach((i, head) -> head.doServerAi());
   }

   private HeadConfiguration findCurrentConfig() {
      for (HeadConfiguration configuration : this.configurations) {
         if (configuration.predicate().test(this.storm)) {
            return configuration;
         }
      }

      return this.configurations.get(0);
   }

   public WitherStormHead getHead(int index) {
      return Objects.requireNonNull((WitherStormHead)this.heads.get(index), "No head exists with index " + index);
   }

   public List<WitherStormHead> getHeads() {
      return ImmutableList.copyOf(this.heads.values());
   }

   public List<AdditionalHead> getOtherHeads() {
      return this.heads.values().stream().filter(h -> h instanceof AdditionalHead).map(h -> (AdditionalHead)h).toList();
   }

   public HeadManager.PackedHeadRots packHeadRotations() {
      byte[] xHeadsRot = new byte[this.heads.size()];
      byte[] yHeadsRot = new byte[this.heads.size()];

      for (WitherStormHead head : this.getHeads()) {
         if (head.syncHeadRotations()) {
            xHeadsRot[head.getIndex()] = (byte)Mth.floor(head.getHeadXRot() * 256.0F / 360.0F);
            yHeadsRot[head.getIndex()] = (byte)Mth.floor(head.getHeadYRot() * 256.0F / 360.0F);
         }
      }

      return new HeadManager.PackedHeadRots(xHeadsRot, yHeadsRot);
   }

   public void updateHeadsFromPacked(HeadManager.PackedHeadRots rots) {
      for (int i = 0; i < this.heads.size(); i++) {
         WitherStormHead head = (WitherStormHead)this.heads.get(i);
         if (head.syncHeadRotations()) {
            float xRot = HeadManager.PackedHeadRots.unpack(rots.xRots[i]);
            float yRot = HeadManager.PackedHeadRots.unpack(rots.yRots[i]);
            head.setHeadXRot(xRot);
            head.setHeadYRot(yRot);
         }
      }
   }

   public boolean areOtherHeadsDisabled() {
      return (Boolean)this.storm.getEntityData().get(OTHER_HEADS_DISABLED);
   }

   public void setOtherHeadsDisabled(boolean value) {
      this.storm.getEntityData().set(OTHER_HEADS_DISABLED, value);
      this.storm.getSegmentsManager().ifPresent(manager -> {
         WitherStormSegmentEntity[] segments = manager.getSegments();

         for (int i = 0; i < segments.length; i++) {
            if (segments[i] != null) {
               segments[i].setOtherHeadsDisabled(value);
            }
         }
      });
   }

   public void setHeadInjuryTime(int time) {
      this.storm.getEntityData().set(INJURY_TIME, time);
   }

   public int getHeadInjuryTime() {
      return (Integer)this.storm.getEntityData().get(INJURY_TIME);
   }

   public static void bootstrap() {
   }

   public static record PackedHeadRots(byte[] xRots, byte[] yRots) {
      public void toPacket(FriendlyByteBuf buffer) {
         buffer.writeByteArray(this.xRots);
         buffer.writeByteArray(this.yRots);
      }

      public static HeadManager.PackedHeadRots fromPacket(FriendlyByteBuf buffer) {
         byte[] xRots = buffer.readByteArray();
         byte[] yRots = buffer.readByteArray();
         return new HeadManager.PackedHeadRots(xRots, yRots);
      }

      public static float unpack(byte b) {
         return (float)(b * 360) / 256.0F;
      }
   }
}
