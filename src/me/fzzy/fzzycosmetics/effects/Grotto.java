package me.fzzy.fzzycosmetics.effects;

import me.fzzy.fzzycosmetics.Effect;
import me.fzzy.fzzycosmetics.EffectType;
import me.fzzy.fzzycosmetics.FzzyCosmetics;
import me.fzzy.fzzycosmetics.User;
import me.fzzy.vitalcosmetics.util.EffectInfo;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Grotto implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "grotto";
        return EffectInfo.createNewEffectInfo("Grotto",
                configName,
                Material.WATER_LILY);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().clone().subtract(0, 0.001, 0).getBlock().getType().isSolid()) {
            User user = FzzyCosmetics.getUser(player);
            if (user.hasEffect(EffectType.GROTTO)) {
                Effect effect = user.getEffect(EffectType.GROTTO);
                if (effect.isEnabled()) {
                    if (event.getFrom().distanceSquared(event.getTo()) > 0.001) {
                        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, event.getFrom(), 2);
                    }
                }
            }
        }
    }
}