package com.corwinjv.achunkloader.eventhandlers;

import com.corwinjv.achunkloader.storage.ChunkLoaders;
import com.corwinjv.achunkloader.storage.SavedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.logging.log4j.Level;

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
