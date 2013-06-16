package me.monowii.mwSchematics;

import java.io.File;
import java.io.IOException;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class mwSchematics extends JavaPlugin
{
	private WorldEditPlugin we = null;
	
	public void onEnable()
	{
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		}
		catch (IOException e) {
			System.out.println("Failed to submit data :(");
		}
		
		we = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
		
		if (we.isEnabled()) {
			if (!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
		}
		else {
			System.out.println("Impossible to init worldEdit in mwSchematics !");
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args)
	{
		Player p = null;
		if (sender instanceof Player) p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("ms") && (p == null || p.hasPermission("mwSchematics.admin")))
		{
			if (we.isEnabled())
			{
				if (args.length == 0)
				{
					sender.sendMessage("§a---=[ mwSchematics v"+getDescription().getVersion()+" by monowii ]=---");
					sender.sendMessage("/ms copy <schematicFileName> [x y z worldName]");
				}
				else
				{
					if (args[0].equalsIgnoreCase("copy") )
					{
						if (p != null && args.length == 2)
						{
							String fileName = args[1];
							File schematicFile = new File(getDataFolder(), fileName + ".schematic");

							if (schematicFile.exists())
							{
								Vector loc = new Vector(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ() + 1);
								
								if (copySchematic(p.getWorld(), schematicFile, loc, true))
								{
									p.sendMessage("§aSchematic pasted");
								}
								else
								{
									p.sendMessage("§cError while pasting schematic");
								}
							}
							else
							{
								p.sendMessage("§cSchematic file not found");
							}
						}
						else if (args.length == 6)
						{
							String fileName = args[1];
							File schematicFile = new File(getDataFolder(), fileName + ".schematic");
							
							if (schematicFile.exists())
							{
								if (isInt(args[2]) && isInt(args[3]) && isInt(args[4])) //Check if positions are double
								{
									if (getServer().getWorld(args[5]) != null) //Check if world exist
									{
										int xPos = parseInt(args[2]);
										int yPos = parseInt(args[3]);
										int zPos = parseInt(args[4]) + 1;
										String world = args[5];
										Vector loc = new Vector(xPos, yPos, zPos);
										
										if (copySchematic(getServer().getWorld(world), schematicFile, loc, true))
										{
											sender.sendMessage("§aSchematic pasted");
										}
										else
										{
											sender.sendMessage("§cError while pasting schematic");
										}
									}
									else
									{
										sender.sendMessage("§cIncorrect world !");
									}
								}
								else
								{
									sender.sendMessage("§cIncorrect positions !");
								}
							}
							else
							{
								sender.sendMessage("§cSchematic file not found");
							}
						}
						else
						{
							sender.sendMessage("§cIncorrect command usage !");
						}
					}
					else
					{
						sender.sendMessage("§cIncorrect command usage !");
					}
				}
			}
			else
			{
				sender.sendMessage("§cWorldEdit is not enable !");
			}
		}

		return false;
	}
	
	private int parseInt(String number)
	{
		return Integer.valueOf(number);
	}
	
	private boolean isInt(String number)
	{
		try
		{
			parseInt(number);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean copySchematic(World w, File schematicFile, Vector origin, boolean p)
	{
		EditSession es = new EditSession(new BukkitWorld(w), 999999999);
		
		SchematicFormat schematic = SchematicFormat.getFormat(schematicFile);
		 
        try {
            CuboidClipboard clipboard = schematic.load(schematicFile);
            clipboard.paste(es, origin, false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return false;
	}
}
