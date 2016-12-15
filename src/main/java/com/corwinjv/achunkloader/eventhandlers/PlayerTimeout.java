package com.corwinjv.achunkloader.eventhandlers;

import com.corwinjv.achunkloader.blocks.tileentities.ChunkLoaderTileEntity;
import com.corwinjv.achunkloader.config.ConfigurationHandler;
import com.corwinjv.achunkloader.storage.ChunkLoaderPos;
import com.corwinjv.achunkloader.storage.ChunkLoaders;
import com.corwinjv.achunkloader.storage.SavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by CorwinJV on 12/15/2016.
 */
public class PlayerTimeout {
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent e)
    {
        long timeout = ConfigurationHandler.playerInactivityTimeout;
        if(timeout == 0)
        {
            return;
        }
        timeout = timeout * 60 * 60 * 1000;

        if(!e.world.isRemote
                && e.world.getTotalWorldTime() % 1200 == 0)
        {
            SavedData data = SavedData.get(e.world);
            if(data != null)
            {
                ChunkLoaders cl = data.getChunkLoaders();
                for(ChunkLoaderPos loaderPos : cl.getLoaders())
                {
                    if(System.currentTimeMillis() - loaderPos.loginTimeStamp >= timeout
                            && !isPlayerOnline(e.world, loaderPos.ownerId))
                    {
                        ChunkLoaderTileEntity te = (ChunkLoaderTileEntity)e.world.getTileEntity(loaderPos.pos);
                        if(te != null)
                        {
                            te.disable();
                        }
                    }
                }
            }
        }
    }

    public boolean isPlayerOnline(World world, String ownerId)
    {
        boolean ret = false;
        for(EntityPlayer playerEntity : world.playerEntities)
        {
            if(playerEntity.getUniqueID().toString().equals(ownerId))
            {
                ret = true;
                break;
            }
        }
        return ret;
    }
}
