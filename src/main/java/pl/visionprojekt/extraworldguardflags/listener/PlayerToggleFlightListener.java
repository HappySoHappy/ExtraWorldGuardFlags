package pl.visionprojekt.extraworldguardflags.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import pl.visionprojekt.extraworldguardflags.flags.ExtraFlags;

public class PlayerToggleFlightListener implements Listener {
    @EventHandler
    public void onPlayerFlightToggle(PlayerToggleFlightEvent event) {
        if (WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(
                        WorldGuardPlugin.inst().wrapPlayer(event.getPlayer()),
                        BukkitAdapter.adapt(event.getPlayer().getWorld()))) {
            return;
        }

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(BukkitAdapter.adapt(event.getPlayer().getWorld()));
        if (regionManager == null) return;

        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(BukkitAdapter.asBlockVector(event.getPlayer().getLocation()));

        StateFlag.State entryFlight = regionSet.queryValue(null, ExtraFlags.FLIGHT);
        if (entryFlight == StateFlag.State.DENY) {
            event.getPlayer().setFlying(false);
            if (event.isFlying()) {
                event.setCancelled(true);
            }
        }
    }
}
