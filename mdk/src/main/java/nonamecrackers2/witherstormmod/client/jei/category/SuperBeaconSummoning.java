package nonamecrackers2.witherstormmod.client.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import nonamecrackers2.witherstormmod.client.instancing.RenderBufferer;
import nonamecrackers2.witherstormmod.client.jei.WitherStormModJEICompat;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.AdditionalHead;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.HeadManager;
import nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head.WitherStormHead;
import nonamecrackers2.witherstormmod.common.init.WitherStormModBlocks;
import nonamecrackers2.witherstormmod.common.item.crafting.ResummonSuperBeaconRecipe;
import nonamecrackers2.witherstormmod.common.item.crafting.SuperBeaconRecipe;

public class SuperBeaconSummoning extends SuperBeaconCategory<ResummonSuperBeaconRecipe> {
   private static final ResourceLocation ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath("witherstormmod", "textures/gui/jei/summoning_icon.png");
   private final IDrawable icon;

   public SuperBeaconSummoning(IGuiHelper helper) {
      super(helper);
      this.icon = new SuperBeaconCategory.Icon(helper.createDrawableItemStack(new ItemStack((ItemLike)WitherStormModBlocks.SUPER_BEACON.get())), ICON_TEXTURE);
   }

   public RecipeType<ResummonSuperBeaconRecipe> getRecipeType() {
      return WitherStormModJEICompat.SUPER_BEACON_SUMMONING;
   }

   public Component getTitle() {
      return Component.translatable("witherstormmod.jei.resummoning_super_beacon.title");
   }

   public IDrawable getIcon() {
      return this.icon;
   }

   protected void addResult(IRecipeLayoutBuilder builder, ResummonSuperBeaconRecipe recipe, IFocusGroup focuses, int x, int y, RegistryAccess access) {
      EntityType<?> type = recipe.getResummonEntity();
      if (type != null) {
         SpawnEggItem spawnEgg = ForgeSpawnEggItem.fromEntityType(type);
         if (spawnEgg != null) {
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(new ItemStack(spawnEgg));
         } else {
            builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(ItemStack.EMPTY);
         }

         Minecraft mc = Minecraft.getInstance();
         if (type.create(mc.level) instanceof LivingEntity living) {
            recipe.toRender = living;
            if (living instanceof WitherStormEntity storm) {
               storm.setPhase(4);
            }
         }
      } else {
         builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(ItemStack.EMPTY);
      }
   }

   public void draw(ResummonSuperBeaconRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
      if (recipe.toRender != null) {
         int x = this.getWidth() / 2;
         int y = this.getHeight() / 2;
         if (recipe.getCondition() != SuperBeaconRecipe.Condition.NONE) {
            y -= 5;
         }

         y = (int)((float)y + recipe.toRender.getBbHeight() / 2.0F * 20.0F);
         float angleX = (float)(-mouseX) + (float)x;
         float angleY = (float)(-mouseY) + (float)y - recipe.toRender.getEyeHeight() * 20.0F;
         stack.pose().pushPose();
         stack.pose().translate(0.0, 0.0, 50.0);
         if (recipe.toRender instanceof WitherStormEntity storm) {
            HeadManager manager = storm.getHeadManager();

            for (WitherStormHead head : manager.getHeads()) {
               if (head instanceof AdditionalHead additionalHead) {
                  additionalHead.yRot = angleX * 20.0F * (float) (Math.PI / 180.0) + 180.0F;
                  additionalHead.xRot = -angleY * 20.0F * (float) (Math.PI / 180.0);
                  additionalHead.yRotO = additionalHead.yRot;
                  additionalHead.xRotO = additionalHead.xRot;
               }
            }

            stack.pose().translate(80.0, 60.0, 0.0);
            stack.pose().scale(0.1F, 0.1F, 0.1F);
         }

         RenderBufferer.pushTempDisabled();
         InventoryScreen.renderEntityInInventoryFollowsMouse(stack, x, y, 20, angleX, angleY, recipe.toRender);
         stack.pose().popPose();
      }

      super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
   }

   public boolean isHandled(ResummonSuperBeaconRecipe recipe) {
      return !recipe.getId().toString().equals("witherstormmod:summon_pig");
   }
}
