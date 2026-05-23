package nonamecrackers2.witherstormmod.common.util;

public class DebrisRingSettings {
   private final int segments;
   private final float bottomRadius;
   private final float topRadius;
   private final float y;
   private final float height;
   private final float speedModifier;
   private final boolean clockwise;
   private final int phaseRequirement;
   private float alpha = 1.0F;

   public DebrisRingSettings(
      int segments, float bottomRadius, float topRadius, float y, float height, float speedModifier, boolean clockwise, int phase, boolean hidden
   ) {
      this.segments = segments;
      this.bottomRadius = bottomRadius;
      this.topRadius = topRadius;
      this.y = y;
      this.height = y + height;
      this.speedModifier = speedModifier;
      this.clockwise = clockwise;
      this.phaseRequirement = phase;
      if (hidden) {
         this.alpha = 0.0F;
      }
   }

   public int getSegments() {
      return this.segments;
   }

   public float getBottomRadius() {
      return this.bottomRadius;
   }

   public float getTopRadius() {
      return this.topRadius;
   }

   public float getY() {
      return this.y;
   }

   public float getHeight() {
      return this.height;
   }

   public float getSpeedModifier() {
      return this.speedModifier;
   }

   public boolean clockwise() {
      return this.clockwise;
   }

   public int getPhaseRequirement() {
      return this.phaseRequirement;
   }

   public float alpha() {
      return this.alpha;
   }

   public void setAlpha(float alpha) {
      this.alpha = alpha;
   }
}
