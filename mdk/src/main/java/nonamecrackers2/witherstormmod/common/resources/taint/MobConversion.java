package nonamecrackers2.witherstormmod.common.resources.taint;

import net.minecraft.world.entity.EntityType;

public record MobConversion(EntityType<?> from, EntityType<?> to, boolean canBeConvertedFromWitherSickness) {
}
