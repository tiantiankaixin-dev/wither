package nonamecrackers2.witherstormmod.common.potion;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import nonamecrackers2.witherstormmod.common.capability.WitherSicknessTracker;
import nonamecrackers2.witherstormmod.common.init.WitherStormModCapabilities;
import nonamecrackers2.witherstormmod.common.init.WitherStormModDamageTypes;
import nonamecrackers2.witherstormmod.common.tags.WitherStormModEntityTags;

public class WitherSicknessEffect extends MobEffect {
   public WitherSicknessEffect(MobEffectCategory type, int color) {
      super(type, color);
   }

   public void applyEffectTick(LivingEntity entity, int amplifier) {
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
   }

   public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
      int i = 7200 >> p_76397_2_;
      return i > 0 ? p_76397_1_ % i == 0 : true;
   }

   public void addToMaxHealthModifier(LivingEntity entity, double amount, int amplifier) {
      Attribute attribute = Attributes.MAX_HEALTH;
      if (this.getAttributeModifiers().containsKey(attribute)) {
         UUID id = ((AttributeModifier)this.getAttributeModifiers().get(attribute)).getId();
         AttributeModifier modifier = entity.getAttribute(attribute).getModifier(id);
         if (modifier != null) {
            double value = modifier.getAmount();
            value = Math.max(entity.getAttributeBaseValue(attribute) * -1.0 + 1.0, value + amount);
            this.updateAttributeModifier(entity, Attributes.MAX_HEALTH, value + amount, amplifier);
         }
      }
   }

   public void updateAttributeModifier(LivingEntity entity, Attribute attribute, double amount, int amplifier) {
      UUID id = ((AttributeModifier)this.getAttributeModifiers().get(attribute)).getId();
      AttributeModifier modifier = entity.getAttribute(attribute).getModifier(id);
      AttributeInstance instance = entity.getAttributes().getInstance(attribute);
      if (instance != null) {
         instance.removeModifier(modifier);
         AttributeModifier newModifier = new AttributeModifier(modifier.getId(), this.getDescriptionId() + " " + amplifier, amount, modifier.getOperation());
         instance.addPermanentModifier(newModifier);
      }
   }

   public void addAttributeModifiers(LivingEntity entity, AttributeMap manager, int amplifier) {
      for (Entry<Attribute, AttributeModifier> entry : this.getAttributeModifiers().entrySet()) {
         AttributeInstance instance = manager.getInstance(entry.getKey());
         if (instance != null) {
            AttributeModifier modifier = entry.getValue();
            if (!instance.hasModifier(modifier)) {
               instance.addPermanentModifier(
                  new AttributeModifier(modifier.getId(), this.getDescriptionId() + " " + amplifier, this.getAttributeModifierValue(amplifier, modifier), modifier.getOperation())
               );
            } else {
               AttributeModifier original = instance.getModifier(modifier.getId());
               instance.removeModifier(modifier);
               instance.addPermanentModifier(
                  new AttributeModifier(original.getId(), this.getDescriptionId() + " " + amplifier, this.getAttributeModifierValue(amplifier, original), original.getOperation())
               );
            }
         }
      }
   }

   public List<ItemStack> getCurativeItems() {
      return new ArrayList<>();
   }
}
