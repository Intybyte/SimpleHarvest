package co.vangar.simple_harvest.harvest;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class utils {

    public static int getRandomInt(Integer max){
        Random ran = new Random();
        return ran.nextInt(max);
    }

    public static ItemMeta durabilityDmg(ItemStack item){
        ItemMeta im = item.getItemMeta();
        Damageable dam = (Damageable) im;

        int ranI = getRandomInt(100);
        boolean doDmg = true;

        if(im.hasEnchants()) {
            if(im.hasEnchant(Enchantment.DURABILITY)){
                int unbreaking = im.getEnchantLevel(Enchantment.DURABILITY);
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
        }

        if(doDmg){
            dam.setDamage(dam.getDamage() + 1);
        }

        return dam;
    }

    public static double fortuneMulti(int fortuneLevel){
        int random = getRandomInt(20);

        if(fortuneLevel == 0){
            if(random == 20){
                return 1.5;
            } else return 1;
        } else if(fortuneLevel == 1){
            if(random == 20){
                return 2.5;
            } else if(random >= 10){
                return 1.5;
            } else {
                return 1;
            }
        } else if(fortuneLevel == 2){
            if(random == 20){
                return 2.5;
            } else if(random >= 10){
                return 2;
            } else {
                return 1;
            }
        } else if(fortuneLevel == 3){
            if(random == 20){
                return 3;
            } else if(random >= 10){
                return 2;
            } else {
                return 1.5;
            }
        } else return 1;
    }

    public static boolean isHarvestable(Block b){
        if(b.getType() == Material.POTATOES || b.getType() == Material.CARROTS || b.getType() == Material.WHEAT || b.getType() == Material.BEETROOTS){
            Ageable ageable = (Ageable) b.getBlockData();
            if(ageable.getAge() == 7) return true;
            else return false;
        } else return false;
    }

    public static boolean areaHarvestable(Block b){
        List<Block> blocks = new ArrayList<Block>();
        if(isHarvestable(b)) blocks.add(b);
        for(BlockFace dir : checkDir()){
            if(isHarvestable(b.getRelative(dir))){
                if(!blocks.contains(b.getRelative(dir))){
                    blocks.add(b.getRelative(dir));
                }
            }
        }
        if(blocks.isEmpty()) return false;
        else return true;
    }

    public static List<Block> harvestArea(Block b){
        List<Block> blocks = new ArrayList<Block>();
        if(isHarvestable(b)) blocks.add(b);
        for(BlockFace dir : checkDir()){
            if(isHarvestable(b.getRelative(dir))){
                if(!blocks.contains(b.getRelative(dir))){
                    blocks.add(b.getRelative(dir));
                }
            }
        }
        return blocks;
    }

    public static List<BlockFace> checkDir(){
        List<BlockFace> dir = new ArrayList<BlockFace>();
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
