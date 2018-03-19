package me.fzzy.fzzycosmetics;

import me.fzzy.fzzycosmetics.util.Methods;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.Map;

public class Menu implements Listener {

    public static void openMenu(Player player) {
        User user = FzzyCosmetics.getUser(player);

        Inventory inv = Bukkit.createInventory(null, 6 * 9, "Pick Category");

        for (int i = 1; i <= 9; i++)
            inv.setItem(inv.getSize() - i, Methods.createItem(new Wool(DyeColor.LIME).toItemStack(1), ChatColor.GREEN + "" + ChatColor.BOLD + "BUY COSMETICS"));

        int rows = 2 - (int)Math.floor(Math.ceil(FzzyCosmetics.categories.size() / 4D) / 2D);

        int spacing = 1;
        int row = rows;
        for (Map.Entry<String, Material> e : FzzyCosmetics.categories.entrySet()) {
            inv.setItem(spacing + (row * 9), Methods.createItem(new ItemStack(e.getValue()), ChatColor.YELLOW + "" + ChatColor.BOLD + e.getKey()));
            spacing += 2;
            if (spacing > 7) {
                spacing -= 8;
                row++;
            }
        }
        player.openInventory(inv);
    }

    public static void openPickMenu(Player player, String category) {
        User user = FzzyCosmetics.getUser(player);

        int amt = 0;
        for (EffectType type : EffectType.values()) {
            if (user.hasEffect(type)) {
                amt++;
            }
        }

        int amtRows = (int) Math.ceil(amt / 9);
        if (amtRows < 3)
            amtRows = 3;

        Inventory inv = Bukkit.createInventory(null, amtRows * 9, "Pick Cosmetics");

        for (int i = 1; i <= 8; i++)
            inv.setItem(inv.getSize() - i, Methods.createItem(new Wool(DyeColor.LIME).toItemStack(1), ChatColor.GREEN + "" + ChatColor.BOLD + "BUY COSMETICS"));
        inv.setItem(inv.getSize() - 9, Methods.createItem(new Wool(DyeColor.RED).toItemStack(1), ChatColor.RED + "" + ChatColor.BOLD + "RETURN"));

        for (EffectType type : FzzyCosmetics.enabledEffects) {
            if (user.hasEffect(type)) {
                if (type.getCategory().equals(category)) {
                    Effect effect = user.getEffect(type);
                    if (effect.isEnabled()) {
                        ItemStack newItem = Methods.createItem(new ItemStack(type.getIcon()), ChatColor.GREEN + "" + ChatColor.BOLD + type.getTitle(), ChatColor.GREEN + "" + ChatColor.BOLD + "Enabled");
                        newItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
                        inv.addItem(newItem);
                    } else {
                        ItemStack newItem = Methods.createItem(new ItemStack(type.getIcon()), ChatColor.GREEN + "" + ChatColor.BOLD + type.getTitle(), ChatColor.RED + "" + ChatColor.BOLD + "Disabled");
                        inv.addItem(newItem);
                    }
                }
            }
        }
        player.openInventory(inv);
    }

    public static void openBuyMenu(Player player) {
        User user = FzzyCosmetics.getUser(player);

        int amt = 0;
        for (EffectType type : EffectType.values()) {
            if (!user.hasEffect(type)) {
                amt++;
            }
        }

        int amtRows = (int) Math.ceil(amt / 9);
        if (amtRows < 3)
            amtRows = 3;

        Inventory inv = Bukkit.createInventory(null, amtRows * 9, "Buy Cosmetics");

        for (int i = 1; i <= 9; i++)
            inv.setItem(inv.getSize() - i, Methods.createItem(new Wool(DyeColor.RED).toItemStack(1), ChatColor.RED + "" + ChatColor.BOLD + "BACK"));

        for (EffectType type : FzzyCosmetics.enabledEffects) {
            if (!user.hasEffect(type)) {
                inv.addItem(Methods.createItem(new ItemStack(type.getIcon()), ChatColor.AQUA + "" + ChatColor.BOLD + type.getTitle(), ChatColor.GREEN + "" + ChatColor.BOLD + "Price: " + type.getPrice()));
            }
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() != null) {
            if (event.getCurrentItem() != null) {

                //PICKING CATEGORY
                if (event.getInventory().getTitle().equals("Pick Category")) {
                    Player player = (Player) event.getWhoClicked();
                    ItemStack item = event.getCurrentItem();
                    User user = FzzyCosmetics.getUser(player);
                    if (item.getType() != Material.AIR) {
                        if (item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta.getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "BUY COSMETICS")) {
                                openBuyMenu(player);
                            }
                            if (FzzyCosmetics.categories.containsKey(ChatColor.stripColor(meta.getDisplayName()))) {
                                openPickMenu(player, ChatColor.stripColor(meta.getDisplayName()));
                            }
                        }
                    }
                    event.setCancelled(true);
                }

                //PICKING
                if (event.getInventory().getTitle().equals("Pick Cosmetics")) {
                    Player player = (Player) event.getWhoClicked();
                    ItemStack item = event.getCurrentItem();
                    User user = FzzyCosmetics.getUser(player);
                    if (item.getType() != Material.AIR) {
                        if (item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta.getDisplayName().equals(ChatColor.GREEN + "" + ChatColor.BOLD + "BUY COSMETICS")) {
                                openBuyMenu(player);
                            }
                            if (meta.getDisplayName().equals(ChatColor.RED + "" + ChatColor.BOLD + "RETURN")) {
                                openMenu(player);
                            }
                            EffectType type = EffectType.getByTitle(ChatColor.stripColor(meta.getDisplayName()));
                            if (type != null) {
                                Effect effect = user.getEffect(type);
                                effect.toggle();
                                user.save();
                                if (effect.isEnabled()) {
                                    ItemStack newItem = Methods.createItem(new ItemStack(item.getType()), ChatColor.GREEN + "" + ChatColor.BOLD + type.getTitle(), ChatColor.GREEN + "" + ChatColor.BOLD + "Enabled");
                                    newItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 10);
                                    event.getInventory().setItem(event.getSlot(), newItem);
                                } else {
                                    ItemStack newItem = Methods.createItem(new ItemStack(item.getType()), ChatColor.GREEN + "" + ChatColor.BOLD + type.getTitle(), ChatColor.RED + "" + ChatColor.BOLD + "Disabled");
                                    event.getInventory().setItem(event.getSlot(), newItem);
                                }
                            }
                        }
                    }
                    event.setCancelled(true);
                }

                //BUYING
                if (event.getInventory().getTitle().equals("Buy Cosmetics")) {
                    Player player = (Player) event.getWhoClicked();
                    ItemStack item = event.getCurrentItem();
                    User user = FzzyCosmetics.getUser(player);
                    if (item.getType() != Material.AIR) {
                        if (item.hasItemMeta()) {
                            ItemMeta meta = item.getItemMeta();
                            if (meta.getDisplayName().equals(ChatColor.RED + "" + ChatColor.BOLD + "BACK")) {
                                openMenu(player);
                            }
                            EffectType type = EffectType.getByTitle(ChatColor.stripColor(meta.getDisplayName()));
                            if (type != null) {
                                if (!user.hasEffect(type)) {
                                    int balance = (int) FzzyCosmetics.econ.getBalance(player);
                                    if (balance >= type.getPrice()) {
                                        event.getInventory().remove(item);
                                        Effect effect = type.getNew();
                                        user.addEffect(effect);
                                        FzzyCosmetics.econ.withdrawPlayer(player, type.getPrice());
                                        player.sendMessage(ChatColor.AQUA + "Enjoy!");
                                    } else {
                                        player.sendMessage(ChatColor.RED + "Not enough points!");
                                    }
                                    /*try {
                                        Class clazz = Class.forName("me.fzzy.vitalpoints.VitalPoints");
                                        Method getPoints = clazz.getMethod("getVitalPoints", OfflinePlayer.class);
                                        int balance = (int) getPoints.invoke(clazz, player);
                                        if (balance >= type.getPrice()) {
                                            event.getInventory().remove(item);
                                            Effect effect = type.getNew();
                                            user.addEffect(effect);
                                            Method setPoints = Class.forName("me.fzzy.vitalpoints.VitalPoints").getMethod("setVitalPoints", OfflinePlayer.class, int.class);
                                            setPoints.invoke(clazz, player, balance - type.getPrice());
                                            player.sendMessage(ChatColor.AQUA + "Enjoy!");
                                        } else {
                                            player.sendMessage(ChatColor.RED + "Not enough points!");
                                        }
                                    } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                                        player.sendMessage(ChatColor.RED + "Something broke in the plugin, you should notify the developer");
                                        e.printStackTrace();
                                    }*/
                                }
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

}
