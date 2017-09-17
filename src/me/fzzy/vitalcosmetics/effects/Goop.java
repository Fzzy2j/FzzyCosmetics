package me.fzzy.vitalcosmetics.effects;

import me.fzzy.vitalcosmetics.Effect;
import me.fzzy.vitalcosmetics.EffectType;
import me.fzzy.vitalcosmetics.FzzyCosmetics;
import me.fzzy.vitalcosmetics.User;
import me.fzzy.vitalcosmetics.util.EffectInfo;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Goop implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "goop";
        return EffectInfo.createNewEffectInfo("Goop",
                configName,
                Material.SLIME_BALL);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().clone().subtract(0, 0.001, 0).getBlock().getType().isSolid()) {
            User user = FzzyCosmetics.getUser(player);
            if (user.hasEffect(EffectType.GOOP)) {
                Effect effect = user.getEffect(EffectType.GOOP);
                if (effect.isEnabled()) {
                    if (event.getFrom().distanceSquared(event.getTo()) > 0.001) {
                        player.getWorld().spawnParticle(Particle.SLIME, event.getFrom(), 2);
                    }
                }
            }
        }
    }

}
