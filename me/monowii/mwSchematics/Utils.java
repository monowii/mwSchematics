package me.monowii.mwSchematics;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Item;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;

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
	
	public static boolean copySchematic(World w, File schematicFile, Vector origin, boolean copyAir, boolean removeEntities, final String entitiesToRemove[])
	{
		EditSession es = new EditSession(new BukkitWorld(w), -1);
		
		SchematicFormat schematic = SchematicFormat.getFormat(schematicFile);
		
        try {
            CuboidClipboard clipboard = schematic.load(schematicFile);
            clipboard.paste(es, origin, copyAir);
            
            if (removeEntities)
            {
            	final Location p1 = new Location(w, origin.getX() + clipboard.getOffset().getX(), origin.getY() + clipboard.getOffset().getY(), origin.getZ() + clipboard.getOffset().getZ());
            	final Location p2 = new Location(w, p1.getX() + clipboard.getWidth(), p1.getY() + clipboard.getHeight(), p1.getZ() + clipboard.getLength());
            	
            	removeEntities(p1, p2, entitiesToRemove);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		return false;
	}
	
	private static void removeEntities(Location p1, Location p2, String entitiesToRemove[])
	{
		int xMin = Math.min(p1.getBlockX(), p2.getBlockX());
		int xMax = Math.max(p1.getBlockX(), p2.getBlockX());
		int zMin = Math.min(p1.getBlockZ(), p2.getBlockZ());
		int zMax = Math.max(p1.getBlockZ(), p2.getBlockZ());
		int yMin = Math.min(p1.getBlockY(), p2.getBlockY());
		int yMax = Math.max(p1.getBlockY(), p2.getBlockY());
		
		Chunk c1 = p1.getChunk();
		Chunk c2 = p2.getChunk();
		
		for(int x = Math.min(c1.getX(), c2.getX()); x <= Math.max(c1.getX(), c2.getX()); x++)
			for(int z = Math.min(c1.getZ(), c2.getZ()); z <= Math.max(c1.getZ(), c2.getZ()); z++)
				for (Entity e : p1.getWorld().getChunkAt(x, z).getEntities())
					if ((e.getLocation().getBlockX() >= xMin || e.getLocation().getBlockX() <= xMax) && (e.getLocation().getBlockY() >= yMin || e.getLocation().getBlockY() <= yMax) && (e.getLocation().getBlockZ() >= zMin || e.getLocation().getBlockZ() <= zMax)) {
						
						if (entitiesToRemove == null) {
							if (e instanceof Projectile || e instanceof Boat || e instanceof Item || e instanceof FallingBlock || e instanceof Minecart || e instanceof Hanging || e instanceof TNTPrimed || e instanceof ExperienceOrb) {
								e.remove();
							}
						} else {
							if (argsContain(entitiesToRemove, "all")) {
								if (e instanceof Projectile || e instanceof Boat || e instanceof Item || e instanceof FallingBlock || e instanceof Minecart || e instanceof Hanging || e instanceof TNTPrimed || e instanceof ExperienceOrb) {
									e.remove();
								}
							} else {
								if (argsContain(entitiesToRemove, "minecart")) {
									if (e instanceof Minecart) {
										e.remove();
									}
								}
								if (argsContain(entitiesToRemove, "drop")) {
									if (e instanceof Item) {
										e.remove();
									}
								}
								if (argsContain(entitiesToRemove, "xp")) {
									if (e instanceof ExperienceOrb) {
										e.remove();
									}
								}
								if (argsContain(entitiesToRemove, "projectile")) {
									if (e instanceof Projectile) {
										e.remove();
									}
								}
								if (argsContain(entitiesToRemove, "boat")) {
									if (e instanceof Boat) {
										e.remove();
									}
								}
							}
						}
					}
	}
	
	public static boolean argsContain(String[] args, String arg)
	{
		for (int i = 0 ; i < args.length ; i++)
			if (args[i].equalsIgnoreCase(arg) || args[i].startsWith(arg))
				return true;
		
		
		return false;
	}
}
