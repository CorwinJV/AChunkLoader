package com.corwinjv.achunkloader.blocks;

import com.corwinjv.achunkloader.Reference;
import com.corwinjv.achunkloader.blocks.tileentities.ChunkLoaderTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CorwinJV on 9/1/14.
 */

public class ModBlocks
{
    public static final String CHUNK_LOADER = "chunk_loader";

    private static Map<String, ModBlock> mBlocks = Collections.emptyMap();
    private static Map<String, Class<? extends TileEntity>> mTileEntityClasses = Collections.emptyMap();

    public static void init()
    {
        mBlocks = new HashMap<String, ModBlock>();
        mTileEntityClasses = new HashMap<String, Class<? extends TileEntity>>();

        ModBlock chunk_loader = new ChunkLoaderBlock();
        chunk_loader.setUnlocalizedName(CHUNK_LOADER);
        mBlocks.put(CHUNK_LOADER, chunk_loader);
        mTileEntityClasses.put(CHUNK_LOADER, ChunkLoaderTileEntity.class);
    }

    public static ModBlock getBlock(String key)
    {
        if (mBlocks.containsKey(key))
        {
            return mBlocks.get(key);
        }
        return null;
    }

    public static void registerBlocks()
    {
        for (String key : mBlocks.keySet())
        {
            ModBlock block = mBlocks.get(key);
            if (block != null)
            {
                GameRegistry.register(block.setRegistryName(new ResourceLocation(Reference.MOD_ID, key)));
                GameRegistry.register(new ItemBlock(block), block.getRegistryName());

                Class<? extends TileEntity> tileEntityClass = mTileEntityClasses.get(key);
                if (tileEntityClass != null)
                {
                    GameRegistry.registerTileEntity(tileEntityClass, key);
                }
            }
        }
    }

    public static void registerRenders()
    {
        for (String key : mBlocks.keySet())
        {
            ModBlock block = mBlocks.get(key);
            if (block != null)
            {
                registerRender(block, key);
            }
        }
    }

    private static void registerRender(ModBlock block, String key)
    {
        Item item = Item.getItemFromBlock(block);

        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item,
                        0,
                        new ModelResourceLocation(Reference.RESOURCE_PREFIX + key, "inventory"));
    }

    public static void registerRecipes()
    {
        ModBlock item = mBlocks.get(CHUNK_LOADER);
        GameRegistry.addRecipe(new ItemStack(item, 1),
                "OOO",
                "GEG",
                "OOO",
                'O', Blocks.OBSIDIAN,
                'E', Items.ENDER_PEARL,
                'G', Blocks.GOLD_BLOCK);
    }
}
