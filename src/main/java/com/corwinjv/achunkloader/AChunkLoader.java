package com.corwinjv.achunkloader;
/**
 * Created by corwinjv on 8/30/14.
 */

import com.corwinjv.achunkloader.blocks.ModBlocks;
import com.corwinjv.achunkloader.config.ConfigurationHandler;
import com.corwinjv.achunkloader.eventhandlers.ChunkLoadingCallback;
import com.corwinjv.achunkloader.proxy.CommonProxy;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.MOD_VERSION, guiFactory = Reference.GUI_FACTORY_CLASS)
public class AChunkLoader
{
    @Mod.Instance
    public static AChunkLoader instance;

    @SidedProxy(clientSide = Reference.CLIENT_SIDE_PROXY_CLASS, serverSide = Reference.SERVER_SIDE_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Config
        ConfigurationHandler.Init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(new ConfigurationHandler());

        // Chunk Loading
        ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkLoadingCallback());

        // Blocks and Items
        ModBlocks.init();
        ModBlocks.registerBlocks();
        ModBlocks.registerRecipes();

        // Keybinds
        proxy.registerKeys();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.registerRenders();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.registerGui();
        proxy.registerParticleRenderer();
    }
}
