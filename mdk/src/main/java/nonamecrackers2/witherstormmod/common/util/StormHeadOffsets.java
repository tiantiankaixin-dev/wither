package nonamecrackers2.witherstormmod.common.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import java.util.List;
import net.minecraft.Util;

public class StormHeadOffsets {
   public static final List<HeadConfiguration> MAIN = (List<HeadConfiguration>)Util.make(
      () -> {
         Builder<HeadConfiguration> builder = ImmutableList.builder();
         builder.add(HeadConfiguration.forPhase(0).addOffset(0, 0.0, 3.0, 0.0).addOffset(1, -1.3, 2.2, 0.0).addOffset(2, 1.3, 2.2, 0.0).build());
         builder.add(HeadConfiguration.forPhase(1).addOffset(0, 0.0, 3.0, 0.0).addOffset(1, -1.3, 2.2, 0.0).addOffset(2, 1.3, 2.2, 0.0).build());
         builder.add(HeadConfiguration.forPhase(2).addOffset(0, 0.0, 2.75, 0.5).addOffset(1, -1.3, 2.2, 0.0).addOffset(2, 1.3, 2.2, 0.0).build());
         builder.add(HeadConfiguration.forPhase(3).addOffset(0, 0.0, 2.75, 0.5).addOffset(1, -1.3, 2.2, 0.0).addOffset(2, 1.3, 2.2, 0.0).build());
         builder.addAll(
            HeadConfiguration.makeSameFor(b -> b.addOffset(0, 0.0, 12.0, 10.0).addOffset(1, -12.0, 22.5, 10.0).addOffset(2, 8.5, 24.5, 16.0), 4, 5, 6, 7)
         );
         return builder.build();
      }
   );
   public static final List<HeadConfiguration> SEGMENT = (List<HeadConfiguration>)Util.make(
      () -> HeadConfiguration.makeSameFor(
            b -> b.addOffset(0, 0.0, 9.0, 14.0).addOffset(1, 6.0, 8.0, 12.0).addOffset(2, -6.0, 8.0, 12.0), 0, 1, 2, 3, 4, 5, 6, 7
         )
   );

   private StormHeadOffsets() {
   }
}
