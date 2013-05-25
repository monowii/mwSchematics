package me.monowii.mwSchematics;

import java.io.File;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Schematics extends JavaPlugin
{
	private boolean worldEditEnable = false;
	private EditSession es = null;

	public void onEnable()
	{
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		if (getWorldEdit().isEnabled())
		{
			worldEditEnable = true;
		}
		else
		{
			System.out.println("Impossible to init worldEdit in mwSchematics !");
		}
	}

	private WorldEditPlugin getWorldEdit()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");
		if (plugin == null || !(plugin instanceof WorldEditPlugin))
		{
			return null;
		}
		return (WorldEditPlugin) plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args)
	{
		Player p = null;
		if (sender instanceof Player) p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("ms") && (p == null || p.hasPermission("mwSchematics.admin")))
		{
			if (worldEditEnable)
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
								}
							}
							else
							{
								sender.sendMessage("§cSchematic file not found");
							}
						}
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
		return Integer.parseInt(number);
	}
	
	private boolean isInt(String number)
	{
		try
		{
			Integer.parseInt(number);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public boolean copySchematic(World w, File schematicFile, Vector origin, boolean air)
	{
		if (es == null) {
			es = new EditSession(new BukkitWorld(w), 999999999);
		}

		CuboidClipboard cc;

		try
		{
			cc = CuboidClipboard.loadSchematic(schematicFile);
			cc.paste(es, origin, air);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/*@EventHandler
	public void onInteract(PlayerInteractEvent e) {                //Not usefull, commands blocks works
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign s = (Sign) e.getClickedBlock().getState();
				
				if (s.getLine(0).equalsIgnoreCase("[mwSchematic]"))
				{
					File schematicFile = new File(s.getLine(1) + ".schematic");
					
					if (schematicFile.exists())
					{
						String []pos = s.getLine(2).split(" ");
						
						if (isDouble(pos[0]) && isDouble(pos[1]) && isDouble(pos[2]))
						{
							double xPos = parseDouble(pos[0]);
							double yPos = parseDouble(pos[1]);
							double zPos = parseDouble(pos[2]);
							
							Location loc = new Location(s.getWorld(), xPos, yPos, zPos);
							
							copySchematic(schematicFile, loc, true);
						}
					}
				}
			}
		}
	}*/
}
