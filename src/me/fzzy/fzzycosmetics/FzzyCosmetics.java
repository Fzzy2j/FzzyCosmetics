package me.fzzy.fzzycosmetics;

import me.fzzy.fzzycosmetics.effects.*;
import me.fzzy.fzzycosmetics.listeners.PlayerQuitListener;
import me.fzzy.vitalcosmetics.util.Yaml;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class FzzyCosmetics extends JavaPlugin {

    private static HashMap<UUID, User> users;
    public static FzzyCosmetics plugin;

    public static Yaml effectConfig;

    public static final double DEFAULT_PRICE = 10000D;

    public static List<EffectType> enabledEffects;

    public static HashMap<String, Material> categories;

    public static final String CONFIG_SPLIT_CHAR = ";";

    public static Economy econ;

    @Override
    public void onEnable() {
        plugin = this;
        new File(this.getDataFolder().getAbsolutePath() + File.separator + "players").mkdirs();

        users = new HashMap<>();

        PluginManager pm = Bukkit.getPluginManager();

        if (!setupEconomy()) {
            System.out.println("THIS PLUGIN REQUIRES VAULT TO FUNCTION");
            pm.disablePlugin(this);
            return;
        }

        pm.registerEvents(new PlayerQuitListener(), this);
        pm.registerEvents(new Menu(), this);

        pm.registerEvents(new WaterGlideTrail(), this);
        pm.registerEvents(new LavaGlideTrail(), this);
        pm.registerEvents(new ShockwaveFlight(), this);
        pm.registerEvents(new FloorRings(), this);
        pm.registerEvents(new Carbonated(), this);
        pm.registerEvents(new FizzyFlight(), this);
        pm.registerEvents(new Goop(), this);
        pm.registerEvents(new Twilight(), this);
        pm.registerEvents(new LoveBirds(), this);
        pm.registerEvents(new Strider(), this);
        pm.registerEvents(new Elemental(), this);
        pm.registerEvents(new Piper(), this);
        pm.registerEvents(new Iced(), this);
        pm.registerEvents(new Charmer(), this);
        pm.registerEvents(new Witcher(), this);
        pm.registerEvents(new Grotto(), this);

        enabledEffects = new ArrayList<>(Arrays.asList(EffectType.values()));
        enabledEffects.remove(EffectType.FIZZY_FLIGHT);
        enabledEffects.remove(EffectType.LAVA_GLIDE_TRAIL);
        enabledEffects.remove(EffectType.WATER_GLIDE_TRAIL);
        enabledEffects.remove(EffectType.SHOCKWAVE_FLIGHT);

        reload();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static void reload() {
        effectConfig = new Yaml(plugin.getDataFolder().getAbsolutePath() + File.separator + "effects.yml");
        for (EffectType type : EffectType.values()) {
            if (!effectConfig.contains(type.getConfigName() + ".price")) {
                effectConfig.add(type.getConfigName() + ".price", DEFAULT_PRICE);
                effectConfig.save();
            }
            if (!effectConfig.contains(type.getConfigName() + ".category")) {
                effectConfig.add(type.getConfigName() + ".category", "Basic");
                effectConfig.save();
            }
        }
        if (!effectConfig.contains("categories")) {
            List<String> list = new ArrayList<>();
            list.add("Basic" + CONFIG_SPLIT_CHAR + Material.COAL_BLOCK.name());
            list.add("Uncommon" + CONFIG_SPLIT_CHAR + Material.REDSTONE_BLOCK.name());
            list.add("Rare" + CONFIG_SPLIT_CHAR + Material.EMERALD_BLOCK.name());
            list.add("Legendary" + CONFIG_SPLIT_CHAR + Material.GOLD_BLOCK.name());
            effectConfig.add("categories", list);
            effectConfig.save();
        }

        categories = new HashMap<>();
        for (String s : effectConfig.getStringList("categories")) {
            String[] split = s.split(CONFIG_SPLIT_CHAR);
            categories.put(split[0], Material.valueOf(split[1]));
        }
    }

    public static User getUser(Player player) {
        if (users.containsKey(player.getUniqueId())) {
            return users.get(player.getUniqueId());
        } else {
            User user = new User(player);
            user.load();
            users.put(player.getUniqueId(), user);
            return user;
        }
    }

    public static User getOfflineLoadedUser(OfflinePlayer player) {
        User user = new User(player);
        user.load();
        return user;
    }

    public static void unloadUser(Player player) {
        User user = getUser(player);
        if (users.containsKey(player.getUniqueId()))
            users.remove(player.getUniqueId());
        user.save();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
        if (label.equalsIgnoreCase("vitaleffect") || label.equalsIgnoreCase("ve")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.isOp()) {
                    if (args.length == 1) {
                        if (args[0].equalsIgnoreCase("reload")) {
                            reload();
                            player.sendMessage(ChatColor.AQUA + "Reloaded.");
                            return true;
                        }
                    }
                    if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("toggle")) {
                            EffectType type = EffectType.getByTitle(args[1].replace("_", " "));
                            if (type != null) {
                                User user = getUser(player);
                                if (user.hasEffect(type)) {
                                    Effect effect = user.getEffect(type);
                                    effect.toggle();
                                } else {
                                    Effect effect = user.addEffect(type.getNew());
                                    effect.toggle();
                                }
                                player.sendMessage(ChatColor.AQUA + "Toggled.");
                            }
                            return true;
                        }
                    }
                }
                Menu.openMenu(player);
                return true;
            }
        }
        return false;
    }

}
