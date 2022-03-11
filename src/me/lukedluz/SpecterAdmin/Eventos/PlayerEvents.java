package me.lukedluz.SpecterAdmin.Eventos;

import me.lukedluz.SpecterAdmin.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;

public class PlayerEvents implements Listener {

    public static HashMap<Player, Jail> telando = new HashMap<>();
    public static HashSet<Jail> jails = new HashSet<>();
    public static Inventory inv = Bukkit.createInventory(null, Main.getPlugin().getConfig().getInt("Menu.Linhas")*9, Main.getPlugin().getConfig().getString("Menu.Nome").replace("&", "§"));


    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if (e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if (Admin.getAdmins().contains(p)) {
                e.setCancelled(true);
            }
            p.sendMessage("§aPor você estar preso, você não levará dano.");
        }
    }
    @EventHandler
    public void onFood(FoodLevelChangeEvent e){
        Player p = (Player) e.getEntity();
        if (Admin.getAdmins().contains(p)){
            e.setCancelled(true);
        }
        p.sendMessage("§aPor você estar preso, você não sentirá fome.");
    }
    @EventHandler
    public void onPickup(PlayerPickupItemEvent e){
        Player p = e.getPlayer();
        if (Admin.getAdmins().contains(p)){
            e.setCancelled(true);
        }
        p.sendMessage("§aPor você estar preso, você não poderá pegar itens do chão.");
    }
    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        Player p = e.getPlayer();
        if (telando.containsKey(p) && !aaaa.containsKey(p)) {
            e.setCancelled(true);
            p.sendMessage(Main.getPlugin().getConfig().getString("Erros.Teleporte").replace("&", "§"));
        }
    }
    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();
        if (telando.containsKey(p)) {
            e.setCancelled(true);
            p.sendMessage(Main.getPlugin().getConfig().getString("Erros.Comandos").replace("&", "§"));
        }
    }
    @SuppressWarnings("deprecation")
    public void updateJailMenu(){
        inv.clear();
        for (Jail j : jails) {
            if (j.isInUse()) {
                ItemStack item = new ItemStack(397, 1, (byte)3);
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                meta.setDisplayName("§c" + j.getPlayer().getName());
                meta.setOwner(j.getPlayer().getName());
//				meta.setLore(Arrays.asList("§7preso por §r" + tag.getPrefixTag(tag.getCargo(j.getStaff().getName())) + " " + j.getStaff().getName()));
                item.setItemMeta(meta);

                inv.setItem(getSlot(inv), item);
            }
        }
    }

    public void loadJails(){
        for (String b : Main.getPlugin().getConfig().getConfigurationSection("jails").getKeys(false)) {

            String a = Main.getPlugin().getConfig().getString("jails." + b);

            double x = Double.valueOf(a.split(":")[0]);
            double y = Double.valueOf(a.split(":")[1]);
            double z = Double.valueOf(a.split(":")[2]);
            double yaw = Double.valueOf(a.split(":")[3]);
            double pitch = Double.valueOf(a.split(":")[4]);

            World world = Bukkit.getWorld(a.split(":")[5]);

            Location l = new Location(world, x, y, z, (short)yaw, (short)pitch);

            Jail j = new Jail(l);
            jails.add(j);

        }
    }


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            final Player p = e.getPlayer();
            if (Admin.getAdmins().contains(p)) {
                ItemStack item = p.getItemInHand();
                if (item.getTypeId() == 351 && item.getDurability() == 8) {
                    if (item.hasItemMeta()) {
                        p.getInventory().clear();
                        for (Player o : Bukkit.getOnlinePlayers()) {
                            if (!o.hasPermission(Main.getPlugin().getConfig().getString("Permissoes.Admin")))
                                o.showPlayer(p);
                        }
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10, 1);
                        p.sendMessage("§cVocê está visível");
                        e.setCancelled(true);
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                for (Player o : Bukkit.getOnlinePlayers()) {
                                    if (!o.hasPermission(Main.getPlugin().getConfig().getString("Permissoes.Admin")))
                                        o.hidePlayer(p);
                                }
                                p.sendMessage("§aVocê está invisível");
                                p.getInventory().setItem(2, Admin.getJail());
                                p.getInventory().setItem(3, Admin.getInfo());
                                p.getInventory().setItem(4, Admin.getPresos());
                                p.getInventory().setItem(5, Admin.getQuickChange());
                                p.getInventory().setItem(6, Admin.getGM());
                            }
                        }.runTaskLater((Plugin) this, 1 * 20L);
                    }
                }
                if (item.getType() == Material.ANVIL){
                    if (item.hasItemMeta()) {
                        if (item.getItemMeta().hasDisplayName()){
                            e.setCancelled(true);
                            p.openInventory(inv);
                        }
                    }
                }
                if (item.getType() == Material.PAPER){
                    if (item.hasItemMeta()) {
                        if (item.getItemMeta().hasDisplayName()){
                            e.setCancelled(true);
                            p.setGameMode(GameMode.SPECTATOR);
                            p.sendMessage("§aSeu modo de jogo foi atualizado, para voltar ao modo de jogo padrão, utize /admin!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if (telando.containsKey(p)){
            Jail j = telando.get(p);
            telando.remove(p);

            if (j.getStaff() != null){
                j.getStaff().sendMessage(Main.getPlugin().getConfig().getString("Mensagens.DeslogouJail"));
                j.getStaff().sendMessage("");
                j.getStaff().sendMessage("");
                j.getStaff().sendMessage("");
                j.getStaff().sendMessage("");
                j.getStaff().sendMessage("§f" + p.getName() + "§c§l deslogou na jail!");
                j.getStaff().sendMessage("");
                j.getStaff().sendMessage("");
                j.getStaff().sendMessage("");
                j.getStaff().sendMessage("");
                j.getStaff().playSound(j.getStaff().getLocation(), Sound.NOTE_BASS, 10, -4);
            } else {
                for (Player o : Bukkit.getOnlinePlayers()){
                    if (o.hasPermission(Main.getPlugin().getConfig().getString("Permissoes.Admin"))) {
                        o.sendMessage("");
                        o.sendMessage("");
                        o.sendMessage("");
                        o.sendMessage("");
                        o.sendMessage("§f" + p.getName() + "§c§l deslogou na jail!");
                        o.sendMessage("");
                        o.sendMessage("");
                        o.sendMessage("");
                        o.sendMessage("");
                        o.playSound(j.getStaff().getLocation(), Sound.NOTE_BASS, 10, -4);
                    }
                }
            }
            j.setUse(false);
            j.setStaff(null);
            j.setEntity(null);
            updateJailMenu();
        } else {
            if (Admin.getAdmins().contains(p)) {
                Admin.remove(p);
            }
        }
    }
    public static HashMap<Player, Player> aaaa = new HashMap<>();
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e){
        if (e.getRightClicked() instanceof Player && Admin.getAdmins().contains(e.getPlayer())){
            Player clicked = (Player) e.getRightClicked();
            Player p = e.getPlayer();
            e.setCancelled(true);
            if (p.getItemInHand() == null || p.getItemInHand().getType() == Material.AIR){
                p.openInventory(clicked.getInventory());
                return;
            }
            if (p.getItemInHand().isSimilar(Admin.getJail())){
                if (telando.containsKey(clicked)){

                    Jail j = telando.get(clicked);
                    telando.remove(clicked);

                    if (j.getStaff().getName().equalsIgnoreCase(p.getName()))
                        p.sendMessage("§f" + clicked.getName() + "§a não está mais te vendo.");

                    j.setUse(false);
                    j.setStaff(null);
                    j.setEntity(null);

                    clicked.teleport(Bukkit.getWorld("spawn").getSpawnLocation());
                    clicked.sendMessage("§aVocê foi liberado.");
                    clicked.playSound(clicked.getLocation(), Sound.ORB_PICKUP, 10, 1);

                    for (Player o : Admin.getAdmins()){
                        clicked.hidePlayer(o);
                    }
                    p.sendMessage("§aJogador liberado.");
                    updateJailMenu();
                    return;

                }
                boolean achou = false;
                for (Jail j : jails){
                    if (!j.isInUse()){
                        j.setUse(true);
                        j.setStaff(p);
                        j.setEntity(clicked);
                        aaaa.put(clicked, clicked);
                        telando.put(clicked, j);
                        clicked.teleport(j.getLoc());
                        p.teleport(j.getLoc().clone().add(0, 5, 0));
                        p.sendMessage("§aVocê foi teleportado até a jail do jogador.");
                        p.sendMessage("§aVocê está visível para §f" + clicked.getName());
                        p.sendMessage("§aPara soltar o jogador, clique com o direito nele com o item da jail.");
                        clicked.showPlayer(p);
                        Titles.sendTitle(clicked, "", "§cVoc§ foi preso!", 10, 10, 10);
                        clicked.sendMessage("");
                        clicked.sendMessage("");
                        clicked.sendMessage("");
//						clicked.sendMessage("§cVoc§ foi preso por §r" + tag.getPrefixTag(tag.getCargo(p.getName())) + " " + p.getName());
                        clicked.sendMessage("§cNão deslogue do servidor, caso contrário será punido!");
                        clicked.sendMessage("§cBaixe o aplicativo AnyDesk.");
                        clicked.sendMessage("§cMande seu código para o staffer.");
                        clicked.sendMessage("§cAtenda ao pedido de conexão.");
                        clicked.sendMessage("§cNão deslogue, nem feche seu anydesk em meio ao processo de screenshare.");
                        clicked.sendMessage("");
                        clicked.sendMessage("");
                        clicked.sendMessage("");
                        clicked.playSound(clicked.getLocation(), Sound.NOTE_BASS, 10, -5);

                        achou = true;

                        ItemStack item = new ItemStack(397, 1, (byte)3);
                        SkullMeta meta = (SkullMeta) item.getItemMeta();
                        meta.setDisplayName("§c" + clicked.getName());
                        meta.setOwner(clicked.getName());
//						meta.setLore(Arrays.asList("§7preso por §r" + tag.getPrefixTag(tag.getCargo(p.getName())) + " " + p.getName()));
                        item.setItemMeta(meta);

                        inv.setItem(getSlot(inv), item);

                        aaaa.remove(clicked);

                        break;

                    }

                }
                if (!achou)
                    p.sendMessage("§cNenhuma jail dispon§vel!");
            }
            if (p.getItemInHand().isSimilar(Admin.getInfo())){
                p.sendMessage("");
                p.sendMessage("§fInformações: §6" + clicked.getName());
                p.sendMessage("§fGamemode: §6" + clicked.getGameMode().name());
                p.sendMessage("§fIp: §6" + clicked.getAddress().getAddress().getHostAddress());
                p.sendMessage("§fVida: §6" + (int)clicked.getHealth());
                p.sendMessage("");
            }
        }
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (!p.hasPermission(Main.getPlugin().getConfig().getString("Permissoes.Admin"))) {
            for (Player o : Admin.getAdmins()){
                p.hidePlayer(o);
            }
        }
    }
    /*@EventHandler
    public void onPvP(AntiRelogEnterPvPEvent e){
        Player p = e.getAttacker();
        if (Admin.getAdmins().contains(p))
            e.setCancelled(true);
    }*/
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if (telando.containsKey(p)){
            e.setCancelled(true);
        } else {
            if (Admin.getAdmins().contains(p)){
                ItemStack item = e.getItemDrop().getItemStack();
                if (item.isSimilar(Admin.getGM())
                        || item.isSimilar(Admin.getInfo())
                        || item.isSimilar(Admin.getJail())
                        || item.isSimilar(Admin.getQuickChange())
                        || item.isSimilar(Admin.getPresos())){
                    e.setCancelled(true);
                }
            }
        }
    }
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equalsIgnoreCase("Presos - Info")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null){
                if (e.getCurrentItem().getType() != Material.AIR){
                    ItemStack item = e.getCurrentItem();
                    if (item.getType() == Material.SLIME_BLOCK) {

                        ItemStack a = e.getInventory().getItem(13);
                        String n = a.getItemMeta().getDisplayName().split("§c")[1];
                        Player o = Bukkit.getPlayer(n);
                        if (o == null){
                            p.closeInventory();
                            p.sendMessage("§cJogador n§o encontrado.");
                            return;
                        }
                        if (!telando.containsKey(o)){
                            p.closeInventory();
                            p.sendMessage("§cEste jogador n§o est§ mais preso.");
                            return;
                        }
                        Jail j = telando.get(o);
                        p.closeInventory();
                        telando.remove(o);

                        if (j.getStaff().getName().equalsIgnoreCase(p.getName()))
                            p.sendMessage("§f" + o.getName() + "§a n§o est§ mais te vendo.");

                        j.setUse(false);
                        j.setStaff(null);
                        j.setEntity(null);

                        o.teleport(Bukkit.getWorld("spawn").getSpawnLocation());
                        o.sendMessage("§aVoc§ foi liberado.");
                        o.playSound(o.getLocation(), Sound.ORB_PICKUP, 10, 1);

                        for (Player b : Admin.getAdmins()){
                            o.hidePlayer(b);
                        }
                        p.sendMessage("§aJogador liberado.");
                        updateJailMenu();
                        return;

                    }
                    if (item.getType() == Material.ENDER_PEARL) {
                        ItemStack a = e.getInventory().getItem(13);
                        String n = a.getItemMeta().getDisplayName().split("§c")[1];
                        Player o = Bukkit.getPlayer(n);
                        if (o == null){
                            p.closeInventory();
                            p.sendMessage("§cJogador n§o encontrado.");
                            return;
                        }
                        if (!telando.containsKey(o)){
                            p.closeInventory();
                            p.sendMessage("§cEste jogador n§o est§ mais preso.");
                            return;
                        }
                        Jail j = telando.get(o);
                        p.teleport(j.getLoc().clone().add(0, 5, 0));
                        p.sendMessage("§aVoc§ foi teleportado at§ a jail do jogador.");
                        p.closeInventory();
                        return;
                    }
                }
            }
        }
        if (e.getInventory().getName().equalsIgnoreCase("Presos")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null){
                if (e.getCurrentItem().getType() != Material.AIR){
                    ItemStack item = e.getCurrentItem();
                    if (item.getTypeId() == 397 && item.getDurability() == 3) {

                        if (item.hasItemMeta()){
                            if (item.getItemMeta().hasDisplayName()){
                                String display = item.getItemMeta().getDisplayName();
                                String nick = display.split("§c")[1];
                                Player player = Bukkit.getPlayer(nick);
                                if (telando.containsKey(player)){

                                    Inventory inv = Bukkit.createInventory(null, 4*9, "Presos - Info");

                                    ItemStack lib = new ItemStack(Material.SLIME_BLOCK);
                                    ItemMeta libm = lib.getItemMeta();
                                    libm.setDisplayName("§aSoltar jogador");
                                    lib.setItemMeta(libm);

                                    ItemStack t = new ItemStack(Material.ENDER_PEARL);
                                    ItemMeta tm = t.getItemMeta();
                                    tm.setDisplayName("§aTeleportar");
                                    t.setItemMeta(tm);

                                    inv.setItem(13, item);
                                    inv.setItem(20, lib);
                                    inv.setItem(24, t);
                                    p.openInventory(inv);
                                } else {
                                    p.sendMessage("§cJogador inv§lido");
                                    updateJailMenu();

                                }
                            }
                        }
                    }
                }
            }

            return;
        }
        if (Admin.getAdmins().contains(p)){
            if (e.getCurrentItem() != null){
                if (e.getCurrentItem().getType() != Material.AIR){
                    ItemStack item = e.getCurrentItem();
                    if (item.isSimilar(Admin.getGM())
                            || item.isSimilar(Admin.getInfo())
                            || item.isSimilar(Admin.getJail())
                            || item.isSimilar(Admin.getQuickChange())
                            || item.isSimilar(Admin.getPresos())){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    public int getSlot(Inventory inv) {
        int[] i = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28};
        for (int a : i){
            if (inv.getItem(a) == null || inv.getItem(a).getType() == Material.AIR){
                return a;
            }
        }
        return -1;
    }
}
