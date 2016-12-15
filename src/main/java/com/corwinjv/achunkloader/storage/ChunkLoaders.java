package com.corwinjv.achunkloader.storage;
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
        synchronized(lock)
        {
            if(!loaders.contains(pos))
            {
                loaders.add(pos);
            }
        }
    }

    public void removeLoader(ChunkLoaderPos pos)
    {
        synchronized(lock)
        {
            if(pos != null)
            {
                loaders.remove(pos);
            }
        }
    }
}
