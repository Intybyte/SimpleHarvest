package co.vangar.simple_harvest.utils;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HarvestUtils {
    private static final Random rng = new Random();

    public static int getRandomInt(Integer max){
        return rng.nextInt(max);
    }

    public static boolean canBreakAll(ItemStack item, int i){
        ItemMeta im = item.getItemMeta();
        Damageable dam = (Damageable) im;

        if(dam.hasDamage()){
            int maxDur = item.getType().getMaxDurability();
            int currentDur = maxDur - dam.getDamage();

            return currentDur > i;
        }

        return true;
    }

    public static void durabilityDmg(Player p){
        if (!ConfigStorage.durabilityDamage)
            return;

        if (p.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        ItemStack item = p.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();

        if(!(meta instanceof Damageable))
            return;

        Damageable dam = (Damageable) meta;

        int maxDur = item.getType().getMaxDurability();
        int currentDur = maxDur - dam.getDamage();

        int ranI = getRandomInt(100);
        boolean doDmg = true;

        int unbreaking = dam.getEnchantLevel(Enchantment.DURABILITY);
        if(unbreaking != 0) {

            if(unbreaking == 1){
                if(ranI <= 20){
                    doDmg = false;
                }
            } else if(unbreaking == 2){
                if(ranI <= 27){
                    doDmg = false;
                }
            } else if(unbreaking == 3){
                if(ranI <= 30){
                    doDmg = false;
                }
            }
        }

        if (!doDmg) {
            return;
        }

        if(currentDur - 1 < 1){
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
            p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        } else {
            dam.setDamage(dam.getDamage() + 1);
            item.setItemMeta(dam);
            p.getInventory().setItemInMainHand(item);
        }
    }

    public static double fortuneMulti(int fortuneLevel){
        int random = getRandomInt(30);

        switch (fortuneLevel) {
            case 0:
                if (random >= 20) {
                    return 1.5;
                } else return 1;
            case 1:
                if (random <= 10) {
                    return 2.5;
                } else if (random <= 20) {
                    return 1.5;
                } else {
                    return 1;
                }
            case 2:
                if (random <= 10) {
                    return 2.5;
                } else if (random <= 20) {
                    return 2;
                } else {
                    return 1;
                }
            case 3:
                if (random <= 10) {
                    return 3;
                } else if (random <= 20) {
                    return 2;
                } else {
                    return 1.5;
                }
            default:
                return 1;
        }
    }

    public static boolean isHarvestable(Block b){

        Material type = b.getType();
        BlockData data = b.getBlockData();

        if (!(data instanceof Ageable ageable)) {
            return false;
        }

        int age = ageable.getAge();

        return switch(type) {
            case POTATOES, CARROTS, WHEAT, BEETROOTS -> age == 7;
            case NETHER_WART -> age == 3;
            default -> false;
        };
    }

    public static boolean areaHarvestable(Block b){
        List<Block> blocks = new ArrayList<>();
        if(isHarvestable(b)) blocks.add(b);
        for(BlockFace dir : checkDir()){
            if (!isHarvestable(b.getRelative(dir))) {
                continue;
            }

            if(!blocks.contains(b.getRelative(dir))){
                blocks.add(b.getRelative(dir));
            }
        }
        return !blocks.isEmpty();
    }

    public static List<Block> harvestArea(Block b){
        List<Block> blocks = new ArrayList<>();
        if(isHarvestable(b)) blocks.add(b);

        for(BlockFace dir : checkDir()){
            if (!isHarvestable(b.getRelative(dir))) {
                continue;
            }

            if(!blocks.contains(b.getRelative(dir))){
                blocks.add(b.getRelative(dir));
            }
        }
        return blocks;
    }

    public static List<BlockFace> checkDir(){
        List<BlockFace> dir = new ArrayList<>();
        dir.add(BlockFace.NORTH);
        dir.add(BlockFace.NORTH_EAST);
        dir.add(BlockFace.NORTH_WEST);
        dir.add(BlockFace.SOUTH);
        dir.add(BlockFace.SOUTH_EAST);
        dir.add(BlockFace.SOUTH_WEST);
        dir.add(BlockFace.EAST);
        dir.add(BlockFace.WEST);
        return dir;
    }
}
