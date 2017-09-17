package me.fzzy.vitalcosmetics;

import me.fzzy.vitalcosmetics.effects.*;
import me.fzzy.vitalcosmetics.util.EffectInfo;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum EffectType {

    WATER_GLIDE_TRAIL(WaterGlideTrail.getEffectInfo()),
    LAVA_GLIDE_TRAIL(LavaGlideTrail.getEffectInfo()),
    SHOCKWAVE_FLIGHT(ShockwaveFlight.getEffectInfo()),
    FLOOR_RINGS(FloorRings.getEffectInfo()),
    CARBONATED(Carbonated.getEffectInfo()),
    FIZZY_FLIGHT(FizzyFlight.getEffectInfo()),
    GOOP(Goop.getEffectInfo()),
    TWILIGHT(Twilight.getEffectInfo()),
    LOVE_BIRDS(LoveBirds.getEffectInfo()),
    STRIDER(Strider.getEffectInfo()),
    ELEMENTAL(Elemental.getEffectInfo()),
    PIPER(Piper.getEffectInfo()),
    ICED(Iced.getEffectInfo()),
    CHARMER(Charmer.getEffectInfo()),
    WITCHER(Witcher.getEffectInfo()),
    GROTTO(Grotto.getEffectInfo());

    private EffectInfo effect;

    EffectType(EffectInfo effect) {
        this.effect = effect;
    }

    public String getConfigName() {
        return effect.getConfigName();
    }

    public Effect getNew() {
        return new Effect(this);
    }

    public int getPrice() {
        return FzzyCosmetics.effectConfig.getInteger(effect.getConfigName() + ".price");
    }

    public String getCategory() {
        return FzzyCosmetics.effectConfig.getString(effect.getConfigName() + ".category");
    }

    public String getTitle() {
        return effect.getTitle();
    }

    public Material getIcon() {
        return effect.getIcon();
    }

    public static EffectType getByTitle(String title) {
        for (EffectType type : values()) {
            if (type.getTitle().toLowerCase().equals(title.toLowerCase()))
                return type;
        }
        return null;
    }

}
