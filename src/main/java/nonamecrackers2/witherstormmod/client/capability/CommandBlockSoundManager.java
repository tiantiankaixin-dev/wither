package nonamecrackers2.witherstormmod.client.capability;

import net.minecraft.client.Minecraft;
import nonamecrackers2.witherstormmod.client.audio.CommandBlockEntityLoop;
import nonamecrackers2.witherstormmod.client.audio.EntitySoundManager;
import nonamecrackers2.witherstormmod.common.entity.CommandBlockEntity;

public class CommandBlockSoundManager extends EntitySoundManager<CommandBlockEntity, CommandBlockEntityLoop> {
   public CommandBlockSoundManager(Minecraft minecraft) {
      super(minecraft, CommandBlockEntity.class);
   }

   protected boolean alreadyHasLoop(CommandBlockEntity entity) {
      boolean flag = false;

      for (CommandBlockEntityLoop loop : this.loops) {
         if (loop.entity == entity) {
            flag = true;
            break;
         }
      }

      return flag;
   }

   protected CommandBlockEntityLoop create(CommandBlockEntity entity) {
      return new CommandBlockEntityLoop(entity);
   }

   protected CommandBlockEntityLoop copyFrom(CommandBlockEntityLoop loop) {
      return new CommandBlockEntityLoop(loop.entity);
   }
}
