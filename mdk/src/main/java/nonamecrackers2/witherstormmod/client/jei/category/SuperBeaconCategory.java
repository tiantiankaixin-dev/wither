package nonamecrackers2.witherstormmod.client.jei.category;

import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Ingredient;
import nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe;

public abstract class SuperBeaconCategory<T extends SuperBeaconRecipe> implements IRecipeCategory<T> {
   protected static final ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/gui/jei/slot.png");
   private final IDrawable background;
   private final IDrawable slotBackground;

   protected SuperBeaconCategory(IGuiHelper helper) {
      this.background = helper.createBlankDrawable(180, 120);
      this.slotBackground = helper.createDrawable(SLOT, 0, 0, 18, 18);
   }

   public IDrawable getBackground() {
      return this.background;
   }

   public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
      List<Ingredient> ingredients = recipe.getIngredients();
      int totalSize = ingredients.size();
      float angleOffset = 360.0F / (float)totalSize;
      int halfWidth = this.getWidth() / 2;
      int halfHeight = this.getHeight() / 2;
      if (recipe.getCondition() != SuperBeaconRecipe.Condition.NONE) {
         halfHeight -= 5;
      }

      for (int i = 0; i < totalSize; i++) {
         float angle = angleOffset * (float)i;
         float x = Mth.cos(angle * (float) (Math.PI / 180.0)) * 40.0F + (float)halfWidth - 8.0F;
         float y = Mth.sin(angle * (float) (Math.PI / 180.0)) * 40.0F + (float)halfHeight - 8.0F;
         Ingredient ingredient = ingredients.get(i);
         this.addSlot(builder, RecipeIngredientRole.INPUT, (int)x, (int)y).addIngredients(ingredient);
      }

      Minecraft mc = Minecraft.getInstance();
      this.addResult(builder, recipe, focuses, halfWidth, halfHeight, mc.level.registryAccess());
   }

   protected IRecipeSlotBuilder addSlot(IRecipeLayoutBuilder builder, RecipeIngredientRole role, int x, int y) {
      return builder.addSlot(role, x, y).setBackground(this.slotBackground, -1, -1);
   }

   protected abstract void addResult(IRecipeLayoutBuilder var1, T var2, IFocusGroup var3, int var4, int var5, RegistryAccess var6);

   public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
      String desc = recipe.getCondition().getDescription();
      if (desc != null) {
         Minecraft mc = Minecraft.getInstance();
         stack.drawCenteredString(mc.font, Component.translatable(desc), this.getWidth() / 2, this.getHeight() - 9 - 1, -1);
      }

   }

   protected static class Icon implements IDrawable {
      private final IDrawable wrapped;
      private final ResourceLocation iconTexture;

      public Icon(IDrawable wrapped, ResourceLocation iconTexture) {
         this.wrapped = wrapped;
         this.iconTexture = iconTexture;
      }

      public int getWidth() {
         return this.wrapped.getWidth();
      }

      public int getHeight() {
         return this.wrapped.getHeight();
      }

      public void draw(GuiGraphics stack, int xOffset, int yOffset) {
         this.wrapped.draw(stack, xOffset, yOffset);
         stack.blit(this.iconTexture, xOffset + 8, yOffset + 8, 0, 0.0F, 0.0F, 8, 8, 8, 8);
      }
   }
}
