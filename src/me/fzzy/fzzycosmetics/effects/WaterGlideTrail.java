package me.fzzy.fzzycosmetics.effects;

import me.fzzy.fzzycosmetics.Effect;
import me.fzzy.fzzycosmetics.EffectType;
import me.fzzy.fzzycosmetics.User;
import me.fzzy.fzzycosmetics.FzzyCosmetics;
import me.fzzy.vitalcosmetics.util.Distance;
import me.fzzy.vitalcosmetics.util.EffectInfo;
import me.fzzy.vitalcosmetics.util.Yaml;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class WaterGlideTrail implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "waterglide";
        return EffectInfo.createNewEffectInfo("Water Glide Trail",
                configName,
                Material.WATER_BUCKET);
    }

    private HashMap<UUID, Location> prevLocations = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = FzzyCosmetics.getUser(player);
        if (user.hasEffect(EffectType.WATER_GLIDE_TRAIL)) {
            Effect effect = user.getEffect(EffectType.WATER_GLIDE_TRAIL);
            if (effect.isEnabled()) {
                if (player.isGliding()) {
                    Location prevLocation = prevLocations.getOrDefault(player.getUniqueId(), event.getFrom());
                    ArrayList<Location> loclist = new ArrayList<>();

                    Location loc = prevLocation;
                    Location loc2 = event.getFrom();
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
                    for (double i = interval; i <= distance + 0.6; i += interval) {
                        Location loc1 = new Location(loc.getWorld(), px + i * x, py + i * z, pz + i * y);
                        loclist.add(loc1);
                    }
                    for (Location loc3 : loclist) {
                        loc3.getWorld().spawnParticle(Particle.DRIP_WATER, loc3, 1, 0, 0, 0);
                    }
                    prevLocations.put(player.getUniqueId(), event.getFrom());
                } else {
                    if (prevLocations.containsKey(player.getUniqueId()))
                        prevLocations.remove(player.getUniqueId());
                }
            }
        }
    }

}
