package me.fzzy.fzzycosmetics.effects;

import me.fzzy.fzzycosmetics.Effect;
import me.fzzy.fzzycosmetics.EffectType;
import me.fzzy.fzzycosmetics.FzzyCosmetics;
import me.fzzy.fzzycosmetics.User;
import me.fzzy.fzzycosmetics.util.Distance;
import me.fzzy.fzzycosmetics.util.EffectInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class CorkscrewFlight implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "corkscrewflight";
        return EffectInfo.createNewEffectInfo("Corkscrew Flight",
                configName,
                Material.WOOD);
    }

    private static HashMap<UUID, Location> prevLocations = new HashMap<>();
    private static HashMap<UUID, Double> playerDistances = new HashMap<>();

    private double density = 0.1D;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        User user = FzzyCosmetics.getUser(player);
        if (user.hasEffect(EffectType.CORKSCREW_FLIGHT)) {
            Effect effect = user.getEffect(EffectType.CORKSCREW_FLIGHT);
            if (effect.isEnabled()) {
                if (player.isGliding()) {
                    Location prevLocation = prevLocations.getOrDefault(player.getUniqueId(), event.getFrom());
                    double distance = ((prevLocation.distance(event.getFrom()) * (Math.PI / 180)) * 20 + playerDistances.getOrDefault(player.getUniqueId(), 0D)) % (360 * (Math.PI / 180));
                    Location lookAt = Distance.lookAt(prevLocation, event.getFrom());
                    double yaw = -lookAt.getYaw();
                    double pitch = lookAt.getPitch() + 90;
                    double powerFactor = 0.07;
                    double x = 1;
                    double y = 0;
                    double z = 0;

                    for (int i = 0; i < 4; i++) {
                        double add = Math.toRadians(i * (360 / 4));
                        double[] rotate = rotateAroundY(x, y, z, (distance + add));
                        rotate = rotateAroundZ(rotate[0], rotate[1], rotate[2], pitch * (Math.PI / 180));
                        rotate = rotateAroundY(rotate[0], rotate[1], rotate[2], (yaw + 90) * (Math.PI / 180));

                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, event.getFrom(), 0, rotate[0] * powerFactor, rotate[1] * powerFactor, rotate[2] * powerFactor);
                    }
                    playerDistances.put(player.getUniqueId(), distance);
                    prevLocations.put(player.getUniqueId(), event.getFrom());
                } else {
                    if (prevLocations.containsKey(player.getUniqueId()))
                        prevLocations.remove(player.getUniqueId());
                }
            }
        }
    }

    public double[] rotateAroundZ(double x, double y, double z, double t) {
        double x2 = x * Math.cos(t) - y * Math.sin(t);
        double y2 = x * Math.sin(t) + y * Math.cos(t);
        double z2 = z;
        double[] all = {x2, y2, z2};
        return all;
    }

    public double[] rotateAroundY(double x, double y, double z, double t) {
        double x2 = x * Math.cos(t) + z * Math.sin(t);
        double y2 = y;
        double z2 = -x * Math.sin(t) + z * Math.cos(t);
        double[] all = {x2, y2, z2};
        return all;
    }

    public double[] rotateAroundX(double x, double y, double z, double t) {
        double x2 = x;
        double y2 = y * Math.cos(t) - z * Math.sin(t);
        double z2 = y * Math.sin(t) + z * Math.cos(t);
        double[] all = {x2, y2, z2};
        return all;
    }
}
