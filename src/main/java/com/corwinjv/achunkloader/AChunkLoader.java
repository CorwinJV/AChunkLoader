package com.corwinjv.achunkloader;
/**
 * Created by corwinjv on 8/30/14.
 */

import com.corwinjv.achunkloader.blocks.ModBlocks;
import com.corwinjv.achunkloader.blocks.tileentities.ChunkLoaderTileEntity;
import com.corwinjv.achunkloader.commands.CommandStats;
import com.corwinjv.achunkloader.config.ConfigurationHandler;
import com.corwinjv.achunkloader.eventhandlers.ChunkLoadingCallback;
import com.corwinjv.achunkloader.eventhandlers.PlayerActivity;
import com.corwinjv.achunkloader.eventhandlers.PlayerTimeout;
import com.corwinjv.achunkloader.proxy.CommonProxy;
import com.corwinjv.achunkloader.storage.ChunkLoaderPos;
import com.corwinjv.achunkloader.storage.ChunkLoaders;
import com.corwinjv.achunkloader.storage.SavedData;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;


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

        // Login/Logout Tracking
        MinecraftForge.EVENT_BUS.register(new PlayerActivity());
        MinecraftForge.EVENT_BUS.register(new PlayerTimeout());

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

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent e)
    {
        World world = DimensionManager.getWorld(DimensionType.OVERWORLD.getId());
        SavedData data = SavedData.get(world);
        ChunkLoaders chunkLoadersModel = data.getChunkLoaders();

        for (ChunkLoaderPos chunkLoaderPos : chunkLoadersModel.getLoaders())
        {
            world = DimensionManager.getWorld(chunkLoaderPos.dimension);
            if (world != null && !world.isRemote)
            {
                ChunkLoaderTileEntity chunkLoader = (ChunkLoaderTileEntity) world.getTileEntity(chunkLoaderPos.pos);
                if (chunkLoader != null
                        && chunkLoader.getEnabled())
                {
                    chunkLoader.setWorld(world);
                    chunkLoader.validate();
                    //FMLLog.log(Level.INFO, "The chunk at " + chunkLoaderPos + " has been loaded.");
                }
                else
                {
                    chunkLoadersModel.removeLoader(chunkLoaderPos);
                }
            }
        }
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandStats());
    }
}
