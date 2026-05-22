package nonamecrackers2.witherstormmod.client.renderer.entity.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.animation.AnimationChannel.Interpolations;
import net.minecraft.client.animation.AnimationChannel.Targets;
import net.minecraft.client.animation.AnimationDefinition.Builder;

public class TaintedSlimeAnimations {
   public static final AnimationDefinition MODEL_IDLE = Builder.withLength(8.125F)
      .looping()
      .addAnimation(
         "bone",
         new AnimationChannel(
            Targets.ROTATION,
            new Keyframe[]{
               new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), Interpolations.CATMULLROM),
               new Keyframe(2.0F, KeyframeAnimations.degreeVec(15.0F, 90.0F, 0.0F), Interpolations.CATMULLROM),
               new Keyframe(4.0F, KeyframeAnimations.degreeVec(30.0F, 180.0F, 30.0F), Interpolations.CATMULLROM),
               new Keyframe(6.0F, KeyframeAnimations.degreeVec(0.0F, 270.0F, 15.0F), Interpolations.CATMULLROM),
               new Keyframe(8.0F, KeyframeAnimations.degreeVec(0.0F, 360.0F, 0.0F), Interpolations.CATMULLROM)
            }
         )
      )
      .addAnimation(
         "bone",
         new AnimationChannel(
            Targets.SCALE,
            new Keyframe[]{
               new Keyframe(2.0F, KeyframeAnimations.scaleVec(0.95F, 0.95F, 0.95F), Interpolations.CATMULLROM),
               new Keyframe(4.0F, KeyframeAnimations.scaleVec(0.85F, 0.85F, 0.85F), Interpolations.CATMULLROM),
               new Keyframe(6.0F, KeyframeAnimations.scaleVec(0.95F, 0.95F, 0.95F), Interpolations.CATMULLROM)
            }
         )
      )
      .build();
}
