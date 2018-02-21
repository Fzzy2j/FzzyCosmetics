package me.fzzy.fzzycosmetics.effects;

import me.fzzy.fzzycosmetics.Effect;
import me.fzzy.fzzycosmetics.EffectType;
import me.fzzy.fzzycosmetics.User;
import me.fzzy.fzzycosmetics.FzzyCosmetics;
import me.fzzy.vitalcosmetics.util.Distance;
import me.fzzy.vitalcosmetics.util.EffectInfo;
import me.fzzy.vitalcosmetics.util.Yaml;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class ShockwaveFlight implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "shockwaveflight";
        return EffectInfo.createNewEffectInfo("Shockwave Flight",
                configName,
                Material.SNOW_BALL);
    }

    private HashMap<UUID, Location> prevLocations = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        User user = FzzyCosmetics.getUser(player);
        if (user.hasEffect(EffectType.SHOCKWAVE_FLIGHT)) {
            Effect effect = user.getEffect(EffectType.SHOCKWAVE_FLIGHT);
            if (effect.isEnabled()) {
                if (player.isGliding()) {
                    Location prevLocation = prevLocations.getOrDefault(player.getUniqueId(), event.getFrom());
                    Location lookAt = Distance.lookAt(prevLocation, event.getFrom());
                    double yaw = -lookAt.getYaw();
                    double pitch = lookAt.getPitch() + 90;
                    double powerFactor = 0.07;
                    for (int i = 0; i < 360; i += 10) {
                        double x = 1;
                        double y = 0;
                        double z = 0;

                        double[] rotate = rotateAroundY(x, y, z, i * (Math.PI / 180));
                        rotate = rotateAroundZ(rotate[0], rotate[1], rotate[2], pitch * (Math.PI / 180));
                        rotate = rotateAroundY(rotate[0], rotate[1], rotate[2], (yaw + 90) * (Math.PI / 180));

                        player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, event.getFrom(), 0, rotate[0] * powerFactor, rotate[1] * powerFactor, rotate[2] * powerFactor);
                    }
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
