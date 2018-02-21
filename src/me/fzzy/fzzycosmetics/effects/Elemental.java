package me.fzzy.fzzycosmetics.effects;

import me.fzzy.fzzycosmetics.Effect;
import me.fzzy.fzzycosmetics.EffectType;
import me.fzzy.fzzycosmetics.FzzyCosmetics;
import me.fzzy.fzzycosmetics.User;
import me.fzzy.vitalcosmetics.util.Distance;
import me.fzzy.vitalcosmetics.util.EffectInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class Elemental implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "elemental";
        return EffectInfo.createNewEffectInfo("Elemental",
                configName,
                Material.MAGMA_CREAM);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().clone().subtract(0, 0.001, 0).getBlock().getType().isSolid()) {
            User user = FzzyCosmetics.getUser(player);
            if (user.hasEffect(EffectType.ELEMENTAL)) {
                Effect effect = user.getEffect(EffectType.ELEMENTAL);
                if (effect.isEnabled()) {
                    if (event.getFrom().distanceSquared(event.getTo()) > 0.001) {
                        player.getWorld().spawnParticle(Particle.DRIP_WATER, event.getFrom().clone().add(0, 0.05, 0), 1);
                        player.getWorld().spawnParticle(Particle.DRIP_LAVA, event.getFrom().clone().add(0, 0.05, 0), 1);
                        player.getWorld().spawnParticle(Particle.SUSPENDED_DEPTH, event.getFrom().clone().add(0, 0.05, 0), 10);
                    }
                }
            }
        }
    }

    private Random random = new Random();

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            User user = FzzyCosmetics.getUser(player);
            if (user.hasEffect(EffectType.ELEMENTAL)) {
                Effect effect = user.getEffect(EffectType.ELEMENTAL);
                if (effect.isEnabled()) {
                    Projectile proj = (Projectile) event.getProjectile();
                    int choose = random.nextInt(2);
                    Particle particle = choose == 0 ? Particle.DRIP_WATER : Particle.DRIP_LAVA;
                    new BukkitRunnable() {
                        int count = 0;
                        Location prevLocation = proj.getLocation();

                        @Override
                        public void run() {
                            if (count > 60)
                                this.cancel();
                            count++;

                            ArrayList<Location> loclist = new ArrayList<>();

                            Location loc = prevLocation;
                            Location loc2 = proj.getLocation();
                            int distance = (int) Math.floor(loc.distance(loc2));

                            loc = Distance.lookAt(loc, loc2);

                            double px = loc.getX();
                            double py = loc.getY();
                            double pz = loc.getZ();

                            double yaw = Math.toRadians(loc.getYaw() + 90);
                            double pitch = Math.toRadians(loc.getPitch() + 90);

                            double x = Math.sin(pitch) * Math.cos(yaw);
                            double y = Math.sin(pitch) * Math.sin(yaw);
                            double z = Math.cos(pitch);

                            double interval = 0.2;
                            for (double i = interval; i <= distance + 1; i += interval) {
                                Location loc1 = new Location(loc.getWorld(), px + i * x, py + i * z, pz + i * y);
                                loclist.add(loc1);
                            }
                            for (Location loc3 : loclist) {
                                proj.getWorld().spawnParticle(particle, loc3, 1);
                            }
                            prevLocation = proj.getLocation();
                        }
                    }.runTaskTimer(FzzyCosmetics.plugin, 0L, 1L);
                }
            }
        }
    }
}
