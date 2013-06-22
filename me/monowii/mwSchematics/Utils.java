package me.monowii.mwSchematics;

import java.io.File;

import org.bukkit.World;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;

public class Utils
{
	public static int parseInt(String number)
	{
		return Integer.valueOf(number);
	}
	
	public static boolean isInt(String number)
	{
		try
		{
			Integer.valueOf(number);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	
	public static boolean copySchematic(World w, File schematicFile, Vector origin)
	{
		EditSession es = new EditSession(new BukkitWorld(w), -1);
		
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
