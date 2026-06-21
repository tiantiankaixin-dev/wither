package nonamecrackers2.witherstormmod.common.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import nonamecrackers2.witherstormmod.api.common.ai.symbiont.SpellType;
import nonamecrackers2.witherstormmod.api.common.registry.WitherStormModRegistries;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSymbiontSpellTypes;
import nonamecrackers2.witherstormmod.common.util.WitherStormModNBTUtil;

public class WitherStormModDataSerializers {
   public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(Keys.ENTITY_DATA_SERIALIZERS, "witherstormmod");
   public static final EntityDataSerializer<Map<BlockPos, BlockState>> BLOCK_STATE_POS_MAP = EntityDataSerializer.forValueType(
      StreamCodec.of(WitherStormModDataSerializers::writeBlockStatePosMap, WitherStormModDataSerializers::readBlockStatePosMap)
   );
   public static final EntityDataSerializer<List<CompoundTag>> COMPOUND_LIST = EntityDataSerializer.forValueType(
      StreamCodec.of(WitherStormModDataSerializers::writeCompoundList, WitherStormModDataSerializers::readCompoundList)
   );
   public static final EntityDataSerializer<Vec2> VECTOR_2F = EntityDataSerializer.forValueType(
      StreamCodec.of(WitherStormModDataSerializers::writeVec2, WitherStormModDataSerializers::readVec2)
   );
   public static final EntityDataSerializer<Optional<Vec3>> OPTIONAL_VECTOR_3D = EntityDataSerializer.forValueType(
      StreamCodec.of(WitherStormModDataSerializers::writeOptionalVec3, WitherStormModDataSerializers::readOptionalVec3)
   );
   public static final EntityDataSerializer<SpellType> SPELL_TYPE = EntityDataSerializer.forValueType(
      StreamCodec.of(WitherStormModDataSerializers::writeSpellType, WitherStormModDataSerializers::readSpellType)
   );
   public static final WitherStormModDataSerializers.EnumDataSerializer<CommandBlockEntity.Mode> MODE_ENUM = new WitherStormModDataSerializers.EnumDataSerializer<>(
      CommandBlockEntity.Mode.class
   );
   public static final WitherStormModDataSerializers.EnumDataSerializer<CommandBlockEntity.State> STATE_ENUM = new WitherStormModDataSerializers.EnumDataSerializer<>(
      CommandBlockEntity.State.class
   );
   public static final WitherStormModDataSerializers.EnumDataSerializer<WitheredSymbiontEntity.BossfightStage> BOSSFIGHT_STAGE_ENUM = new WitherStormModDataSerializers.EnumDataSerializer<>(
      WitheredSymbiontEntity.BossfightStage.class
   );

   static {
      DATA_SERIALIZERS.register("block_state_pos_map", () -> BLOCK_STATE_POS_MAP);
      DATA_SERIALIZERS.register("compound_list", () -> COMPOUND_LIST);
      DATA_SERIALIZERS.register("vector2f", () -> VECTOR_2F);
      DATA_SERIALIZERS.register("mode_enum", () -> MODE_ENUM);
      DATA_SERIALIZERS.register("state_enum", () -> STATE_ENUM);
      DATA_SERIALIZERS.register("bossfight_state_enum", () -> BOSSFIGHT_STAGE_ENUM);
      DATA_SERIALIZERS.register("vector3d", () -> OPTIONAL_VECTOR_3D);
      DATA_SERIALIZERS.register("spell_type", () -> SPELL_TYPE);
   }

   public static class EnumDataSerializer<T extends Enum<T>> implements EntityDataSerializer<T> {
      private final Class<T> enumClass;
      private final StreamCodec<RegistryFriendlyByteBuf, T> codec;

      private EnumDataSerializer(Class<T> enumClass) {
         this.enumClass = enumClass;
         this.codec = StreamCodec.of((buffer, value) -> buffer.writeEnum(value), buffer -> buffer.readEnum(this.enumClass));
      }

      @Override
      public StreamCodec<? super RegistryFriendlyByteBuf, T> codec() {
         return this.codec;
      }

      @Override
      public T copy(T value) {
         return value;
      }
   }

   private static void writeBlockStatePosMap(RegistryFriendlyByteBuf buffer, Map<BlockPos, BlockState> map) {
      buffer.writeMap(map, (buf, pos) -> buf.writeBlockPos(pos), (buf, state) -> buf.writeVarInt(Block.getId(state)));
   }

   private static Map<BlockPos, BlockState> readBlockStatePosMap(RegistryFriendlyByteBuf buffer) {
      return buffer.readMap(HashMap::new, buf -> buf.readBlockPos(), buf -> Block.stateById(buf.readVarInt()));
   }

   private static void writeCompoundList(RegistryFriendlyByteBuf buffer, List<CompoundTag> list) {
      CompoundTag compound = new CompoundTag();
      compound.put("List", WitherStormModNBTUtil.writeCompoundList(list));
      buffer.writeNbt(compound);
   }

   private static List<CompoundTag> readCompoundList(RegistryFriendlyByteBuf buffer) {
      CompoundTag compound = buffer.readNbt();
      return compound != null ? WitherStormModNBTUtil.readCompoundList(compound.getList("List", 10)) : new ArrayList<>();
   }

   private static void writeVec2(RegistryFriendlyByteBuf buffer, Vec2 vector) {
      buffer.writeFloat(vector.x);
      buffer.writeFloat(vector.y);
   }

   private static Vec2 readVec2(RegistryFriendlyByteBuf buffer) {
      return new Vec2(buffer.readFloat(), buffer.readFloat());
   }

   private static void writeOptionalVec3(RegistryFriendlyByteBuf buffer, Optional<Vec3> vector) {
      buffer.writeBoolean(vector.isPresent());
      vector.ifPresent(pos -> {
         buffer.writeDouble(pos.x());
         buffer.writeDouble(pos.y());
         buffer.writeDouble(pos.z());
      });
   }

   private static Optional<Vec3> readOptionalVec3(RegistryFriendlyByteBuf buffer) {
      return buffer.readBoolean() ? Optional.of(new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())) : Optional.empty();
   }

   private static void writeSpellType(RegistryFriendlyByteBuf buffer, SpellType type) {
      ResourceLocation id = Objects.requireNonNull(WitherStormModRegistries.SPELL_TYPES.get().getKey(type), "Unregistered symbiont spell type");
      buffer.writeResourceLocation(id);
   }

   private static SpellType readSpellType(RegistryFriendlyByteBuf buffer) {
      SpellType type = WitherStormModRegistries.SPELL_TYPES.get().getValue(buffer.readResourceLocation());
      return type != null ? type : WitherStormModSymbiontSpellTypes.EMPTY.get();
   }
}
