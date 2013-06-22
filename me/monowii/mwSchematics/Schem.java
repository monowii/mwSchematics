package me.monowii.mwSchematics;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Schem extends JavaPlugin
{
	public static WorldEditPlugin WorldEdit = null;
	
	public void onEnable()
	{
		//Start Metrics
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) { }
		
		getCommand("mws").setExecutor(new SchemCommand(this));
		
		WorldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		
		if (!WorldEdit.isEnabled()) {
			System.out.println("Impossible to initialize worldEdit in mwSchematics !");
		}
	}
}
