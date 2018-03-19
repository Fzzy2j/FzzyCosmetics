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

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class LoveBirds implements Listener {

    public static EffectInfo getEffectInfo() {
        String configName = "lovebirds";
        return EffectInfo.createNewEffectInfo("Love Birds",
                configName,
                Material.CHORUS_FRUIT_POPPED);
    }

    private Random random = new Random();
    private HashMap<UUID, Integer> tickCounter = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getLocation().clone().subtract(0, 0.001, 0).getBlock().getType().isSolid()) {
            User user = FzzyCosmetics.getUser(player);
            if (user.hasEffect(EffectType.LOVE_BIRDS)) {
                Effect effect = user.getEffect(EffectType.LOVE_BIRDS);
                if (effect.isEnabled()) {
                    if (event.getFrom().distanceSquared(event.getTo()) > 0.001) {
                        if (!tickCounter.containsKey(player.getUniqueId()))
                            tickCounter.put(player.getUniqueId(), 0);
                        int timePassed = tickCounter.get(player.getUniqueId());
                        if (timePassed > 5) {
                            double randomX = random.nextInt(10) / 10D;
                            double randomZ = random.nextInt(10) / 10D;
                            player.getWorld().spawnParticle(Particle.HEART, event.getFrom().clone().add(randomX - 0.5D, 2.2, randomZ - 0.5D), 1);
                            tickCounter.put(player.getUniqueId(), timePassed - 5);
                        }
                        tickCounter.put(player.getUniqueId(), tickCounter.get(player.getUniqueId()) + 1);
                    }
                }
            }
        }
    }
}
