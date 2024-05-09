package co.vangar.simple_harvest;

import co.vangar.simple_harvest.harvest.CropListener;
import co.vangar.simple_harvest.utils.ConfigStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleHarvest extends JavaPlugin {

    public static boolean mcMMO = false;

    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();
        new ConfigStorage(config);

        mcMMO = getServer().getPluginManager().isPluginEnabled("mcMMO");
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new CropListener(), this);
    }
}
