package nonamecrackers2.crackerslib.common.util.primitives;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class PrimitiveHelper {
   public static CompoundTag quaternionToTag(Quaternionf quaternion) {
      CompoundTag tag = new CompoundTag();
      tag.m_128350_("x", quaternion.x());
      tag.m_128350_("y", quaternion.y());
      tag.m_128350_("z", quaternion.z());
      tag.m_128350_("w", quaternion.w());
      return tag;
   }

   public static Quaternionf quaternionFromTag(CompoundTag tag) {
      Quaternionf quaternion = new Quaternionf().identity();
      if (tag.m_128425_("x", 5)) {
         quaternion.x = tag.m_128457_("x");
      }

      if (tag.m_128425_("y", 5)) {
         quaternion.y = tag.m_128457_("y");
      }

      if (tag.m_128425_("z", 5)) {
         quaternion.z = tag.m_128457_("z");
      }

      if (tag.m_128425_("w", 5)) {
         quaternion.w = tag.m_128457_("w");
      }

      return quaternion;
   }

   public static CompoundTag vector3fToTag(Vector3f vec) {
      CompoundTag tag = new CompoundTag();
      tag.m_128350_("x", vec.x);
      tag.m_128350_("y", vec.y);
      tag.m_128350_("z", vec.z);
      return tag;
   }

   public static Vector3f vector3fFromTag(CompoundTag tag) {
      return new Vector3f(tag.m_128457_("x"), tag.m_128457_("y"), tag.m_128457_("z"));
   }

   public static CompoundTag vec3ToTag(Vec3 vec) {
      CompoundTag tag = new CompoundTag();
      tag.m_128347_("x", vec.f_82479_);
      tag.m_128347_("y", vec.f_82480_);
      tag.m_128347_("z", vec.f_82481_);
      return tag;
   }

   public static Vec3 vec3FromTag(CompoundTag tag) {
      return new Vec3(tag.m_128459_("x"), tag.m_128459_("y"), tag.m_128459_("z"));
   }

   public static CompoundTag chunkPosToTag(ChunkPos pos) {
      CompoundTag tag = new CompoundTag();
      tag.m_128405_("chunkX", pos.f_45578_);
      tag.m_128405_("chunkZ", pos.f_45579_);
      return tag;
   }

   public static ChunkPos chunkPosFromTag(CompoundTag tag) {
      int x = tag.m_128451_("chunkX");
      int z = tag.m_128451_("chunkZ");
      return new ChunkPos(x, z);
   }

   public static CompoundTag vec2ToTag(Vec2 vec) {
      CompoundTag tag = new CompoundTag();
      tag.m_128347_("x", vec.f_82470_);
      tag.m_128347_("y", vec.f_82471_);
      return tag;
   }

   public static Vec2 vec2FromTag(CompoundTag tag) {
      return new Vec2(tag.m_128457_("x"), tag.m_128457_("y"));
   }

   public static void encodeVec3(FriendlyByteBuf buffer, Vec3 vec) {
      buffer.writeDouble(vec.f_82479_);
      buffer.writeDouble(vec.f_82480_);
      buffer.writeDouble(vec.f_82481_);
   }

   public static Vec3 decodeVec3(FriendlyByteBuf buffer) {
      return new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
   }

   public static void saveEnum(Enum<?> enub, CompoundTag tag, String id) {
      tag.m_128405_(id, enub.ordinal());
   }

   public static <T extends Enum<T>> T readEnum(Class<T> clazz, CompoundTag tag, String id) {
      T[] values = (T[])clazz.getEnumConstants();
      int ordinal = tag.m_128451_(id);
      return ordinal < values.length && ordinal >= 0 ? values[ordinal] : values[0];
   }

   public static Vector3f vec3ToVector3f(Vec3 vec) {
      return new Vector3f((float)vec.f_82479_, (float)vec.f_82480_, (float)vec.f_82481_);
   }

   public static Vec3 vector3fToVec3(Vector3f vec) {
      return new Vec3(vec);
   }

   public static Vec3 rotate(Vec3 vec, Quaternionf rotation) {
      Vector3f vecF = vec3ToVector3f(vec);
      vecF.rotate(rotation);
      return vector3fToVec3(vecF);
   }
}
