package org.meowcat.DontTapTheWhiteTile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListener implements Listener {
	public Player currentset;
	public int setstage;
	public Player currentplayer;
	public boolean on;
	public Location buttonloc;
	public Location bl;
	public Location br;
	public ArrayList<Location> locations;
	public boolean isset;
	public Random random;
	public int blockremaining;
	public long starttime;
	public Location[] locationa;
	public ArrayList<Location> locationc = new ArrayList<Location>();
	public Location signat1;
	public Location signat2;
	public Location signat3;

	public EventListener() {
		this.isset = false;
		this.random = new Random();
		this.on = false;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
				&& (event.getClickedBlock().getLocation().equals(this.buttonloc)) && (this.isset)) {
			if ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
					&& (event.getClickedBlock().getLocation().equals(this.buttonloc))) {
				event.getPlayer().sendMessage("Don't Tap The White Block!");
				resetstage();
				this.starttime = 0L;
				this.on = true;
				this.blockremaining = 45;
				this.currentplayer = event.getPlayer();
			}
		} else if ((this.on) && (event.getPlayer().equals(this.currentplayer))
				&& ((event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
						|| (event.getAction().equals(Action.LEFT_CLICK_BLOCK)))) {
			Block blockclicked = event.getClickedBlock();
			if (blockclicked.getLocation().getBlockY() != this.bl.getBlockY()) {
				return;
			}
			if (blockclicked.getData() == 0) {
				this.currentplayer.sendMessage("Game Over!");
				this.on = false;
				this.currentplayer = null;
				this.starttime = 0L;
				for (Location lo : this.locations) {
					lo.getBlock().setType(Material.WOOL);
					lo.getBlock().setData((byte) 14);
				}
				return;
			}
			if (blockclicked.getData() == 15) {
				if (this.starttime == 0L) {
					this.starttime = System.currentTimeMillis();
				}
				for (int at = 0; at < 16; at++) {
					this.locationa[at].getBlock().setData(this.locationa[(at + 4)].getBlock().getData());
				}
				if (this.blockremaining > 0) {
					int blackat = 16 + this.random.nextInt(4);
					for (int at = 16; at < 20; at++) {
						if (at == blackat) {
							this.locationa[at].getBlock().setData((byte) 15);
						} else {
							this.locationa[at].getBlock().setData((byte) 0);
						}
					}
					this.blockremaining -= 1;
				} else {
					for (int at = 16; at < 20; at++) {
						this.locationa[at].getBlock().setData((byte) 5);
					}
					this.blockremaining -= 1;
				}
				if (this.blockremaining == -5) {
					long endtime = System.currentTimeMillis();
					long espl = endtime - this.starttime;

					this.currentplayer.sendMessage("You Win!");
					this.currentplayer.sendMessage("Time: " + (float) espl / 1000.0F);
					highscore(this.currentplayer, espl);
					this.starttime = 0L;
					this.on = false;
					this.currentplayer = null;
				}
			}
		} else if ((this.currentset != null) && (event.getPlayer().equals(this.currentset))
				&& (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			if ((this.setstage == 0) && ((event.getClickedBlock().getType().equals(Material.STONE_BUTTON))
					|| (event.getClickedBlock().getType().equals(Material.WOOD_BUTTON)))) {
				this.buttonloc = event.getClickedBlock().getLocation();
				this.currentset.sendMessage("Button location set. Next: Wool bottom-left corner.");
				this.setstage = 1;
				return;
			}
			if (this.setstage == 1) {
				this.bl = event.getClickedBlock().getLocation();
				this.currentset
						.sendMessage("Bottom-left location set. Next: Wool bottom-right corner.");
				this.setstage = 2;
				return;
			}
			if (this.setstage == 2) {
				this.br = event.getClickedBlock().getLocation();
				this.currentset.sendMessage("Bottom-right location set. Next: Sign 1");
				this.setstage = 3;

				return;
			}
			if (this.setstage == 3) {
				this.signat1 = event.getClickedBlock().getLocation();

				this.currentset.sendMessage("Sign 1 set. Next: Sign 2.");
				this.setstage = 4;
				return;
			}
			if (this.setstage == 4) {
				this.signat2 = event.getClickedBlock().getLocation();
				this.currentset.sendMessage("Sign 2 set. Next: Sign 3.");
				this.setstage = 5;
				return;
			}
			if (this.setstage == 5) {
				this.signat3 = event.getClickedBlock().getLocation();
				this.currentset.sendMessage("Sign 3 set.");
				this.setstage = 6;
				calclocations();
				this.currentset = null;
				this.setstage = 0;
				return;
			}
		}
	}

	public void highscore(Player p, long time) {
		Float ftime = Float.valueOf((float) time / 1000.0F);
		if (((this.signat1.getBlock().getState() instanceof Sign))
				&& ((this.signat2.getBlock().getState() instanceof Sign))
				&& ((this.signat3.getBlock().getState() instanceof Sign))) {
			Sign sign1 = (Sign) this.signat1.getBlock().getState();
			Sign sign2 = (Sign) this.signat2.getBlock().getState();
			Sign sign3 = (Sign) this.signat3.getBlock().getState();
			if ((sign1.getLine(3).equals("")) || (Float.parseFloat(sign1.getLine(3)) < 0.1D)) {
				sign1.setLine(3, "999.999");
				sign1.update();
			}
			if ((sign2.getLine(3).equals("")) || (Float.parseFloat(sign2.getLine(3)) < 0.1D)) {
				sign2.setLine(3, "999.999");
				sign2.update();
			}
			if ((sign3.getLine(3).equals("")) || (Float.parseFloat(sign3.getLine(3)) < 0.1D)) {
				sign3.setLine(3, "999.999");
				sign3.update();
			}
			if (Float.parseFloat(sign3.getLine(3)) < ftime.floatValue()) {
				return;
			}
			if (Float.parseFloat(sign1.getLine(3)) > ftime.floatValue()) {
				sign3.setLine(3, sign2.getLine(3));
				sign3.setLine(2, sign2.getLine(2));
				sign2.setLine(3, sign1.getLine(3));
				sign2.setLine(2, sign1.getLine(2));
				sign1.setLine(3, String.valueOf(ftime));
				sign1.setLine(2, p.getName().substring(0, Math.min(16, p.getName().length())));
				sign1.update();
				sign2.update();
				sign3.update();
				p.sendMessage("New Highscore (1st)!!!");
				return;
			}
			if (Float.parseFloat(sign2.getLine(3)) > ftime.floatValue()) {
				sign3.setLine(3, sign2.getLine(3));
				sign3.setLine(2, sign2.getLine(2));
				sign2.setLine(3, String.valueOf(ftime));
				sign2.setLine(2, p.getName().substring(0, Math.min(16, p.getName().length())));
				sign2.update();
				sign3.update();
				p.sendMessage("New Highscore (2nd)!!!");
				return;
			}
			if (Float.parseFloat(sign3.getLine(3)) > ftime.floatValue()) {
				sign3.setLine(3, String.valueOf(ftime));
				sign3.setLine(2, p.getName().substring(0, Math.min(16, p.getName().length())));
				sign3.update();
				p.sendMessage("New Highscore (3rd)!!!");
				return;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void resetstage() {
		int nowat = 0;
		int putat = 0;
		for (Location loc : this.locations) {
			if (nowat % 4 == 0) {
				putat = nowat + this.random.nextInt(4);
			}
			if (nowat == putat) {
				loc.getBlock().setData((byte) 15);
			} else {
				loc.getBlock().setData((byte) 0);
			}
			nowat++;
		}
	}

	@SuppressWarnings("deprecation")
	public void calclocations() {
		if (this.br.getBlockY() != this.bl.getBlockY()) {
			this.currentset.sendMessage("Wrong block Y.");
			return;
		}
		if (((this.br.getBlockX() != this.bl.getBlockX()) || (Math.abs(this.br.getBlockZ() - this.bl.getBlockZ()) != 3))
				&& ((this.br.getBlockZ() != this.bl.getBlockZ())
						|| (Math.abs(this.br.getBlockX() - this.bl.getBlockX()) != 3))) {
			this.currentset.sendMessage("Wrong block XZ.");
			this.currentset.sendMessage(this.bl.toString());
			this.currentset.sendMessage(this.br.toString());
			return;
		}
		if ((this.br.clone().subtract(this.bl).getBlockZ() > 1)
				|| (this.br.clone().subtract(this.bl).getBlockX() > 1)) {
			Location temp = this.bl.clone();
			this.bl = this.br;
			this.br = temp;
		}
		this.locations = new ArrayList<Location>();
		Location offset = this.br.clone().subtract(this.bl);
		offset = offset.multiply(0.25D);
		int x;
		for (int y = 0; y < 5; y++) {
			for (x = 0; x < 4; x++) {
				this.locations.add(this.bl.clone().add(offset.clone().multiply(x)).add(0.0D, y, 0.0D));
				if (y == 0) {
					this.locationc.add(this.bl.clone().add(offset.clone().multiply(x)).add(0.0D, y, 0.0D));
				}
			}
		}
		for (Location loca : this.locations) {
			loca.getBlock().setType(Material.WOOL);
			loca.getBlock().setData((byte) 0);
		}
		this.locationa = ((Location[]) this.locations.toArray(new Location[this.locations.size()]));
		this.isset = true;
	}

	public boolean registerset(Player p) {
		boolean ret = false;
		if (this.currentset != null) {
			ret = true;
		}
		this.currentset = p;
		this.setstage = 0;
		return ret;
	}

	public void save(File fold) {
		if (!this.isset) {
			return;
		}
		File folder = fold;
		if (!folder.exists()) {
			try {
				folder.mkdir();
			} catch (Exception localException) {
			}
		}
		File fi = new File(folder, "config.txt");
		if (fi.exists()) {
			fi.delete();
		}
		try {
			Bukkit.getLogger().info("[DTTWB] saving");
			fi.createNewFile();
			PrintWriter wrt = new PrintWriter(new FileWriter(fi));
			wrt.println(this.buttonloc.getWorld().getName() + ":" + this.buttonloc.getBlockX() + ":"
					+ this.buttonloc.getBlockY() + ":" + this.buttonloc.getBlockZ());
			wrt.println(this.bl.getWorld().getName() + ":" + this.bl.getBlockX() + ":" + this.bl.getBlockY() + ":"
					+ this.bl.getBlockZ());
			wrt.println(this.br.getWorld().getName() + ":" + this.br.getBlockX() + ":" + this.br.getBlockY() + ":"
					+ this.br.getBlockZ());
			wrt.println(this.signat1.getWorld().getName() + ":" + this.signat1.getBlockX() + ":"
					+ this.signat1.getBlockY() + ":" + this.signat1.getBlockZ());
			wrt.println(this.signat2.getWorld().getName() + ":" + this.signat2.getBlockX() + ":"
					+ this.signat2.getBlockY() + ":" + this.signat2.getBlockZ());
			wrt.println(this.signat3.getWorld().getName() + ":" + this.signat3.getBlockX() + ":"
					+ this.signat3.getBlockY() + ":" + this.signat3.getBlockZ());
			wrt.close();
		} catch (IOException localIOException) {
		}
	}

	public void load(File fold) {
		File folder = fold;
		if (!folder.exists()) {
			try {
				folder.mkdir();
			} catch (Exception localException) {
			}
		}
		File fi = new File(folder, "config.txt");
		if (!fi.exists()) {
			return;
		}
		Bukkit.getLogger().info("[DTTWB] loading");
		List<String> got = scan(fi);
		String[] gota = (String[]) got.toArray(new String[6]);

		String[] temp = gota[0].split(":");
		this.buttonloc = new Location(Bukkit.getWorld(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),
				Integer.parseInt(temp[3]));
		temp = gota[1].split(":");
		this.bl = new Location(Bukkit.getWorld(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),
				Integer.parseInt(temp[3]));
		temp = gota[2].split(":");
		this.br = new Location(Bukkit.getWorld(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),
				Integer.parseInt(temp[3]));
		temp = gota[3].split(":");
		this.signat1 = new Location(Bukkit.getWorld(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),
				Integer.parseInt(temp[3]));
		temp = gota[4].split(":");
		this.signat2 = new Location(Bukkit.getWorld(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),
				Integer.parseInt(temp[3]));
		temp = gota[5].split(":");
		this.signat3 = new Location(Bukkit.getWorld(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]),
				Integer.parseInt(temp[3]));
		calclocations();
		this.isset = true;
	}

	public List<String> scan(File file) {
		List<String> ret = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new FileReader(file));
			while (scanner.hasNextLine()) {
				ret.add(scanner.nextLine());
			}
			scanner.close();
		} catch (Exception localException) {
		}
		return ret;
	}
}
