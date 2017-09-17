package me.fzzy.vitalcosmetics;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.HashMap;
import java.util.UUID;

public class Effect {

    private EffectType type;
    private boolean enabled;

    public Effect(EffectType type) {
        this.type = type;
        this.enabled = false;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = true;
    }

    public void toggle() {
        enabled = !enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getTitle() {
        return type.getTitle();
    }

    public Material getIcon() {
        return type.getIcon();
    }

    public int getPrice() {
        return type.getPrice();
    }

    public String getCategory() {
        return type.getCategory();
    }

    public EffectType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.name();
    }

}
