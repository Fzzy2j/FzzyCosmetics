package me.fzzy.fzzycosmetics.effects;

import me.fzzy.fzzycosmetics.Effect;
import me.fzzy.fzzycosmetics.EffectType;
import me.fzzy.fzzycosmetics.FzzyCosmetics;
import me.fzzy.fzzycosmetics.User;
import me.fzzy.fzzycosmetics.util.EffectInfo;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Strider implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "strider";
        return EffectInfo.createNewEffectInfo("Strider",
                configName,
                Material.DIAMOND_BOOTS);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().clone().subtract(0, 0.001, 0).getBlock().getType().isSolid()) {
            User user = FzzyCosmetics.getUser(player);
            if (user.hasEffect(EffectType.STRIDER)) {
                Effect effect = user.getEffect(EffectType.STRIDER);
                if (effect.isEnabled()) {
                    if (event.getFrom().distanceSquared(event.getTo()) > 0.001) {
                        for (int i = 0; i < 360; i += 30) {
                            double x = Math.sin(Math.toRadians(i));
                            double z = Math.cos(Math.toRadians(i));

                            player.getWorld().spawnParticle(Particle.WATER_SPLASH, event.getTo().clone().add(x * 0.3, 0, z * 0.3), 0, 0, 0, 0);
                        }
                    }
                }
            }
        }
    }
}
