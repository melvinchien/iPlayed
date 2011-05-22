/*package com.gmail.melvinchien.InspirePlayed;

import com.gmail.melvinchien.InspirePlayed.InspirePlayedPlayerListener;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
*/

/**
 * InspirePlayed block listener
 * @author VeiN
 */

/*
public class InspirePlayedBlockListener extends BlockListener {
    public final InspirePlayed plugin;

    public InspirePlayedBlockListener(final InspirePlayed plugin) {
        this.plugin = plugin;
    }

    public void onBlockPlace(BlockPlaceEvent event) {
		 //Get the player doing the placing
			Player player = event.getPlayer();
			//Get the block that was placed
			Block block = event.getBlockPlaced();
			//If the block is a torch and the player has the command enabled. Do this.
			if(block.getType() == Material.TORCH && InspirePlayedPlayerListener.plugin.enabled(player)){
				player.sendMessage("You placed a torch!");
			}
		}
}
*/