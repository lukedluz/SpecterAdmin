package me.lukedluz.SpecterAdmin.Eventos;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Jail {

	private Location loc;
	private boolean emuso = false;
	private Player entity;
	private Player staff;
	
	public Jail(Location loc){
		this.loc = loc;
	}
	public void setStaff(Player p){
		this.staff = p;
	}
	public void setEntity(Player p){
		this.entity = p;
	}
	public Player getStaff(){
		return staff;
	}
	public Player getPlayer(){
		return entity;
	}
	public boolean isInUse(){
		return emuso;
	}
	public void setUse(boolean i){
		this.emuso = i;
	}
	public Location getLoc(){
		return loc;
	}
}
