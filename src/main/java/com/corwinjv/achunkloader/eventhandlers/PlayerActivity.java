package com.corwinjv.achunkloader.eventhandlers;

import com.corwinjv.achunkloader.blocks.tileentities.ChunkLoaderTileEntity;
import com.corwinjv.achunkloader.storage.ChunkLoaderPos;
import com.corwinjv.achunkloader.storage.ChunkLoaders;
import com.corwinjv.achunkloader.storage.SavedData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by CorwinJV on 12/15/2016.
 */
public class PlayerActivity
{
    @SubscribeEvent
    public void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(!e.player.worldObj.isRemote)
        {
            SavedData data = SavedData.get(e.player.worldObj);
            if(data != null)
            {
                ChunkLoaders cl = data.getChunkLoaders();
                cl.updateLoginTimestamp(e.player.getUniqueID().toString(), System.currentTimeMillis());
                data.setChunkLoaders(cl);

                // Enable all of the chunk loaders this player owns
                for(ChunkLoaderPos loaderPos : cl.getLoaders())
                {
                    ChunkLoaderTileEntity te = (ChunkLoaderTileEntity)e.player.worldObj.getTileEntity(loaderPos.pos);
                    if(te != null)
                    {
                        te.enable();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent e)
    {
        if(!e.player.worldObj.isRemote)
        {
            SavedData data = SavedData.get(e.player.worldObj);
            if(data != null)
            {
                ChunkLoaders cl = data.getChunkLoaders();
                cl.updateLoginTimestamp(e.player.getUniqueID().toString(), System.currentTimeMillis());
                data.setChunkLoaders(cl);
            }
        }
    }
}
