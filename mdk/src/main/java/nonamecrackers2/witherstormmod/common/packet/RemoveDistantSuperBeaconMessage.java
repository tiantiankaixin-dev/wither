package nonamecrackers2.witherstormmod.common.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import // TODO_MIG: DistExecutor removed, use FMLEnvironment.dist == Dist.CLIENT;
import // TODO_MIG: NetworkEvent removed, use IPayloadContext.Context;
import nonamecrackers2.crackerslib.common.packet.Packet;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class RemoveDistantSuperBeaconMessage extends Packet {
   private BlockPos pos;

   public RemoveDistantSuperBeaconMessage(BlockPos pos) {
      super(true);
      this.pos = pos;
   }

   public RemoveDistantSuperBeaconMessage() {
      super(false);
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public void decode(FriendlyByteBuf buffer) {
      this.pos = buffer.readBlockPos();
   }

   public void encode(FriendlyByteBuf buffer) {
      buffer.writeBlockPos(this.pos);
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processRemoveDistantSuperBeaconMessage(this));
   }
}
