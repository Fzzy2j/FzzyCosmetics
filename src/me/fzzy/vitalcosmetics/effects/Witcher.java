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

public class Witcher implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "witcher";
        return EffectInfo.createNewEffectInfo("Witcher",
                configName,
                Material.EYE_OF_ENDER);
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().clone().subtract(0, 0.001, 0).getBlock().getType().isSolid()) {
            User user = FzzyCosmetics.getUser(player);
            if (user.hasEffect(EffectType.WITCHER)) {
                Effect effect = user.getEffect(EffectType.WITCHER);
                if (effect.isEnabled()) {
                    if (event.getFrom().distanceSquared(event.getTo()) > 0.001) {
                        player.getWorld().spawnParticle(Particle.SPELL_MOB, event.getFrom(), 2);
                    }
                }
            }
        }
    }
}
