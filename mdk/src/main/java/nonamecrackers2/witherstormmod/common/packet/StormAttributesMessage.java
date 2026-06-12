package nonamecrackers2.witherstormmod.common.packet;

import net.neoforged.neoforge.network.handling.IPayloadContext;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket.AttributeSnapshot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.core.registries.BuiltInRegistries;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;

public class StormAttributesMessage extends DistantRendererMessage {
   private int entityId;
   private List<AttributeSnapshot> attributes = Lists.newArrayList();

   public StormAttributesMessage(List<Integer> applicable, int id, Collection<AttributeInstance> attributes) {
      super(true, applicable);
      this.entityId = id;

      for (AttributeInstance attribute : attributes) {
         this.attributes.add(new AttributeSnapshot(attribute.getAttribute(), attribute.getBaseValue(), attribute.getModifiers()));
      }
   }

   public StormAttributesMessage(List<Integer> applicable, int id, List<AttributeSnapshot> attributes) {
      super(true, applicable);
      this.entityId = id;
      this.attributes = attributes;
   }

   public StormAttributesMessage() {
      super(false, Lists.newArrayList());
   }

   public int getEntityID() {
      return this.entityId;
   }

   public List<AttributeSnapshot> getAttributes() {
      return this.attributes;
   }

   @Override
   public void encode(FriendlyByteBuf buffer) {
      super.encode(buffer);
      buffer.writeVarInt(this.entityId);
      buffer.writeCollection(this.attributes, (buffer1, snapshot) -> {
         buffer1.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(snapshot.attribute()));
         buffer1.writeDouble(snapshot.base());
         buffer1.writeCollection(snapshot.modifiers(), (buffer2, modifier) -> {
            buffer2.writeUUID(modifier.getId());
            buffer2.writeDouble(modifier.getAmount());
            buffer2.writeByte(modifier.operation().toValue());
         });
      });
   }

   @Override
   public void decode(FriendlyByteBuf buffer) {
      super.decode(buffer);
      this.entityId = buffer.readVarInt();
      this.attributes = buffer.readList(
         buffer1 -> {
            ResourceLocation location = buffer1.readResourceLocation();
            Attribute attribute = BuiltInRegistries.ATTRIBUTE.get(location);
            double base = buffer1.readDouble();
            List<AttributeModifier> list = buffer1.readList(
               buffer2 -> new AttributeModifier(
                     buffer2.readUUID(), "Unknown synced attribute modifier", buffer.readDouble(), Operation.fromValue(buffer.readByte())
                  )
            );
            return new AttributeSnapshot(attribute, base, list);
         }
      );
   }

   public Runnable getProcessor(IPayloadContext context) {
      return () -> client(() -> WitherStormModMessageHandlerClient.processStormAttributesMessage(this));
   }

   public String toString() {
      return "StormAttributesMessage[id=" + this.entityId + ", properties=" + this.attributes.toString() + "]";
   }
}
