package co.vangar.simple_harvest;

import co.vangar.simple_harvest.harvest.CropListener;
import co.vangar.simple_harvest.utils.ConfigStorage;
import com.gmail.nossr50.config.experience.ExperienceConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SimpleHarvest extends JavaPlugin {

    public static ExperienceConfig exp = null;

    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();

        getDataFolder().mkdirs();
        if(!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();

        new ConfigStorage(config);

        if(getServer().getPluginManager().isPluginEnabled("mcMMO")) {
            exp = ExperienceConfig.getInstance();
        }
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new CropListener(), this);
    }
}
