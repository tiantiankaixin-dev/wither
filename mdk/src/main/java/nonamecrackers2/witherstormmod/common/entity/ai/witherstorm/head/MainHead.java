package nonamecrackers2.witherstormmod.common.entity.ai.witherstorm.head;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import nonamecrackers2.witherstormmod.common.entity.WitherStormEntity;

public class MainHead extends WitherStormHead {
   public MainHead(WitherStormEntity storm, int headIndex) {
      super(storm, headIndex, false);
   }

   @Override
   public void setLookPos(Vec3 pos, int steps) {
      if (pos != null) {
         this.storm.getLookControl().setLookAt(pos);
      }
   }

   @Override
   public float getHeadXRot() {
      return this.storm.getXRot();
   }

   @Override
   public float getHeadXRotO() {
      return this.storm.xRotO;
   }

   @Override
   public float getHeadYRot() {
      return this.storm.yHeadRot;
   }

   @Override
   public float getHeadYRotO() {
      return this.storm.yHeadRotO;
   }

   @Override
   public void setHeadXRot(float rot) {
      this.storm.setXRot(rot);
   }

   @Override
   public void setHeadYRot(float rot) {
      this.storm.setYRot(rot);
   }

   @Override
   public LivingEntity getTarget() {
      return this.storm.getTarget();
   }

   @Override
   public void setTarget(LivingEntity target) {
      this.storm.setTarget(target);
   }

   @Override
   protected boolean canShootFlamingSkull() {
      return super.canShootFlamingSkull() || this.storm.getPhase() < 4 && this.getTarget() != null;
   }

   @Override
   public void doHeadLookLogic() {
   }
}
