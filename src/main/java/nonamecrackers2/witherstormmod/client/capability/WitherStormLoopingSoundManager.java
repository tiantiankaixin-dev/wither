package nonamecrackers2.witherstormmod.client.capability;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import nonamecrackers2.witherstormmod.client.audio.ISoundManager;
import nonamecrackers2.witherstormmod.client.audio.WitherStormSoundLoop;
import nonamecrackers2.witherstormmod.client.audio.WitherStormTractorBeamLoop;
import nonamecrackers2.witherstormmod.common.init.WitherStormModSoundEvents;

public class WitherStormLoopingSoundManager implements ISoundManager {
   private final Minecraft minecraft;
   private final Int2ObjectMap<WitherStormSoundLoop> sounds = new Int2ObjectOpenHashMap();
   private final Int2ObjectMap<WitherStormSoundLoop> pendingReplacements = new Int2ObjectOpenHashMap();
   private final Int2ObjectMap<WitherStormSoundLoop> additional = new Int2ObjectOpenHashMap();
   private final Int2ObjectMap<WitherStormTractorBeamLoop[]> beamLoops = new Int2ObjectOpenHashMap();

   public WitherStormLoopingSoundManager(Minecraft mc) {
      this.minecraft = mc;
   }

   public WitherStormLoopingSoundManager() {
      this.minecraft = null;
   }

   @Override
   public void tick() {
      this.applyPendingReplacements();
      this.removeAllPendingRemovals();
   }

   private void applyPendingReplacements() {
      ObjectIterator<Entry<WitherStormSoundLoop>> iterator = this.pendingReplacements.int2ObjectEntrySet().iterator();

      while (iterator.hasNext()) {
         Entry<WitherStormSoundLoop> entry = (Entry<WitherStormSoundLoop>)iterator.next();
         ((WitherStormSoundLoop)this.sounds.get(entry.getIntKey())).stopSound();
         WitherStormSoundLoop replacement = (WitherStormSoundLoop)entry.getValue();
         this.sounds.replace(entry.getIntKey(), replacement);
         this.pendingReplacements.remove(entry.getIntKey());
         this.minecraft.getSoundManager().queueTickingSound(replacement);
      }
   }

   private void removeAllPendingRemovals() {
      ObjectIterator<Entry<WitherStormSoundLoop>> iterator = this.sounds.int2ObjectEntrySet().iterator();

      while (iterator.hasNext()) {
         Entry<WitherStormSoundLoop> entry = (Entry<WitherStormSoundLoop>)iterator.next();
         WitherStormSoundLoop sound = (WitherStormSoundLoop)entry.getValue();
         if (sound.isStopped()) {
            iterator.remove();
         }
      }

      ObjectIterator<Entry<WitherStormTractorBeamLoop[]>> iterator1 = this.beamLoops.int2ObjectEntrySet().iterator();

      while (iterator1.hasNext()) {
         Entry<WitherStormTractorBeamLoop[]> entry = (Entry<WitherStormTractorBeamLoop[]>)iterator1.next();
         WitherStormTractorBeamLoop[] loops = (WitherStormTractorBeamLoop[])entry.getValue();

         for (int i = 0; i < loops.length; i++) {
            if (loops[i] != null && loops[i].isStopped()) {
               loops[i] = null;
            }
         }
      }

      ObjectIterator<Entry<WitherStormSoundLoop>> iterator2 = this.additional.int2ObjectEntrySet().iterator();

      while (iterator2.hasNext()) {
         Entry<WitherStormSoundLoop> entry = (Entry<WitherStormSoundLoop>)iterator2.next();
         WitherStormSoundLoop sound = (WitherStormSoundLoop)entry.getValue();
         if (sound.isStopped()) {
            iterator2.remove();
         }
      }
   }

   public void putSound(int id, WitherStormSoundLoop sound) {
      if (!this.sounds.containsKey(id)) {
         this.minecraft.getSoundManager().queueTickingSound(sound);
         this.sounds.put(id, sound);
      }
   }

   public void putAdditionalSound(int id, WitherStormSoundLoop sound) {
      if (!this.additional.containsKey(id)) {
         this.minecraft.getSoundManager().queueTickingSound(sound);
         this.additional.put(id, sound);
      }
   }

   @Override
   public void refresh() {
      ObjectIterator<Entry<WitherStormSoundLoop>> iterator = this.sounds.int2ObjectEntrySet().iterator();

      while (iterator.hasNext()) {
         Entry<WitherStormSoundLoop> entry = (Entry<WitherStormSoundLoop>)iterator.next();
         WitherStormSoundLoop sound = (WitherStormSoundLoop)entry.getValue();
         sound.forceStop();
         int key = entry.getIntKey();
         this.sounds.remove(entry.getIntKey());
         if (key != 0) {
            this.putSound(key, new WitherStormSoundLoop(sound.getPos(), sound.getSoundEvent()));
         }
      }

      ObjectIterator<Entry<WitherStormTractorBeamLoop[]>> iterator1 = this.beamLoops.int2ObjectEntrySet().iterator();

      while (iterator1.hasNext()) {
         Entry<WitherStormTractorBeamLoop[]> entry = (Entry<WitherStormTractorBeamLoop[]>)iterator1.next();
         WitherStormTractorBeamLoop[] loops = (WitherStormTractorBeamLoop[])entry.getValue();

         for (int i = 0; i < loops.length; i++) {
            if (loops[i] != null) {
               loops[i].forceStop();
               WitherStormTractorBeamLoop newLoop = new WitherStormTractorBeamLoop(loops[i].getEntity(), loops[i].getHead());
               loops[i] = null;
               this.putBeamSound(newLoop.getEntity().getId(), newLoop.getHead(), newLoop);
            }
         }
      }

      ObjectIterator<Entry<WitherStormSoundLoop>> iterator2 = this.additional.int2ObjectEntrySet().iterator();

      while (iterator2.hasNext()) {
         Entry<WitherStormSoundLoop> entry = (Entry<WitherStormSoundLoop>)iterator2.next();
         WitherStormSoundLoop sound = (WitherStormSoundLoop)entry.getValue();
         sound.forceStop();
         int key = entry.getIntKey();
         this.additional.remove(entry.getIntKey());
         if (key != 0) {
            this.putSound(key, new WitherStormSoundLoop(sound.getPos(), sound.getSoundEvent()));
         }
      }
   }

   public WitherStormSoundLoop getSound(int id) {
      return (WitherStormSoundLoop)this.sounds.get(id);
   }

   public WitherStormSoundLoop getAdditional(int id) {
      return (WitherStormSoundLoop)this.additional.get(id);
   }

   public WitherStormTractorBeamLoop[] getBeamSound(int id) {
      return (WitherStormTractorBeamLoop[])this.beamLoops.get(id);
   }

   public void putBeamSound(int id, int head, WitherStormTractorBeamLoop loop) {
      WitherStormTractorBeamLoop[] loops = new WitherStormTractorBeamLoop[3];
      if (this.beamLoops.containsKey(id)) {
         loops = (WitherStormTractorBeamLoop[])this.beamLoops.get(id);
      }

      loops[head] = loop;
      this.beamLoops.put(id, loops);
      this.minecraft.getSoundManager().queueTickingSound(loop);
   }

   public boolean alreadyHasLoop(int id, int head) {
      if (!this.beamLoops.containsKey(id)) {
         return false;
      } else {
         WitherStormTractorBeamLoop[] loop = (WitherStormTractorBeamLoop[])this.beamLoops.get(id);
         return loop[head] != null && !loop[head].isStopped();
      }
   }

   public void stopSound(int id) {
      if (this.sounds.containsKey(id)) {
         ((WitherStormSoundLoop)this.sounds.get(id)).stopSound();
      }
   }

   public void stopAdditional(int id) {
      if (this.additional.containsKey(id)) {
         ((WitherStormSoundLoop)this.additional.get(id)).stopSound();
      }
   }

   public void replace(int id, WitherStormSoundLoop sound) {
      this.pendingReplacements.putIfAbsent(id, sound);
   }

   public WitherStormSoundLoop getReplacement(int id) {
      return (WitherStormSoundLoop)this.pendingReplacements.get(id);
   }

   public boolean alreadyHasReplacement(int id) {
      return this.pendingReplacements.containsKey(id);
   }

   public static SoundEvent getSoundBasedOnDistance(float distance) {
      SoundEvent event = WitherStormModSoundEvents.WITHER_STORM_CLOSE_LOOP.get();
      if (distance > 3.0F && distance < 6.0F) {
         event = WitherStormModSoundEvents.WITHER_STORM_DISTANT_LOOP.get();
      } else if (distance > 6.0F) {
         event = WitherStormModSoundEvents.WITHER_STORM_FAR_LOOP.get();
      }

      return event;
   }
}
