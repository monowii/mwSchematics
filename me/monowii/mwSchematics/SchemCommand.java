package me.monowii.mwSchematics;

import java.io.File;

import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.Vector;

public class SchemCommand implements CommandExecutor
{
	private JavaPlugin plugin = null;
	public SchemCommand(JavaPlugin pl) { plugin = pl; }
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args)
	{
		if (!Schem.WorldEdit.isEnabled())
			return false;
		
		CommandBlock cmdBlock = null;
		
		if (sender instanceof BlockCommandSender) {
			BlockCommandSender cmdBlockSender = (BlockCommandSender) sender;
			cmdBlock = (CommandBlock) cmdBlockSender.getBlock().getState();
		}
		
		if (args.length == 0) {
			sender.sendMessage("§a---=[ §2mwSchematics§a v"+plugin.getDescription().getVersion()+" by monowii ]=---");
			sender.sendMessage("§8<> : Required // [] : Only for commandBlock");
			sender.sendMessage("§7/mws copy <schematicFileName> <x y z worldName> [relative]");
		}
		//If args 0 is copy and is executed by commandBlock or console and args length >= 6
		else if (args[0].equalsIgnoreCase("copy"))
		{
			if (args.length >= 6)
			{
				
				boolean relativeToCommandBlock = false;
				//Only supported by commandBlock
				if (args.length > 6 && cmdBlock != null)
					if (args[6].equalsIgnoreCase("relative"))
						relativeToCommandBlock = true;
				
				
				File schematicFile = new File(Schem.WorldEdit.getDataFolder() + File.separator + "schematics", args[1] + ".schematic");
				
				if (schematicFile.exists())
				{
					//Check if positions are double and world exist
					if (Utils.isInt(args[2]) && Utils.isInt(args[3]) && Utils.isInt(args[4]) && plugin.getServer().getWorld(args[5]) != null) 
					{
						int xPos = Utils.parseInt(args[2]);
						int yPos = Utils.parseInt(args[3]);
						int zPos = Utils.parseInt(args[4]);
						
						if (relativeToCommandBlock) {
							xPos = cmdBlock.getBlock().getX() + xPos;
							yPos = cmdBlock.getBlock().getY() + yPos;
							zPos = cmdBlock.getBlock().getZ() + zPos;
						}
						
						String world = args[5];
						Vector loc = new Vector(xPos, yPos, zPos);
						
						if (Utils.copySchematic(plugin.getServer().getWorld(world), schematicFile, loc)) {
							sender.sendMessage("§aSchematic pasted !");
						} else {
							sender.sendMessage("§cError while pasting schematic");
						}
					}
				}
			} else {
				sender.sendMessage("§4Bad command arguments");
			}
			
			
		} else {
			sender.sendMessage("§4Only command block can execute mwSchematics commands !");
		}
		

		return false;
	}

}
