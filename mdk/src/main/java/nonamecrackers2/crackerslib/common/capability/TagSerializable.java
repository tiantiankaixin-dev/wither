package nonamecrackers2.crackerslib.common.capability;

import net.minecraft.nbt.CompoundTag;

public interface TagSerializable {
   default CompoundTag write() {
      CompoundTag tag = new CompoundTag();
      this.write(tag);
      return tag;
   }

   void write(CompoundTag var1);

   void read(CompoundTag var1);
}
