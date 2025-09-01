package pl.visionprojekt.extraworldguardflags.listener;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.visionprojekt.extraworldguardflags.ExtraWorldGuardFlags;
import pl.visionprojekt.extraworldguardflags.flags.ExtraFlags;

import java.util.Set;

//TODO: jesus christ this is hard to read...
public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location to = event.getTo(), from = event.getFrom();
        if (to.equals(from)) return;

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager oldRegionManager = regionContainer.get(BukkitAdapter.adapt(from.getWorld())),
                newRegionManager = regionContainer.get(BukkitAdapter.adapt(to.getWorld()));
        if (oldRegionManager == null || newRegionManager == null) return;

        ApplicableRegionSet oldRs = oldRegionManager.getApplicableRegions(BukkitAdapter.asBlockVector(from)),
                newRs = newRegionManager.getApplicableRegions(BukkitAdapter.asBlockVector(to));

        Player player = event.getPlayer();

        Set<String> enterCommands = newRs.queryValue(null, ExtraFlags.ENTRY_COMMANDS);
        if (enterCommands != null) {
            for (ProtectedRegion region : newRs.getRegions()) {
                if (oldRs.getRegions().contains(region)) continue;

                enterCommands.forEach(command -> {
                    //Why can't I just do command.replace().replace().replaceFirst()...
                    command = command.replace("%player%", player.getName());
                    command = command.replace("%uuid%", player.getUniqueId().toString());
                    command = command.replaceFirst("/", "");
                    ExtraWorldGuardFlags.getPluginLogger().info("Making "+player.getName()+" issue server command: /"+command);
                    player.performCommand(command);
                });
                return;
            }
        }

        Set<String> exitCommands = oldRs.queryValue(null, ExtraFlags.EXIT_COMMANDS);
        if (exitCommands != null) {
            for (ProtectedRegion region : oldRs.getRegions()) {
                if (newRs.getRegions().contains(region)) continue;

                exitCommands.forEach(command -> {
                    String finalCommand = command.replace("%player%", player.getName()
                            .replace("%uuid%", player.getUniqueId().toString())
                            .replaceFirst("/", "")
                    );
                    //Why can't I just do command.replace().replace().replaceFirst()...
                    command = command.replace("%player%", player.getName());
                    command = command.replace("%uuid%", player.getUniqueId().toString());
                    command = command.replaceFirst("/", "");
                    ExtraWorldGuardFlags.getPluginLogger().info("Making "+player.getName()+" issue server command: /"+command);
                    player.performCommand(command);
                });
                return; // fixme: this breaks everythign else!!!
            }
        }
    }

    @EventHandler
    public void onMoveFlight(PlayerMoveEvent event) {
        Location to = event.getTo(), from = event.getFrom();
        if (to.equals(from)) return;

        if (WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(
                WorldGuardPlugin.inst().wrapPlayer(event.getPlayer()),
                BukkitAdapter.adapt(event.getPlayer().getWorld()))) {
            return;
        }

        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager oldRegionManager = regionContainer.get(BukkitAdapter.adapt(from.getWorld())),
                newRegionManager = regionContainer.get(BukkitAdapter.adapt(to.getWorld()));
        if (oldRegionManager == null || newRegionManager == null) return;

        ApplicableRegionSet oldRs = oldRegionManager.getApplicableRegions(BukkitAdapter.asBlockVector(from)),
                newRs = newRegionManager.getApplicableRegions(BukkitAdapter.asBlockVector(to));

        Player player = event.getPlayer();

        StateFlag.State entryFlight = newRs.queryValue(null, ExtraFlags.FLIGHT);
        if (entryFlight == StateFlag.State.DENY) {
            /*for (ProtectedRegion region : newRs.getRegions()) {
                if (oldRs.getRegions().contains(region)) continue;

                player.setFlying(false);
                return;
            }*/

            for (ProtectedRegion region : oldRs.getRegions()) {
                if (newRs.getRegions().contains(region)) {
                    continue;
                }

                player.setFlying(false);
                return;
            }
        }
    }
}
