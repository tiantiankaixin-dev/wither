package nonamecrackers2.witherstormmod.common.util;

/**
 * Compatibility enum for IronGolem crackiness levels.
 * In 1.21, the IronGolem.Crackiness enum was removed.
 * This enum provides the same functionality for the mod's sickened iron golem.
 */
public enum IronGolemCrackiness {
    NONE,
    LOW,
    MEDIUM,
    HIGH;
    
    public static IronGolemCrackiness byFraction(float healthFraction) {
        if (healthFraction < 0.25F) {
            return HIGH;
        } else if (healthFraction < 0.5F) {
            return MEDIUM;
        } else if (healthFraction < 0.75F) {
            return LOW;
        } else {
            return NONE;
        }
    }
}
