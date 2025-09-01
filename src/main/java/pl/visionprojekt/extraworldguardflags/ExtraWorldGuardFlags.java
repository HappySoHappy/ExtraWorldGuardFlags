package pl.visionprojekt.extraworldguardflags;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.visionprojekt.extraworldguardflags.flags.ExtraFlags;
import pl.visionprojekt.extraworldguardflags.listener.*;

import java.util.logging.Logger;

public final class ExtraWorldGuardFlags extends JavaPlugin {
    private static ExtraWorldGuardFlags instance;
    @Override
    public void onLoad() { //Register WorldGuard flags here, since we can't do that after plugin is enabled
        ExtraFlags.registerAll();
    }

    @Override
    public void onEnable() {
        instance = this;
        registerListeners();
    }

    @Override
    public void onDisable() {

    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BlockPhysicsListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityChangeBlockListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryPickupItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerToggleFlightListener(), this);
    }

    public static ExtraWorldGuardFlags getInstance() {
        return instance;
    }

    public static Logger getPluginLogger() {
        return instance.getLogger();
    }
}
