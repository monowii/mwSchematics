package me.monowii.mwSchematics;

import java.io.File;

import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sk89q.worldedit.Vector;

public class SchemCommand implements CommandExecutor
{
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
			sender.sendMessage("§a---=[ §2mwSchematics§a v"+Schem.getPlugin().getDescription().getVersion()+" by monowii ]=---");
			sender.sendMessage("§8<> : Required / () : Optional");
			sender.sendMessage("§7/mws copy <schematicFileName> <x y z worldName> (relative -rel) (copyAir -ca) (removeEntities -re[minecart,drop])");
		}
		else
		{
			if (args[0].equalsIgnoreCase("copy") && (cmdBlock != null && sender.hasPermission("mwSchematics.admin")))
			{
				if (args.length >= 6)
				{
					File schematicFile = new File(Schem.WorldEdit.getDataFolder() + File.separator + "schematics", args[1] + ".schematic");
					
					//Only supported by commandBlock
					boolean relativeToCommandBlock = false;
					if (args.length > 6 && cmdBlock != null)
						if (Utils.argsContain(args, "-rel"))
							relativeToCommandBlock = true;
					
					boolean copyAir = false;
					if (args.length > 6)
						if (Utils.argsContain(args, "-ca"))
							copyAir = true;
					
					boolean removeEntities = false;
					String entitiesToRemove[] = null;
					if (args.length > 6)
						for (int i = 0 ; i < args.length ; i++)
							if (args[i].startsWith("-re")) {
								//If entities are specified
								if (args[i].length() > 5)
									entitiesToRemove = args[i].substring(3).replace("[", "").replace("]", "").split(",");
								
								removeEntities = true;
							}
					
					if (schematicFile.exists())
					{
						if (Utils.isInt(args[2]) && Utils.isInt(args[3]) && Utils.isInt(args[4]) && Schem.getPlugin().getServer().getWorld(args[5]) != null) 
						{
							int xPos = Utils.parseInt(args[2]);
							int yPos = Utils.parseInt(args[3]);
							int zPos = Utils.parseInt(args[4]);
							
							if (relativeToCommandBlock) {
								xPos = cmdBlock.getBlock().getX() + xPos;
								yPos = cmdBlock.getBlock().getY() + yPos;
								zPos = cmdBlock.getBlock().getZ() + zPos;
							}
							
							String worldName = args[5];
							Vector origin = new Vector(xPos, yPos, zPos);
							
							if (Utils.copySchematic(Schem.getPlugin().getServer().getWorld(worldName), schematicFile, origin, copyAir, removeEntities, entitiesToRemove)) {
								sender.sendMessage("§aSchematic pasted !");
							} else {
								sender.sendMessage("§cError while pasting schematic");
							}
						} else {
							sender.sendMessage("§cBad positions");
						}
					} else {
						sender.sendMessage("§cThis schematic file not exist");
					}
				} else {
					sender.sendMessage("§cBad command arguments");
				}
			} else {
				sender.sendMessage("§cUnknown argument");
			}
		}
		

		return false;
	}

}
