package nonamecrackers2.witherstormmod.api.common.event;

import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class WitherStormModifyEvolutionSpeedEvent extends WitherStormEvent {
   private double originalEvolutionSpeedModifier;

   public WitherStormModifyEvolutionSpeedEvent(WitherStormEntity storm, double evolutionSpeedModifier) {
      super(storm);
      this.originalEvolutionSpeedModifier = evolutionSpeedModifier;
   }

   public double getOriginalEvolutionSpeedModifier() {
      return this.originalEvolutionSpeedModifier;
   }

   public void setEvolutionSpeedModifier(double evolutionSpeedModifier) {
      this.originalEvolutionSpeedModifier = evolutionSpeedModifier;
   }
}
