package co.vangar.simple_harvest.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigStorage {
    public static boolean hoeRequired;
    public static boolean areaHarvest;
    public static boolean durabilityDamage;

    public ConfigStorage(FileConfiguration cfg) {
        hoeRequired = cfg.getBoolean("requires-hoe");
        areaHarvest = cfg.getBoolean("area-harvest");
        durabilityDamage = cfg.getBoolean("durability-damage");
    }
}
