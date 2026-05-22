package nonamecrackers2.witherstormmod.client.capability;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import nonamecrackers2.witherstormmod.client.audio.FormidibombFuseLoop;
import nonamecrackers2.witherstormmod.client.audio.ISoundManager;
import nonamecrackers2.witherstormmod.common.util.IFormidibomb;
import nonamecrackers2.witherstormmod.common.util.WorldUtil;

public class FormidibombEffectsManager implements ISoundManager {
   private final Minecraft minecraft;
   @Nullable
   private IFormidibomb formidibomb;
   private final List<FormidibombFuseLoop> loops = Lists.newArrayList();

   public FormidibombEffectsManager(Minecraft minecraft) {
      this.minecraft = minecraft;
   }

   public FormidibombEffectsManager() {
      this.minecraft = null;
   }

   @Override
   public void tick() {
      ClientLevel world = this.minecraft.level;
      IFormidibomb formidibomb = null;

      for (Entity entity : world.entitiesForRendering()) {
         if (entity instanceof IFormidibomb) {
            IFormidibomb current = (IFormidibomb)entity;
            formidibomb = this.compare(formidibomb, current);
            if (!this.alreadyHasLoop(current)) {
               this.putLoop(new FormidibombFuseLoop(current));
            }
         }
      }

      for (BlockEntity tile : WorldUtil.getBlockEntitiesInAABB(world, this.minecraft.player.getBoundingBox().inflate(50.0))) {
         if (tile instanceof IFormidibomb) {
            IFormidibomb current = (IFormidibomb)tile;
            formidibomb = this.compare(formidibomb, current);
            if (!this.alreadyHasLoop(current)) {
               this.putLoop(new FormidibombFuseLoop(current));
            }
         }
      }

      this.formidibomb = formidibomb;

      for (int i = 0; i < this.loops.size(); i++) {
         FormidibombFuseLoop loop = this.loops.get(i);
         if (loop.isStopped() || loop.shouldStop()) {
            this.loops.remove(i);
         }
      }
   }

   public IFormidibomb compare(@Nullable IFormidibomb previous, IFormidibomb current) {
      if (previous != null) {
         if (current.getStartFuse() > 0) {
            int percentage = (current.getStartFuse() - current.getFuseLife()) / current.getStartFuse();
            if (previous.getStartFuse() > 0) {
               return percentage > (previous.getStartFuse() - previous.getFuseLife()) / previous.getStartFuse() ? current : previous;
            } else {
               return current;
            }
         } else {
            return previous;
         }
      } else {
         return current;
      }
   }

   private boolean alreadyHasLoop(IFormidibomb formidibomb) {
      boolean flag = false;

      for (FormidibombFuseLoop loop : this.loops) {
         if (loop.formidibomb == formidibomb) {
            flag = true;
            break;
         }
      }

      return flag;
   }

   private void putLoop(FormidibombFuseLoop loop) {
      if (!this.loops.contains(loop)) {
         this.loops.add(loop);
         this.minecraft.getSoundManager().queueTickingSound(loop);
      }
   }

   public int getLife() {
      return this.formidibomb != null ? this.formidibomb.getFuseLife() : 0;
   }

   public int getStartFuse() {
      return this.formidibomb != null ? this.formidibomb.getStartFuse() : 0;
   }

   @Override
   public void refresh() {
      List<FormidibombFuseLoop> soundsToAdd = new ArrayList<>();

      for (int i = 0; i < this.loops.size(); i++) {
         FormidibombFuseLoop loop = this.loops.get(i);
         FormidibombFuseLoop newLoop = new FormidibombFuseLoop(loop.formidibomb);
         loop.forceStop();
         soundsToAdd.add(newLoop);
         this.minecraft.getSoundManager().queueTickingSound(newLoop);
      }

      this.loops.clear();
      this.loops.addAll(soundsToAdd);
   }
}
