package nonamecrackers2.witherstormmod.common.item;

import java.util.List;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class EyeOfTheStormItem extends CommandBlockSwordItem {
   public static final UUID DAMAGE_MODIFIER_ID = UUID.fromString("823350e7-4c91-4a1f-8c01-8735113f066e");
   public static final String ENTITY_HEALTH_RATIO = "EntityHealthRatio";

   public EyeOfTheStormItem(Tier tier, int damage, float attackSpeed, Properties properties) {
      super(tier, damage, attackSpeed, properties);
   }

   public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
      CompoundTag tag = stack.getOrCreateTag();
      if (entity instanceof LivingEntity living && (!(living instanceof Player) || !((Player)living).getAbilities().instabuild)) {
         tag.putFloat("EntityHealthRatio", living.getHealth() / living.getMaxHealth());
         return;
      }

      tag.remove("EntityHealthRatio");
   }

   public void appendHoverText(ItemStack stack, Level level, List<Component> text, TooltipFlag flag) {
      text.add(Component.translatable("item.witherstormmod.eye_of_the_storm.author").withStyle(ChatFormatting.DARK_GRAY));
   }

   public boolean hurtEnemy(ItemStack stack, LivingEntity hit, LivingEntity living) {
      if (!super.hurtEnemy(stack, hit, living)) {
         return false;
      } else {
         if (living.getRandom().nextFloat() > living.getHealth() / living.getMaxHealth() || living instanceof Player player && player.getAbilities().instabuild) {
            double minHeight = Math.min(hit.getY(), living.getY());
            double maxHeight = Math.max(hit.getY(), living.getY()) + 1.0;
            int total = 5;
            int spread = 2;
            float hitAngle = (float)Mth.atan2(hit.getZ() - living.getZ(), hit.getX() - living.getX());
            float damageModifier = 0.0F; // TODO_MIG[ENCHANT]: 1.21 EnchantmentHelper.getDamageBonus signature changed; reimplement against new lookup-based API
            createSpike(living, hit.getX(), hit.getZ(), minHeight, maxHeight, hitAngle, 0, damageModifier);

            for (int i = 0; i < total; i++) {
               float angle = (float)i / (float)total * (float) Math.PI * 2.0F + hitAngle;

               for (int j = 0; j < spread; j++) {
                  double x = hit.getX() + (double)Mth.cos(angle) * (double)(j + 1);
                  double z = hit.getZ() + (double)Mth.sin(angle) * (double)(j + 1);
                  createSpike(living, x, z, minHeight, maxHeight, angle, (j + 1) * 5 + living.getRandom().nextInt(4) - 2, damageModifier);
               }
            }
         }

         return true;
      }
   }

   private static void createSpike(LivingEntity param0, double param1, double param3, double param5, double param7, float param9, int param10, float param11) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      //
      // Bytecode:
      // 00: dload 1
      // 01: dload 7
      // 03: dload 3
      // 04: invokestatic net/minecraft/core/BlockPos.containing (DDD)Lnet/minecraft/core/BlockPos;
      // 07: astore 12
      // 09: bipush 0
      // 0a: istore 13
      // 0c: dconst_0
      // 0d: dstore 14
      // 0f: aload 12
      // 11: invokevirtual net/minecraft/core/BlockPos.below ()Lnet/minecraft/core/BlockPos;
      // 14: astore 16
      // 16: aload 0
      // 17: invokevirtual net/minecraft/world/entity/LivingEntity.level ()Lnet/minecraft/world/level/Level;
      // 1a: aload 16
      // 1c: invokevirtual net/minecraft/world/level/Level.getBlockState (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
      // 1f: astore 17
      // 21: aload 17
      // 23: aload 0
      // 24: invokevirtual net/minecraft/world/entity/LivingEntity.level ()Lnet/minecraft/world/level/Level;
      // 27: aload 16
      // 29: getstatic net/minecraft/core/Direction.UP Lnet/minecraft/core/Direction;
      // 2c: invokevirtual net/minecraft/world/level/block/state/BlockState.isFaceSturdy (Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z
      // 2f: ifeq 6e
      // 32: aload 0
      // 33: invokevirtual net/minecraft/world/entity/LivingEntity.level ()Lnet/minecraft/world/level/Level;
      // 36: aload 12
      // 38: invokevirtual net/minecraft/world/level/Level.isEmptyBlock (Lnet/minecraft/core/BlockPos;)Z
      // 3b: ifne 68
      // 3e: aload 0
      // 3f: invokevirtual net/minecraft/world/entity/LivingEntity.level ()Lnet/minecraft/world/level/Level;
      // 42: aload 12
      // 44: invokevirtual net/minecraft/world/level/Level.getBlockState (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;
      // 47: astore 18
      // 49: aload 18
      // 4b: aload 0
      // 4c: invokevirtual net/minecraft/world/entity/LivingEntity.level ()Lnet/minecraft/world/level/Level;
      // 4f: aload 12
      // 51: invokevirtual net/minecraft/world/level/block/state/BlockState.getCollisionShape (Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;
      // 54: astore 19
      // 56: aload 19
      // 58: invokevirtual net/minecraft/world/phys/shapes/VoxelShape.isEmpty ()Z
      // 5b: ifne 68
      // 5e: aload 19
      // 60: getstatic net/minecraft/core/Direction$Axis.Y Lnet/minecraft/core/Direction$Axis;
      // 63: invokevirtual net/minecraft/world/phys/shapes/VoxelShape.max (Lnet/minecraft/core/Direction$Axis;)D
      // 66: dstore 14
      // 68: bipush 1
      // 69: istore 13
      // 6b: goto 84
      // 6e: aload 12
      // 70: invokevirtual net/minecraft/core/BlockPos.below ()Lnet/minecraft/core/BlockPos;
      // 73: astore 12
      // 75: aload 12
      // 77: invokevirtual net/minecraft/core/BlockPos.getY ()I
      // 7a: dload 5
      // 7c: invokestatic net/minecraft/util/Mth.floor (D)I
      // 7f: bipush 1
      // 80: isub
      // 81: if_icmpge 0f
      // 84: iload 13
      // 86: ifeq ae
      // 89: aload 0
      // 8a: invokevirtual net/minecraft/world/entity/LivingEntity.level ()Lnet/minecraft/world/level/Level;
      // 8d: new nonamecrackers2/witherstormmod/common/entity/TentacleSpike
      // 90: dup
      // 91: aload 0
      // 92: invokevirtual net/minecraft/world/entity/LivingEntity.level ()Lnet/minecraft/world/level/Level;
      // 95: dload 1
      // 96: aload 12
      // 98: invokevirtual net/minecraft/core/BlockPos.getY ()I
      // 9b: i2d
      // 9c: dload 14
      // 9e: dadd
      // 9f: dload 3
      // a0: fload 9
      // a2: iload 10
      // a4: aload 0
      // a5: fload 11
      // a7: invokespecial nonamecrackers2/witherstormmod/common/entity/TentacleSpike.<init> (Lnet/minecraft/world/level/Level;DDDFILnet/minecraft/world/entity/LivingEntity;F)V
      // aa: invokevirtual net/minecraft/world/level/Level.addFreshEntity (Lnet/minecraft/world/entity/Entity;)Z
      // ad: pop
      // ae: return
   }
}
