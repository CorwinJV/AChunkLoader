package com.corwinjv.achunkloader.storage;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CorwinJV on 12/14/2016.
 */
public class ChunkLoaders
{
    private final Object lock = new Object();
    private List<ChunkLoaderPos> loaders = new ArrayList<ChunkLoaderPos>();

    public List<ChunkLoaderPos> getLoaders()
    {
        return new ArrayList<ChunkLoaderPos>(loaders);
    }

    public void addLoader(ChunkLoaderPos pos)
    {
        if(!loaders.contains(pos))
        {
            loaders.add(pos);
        }
    }

    public void removeLoader(ChunkLoaderPos pos)
    {
        if(pos != null)
        {
            loaders.remove(pos);
        }
    }

    public void updateLoginTimestamp(String ownerId, long loginTimeStamp)
    {
        for(ChunkLoaderPos loaderPosItr : loaders)
        {
            if(loaderPosItr.ownerId.equals(ownerId))
            {
                loaderPosItr.loginTimeStamp = loginTimeStamp;
            }
        }
    }
}
