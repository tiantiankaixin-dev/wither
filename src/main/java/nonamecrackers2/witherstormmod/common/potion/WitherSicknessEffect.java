package nonamecrackers2.witherstormmod.common.potion;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;
import nonamecrackers2.witherstormmod.common.util.AttributeModifierUtil;

public class WitherSicknessEffect extends MobEffect {
   public static final ResourceLocation MAX_HEALTH_MODIFIER = AttributeModifierUtil.id("wither_sickness_max_health");

   public WitherSicknessEffect(MobEffectCategory type, int color) {
      super(type, color);
   }

   @Override
   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
      LazyOptional<WitherSicknessTracker> trackerOptional = entity.getCapability(WitherStormModCapabilities.WITHER_SICKNESS_TRACKER);
      trackerOptional.ifPresent(tracker -> {
         if (!tracker.isBeingCured()) {
            float damagex = entity.getType().is(WitherStormModEntityTags.HIGH_IMMUNITY) ? 1.0F : 2.0F;
            entity.hurt(WitherStormModDamageTypes.source(entity.level().registryAccess(), WitherStormModDamageTypes.WITHER_SICKNESS), damagex);
            this.addToMaxHealthModifier(entity, -0.5, amplifier);
         }
      });
      if (!trackerOptional.isPresent()) {
         float damage = entity.getType().is(WitherStormModEntityTags.HIGH_IMMUNITY) ? 1.0F : 2.0F;
         entity.hurt(WitherStormModDamageTypes.source(entity.level().registryAccess(), WitherStormModDamageTypes.WITHER_SICKNESS), damage);
         this.addToMaxHealthModifier(entity, -0.5, amplifier);
      }

      return true;
   }

   @Override
   public boolean shouldApplyEffectTickThisTick(int p_76397_1_, int p_76397_2_) {
      int i = 7200 >> p_76397_2_;
      return i > 0 ? p_76397_1_ % i == 0 : true;
   }

   public void addToMaxHealthModifier(LivingEntity entity, double amount, int amplifier) {
      AttributeInstance instance = entity.getAttribute(Attributes.MAX_HEALTH);
      if (instance != null) {
         AttributeModifier modifier = instance.getModifier(MAX_HEALTH_MODIFIER);
         if (modifier != null) {
            double value = Math.max(entity.getAttributeBaseValue(Attributes.MAX_HEALTH) * -1.0 + 1.0, modifier.amount() + amount);
            this.updateAttributeModifier(entity, value);
         }
      }
   }

   public void updateAttributeModifier(LivingEntity entity, double amount) {
      AttributeInstance instance = entity.getAttributes().getInstance(Attributes.MAX_HEALTH);
      if (instance != null) {
         instance.removeModifier(MAX_HEALTH_MODIFIER);
         instance.addPermanentModifier(new AttributeModifier(MAX_HEALTH_MODIFIER, amount, Operation.ADD_VALUE));
      }
   }

   public void addAttributeModifiers(AttributeMap manager, int amplifier) {
      AttributeInstance instance = manager.getInstance(Attributes.MAX_HEALTH);
      if (instance != null && !instance.hasModifier(MAX_HEALTH_MODIFIER)) {
         instance.addPermanentModifier(new AttributeModifier(MAX_HEALTH_MODIFIER, 0.0, Operation.ADD_VALUE));
      }
   }

   public List<ItemStack> getCurativeItems() {
      return new ArrayList<>();
   }
}
