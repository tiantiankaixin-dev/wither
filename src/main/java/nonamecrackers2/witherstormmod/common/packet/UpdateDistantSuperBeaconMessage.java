package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class UpdateDistantSuperBeaconMessage extends Packet {
   private BlockPos pos;
   private int[] color;
   public boolean active;
   public int beaconHeight;
   public float beamWidth;
   public float outerBeamWidth;

   public UpdateDistantSuperBeaconMessage(BlockPos pos, int[] color, boolean active, int beaconHeight, float beamWidth, float outerBeamWidth) {
      super(true);
      this.pos = pos;
      this.color = color;
      this.active = active;
      this.beaconHeight = beaconHeight;
      this.beamWidth = beamWidth;
      this.outerBeamWidth = outerBeamWidth;
   }

   public UpdateDistantSuperBeaconMessage() {
      super(false);
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public int[] getColor() {
      return this.color;
   }

   public boolean isActive() {
      return this.active;
   }

   public int getBeaconHeight() {
      return this.beaconHeight;
   }

   public float getBeamWidth() {
      return this.beamWidth;
   }

   public float getOuterBeamWidth() {
      return this.outerBeamWidth;
   }

   public void decode(FriendlyByteBuf buffer) {
      this.pos = buffer.readBlockPos();
      this.color = buffer.readVarIntArray();
      this.active = buffer.readBoolean();
      this.beaconHeight = buffer.readInt();
      this.beamWidth = buffer.readFloat();
      this.outerBeamWidth = buffer.readFloat();
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeBlockPos(this.pos);
      buffer.writeVarIntArray(this.color);
      buffer.writeBoolean(this.active);
      buffer.writeInt(this.beaconHeight);
      buffer.writeFloat(this.beamWidth);
      buffer.writeFloat(this.outerBeamWidth);
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processUpdateDistantSuperBeaconMessage(this));
   }
}
