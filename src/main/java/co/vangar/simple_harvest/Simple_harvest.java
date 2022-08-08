package co.vangar.simple_harvest;

import co.vangar.simple_harvest.harvest.listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Simple_harvest extends JavaPlugin {

    private Simple_harvest plugin = this;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new listener(), this);
    }
}
