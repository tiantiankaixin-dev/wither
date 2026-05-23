package nonamecrackers2.witherstormmod.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.event.RenderPlayerEvent.Post;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.bus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.client.util.Contributors;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;

public class PlayerCosmeticsEvents {
   @SubscribeEvent
   public static void onRenderPlayer(Post event) {
      Player player = event.getEntity();
      if (((Boolean)WitherStormModConfig.CLIENT.patronCosmetic.get() || player != Minecraft.getInstance().player) && !player.isInvisible()) {
         String name = player.getGameProfile().getName();
         BlockState state = null;
         BlockState devState = null;
         BlockState kofiState = null;
         if (Contributors.isDeveloper(name)) {
            devState = Blocks.COMMAND_BLOCK.defaultBlockState();
         }

         if (Contributors.isPatron(name)) {
            state = ((Block)WitherStormModBlocks.FORMIDIBOMB.get()).defaultBlockState();
         }

         if (Contributors.isKofi(name)) {
            kofiState = ((Block)WitherStormModBlocks.TAINTED_ZOMBIE_LYING.get()).defaultBlockState();
         }

         Minecraft mc = Minecraft.getInstance();
         if (state != null) {
            PoseStack stack = event.getPoseStack();
            stack.pushPose();
            float tickCount = (float)player.tickCount + event.getPartialTick();
            stack.mulPose(Axis.YP.rotationDegrees(tickCount));
            stack.translate(0.6, (double)(player.getBbHeight() + 0.2F), 0.0);
            stack.pushPose();
            float scale = (Mth.sin(tickCount * 0.1F) + 10.0F) * 0.025F;
            stack.mulPose(Axis.XP.rotationDegrees(tickCount));
            stack.mulPose(Axis.ZN.rotationDegrees(tickCount * 4.0F));
            stack.scale(scale, scale, scale);
            stack.translate(-0.5, -0.5, -0.5);
            int overlay = OverlayTexture.NO_OVERLAY;
            if (player.tickCount / 20 % 5 == 0 && Contributors.isPatron(name)) {
               overlay = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
            }

            mc.getBlockRenderer().renderSingleBlock(state, stack, event.getMultiBufferSource(), 15728880, overlay, ModelData.EMPTY, null);
            stack.popPose();
            stack.popPose();
         }

         if (devState != null) {
            PoseStack devStack = event.getPoseStack();
            devStack.pushPose();
            float tickCount = (float)player.tickCount + event.getPartialTick();
            devStack.mulPose(Axis.YP.rotationDegrees(tickCount));
            devStack.translate(-0.6, (double)(player.getBbHeight() + 0.2F), 0.0);
            devStack.pushPose();
            float scale = (Mth.sin(tickCount * 0.1F) + 10.0F) * 0.025F;
            devStack.mulPose(Axis.XP.rotationDegrees(tickCount));
            devStack.mulPose(Axis.ZN.rotationDegrees(tickCount * 4.0F));
            devStack.scale(scale, scale, scale);
            devStack.translate(-0.5, -0.5, -0.5);
            int overlay = OverlayTexture.NO_OVERLAY;
            if (player.tickCount / 20 % 5 == 0 && Contributors.isPatron(name)) {
               overlay = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
            }

            mc.getBlockRenderer().renderSingleBlock(devState, devStack, event.getMultiBufferSource(), 15728880, overlay, ModelData.EMPTY, null);
            devStack.popPose();
            devStack.popPose();
         }

         if (kofiState != null) {
            PoseStack kofiStack = event.getPoseStack();
            kofiStack.pushPose();
            float tickCount = (float)player.tickCount + event.getPartialTick();
            kofiStack.mulPose(Axis.YP.rotationDegrees(-tickCount));
            kofiStack.translate(-0.6, (double)(player.getBbHeight() + 0.2F), 1.0);
            kofiStack.pushPose();
            float scale = 0.2727F;
            kofiStack.mulPose(Axis.XP.rotationDegrees(-tickCount * 6.0F));
            kofiStack.mulPose(Axis.ZN.rotationDegrees(-tickCount * 6.0F));
            kofiStack.scale(scale, scale, scale);
            kofiStack.translate(-0.5, -0.5, -0.5);
            int overlay = OverlayTexture.NO_OVERLAY;
            mc.getBlockRenderer().renderSingleBlock(kofiState, kofiStack, event.getMultiBufferSource(), 15728880, overlay, ModelData.EMPTY, null);
            kofiStack.popPose();
            kofiStack.popPose();
         }
      }
   }
}
