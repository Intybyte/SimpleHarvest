package co.vangar.simple_harvest.harvest;

import co.vangar.simple_harvest.SimpleHarvest;
import co.vangar.simple_harvest.utils.ConfigStorage;
import co.vangar.simple_harvest.utils.HarvestUtils;
import com.gmail.nossr50.api.ExperienceAPI;
import com.gmail.nossr50.api.exceptions.McMMOPlayerNotFoundException;
import com.gmail.nossr50.datatypes.skills.PrimarySkillType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CropListener implements Listener {

    @EventHandler
    public void hoeHarvest(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action action = e.getAction();
        ItemStack mainHand = p.getInventory().getItemInMainHand();
        String itemName = mainHand.getType().name().toLowerCase();

        if (action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }


        if (ConfigStorage.hoeRequired && !itemName.contains("hoe")) {
            return;
        }

        //region null checks
        Block block = e.getClickedBlock();

        if (block == null)
            return;

        ItemMeta meta = mainHand.getItemMeta();

        int fortune = 0;
        if (meta!=null)
            fortune = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
        //endregion


        if(checkAreaHarvest(p, block, fortune)) {
            return;
        }

        if (!HarvestUtils.isHarvestable(block)) {
            return;
        }

        Collection<ItemStack> drops = block.getDrops();
        Location loc = block.getLocation();
        for(ItemStack is : drops){
            if (is.getType() != Material.WHEAT)
                is.setAmount((int) (is.getAmount() * HarvestUtils.fortuneMulti(fortune)));
            p.getWorld().dropItem(loc, is);
        }

        harvestBlock(p, block);
    }

    private boolean checkAreaHarvest(Player p, Block block, int fortune) {
        if (!ConfigStorage.areaHarvest)
            return false;

        ItemStack mainHand = p.getInventory().getItemInMainHand();
        Material m = mainHand.getType();
        boolean allowed = m == Material.DIAMOND_HOE || m == Material.NETHERITE_HOE;

        if (!allowed) {
            return false;
        }

        if (!HarvestUtils.areaHarvestable(block)) {
            return true;
        }

        List<Block> field = new ArrayList<>(HarvestUtils.harvestArea(block));

        if(HarvestUtils.canBreakAll(mainHand, field.size())){
            for(Block b : field){
                Collection<ItemStack> drops = b.getDrops();
                Location loc = b.getLocation();
                for(ItemStack is : drops){
                    is.setAmount((int) (is.getAmount() * HarvestUtils.fortuneMulti(fortune)));
                    p.getWorld().dropItem(loc, is);
                }
                harvestBlock(p, block);
            }

            return true;
        }

        if (!HarvestUtils.isHarvestable(block)) {
            return true;
        }

        Collection<ItemStack> drops = block.getDrops();
        Location loc = block.getLocation();
        for(ItemStack is : drops){
            is.setAmount((int) (is.getAmount() * HarvestUtils.fortuneMulti(fortune)));
            p.getWorld().dropItem(loc, is);
        }
        harvestBlock(p, block);

        return true;
    }

    private void addMcMMOxp(Player p, Block block) {
        if (SimpleHarvest.exp == null) {
            return;
        }

        int xp = SimpleHarvest.exp.getXp(PrimarySkillType.HERBALISM, block);

        try {
            ExperienceAPI.addRawXP(p, "Herbalism", xp);
        } catch (McMMOPlayerNotFoundException e) {
            SimpleHarvest.instance.getLogger().warning(p.getName() + "'s profile wasn't loaded yet. So xp wasn't awarded [Not a bug don't worry]");
        }
    }

    private void harvestBlock(Player p, Block block) {
        addMcMMOxp(p, block);
        block.setType(block.getType());
        HarvestUtils.durabilityDmg(p);
    }
}
