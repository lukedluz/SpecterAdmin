package me.lukedluz.SpecterAdmin;

import me.lukedluz.SpecterAdmin.Comandos.AdminC;
import me.lukedluz.SpecterAdmin.Eventos.Admin;
import me.lukedluz.SpecterAdmin.Eventos.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

//	public static SpecterPrefixos tag;
	public static Main plugin;
	public static Main getPlugin() {
		return plugin;
	}
	
	public void onEnable(){

		getCommand("admin").setExecutor(new AdminC());
		getCommand("setadmin").setExecutor(new AdminC());

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerEvents(), this);
//		tag = new SpecterPrefixos();
		saveDefaultConfig();
		Bukkit.getConsoleSender().sendMessage("§b[SpecterPlugins] §fO plugin '§aSpecterAdmin§f' está ativo e funcional.");
		Bukkit.getConsoleSender().sendMessage("§b##############################################");
		Bukkit.getConsoleSender().sendMessage("§b#  §fPlugin idealizado por: §bLucas L.           #");
		Bukkit.getConsoleSender().sendMessage("§b#  §fPlugin desenvolvido por: §bLucas L.         #");
		Bukkit.getConsoleSender().sendMessage("§b#  §fPlugin publicado por: §bSpecterPlugins      #");
		Bukkit.getConsoleSender().sendMessage("§b#  §fAjudantes: §bNinguém                        #");
		Bukkit.getConsoleSender().sendMessage("§b##############################################");
	}

	@Override
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("§b[SpecterPlugins] §fO plugin '§aSpecterAdmin§f' foi desligado com sucesso.");
	}
}
