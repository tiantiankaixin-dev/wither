package nonamecrackers2.witherstormmod.common.event;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import nonamecrackers2.witherstormmod.common.config.WitherStormModConfig;

public class WitherStormModCompatEvents {
   @SubscribeEvent
   public static void onPlayerJoin(PlayerLoggedInEvent event) {
      if (event.getEntity() instanceof ServerPlayer player) {
         ServerLevel level = player.serverLevel();
         if ((Boolean)WitherStormModConfig.SERVER.flyingDisabledWarning.get() && !level.getServer().isFlightAllowed() && player.hasPermissions(2)) {
            player.sendSystemMessage(Component.translatable("chat.witherstormmod.flyingDisabled.notice").withStyle(ChatFormatting.RED));
            WitherStormModConfig.SERVER.flyingDisabledWarning.set(false);
         }
      }
   }
}
