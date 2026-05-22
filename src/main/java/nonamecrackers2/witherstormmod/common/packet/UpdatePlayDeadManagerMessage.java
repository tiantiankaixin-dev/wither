package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.PlayDeadManager;

public class UpdatePlayDeadManagerMessage extends Packet {
   private int entityId;
   private PlayDeadManager.State state = PlayDeadManager.State.NORMAL_BEHAVIOR;
   private int ticks;
   private int revivalTicks;
   private boolean hasRecentlyBeenRevived;
   private boolean updateTick;
   private int revivalPlayerProtection;

   public UpdatePlayDeadManagerMessage(int id, PlayDeadManager manager, boolean updateTick) {
      super(true);
      this.entityId = id;
      this.state = manager.getState();
      this.ticks = manager.getTicks();
      this.revivalTicks = manager.getTicksSinceRevival();
      this.hasRecentlyBeenRevived = manager.hasRecentlyBeenRevived();
      this.updateTick = updateTick;
      this.revivalPlayerProtection = manager.getRevivalPlayerProtectionTime();
   }

   public UpdatePlayDeadManagerMessage() {
      super(false);
   }

   public int getEntityID() {
      return this.entityId;
   }

   public PlayDeadManager.State getState() {
      return this.state;
   }

   public int getTicks() {
      return this.ticks;
   }

   public int getTicksSinceRevival() {
      return this.revivalTicks;
   }

   public boolean hasRecentlyBeenRevived() {
      return this.hasRecentlyBeenRevived;
   }

   public boolean shouldUpdateTick() {
      return this.updateTick;
   }

   public int getRevivalPlayerProtectionTime() {
      return this.revivalPlayerProtection;
   }

   public void decode(FriendlyByteBuf buffer) {
      this.entityId = buffer.readVarInt();
      this.state = (PlayDeadManager.State)buffer.readEnum(PlayDeadManager.State.class);
      this.ticks = buffer.readInt();
      this.revivalTicks = buffer.readInt();
      this.hasRecentlyBeenRevived = buffer.readBoolean();
      this.updateTick = buffer.readBoolean();
      this.revivalPlayerProtection = buffer.readInt();
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeVarInt(this.entityId);
      buffer.writeEnum(this.state);
      buffer.writeInt(this.ticks);
      buffer.writeInt(this.revivalTicks);
      buffer.writeBoolean(this.hasRecentlyBeenRevived);
      buffer.writeBoolean(this.updateTick);
      buffer.writeInt(this.revivalPlayerProtection);
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processUpdatePlayDeadManagerMessage(this));
   }

   public String toString() {
      return "UpdatePlayDeadManagerMessage[id="
         + this.entityId
         + ", state="
         + this.state.toString()
         + ", ticks="
         + this.ticks
         + ", updateTick="
         + this.updateTick
         + "]";
   }
}
