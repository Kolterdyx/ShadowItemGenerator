package me.kolterdyx.shadowitemgenerator;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Shadow Items enabled");

        getServer().getPluginManager().registerEvents(new ShadowItemGenerator(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
