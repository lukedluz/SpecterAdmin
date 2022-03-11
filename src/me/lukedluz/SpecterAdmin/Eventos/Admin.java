package me.lukedluz.SpecterAdmin.Eventos;

import me.lukedluz.SpecterAdmin.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.HashSet;

public class Admin {


    private static HashSet<Player> inAdmin = new HashSet<>();
    private static HashMap<Player, ItemStack[]> items = new HashMap<>();

    public static void addPlayer(Player p){
        items.put(p, p.getInventory().getContents());
        inAdmin.add(p);
        p.getInventory().clear();

        p.getInventory().setItem(2, getJail());
        p.getInventory().setItem(3, getInfo());
        p.getInventory().setItem(4, getPresos());
        p.getInventory().setItem(5, getQuickChange());
        p.getInventory().setItem(6, getGM());

        for (Player o : Bukkit.getOnlinePlayers()){
            if (!o.hasPermission(Main.getPlugin().getConfig().getString("Permissoes.Admin"))) {
                o.hidePlayer(p);
            }
        }
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setAllowFlight(true);
        p.sendMessage(Main.getPlugin().getConfig().getString("Mensagens.AdminOn").replace("&", "§"));
        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10, 5);

    }
    public static void remove(Player p){
        inAdmin.remove(p);
        p.getInventory().clear();
        if (items.containsKey(p)){
            p.getInventory().setContents(items.get(p));
            items.remove(p);
        }
        if (p.getGameMode() == GameMode.SPECTATOR){
            p.setGameMode(GameMode.SURVIVAL);
        }
        if (p.getGameMode() != GameMode.CREATIVE){
            p.setAllowFlight(false);
        }
        for (Player o : Bukkit.getOnlinePlayers()){
            o.showPlayer(p);
        }
        p.sendMessage(Main.getPlugin().getConfig().getString("Mensagens.AdminOff").replace("&", "§"));
    }

    public static HashSet<Player> getAdmins(){
        return inAdmin;
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getJail(){
        ItemStack item = new ItemStack(101);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Main.getPlugin().getConfig().getString("Menu.Prender").replace("&", "§"));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack getGM(){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Main.getPlugin().getConfig().getString("Menu.Espectador").replace("&", "§"));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack getInfo(){
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Main.getPlugin().getConfig().getString("Menu.Info").replace("&", "§"));
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack getPresos(){
        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Main.getPlugin().getConfig().getString("Menu.VerJail").replace("&", "§"));
        item.setItemMeta(meta);
        return item;
    }
    @SuppressWarnings("deprecation")
    public static ItemStack getQuickChange(){
        ItemStack item = new ItemStack(351, 1, (short)8);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Main.getPlugin().getConfig().getString("Menu.TrocaRapida").replace("&", "§"));
        item.setItemMeta(meta);
        return item;
    }
}
