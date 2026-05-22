package nonamecrackers2.witherstormmod.common.serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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
import nonamecrackers2.witherstormmod.common.util.WitherStormModNBTUtil;

public class WitherStormModDataSerializers {
   public static final DeferredRegister<EntityDataSerializer<?>> DATA_SERIALIZERS = DeferredRegister.create(Keys.ENTITY_DATA_SERIALIZERS, "witherstormmod");
   public static final EntityDataSerializer<Map<BlockPos, BlockState>> BLOCK_STATE_POS_MAP = new EntityDataSerializer<Map<BlockPos, BlockState>>() {
      public void write(FriendlyByteBuf buffer, Map<BlockPos, BlockState> map) {
         buffer.writeMap(map, (buf, pos) -> buf.writeBlockPos(pos), (buf, state) -> buf.writeVarInt(Block.getId(state)));
      }

      public Map<BlockPos, BlockState> read(FriendlyByteBuf buffer) {
         return buffer.readMap(buf -> buf.readBlockPos(), buf -> Block.stateById(buf.readVarInt()));
      }

      public Map<BlockPos, BlockState> copy(Map<BlockPos, BlockState> map) {
         return new HashMap<>(map);
      }
   };
   public static final EntityDataSerializer<List<CompoundTag>> COMPOUND_LIST = new EntityDataSerializer<List<CompoundTag>>() {
      public void write(FriendlyByteBuf buffer, List<CompoundTag> list) {
         CompoundTag compound = new CompoundTag();
         compound.put("List", WitherStormModNBTUtil.writeCompoundList(list));
         buffer.writeNbt(compound);
      }

      public List<CompoundTag> read(FriendlyByteBuf buffer) {
         CompoundTag compound = buffer.readNbt();
         return WitherStormModNBTUtil.readCompoundList(compound.getList("List", 10));
      }

      public List<CompoundTag> copy(List<CompoundTag> list) {
         return new ArrayList<>(list);
      }
   };
   public static final EntityDataSerializer<Vec2> VECTOR_2F = new EntityDataSerializer<Vec2>() {
      public void write(FriendlyByteBuf buffer, Vec2 vector) {
         buffer.writeFloat(vector.x);
         buffer.writeFloat(vector.y);
      }

      public Vec2 read(FriendlyByteBuf buffer) {
         return new Vec2(buffer.readFloat(), buffer.readFloat());
      }

      public Vec2 copy(Vec2 vector) {
         return new Vec2(vector.x, vector.y);
      }
   };
   public static final EntityDataSerializer<Optional<Vec3>> OPTIONAL_VECTOR_3D = new EntityDataSerializer<Optional<Vec3>>() {
      public void write(FriendlyByteBuf buffer, Optional<Vec3> vector) {
         buffer.writeBoolean(vector.isPresent());
         vector.ifPresent(pos -> {
            buffer.writeDouble(pos.x());
            buffer.writeDouble(pos.y());
            buffer.writeDouble(pos.z());
         });
      }

      public Optional<Vec3> read(FriendlyByteBuf buffer) {
         return buffer.readBoolean() ? Optional.of(new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble())) : Optional.empty();
      }

      public Optional<Vec3> copy(Optional<Vec3> vector) {
         return vector;
      }
   };
   public static final EntityDataSerializer<SpellType> SPELL_TYPE = new EntityDataSerializer<SpellType>() {
      public void write(FriendlyByteBuf buffer, SpellType type) {
         buffer.writeRegistryId(WitherStormModRegistries.SPELL_TYPES.get(), type);
      }

      public SpellType read(FriendlyByteBuf buffer) {
         return (SpellType)buffer.readRegistryId();
      }

      public SpellType copy(SpellType type) {
         return type;
      }
   };
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

      private EnumDataSerializer(Class<T> enumClass) {
         this.enumClass = enumClass;
      }

      public void write(FriendlyByteBuf buffer, T enub) {
         buffer.writeEnum(enub);
      }

      public T read(FriendlyByteBuf buffer) {
         return (T)buffer.readEnum(this.enumClass);
      }

      public T copy(T enub) {
         return enub;
      }
   }
}
