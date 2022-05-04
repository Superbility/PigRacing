package com.superdevelopment.pigracing.race.customentity;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomPig extends EntityPig {
    protected Field FIELD_JUMP = null;
    private long cooldown;

    public CustomPig(World world) {
        super(world);

        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT_ON_A_STICK, false));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.APPLE, false));

        if (FIELD_JUMP == null)
        {
            try
            {
                FIELD_JUMP = EntityLiving.class.getDeclaredField("aY");
                FIELD_JUMP.setAccessible(true);
            } catch (NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void g(float f, float f1) {
        if(this.passenger != null && this.passenger instanceof EntityHuman) {
            this.lastYaw = this.yaw = this.passenger.yaw;
            this.pitch = this.passenger.pitch * 0.5F;
            this.setYawPitch(this.yaw, this.pitch);
            this.aK = this.aI = this.yaw;
            f = ((EntityLiving)this.passenger).aZ * 0.5F;
            f1 = ((EntityLiving)this.passenger).ba;
            if(f1 <= 0.0F) {
                f1 *= 0.25F;
            }
            try {
                if (FIELD_JUMP.getBoolean(this.passenger)) {// && this.onGround)
                    if (cooldown < System.currentTimeMillis()) {
                        cooldown = System.currentTimeMillis() + 50;
                        this.motY += 0.4F;
                    this.ai = true;
                    if (f1 > 0.0F) {
                        float f2 = MathHelper.sin(this.yaw * 3.141593F / 180.0F);
                        float f3 = MathHelper.cos(this.yaw * 3.141593F / 180.0F);
                        this.motX += -0.4F * f2 * ((EntityInsentient) this).br();
                        this.motZ += 0.4F * f3 * ((EntityInsentient) this).br();
                    }
                }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            this.S = 1.0F; this.aM = this.bI() * 0.1F; if(!this.world.isClientSide) {
            float speed = 0.1F;
            this.k(speed);
            super.g(f, f1);
        }
            this.aA = this.aB; double d0 = this.locX - this.lastX; double d1 = this.locZ - this.lastZ; float f4 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
            if(f4 > 1.0F) {
                f4 = 1.0F;
            }
            this.aB += (f4 - this.aB) * 0.4F; this.aC += this.aB;
        } else {
            this.S = 0.5F; this.aM = 0.02F; super.g(f, f1);
        }
    }

    public static Pig spawn(Location loc){
        World mcWorld = ((CraftWorld) loc.getWorld()).getHandle();
        final CustomPig customEnt = new CustomPig(mcWorld);
        customEnt.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftLivingEntity) customEnt.getBukkitEntity()).setRemoveWhenFarAway(false); //Do we want to remove it when the NPC is far away? I won
        mcWorld.addEntity(customEnt, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Pig) customEnt.getBukkitEntity();
    }

    public static void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass){
        try {

            List<Map<?, ?>> dataMap = new ArrayList<Map<?, ?>>();
            for (Field f : EntityTypes.class.getDeclaredFields()){
                if (f.getType().getSimpleName().equals(Map.class.getSimpleName())){
                    f.setAccessible(true);
                    dataMap.add((Map<?, ?>) f.get(null));
                }
            }

            if (dataMap.get(2).containsKey(id)){
                dataMap.get(0).remove(name);
                dataMap.get(2).remove(id);
            }

            Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            method.setAccessible(true);
            method.invoke(null, customClass, name, id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
