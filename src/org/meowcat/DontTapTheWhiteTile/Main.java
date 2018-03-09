package org.meowcat.DontTapTheWhiteTile;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public static Main Main;
	public EventListener EventListener = new EventListener();
	public void onEnable() {
		Main=this;
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.EventListener, this);
		this.EventListener.load(getDataFolder());
	}

	public void onDisable() {
	}

	public boolean onCommand(CommandSender player, Command command, String label, String[] arguments) {
		if (!(player instanceof Player)) {
			return false;
		}
		Player p = (Player) player;
		if ((command.getName().equals("dttwb")) && (arguments.length == 1)) {
			if ((!this.EventListener.isset) && (arguments[0].equals("set"))) {
				this.EventListener.registerset(p);
				p.sendMessage("Start setting. Right-Click a button.");
				return true;
			}
			if ((this.EventListener.isset) && (arguments[0].equals("set"))) {
				PlayerInteractEvent.getHandlerList().unregister(this.EventListener);
				this.EventListener = new EventListener();
				this.EventListener.registerset(p);
				getServer().getPluginManager().registerEvents(this.EventListener, this);
				p.sendMessage("Start resetting. Right-Click a button.");
				return true;
			}
			if ((this.EventListener.isset) && (arguments[0].equals("save"))) {
				this.EventListener.save(getDataFolder());
			} else if (arguments[0].equals("load")) {
				this.EventListener.load(getDataFolder());
			}
		}
		return true;
	}
}
