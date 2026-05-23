package nonamecrackers2.witherstormmod.client.util;

import java.awt.Color;
import java.time.temporal.ChronoField;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public enum SpecialDay {
   HALLOWEEN(new Color(135, 82, 28), isDate(10, 31)),
   CHRISTMAS((w, p, h) -> {
      float tickCount = ((float)(w.tickCount + h * 1000 + w.getId() * 1000) + p) / 100.0F;
      float r = Mth.sqrt(Math.max(Mth.sin(tickCount), 0.0F));
      float g = Mth.sqrt(Math.max(Mth.sin(tickCount + (float) Math.PI), 0.0F));
      return new Color(r * 0.5F, g * 0.5F, 0.0F);
   }, isDayInMonth(12, 24, 25));

   private final SpecialDay.ColorGetter color;
   private final BiFunction<Integer, Integer, Boolean> shouldShow;

   private SpecialDay(SpecialDay.ColorGetter color, BiFunction<Integer, Integer, Boolean> shouldShow) {
      this.color = color;
      this.shouldShow = shouldShow;
   }

   private SpecialDay(Color color, BiFunction<Integer, Integer, Boolean> shouldShow) {
      this((e, p, h) -> color, shouldShow);
   }

   public Color getColor(WitherStormEntity storm, float partialTicks, int head) {
      return this.color.getColor(storm, partialTicks, head);
   }

   private static BiFunction<Integer, Integer, Boolean> isDate(int month, int day) {
      return (m, d) -> m == month && d == day;
   }

   private static BiFunction<Integer, Integer, Boolean> isDayInMonth(int month, int... days) {
      return (m, d) -> {
         if (m == month) {
            for (int day : days) {
               if (d == day) {
                  return true;
               }
            }
         }

         return false;
      };
   }

   @Nullable
   public static SpecialDay getForCurrentDate() {
      int month = WitherStormMod.DATE.get(ChronoField.MONTH_OF_YEAR);
      int day = WitherStormMod.DATE.get(ChronoField.DAY_OF_MONTH);

      for (SpecialDay colors : values()) {
         if (colors.shouldShow.apply(month, day)) {
            return colors;
         }
      }

      return null;
   }

   @FunctionalInterface
   public interface ColorGetter {
      Color getColor(WitherStormEntity var1, float var2, int var3);
   }
}
