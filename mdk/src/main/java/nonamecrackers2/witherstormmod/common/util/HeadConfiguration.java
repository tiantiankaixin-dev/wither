package nonamecrackers2.witherstormmod.common.util;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public record HeadConfiguration(Predicate<WitherStormEntity> predicate, Int2ObjectMap<Vec3> offsetsByHead) {
   public static HeadConfiguration.Builder builder(Predicate<WitherStormEntity> predicate) {
      return new HeadConfiguration.Builder(predicate);
   }

   public static HeadConfiguration.Builder forPhase(int phase) {
      return builder(storm -> storm.getPhase() == phase);
   }

   public Vec3 getOffsetForHead(int head) {
      return (Vec3)this.offsetsByHead().getOrDefault(head, Vec3.ZERO);
   }

   public static List<HeadConfiguration> makeSameFor(Consumer<HeadConfiguration.Builder> consumer, int... phases) {
      com.google.common.collect.ImmutableList.Builder<HeadConfiguration> configurations = ImmutableList.builder();

      for (int phase : phases) {
         HeadConfiguration.Builder builder = forPhase(phase);
         consumer.accept(builder);
         configurations.add(builder.build());
      }

      return configurations.build();
   }

   public static class Builder {
      private final Predicate<WitherStormEntity> predicate;
      private final Int2ObjectMap<Vec3> offsetsByHead = new Int2ObjectOpenHashMap();

      private Builder(Predicate<WitherStormEntity> predicate) {
         this.predicate = predicate;
      }

      public HeadConfiguration.Builder addOffset(int head, double x, double y, double z) {
         this.offsetsByHead.put(head, new Vec3(x, y, z));
         return this;
      }

      public HeadConfiguration build() {
         return new HeadConfiguration(this.predicate, this.offsetsByHead);
      }
   }
}
