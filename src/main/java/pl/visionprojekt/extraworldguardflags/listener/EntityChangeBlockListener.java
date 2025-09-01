package pl.visionprojekt.extraworldguardflags.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import pl.visionprojekt.extraworldguardflags.flags.ExtraFlags;

import java.util.Locale;
import java.util.Set;

public class EntityChangeBlockListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntityType() == EntityType.FALLING_BLOCK) {
            if (event.getTo() != Material.AIR) return;

            Block block = event.getBlock();

            Location blockLocation = block.getLocation();
            ApplicableRegionSet set =
                    WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(blockLocation));

            Set<BlockType> blockTypes = set.queryValue(null, ExtraFlags.DENY_FALLING_BLOCK);
            BlockType weBlockType = BlockTypes.get(block.getType().name().toLowerCase(Locale.ROOT));
            if (blockTypes != null && weBlockType != null && blockTypes.contains(weBlockType)) {
                event.setCancelled(true);
            }
        }
    }
}
