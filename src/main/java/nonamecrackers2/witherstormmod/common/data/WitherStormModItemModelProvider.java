package nonamecrackers2.witherstormmod.common.data;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import nonamecrackers2.witherstormmod.common.init.WitherStormModItems;

public class WitherStormModItemModelProvider extends ItemModelProvider {
   public WitherStormModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
      super(output, "witherstormmod", existingFileHelper);
   }

   protected void registerModels() {
      this.handheld((Item)WitherStormModItems.WITHERED_BONE.get());
      this.basicItem((Item)WitherStormModItems.WITHERED_FLESH.get());
      this.basicItem((Item)WitherStormModItems.TAINTED_DUST.get());
      this.basicItem((Item)WitherStormModItems.WITHERED_SPIDER_EYE.get());
      this.basicItem((Item)WitherStormModItems.GOLDEN_APPLE_STEW.get());
      this.basicItem((Item)WitherStormModItems.AMULET.get());
      this.basicItem((Item)WitherStormModItems.COMMAND_BLOCK_BOOK.get());
      this.basicItem((Item)WitherStormModItems.WITHERED_NETHER_STAR.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_CREEPER_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_SKELETON_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_SPIDER_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_VILLAGER_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_ZOMBIE_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_PHANTOM_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_CHICKEN_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_PARROT_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_WOLF_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_CAT_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_COW_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_PIG_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_MUSHROOM_COW_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_BEE_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_PILLAGER_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_VINDICATOR_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_IRON_GOLEM_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.SICKENED_SNOW_GOLEM_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.TENTACLE_SPAWN_EGG.get());
      this.spawnEgg((Item)WitherStormModItems.WITHERED_SYMBIONT_SPAWN_EGG.get());
      this.handheld((Item)WitherStormModItems.COMMAND_BLOCK_SWORD.get());
      this.handheld((Item)WitherStormModItems.COMMAND_BLOCK_PICKAXE.get());
      this.handheld((Item)WitherStormModItems.COMMAND_BLOCK_AXE.get());
      this.handheld((Item)WitherStormModItems.COMMAND_BLOCK_SHOVEL.get());
      this.handheld((Item)WitherStormModItems.COMMAND_BLOCK_HOE.get());
      this.handheld((Item)WitherStormModItems.WOOD_COMMAND_BLOCK_SWORD.get());
      this.handheld((Item)WitherStormModItems.WOOD_COMMAND_BLOCK_PICKAXE.get());
      this.handheld((Item)WitherStormModItems.WOOD_COMMAND_BLOCK_AXE.get());
      this.handheld((Item)WitherStormModItems.WOOD_COMMAND_BLOCK_SHOVEL.get());
      this.handheld((Item)WitherStormModItems.WOOD_COMMAND_BLOCK_HOE.get());
      this.handheld((Item)WitherStormModItems.STONE_COMMAND_BLOCK_SWORD.get());
      this.handheld((Item)WitherStormModItems.STONE_COMMAND_BLOCK_PICKAXE.get());
      this.handheld((Item)WitherStormModItems.STONE_COMMAND_BLOCK_AXE.get());
      this.handheld((Item)WitherStormModItems.STONE_COMMAND_BLOCK_SHOVEL.get());
      this.handheld((Item)WitherStormModItems.STONE_COMMAND_BLOCK_HOE.get());
      this.handheld((Item)WitherStormModItems.IRON_COMMAND_BLOCK_SWORD.get());
      this.handheld((Item)WitherStormModItems.IRON_COMMAND_BLOCK_PICKAXE.get());
      this.handheld((Item)WitherStormModItems.IRON_COMMAND_BLOCK_AXE.get());
      this.handheld((Item)WitherStormModItems.IRON_COMMAND_BLOCK_SHOVEL.get());
      this.handheld((Item)WitherStormModItems.IRON_COMMAND_BLOCK_HOE.get());
      this.handheld((Item)WitherStormModItems.GOLD_COMMAND_BLOCK_SWORD.get());
      this.handheld((Item)WitherStormModItems.GOLD_COMMAND_BLOCK_PICKAXE.get());
      this.handheld((Item)WitherStormModItems.GOLD_COMMAND_BLOCK_AXE.get());
      this.handheld((Item)WitherStormModItems.GOLD_COMMAND_BLOCK_SHOVEL.get());
      this.handheld((Item)WitherStormModItems.GOLD_COMMAND_BLOCK_HOE.get());
      this.basicItem((Item)WitherStormModItems.TAINTED_DOOR.get());
      ((ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(WitherStormModItems.TAINTED_FLESH_VEINS.getId().toString()))
               .parent(new UncheckedModelFile("item/generated")))
            .texture("layer0", this.modLoc("block/tainted_flesh_veins")))
         .renderType("cutout");
      this.basicItem((Item)WitherStormModItems.TAINTED_SIGN.get());
      ((ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(WitherStormModItems.TAINTED_GLASS_PANE.getId().toString()))
               .parent(new UncheckedModelFile("item/generated")))
            .texture("layer0", this.modLoc("block/tainted_glass")))
         .renderType("translucent");
      this.buttonInventory(WitherStormModItems.TAINTED_STONE_BUTTON.getId().getPath(), this.modLoc("block/tainted_stone"));
      this.buttonInventory(WitherStormModItems.TAINTED_BUTTON.getId().getPath(), this.modLoc("block/tainted_planks"));
      this.wallInventory(WitherStormModItems.TAINTED_COBBLESTONE_WALL.getId().getPath(), this.modLoc("block/tainted_cobblestone"));
      this.wallInventory(WitherStormModItems.TAINTED_SANDSTONE_WALL.getId().getPath(), this.modLoc("block/tainted_sandstone"));
      this.wallInventory(WitherStormModItems.TAINTED_SMOOTH_SANDSTONE_WALL.getId().getPath(), this.modLoc("block/tainted_sandstone_top"));
      this.fenceInventory(WitherStormModItems.TAINTED_FENCE.getId().getPath(), this.modLoc("block/tainted_planks"));
      ((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(WitherStormModItems.TAINTED_MUSHROOM.getId().toString()))
            .parent(new UncheckedModelFile("item/generated")))
         .texture("layer0", this.modLoc("block/tainted_mushroom"));
      ((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(WitherStormModItems.TAINTED_TORCH.getId().toString()))
            .parent(new UncheckedModelFile("item/generated")))
         .texture("layer0", this.modLoc("block/tainted_torch"));
      this.handheld((Item)WitherStormModItems.EYE_OF_THE_STORM.get());
      this.handheld((Item)WitherStormModItems.FORMIDI_BLADE.get());
      ModelFile poweringUp = ((ItemModelBuilder)this.withExistingParent(WitherStormModItems.FORMIDI_BLADE.getId().getPath() + "_powering_up", "item/handheld"))
         .texture("layer0", this.modLoc("item/formidi_blade_powering_up"));
      ModelFile poweredUp = ((ItemModelBuilder)this.withExistingParent(WitherStormModItems.FORMIDI_BLADE.getId().getPath() + "_powered_up", "item/handheld"))
         .texture("layer0", this.modLoc("item/formidi_blade_powered_up"));
      ((ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(WitherStormModItems.FORMIDI_BLADE.getId().getPath(), "item/handheld"))
            .texture("layer0", this.modLoc("item/formidi_blade")))
         .override()
         .model(poweringUp)
         .predicate(this.modLoc("anim"), 0.5F)
         .end()
         .override()
         .model(poweredUp)
         .predicate(this.modLoc("anim"), 1.0F)
         .end()
         .override()
         .model(poweringUp)
         .predicate(this.modLoc("anim"), 1.5F)
         .end();
      ((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(WitherStormModItems.TAINTED_TORCH.getId().toString()))
            .parent(new UncheckedModelFile("item/generated")))
         .texture("layer0", this.modLoc("block/tainted_torch"));
   }

   private void handheld(Item item) {
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
      ((ItemModelBuilder)this.withExistingParent(id.getPath(), "item/handheld")).texture("layer0", this.modLoc("item/" + id.getPath()));
   }

   private void spawnEgg(Item item) {
      ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
      this.withExistingParent(id.getPath(), "item/template_spawn_egg");
   }
}
