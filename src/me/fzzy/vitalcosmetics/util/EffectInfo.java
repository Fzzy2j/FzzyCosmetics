package me.fzzy.vitalcosmetics.util;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;

public class EffectInfo {

    private String title;
    private Material icon;

    private String configName;

    private EffectInfo(String title, String configName, Material icon) {
        this.title = title;
        this.icon = icon;
        this.configName = configName;
    }

    public String getConfigName() {
        return this.configName;
    }

    public String getTitle() {
        return title;
    }

    public Material getIcon() {
        return icon;
    }

    public static EffectInfo createNewEffectInfo(String title, String configName, Material icon) {
        return new EffectInfo(title, configName, icon);
    }

}
