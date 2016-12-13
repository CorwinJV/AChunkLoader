package com.corwinjv.achunkloader.creativetab;

import com.corwinjv.achunkloader.Reference;
import com.corwinjv.achunkloader.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * Created by CorwinJV on 9/1/14.
 */
public class CreativeTab
{
    public static final CreativeTabs FT_TAB = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModBlocks.getBlock(ModBlocks.CHUNK_LOADER));
        }
    };
}
