package nonamecrackers2.witherstormmod.client.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import // TODO_MIG: TickEvent split into ServerTickEvent/LevelTickEvent/PlayerTickEvent/EntityTickEvent.ClientTickEvent;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;
import nonamecrackers2.witherstormmod.common.item.AmuletItem;
import org.joml.Matrix4f;

public class AmuletAnimationHelper {
   public static final ResourceLocation GLARE = new ResourceLocation("witherstormmod", "textures/misc/glare.png");
   private static final Map<InteractionHand, AmuletAnimationHelper.AnimationHolder> ANIMATIONS = ImmutableMap.of(
      InteractionHand.MAIN_HAND, new AmuletAnimationHelper.AnimationHolder(), InteractionHand.OFF_HAND, new AmuletAnimationHelper.AnimationHolder()
   );

   public static void onRenderItemInHand(ItemStack item, PoseStack stack, InteractionHand hand, float partialTicks, MultiBufferSource buffer) {
      if (item.getItem() instanceof AmuletItem) {
         AmuletAnimationHelper.AnimationHolder holder = ANIMATIONS.get(hand);
         VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(GLARE));
         stack.translate(0.0, 0.2, 0.07);
         stack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, holder.animO, holder.anim)));
         drawGlare(
            stack,
            hand,
            1.0F,
            -0.53,
            0.089,
            -0.53,
            0.0F,
            0.24705882F,
            1.0F,
            0.0F,
            holder.tickCount + 360,
            partialTicks,
            consumer,
            Mth.lerp(partialTicks, holder.pulseIntensityO[0], holder.pulseIntensity[0])
         );
         drawGlare(
            stack,
            hand,
            0.4F,
            -0.2,
            0.09,
            -0.02,
            0.007843138F,
            0.8980392F,
            0.7019608F,
            0.0F,
            holder.tickCount,
            partialTicks,
            consumer,
            Mth.lerp(partialTicks, holder.pulseIntensityO[1], holder.pulseIntensity[1])
         );
         drawGlare(
            stack,
            hand,
            0.4F,
            -0.2,
            0.09,
            -0.02,
            0.9411765F,
            0.15294118F,
            0.02745098F,
            hand == InteractionHand.MAIN_HAND ? 270.0F : 90.0F,
            holder.tickCount,
            partialTicks,
            consumer,
            Mth.lerp(partialTicks, holder.pulseIntensityO[4], holder.pulseIntensity[4])
         );
         drawGlare(
            stack,
            hand,
            0.4F,
            -0.2,
            0.09,
            -0.02,
            0.85882354F,
            0.85882354F,
            0.85882354F,
            180.0F,
            holder.tickCount,
            partialTicks,
            consumer,
            Mth.lerp(partialTicks, holder.pulseIntensityO[3], holder.pulseIntensity[3])
         );
         drawGlare(
            stack,
            hand,
            0.4F,
            -0.2,
            0.09,
            -0.02,
            0.25882354F,
            0.8666667F,
            0.023529412F,
            hand == InteractionHand.MAIN_HAND ? 90.0F : 270.0F,
            holder.tickCount,
            partialTicks,
            consumer,
            Mth.lerp(partialTicks, holder.pulseIntensityO[2], holder.pulseIntensity[2])
         );
         stack.translate(0.0, -0.2, -0.07);
      }
   }

   public static void drawGlare(
      PoseStack stack,
      InteractionHand hand,
      float scale,
      double xOffset,
      double zOffset,
      double radius,
      float r,
      float g,
      float b,
      float degreeOffset,
      int ticks,
      float partialTicks,
      VertexConsumer consumer,
      float pulseIntensity
   ) {
      stack.pushPose();
      stack.mulPose(Axis.XP.rotationDegrees(degreeOffset - 30.0F));
      stack.mulPose(Axis.YN.rotationDegrees(90.0F));
      Minecraft mc = Minecraft.getInstance();
      HumanoidArm arm = (HumanoidArm)mc.options.mainHand().get();
      boolean swap = arm == HumanoidArm.RIGHT ? hand == InteractionHand.MAIN_HAND : hand == InteractionHand.OFF_HAND;
      stack.translate(xOffset, radius, swap ? -zOffset : zOffset);
      stack.scale(scale, scale, scale);
      Matrix4f matrix = stack.last().pose();
      float alpha = Mth.clamp(Mth.cos(((float)ticks + degreeOffset + partialTicks) * 0.2F) * pulseIntensity, 0.0F, 1.0F);
      if (alpha > 0.0F) {
         consumer.vertex(matrix, 0.0F, 0.0F, 0.0F)
            .color(r, g, b, alpha)
            .uv(0.0F, 0.0F)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(15728880)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         consumer.vertex(matrix, 0.0F, 1.0F, 0.0F)
            .color(r, g, b, alpha)
            .uv(0.0F, 1.0F)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(15728880)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         consumer.vertex(matrix, 1.0F, 1.0F, 0.0F)
            .color(r, g, b, alpha)
            .uv(1.0F, 1.0F)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(15728880)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
         consumer.vertex(matrix, 1.0F, 0.0F, 0.0F)
            .color(r, g, b, alpha)
            .uv(1.0F, 0.0F)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(15728880)
            .normal(0.0F, 1.0F, 0.0F)
            .endVertex();
      }

      stack.popPose();
   }

   public static float getPulseIntensity(AbstractClientPlayer player, ClientLevel level, ItemStack stack, String id, int distance) {
      CompoundTag tag = stack.getOrCreateTag();
      if (tag.contains(id + "Pos")) {
         BlockPos pos = NbtUtils.readBlockPos(tag.getCompound(id + "Pos"));
         float angle = (float)(Mth.atan2((double)pos.getX() - player.getX(), (double)pos.getZ() - player.getZ()) * (180.0 / Math.PI));
         float angleDiff = (Mth.wrapDegrees(-player.yHeadRot) - angle + 180.0F + 360.0F) % 360.0F - 180.0F;
         float value = 1.0F - Mth.clamp(Mth.abs(angleDiff * 0.03F), 0.0F, 0.8F);
         int dist = tag.getInt(id + "Dist");
         return dist >= 0 ? value * Mth.clamp(((float)distance - (float)dist) * 0.05F, 0.0F, 1.0F) : 0.0F;
      } else {
         return 0.0F;
      }
   }

   public static void onClientTick(ClientTickEvent event) {
      Minecraft mc = Minecraft.getInstance();
      if (!mc.isPaused() && mc.player != null) {
         for (InteractionHand hand : InteractionHand.values()) {
            ItemStack item = mc.player.getItemInHand(hand);
            if (item.is((Item)WitherStormModItems.AMULET.get())) {
               AmuletAnimationHelper.AnimationHolder holder = ANIMATIONS.get(hand);
               if (holder != null) {
                  holder.tickCount++;
                  CompoundTag tag = item.getOrCreateTag();
                  int index = tag.getInt("SelectedIndex");
                  float target = getSwapDegrees(hand, index);
                  if (target != holder.targetO) {
                     holder.steps = 16;
                  }

                  holder.animO = holder.anim;
                  if (holder.steps > 0) {
                     holder.anim = holder.anim + Mth.wrapDegrees(target - holder.anim) / (float)holder.steps;
                     holder.steps--;
                  }

                  holder.targetO = target;

                  for (int i = 0; i < holder.pulseIntensity.length; i++) {
                     holder.pulseIntensityO[i] = holder.pulseIntensity[i];
                     holder.pulseIntensity[i] = getPulseIntensity(mc.player, mc.level, item, AmuletItem.TRACKING[i], 1000);
                  }
               }
            }
         }
      }
   }

   public static float getSwapDegrees(InteractionHand hand, int index) {
      if (hand == InteractionHand.OFF_HAND) {
         switch (index) {
            case 1:
               return 0.0F;
            case 2:
               return 90.0F;
            case 3:
               return 180.0F;
            case 4:
               return 270.0F;
            default:
               return 0.0F;
         }
      } else if (hand == InteractionHand.MAIN_HAND) {
         switch (index) {
            case 1:
               return 0.0F;
            case 2:
               return 270.0F;
            case 3:
               return 180.0F;
            case 4:
               return 90.0F;
            default:
               return 0.0F;
         }
      } else {
         return 0.0F;
      }
   }

   private static class AnimationHolder {
      private float anim;
      private float animO;
      private float targetO;
      private int steps;
      private int tickCount;
      private float[] pulseIntensity = new float[5];
      private float[] pulseIntensityO = new float[5];
   }
}
