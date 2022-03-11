package me.lukedluz.SpecterAdmin.Comandos;

import me.lukedluz.SpecterAdmin.Eventos.Admin;
import me.lukedluz.SpecterAdmin.Eventos.Jail;
import me.lukedluz.SpecterAdmin.Main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;

public class AdminC implements CommandExecutor {

    public static HashSet<Jail> jails = new HashSet<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = (Player) sender;
            if (label.equalsIgnoreCase("admin")) {

                if (!p.hasPermission(Main.getPlugin().getConfig().getString("Permissoes.Admin"))) {
                    p.sendMessage(Main.getPlugin().getConfig().getString("Mensagens.SemPermissao"));
                    return true;
                } else {

                    if (Admin.getAdmins().contains(p)){
                        Admin.remove(p);
                    } else {
                        Admin.addPlayer(p);
                    }
                }
            }
            if (label.equalsIgnoreCase("setadmin")) {
                if (!p.hasPermission(Main.getPlugin().getConfig().getString("Permissoes.SetAdmin"))) {
                    p.sendMessage(Main.getPlugin().getConfig().getString("Mensagens.SemPermissao2"));
                    return true;
                }
                if (args.length == 0){
                    p.sendMessage("§bSpecterAdmin §8- §fComandos");
                    p.sendMessage("§a");
                    p.sendMessage("§7/admin - Entrar no modo admin");
                    p.sendMessage("§7/setadmin addjail - Adicionar uma nova jail");
                    p.sendMessage("§7/setadmin clear - Apagar todas as jails");
                    p.sendMessage("§a");
                    return true;
                }
                if (args.length == 1){
                    switch (args[0]) {
                        case "addjail":

                            Location loc = p.getLocation();
                            int i = 0;
                            for (String a : Main.getPlugin().getConfig().getConfigurationSection("jails").getKeys(false)) {
                                i++;
                            }
                            i = i + 1;

                            double x = loc.getX();
                            double y = loc.getY();
                            double z = loc.getZ();
                            double yaw = loc.getYaw();
                            double pitch = loc.getPitch();

                            Main.getPlugin().getConfig().set("jails." + i, x + ":" + y + ":" + z + ":" + yaw + ":" + pitch + ":" + p.getWorld().getName());

                            p.sendMessage("§aJail adicionada.");

                            Main.getPlugin().saveConfig();

                            break;
                        case "clear":

                            Main.getPlugin().getConfig().set("jails", null);
                            Main.getPlugin().saveConfig();
                            p.sendMessage("§aJails resetadas.");
                            jails.clear();

                            break;

                        default:
                            p.sendMessage("§bSpecterAdmin §8- §fComandos");
                            p.sendMessage("§a");
                            p.sendMessage("§7/admin - Entrar no modo admin");
                            p.sendMessage("§7/setadmin addjail - Adicionar uma nova jail");
                            p.sendMessage("§7/setadmin clear - Apagar todas as jails");
                            p.sendMessage("§a");
                            break;
                    }
                }
            }
        }
        return false;
    }
}
