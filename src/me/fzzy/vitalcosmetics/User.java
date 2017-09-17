package me.fzzy.vitalcosmetics;

import me.fzzy.vitalcosmetics.util.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class User {

    private OfflinePlayer player;
    private Yaml yaml;

    private HashMap<String, Effect> effects;

    public User(OfflinePlayer player) {
        this.player = player;
        this.effects = new HashMap<>();
    }

    public Effect getEffect(EffectType type) {
        return effects.getOrDefault(type.name(), null);
    }

    public boolean hasEffect(EffectType type) {
        return effects.containsKey(type.name());
    }

    public Effect addEffect(Effect effect) {
        effects.put(effect.getType().name(), effect);
        save();
        return effect;
    }

    public void removeEffect(EffectType type) {
        if (effects.containsKey(type.name()))
            effects.remove(type);
        if (yaml.contains(type.name()))
            yaml.remove(type.name());
        yaml.save();
    }

    public void load() {
        yaml = new Yaml(FzzyCosmetics.plugin.getDataFolder().getAbsolutePath() + File.separator + "players" + File.separator + player.getUniqueId().toString() + ".yml");

        this.effects = new HashMap<>();

        for (EffectType type : EffectType.values()) {
            if (yaml.contains(type.name())) {
                Effect effect = type.getNew();

                if (yaml.getBoolean(type.name() + ".enabled"))
                    effect.enable();

                effects.put(type.name(), effect);
            }
        }
    }

    public void save() {
        for (Map.Entry<String, Effect> e : effects.entrySet()) {
            yaml.set(e.getKey() + ".enabled", e.getValue().isEnabled());
        }
        yaml.save();
    }

}
