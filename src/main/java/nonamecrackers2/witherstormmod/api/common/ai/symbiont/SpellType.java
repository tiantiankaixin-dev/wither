package nonamecrackers2.witherstormmod.api.common.ai.symbiont;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.sounds.SoundEvent;
import nonamecrackers2.witherstormmod.common.entity.WitheredSymbiontEntity;

public record SpellType(
   BiFunction<WitheredSymbiontEntity, SpellType, SymbiontSpell> spellFactory,
   int spellTime,
   Optional<Supplier<SoundEvent>> spellLoop,
   boolean doProtection,
   double protectionRadius,
   double protectionThrowStrength
) {
   public SpellType(
      BiFunction<WitheredSymbiontEntity, SpellType, SymbiontSpell> spellFactory, int spellTime, Optional<Supplier<SoundEvent>> spellLoop, boolean doProtection
   ) {
      this(spellFactory, spellTime, spellLoop, doProtection, 3.0, 1.0);
   }

   public SpellType(BiFunction<WitheredSymbiontEntity, SpellType, SymbiontSpell> spellFactory, int spellTime, Optional<Supplier<SoundEvent>> spellLoop) {
      this(spellFactory, spellTime, spellLoop, true);
   }

   public SymbiontSpell makeSpell(WitheredSymbiontEntity symbiont) {
      return this.spellFactory.apply(symbiont, this);
   }
}
