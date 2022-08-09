package co.vangar.simple_harvest.harvest;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class listener implements Listener {

    @EventHandler
    public void hoeHarvest(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action action = e.getAction();
        String itemName = p.getInventory().getItemInMainHand().getType().name().toLowerCase();

        if(action == Action.RIGHT_CLICK_BLOCK){
            if(itemName.contains("hoe")){
                Block block = e.getClickedBlock();
                int fortune = 0;

                if(p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)){
                    fortune = p.getInventory().getItemInMainHand().getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
                }

                if(itemName.contains("diamond") || itemName.contains("nether")){
                    if(utils.areaHarvestable(block)){
                        List<Block> field = new ArrayList<Block>();
                        field.addAll(utils.harvestArea(block));

                        if(utils.canBreakAll(p.getInventory().getItemInMainHand(), field.size())){
                            for(Block b : field){
                                Collection<ItemStack> drops = b.getDrops();
                                Location loc = b.getLocation();
                                for(ItemStack is : drops){
                                    is.setAmount((int) (is.getAmount() * utils.fortuneMulti(fortune)));
                                    p.getWorld().dropItem(loc, is);
                                }
                                b.setType(b.getType());
                                utils.durabilityDmg(p);
                            }
                        } else if(utils.isHarvestable(block)){
                            Collection<ItemStack> drops = block.getDrops();
                            Location loc = block.getLocation();
                            for(ItemStack is : drops){
                                is.setAmount((int) (is.getAmount() * utils.fortuneMulti(fortune)));
                                p.getWorld().dropItem(loc, is);
                            }
                            block.setType(block.getType());
                            utils.durabilityDmg(p);
                        }
                    }
                } else {
                    if(utils.isHarvestable(block)){
                        Collection<ItemStack> drops = block.getDrops();
                        Location loc = block.getLocation();
                        for(ItemStack is : drops){
                            is.setAmount((int) (is.getAmount() * utils.fortuneMulti(fortune)));
                            p.getWorld().dropItem(loc, is);
                        }
                        block.setType(block.getType());
                        utils.durabilityDmg(p);
                    }
                }
            }
        }
    }
}
