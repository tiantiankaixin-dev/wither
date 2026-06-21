package nonamecrackers2.witherstormmod.common.packet;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket.AttributeSnapshot;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.DistExecutor;
import nonamecrackers2.witherstormmod.common.network.LegacyNetworkEvent.Context;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.client.packet.WitherStormModMessageHandlerClient;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.HeadManager;

public class WitherStormToDistantRendererMessage extends DistantRendererMessage {
   private final WitherStormEntity entity;
   private int id;
   private UUID uuid;
   private ResourceLocation type;
   private Vec3 pos;
   private Vec3i delta;
   private byte yRot;
   private byte xRot;
   private byte yHeadRot;
   private HeadManager.PackedHeadRots rots;
   private List<DataValue<?>> packedItems;
   private final List<AttributeSnapshot> attributes = Lists.newArrayList();
   private FriendlyByteBuf buffer;

   public WitherStormToDistantRendererMessage(List<Integer> applicable, WitherStormEntity entity) {
      super(true, applicable);
      this.entity = entity;
      this.id = entity.getId();
      this.uuid = entity.getUUID();
      this.type = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
      this.pos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
      this.yRot = (byte)((int)(entity.getYRot() * 256.0F / 360.0F));
      this.xRot = (byte)((int)(entity.getXRot() * 256.0F / 360.0F));
      this.yHeadRot = (byte)((int)(entity.yHeadRot * 256.0F / 360.0F));
      this.rots = entity.getHeadManager().packHeadRotations();
      Vec3 vector3d = entity.getDeltaMovement();
      Vec3i delta = new Vec3i(
         Mth.floor(Mth.clamp(vector3d.x, -3.9, 3.9)),
         Mth.floor(Mth.clamp(vector3d.y, -3.9, 3.9)),
         Mth.floor(Mth.clamp(vector3d.z, -3.9, 3.9))
      );
      this.delta = delta;
      SynchedEntityData data = entity.getEntityData();
      this.packedItems = getPackedData(data);

      for (AttributeInstance attribute : entity.getAttributes().getSyncableAttributes()) {
         this.attributes.add(new AttributeSnapshot(attribute.getAttribute(), attribute.getBaseValue(), attribute.getModifiers()));
      }

      this.buffer = null;
   }

   public WitherStormToDistantRendererMessage() {
      super(false, Lists.newArrayList());
      this.entity = null;
   }

   public int getId() {
      return this.id;
   }

   public UUID getUUID() {
      return this.uuid;
   }

   public ResourceLocation getType() {
      return this.type;
   }

   public Vec3 getPos() {
      return this.pos;
   }

   public Vec3i getDeltaMovement() {
      return this.delta;
   }

   public byte getYRot() {
      return this.yRot;
   }

   public byte getXRot() {
      return this.xRot;
   }

   public byte getHeadYRot() {
      return this.yHeadRot;
   }

   public HeadManager.PackedHeadRots getRots() {
      return this.rots;
   }

   public List<DataValue<?>> getUnpackedData() {
      return this.packedItems;
   }

   public FriendlyByteBuf getBuffer() {
      return this.buffer;
   }

   @Override
   public void decode(FriendlyByteBuf buffer) {
      super.decode(buffer);
      this.id = buffer.readVarInt();
      this.uuid = buffer.readUUID();
      this.type = buffer.readResourceLocation();
      double x = buffer.readDouble();
      double y = buffer.readDouble();
      double z = buffer.readDouble();
      this.pos = new Vec3(x, y, z);
      this.yRot = buffer.readByte();
      this.xRot = buffer.readByte();
      this.yHeadRot = buffer.readByte();
      this.rots = HeadManager.PackedHeadRots.fromPacket(buffer);
      int xd = buffer.readShort();
      int yd = buffer.readShort();
      int zd = buffer.readShort();
      this.delta = new Vec3i(xd, yd, zd);
      List<DataValue<?>> packedData = Lists.newArrayList();

      int j;
      RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf)buffer;
      while ((j = buffer.readUnsignedByte()) != 255) {
         packedData.add(DataValue.read(registryBuffer, j));
      }

      this.packedItems = packedData;
      int attributeSize = buffer.readInt();

      for (int i = 0; i < attributeSize; i++) {
         ResourceLocation location = buffer.readResourceLocation();
         Holder<Attribute> attribute = BuiltInRegistries.ATTRIBUTE.getHolder(location).orElseThrow();
         double base = buffer.readDouble();
         List<AttributeModifier> list = Lists.newArrayList();
         int modifierSize = buffer.readVarInt();

         for (int l = 0; l < modifierSize; l++) {
            ResourceLocation modifierId = buffer.readResourceLocation();
            list.add(new AttributeModifier(modifierId, buffer.readDouble(), Operation.BY_ID.apply(buffer.readByte())));
         }

         this.attributes.add(new AttributeSnapshot(attribute, base, list));
      }

      int count = buffer.readVarInt();
      if (count > 0) {
         FriendlyByteBuf extra = new FriendlyByteBuf(Unpooled.buffer());
         extra.writeBytes(buffer, count);
         this.buffer = extra;
      } else {
         this.buffer = new FriendlyByteBuf(Unpooled.buffer());
      }
   }

   @Override
   public void encode(FriendlyByteBuf buffer) {
      super.encode(buffer);
      buffer.writeVarInt(this.id);
      buffer.writeUUID(this.uuid);
      buffer.writeResourceLocation(this.type);
      buffer.writeDouble(this.pos.x);
      buffer.writeDouble(this.pos.y);
      buffer.writeDouble(this.pos.z);
      buffer.writeByte(this.yRot);
      buffer.writeByte(this.xRot);
      buffer.writeByte(this.yHeadRot);
      this.rots.toPacket(buffer);
      buffer.writeShort(this.delta.getX());
      buffer.writeShort(this.delta.getY());
      buffer.writeShort(this.delta.getZ());
      RegistryFriendlyByteBuf registryBuffer = (RegistryFriendlyByteBuf)buffer;

      for (DataValue<?> value : this.packedItems) {
         value.write(registryBuffer);
      }

      buffer.writeByte(255);
      buffer.writeInt(this.attributes.size());

      for (AttributeSnapshot snapshot : this.attributes) {
         buffer.writeResourceLocation(BuiltInRegistries.ATTRIBUTE.getKey(snapshot.attribute().value()));
         buffer.writeDouble(snapshot.base());
         buffer.writeVarInt(snapshot.modifiers().size());

         for (AttributeModifier modifier : snapshot.modifiers()) {
            buffer.writeResourceLocation(modifier.id());
            buffer.writeDouble(modifier.amount());
            buffer.writeByte(modifier.operation().id());
         }
      }

      FriendlyByteBuf extra = new FriendlyByteBuf(Unpooled.buffer());
      if (this.entity instanceof IEntityAdditionalSpawnData e) {
         e.writeSpawnData(extra);
      }

      this.entity.writeData(extra);
      buffer.writeVarInt(extra.readableBytes());
      buffer.writeBytes(extra);
      extra.release();
   }

   public Runnable getProcessor(Context context) {
      return () -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WitherStormModMessageHandlerClient.processWitherStormToDistantRendererMessage(this));
   }

   public String toString() {
      return "WitherStormToDistantRendererMessage[id="
         + this.id
         + ", uuid="
         + this.uuid
         + ", type="
         + this.type
         + ", x="
         + this.pos.x
         + ", y="
         + this.pos.y
         + ", z="
         + this.pos.z
         + ", yRot= "
         + this.yRot
         + ", xRot= "
         + this.xRot
         + ", yHeadRot= "
         + this.yHeadRot
         + ", headRots= "
         + this.rots.toString()
         + ", xd="
         + this.delta.getX()
         + ", yd="
         + this.delta.getY()
         + ", zd="
         + this.delta.getZ()
         + "]";
   }
}
