package nonamecrackers2.witherstormmod.common.command.argument.entityselector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.commands.IEntitySelectorType;
import nonamecrackers2.witherstormmod.common.init.WitherStormModEntityTypes;

public class WitherStormSelector implements IEntitySelectorType {
   public EntitySelector build(EntitySelectorParser parser) throws CommandSyntaxException {
      parser.setMaxResults(1);
      parser.setIncludesEntities(true);
      parser.setOrder(EntitySelectorParser.ORDER_NEAREST);
      parser.limitToType(WitherStormModEntityTypes.WITHER_STORM.get());
      parser.addPredicate(Entity::isAlive);
      StringReader reader = parser.getReader();
      parser.setSuggestions((b, c) -> suggestOpenOptions(parser, b));
      if (reader.canRead() && reader.peek() == '[') {
         reader.skip();
         parser.setSuggestions((b, c) -> suggestOptionsKeyOrClose(parser, b));
         parser.parseOptions();
      }

      parser.finalizePredicates();
      return parser.getSelector();
   }

   private static CompletableFuture<Suggestions> suggestOpenOptions(EntitySelectorParser parser, SuggestionsBuilder builder) {
      builder.suggest(String.valueOf('['));
      return builder.buildFuture();
   }

   private static CompletableFuture<Suggestions> suggestOptionsKeyOrClose(EntitySelectorParser parser, SuggestionsBuilder builder) {
      builder.suggest(String.valueOf(']'));
      EntitySelectorOptions.suggestNames(parser, builder);
      return builder.buildFuture();
   }

   public Component getSuggestionTooltip() {
      return Component.translatable("argument.witherstormmod.entity.selector.wither_storm");
   }
}
