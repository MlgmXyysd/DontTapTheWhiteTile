package org.meowcat.DontTapTheWhiteTile;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class Commands implements CommandExecutor {



	public boolean onCommand(CommandSender player, Command command, String label, String[] arguments) {
		if (!(player instanceof Player)) {
			return false;
		}
		Player p = (Player) player;
		if ((command.getName().equals("dttwb")) && (arguments.length == 1)) {
			if ((!this.pl.isset) && (arguments[0].equals("set"))) {
				this.pl.registerset(p);
				p.sendMessage("Start setting. Right-Click a button.");
				return true;
			}
			if ((this.pl.isset) && (arguments[0].equals("set"))) {
				PlayerInteractEvent.getHandlerList().unregister(this.pl);
				this.pl = new EventListener();
				this.pl.registerset(p);
				getServer().getPluginManager().registerEvents(this.pl, this);
				p.sendMessage("Start resetting. Right-Click a button.");
				return true;
			}
			if ((this.pl.isset) && (arguments[0].equals("save"))) {
				this.pl.save(getDataFolder());
			} else if (arguments[0].equals("load")) {
				this.pl.load(getDataFolder());
			}
		}
		return true;
	}

}
