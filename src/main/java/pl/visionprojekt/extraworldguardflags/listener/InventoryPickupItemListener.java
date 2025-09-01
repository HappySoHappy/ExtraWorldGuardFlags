package pl.visionprojekt.extraworldguardflags.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import pl.visionprojekt.extraworldguardflags.flags.ExtraFlags;

public class InventoryPickupItemListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        if (event.getInventory().getType() != InventoryType.HOPPER) return; //Hopper Inventory

        Location eventLocation = event.getInventory().getLocation();
        if (eventLocation == null) return; //Virtual inventory, don't care...

        ApplicableRegionSet set =
                WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(BukkitAdapter.adapt(eventLocation));

        if (!set.testState(null, ExtraFlags.HOPPER_ITEM_PICKUP)) {
            event.setCancelled(true);
        }
    }
}
