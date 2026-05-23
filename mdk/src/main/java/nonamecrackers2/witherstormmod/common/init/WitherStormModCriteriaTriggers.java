package nonamecrackers2.witherstormmod.common.init;

import net.minecraft.resources.ResourceLocation;
import nonamecrackers2.witherstormmod.WitherStormMod;
import nonamecrackers2.witherstormmod.common.advancements.criterion.ActivateSuperBeaconTrigger;
import nonamecrackers2.witherstormmod.common.advancements.criterion.CuredSickenedMobTrigger;
import nonamecrackers2.witherstormmod.common.advancements.criterion.LinkAmuletTrigger;
import nonamecrackers2.witherstormmod.common.advancements.criterion.SummonMobSuperBeaconTrigger;
import nonamecrackers2.witherstormmod.common.advancements.criterion.WitherStormTrigger;

public class WitherStormModCriteriaTriggers {
   public static final WitherStormTrigger PLAY_DEAD_TRIGGER = new WitherStormTrigger(new ResourceLocation("witherstormmod", "wither_storm_play_dead"));
   public static final WitherStormTrigger REVIVAL_TRIGGER = new WitherStormTrigger(new ResourceLocation("witherstormmod", "wither_storm_revival"));
   public static final WitherStormTrigger ESCAPE_STORM = new WitherStormTrigger(new ResourceLocation("witherstormmod", "escape_wither_storm"));
   public static final WitherStormTrigger RING_BELL_NEAR_STORM = new WitherStormTrigger(new ResourceLocation("witherstormmod", "ring_bell_near_storm"));
   public static final WitherStormTrigger NEARLY_KILL_WITHER_STORM = new WitherStormTrigger(WitherStormMod.id("nearly_kill_wither_storm"));
   public static final CuredSickenedMobTrigger CURED_SICKENED_MOB = new CuredSickenedMobTrigger();
   public static final ActivateSuperBeaconTrigger ACTIVATE_SUPER_BEACON = new ActivateSuperBeaconTrigger();
   public static final SummonMobSuperBeaconTrigger SUMMON_MOB_SUPER_BEACON = new SummonMobSuperBeaconTrigger();
   public static final LinkAmuletTrigger LINK_AMULET = new LinkAmuletTrigger();
}
