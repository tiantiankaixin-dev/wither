package nonamecrackers2.witherstormmod.common.network;

import java.util.concurrent.CompletableFuture;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.LogicalSide;

public class LegacyNetworkEvent {
   public static class Context {
      private final CustomPayloadEvent.Context delegate;

      public Context(CustomPayloadEvent.Context delegate) {
         this.delegate = delegate;
      }

      public ServerPlayer getSender() {
         return this.delegate.getSender();
      }

      public Connection getNetworkManager() {
         return this.delegate.getConnection();
      }

      public Direction getDirection() {
         return new Direction(this.delegate.isClientSide() ? LogicalSide.CLIENT : LogicalSide.SERVER);
      }

      public CompletableFuture<Void> enqueueWork(Runnable runnable) {
         return this.delegate.enqueueWork(runnable);
      }

      public void setPacketHandled(boolean handled) {
         this.delegate.setPacketHandled(handled);
      }

      public boolean getPacketHandled() {
         return this.delegate.getPacketHandled();
      }
   }

   public static class Direction {
      private final LogicalSide receptionSide;

      private Direction(LogicalSide receptionSide) {
         this.receptionSide = receptionSide;
      }

      public LogicalSide getReceptionSide() {
         return this.receptionSide;
      }
   }
}
