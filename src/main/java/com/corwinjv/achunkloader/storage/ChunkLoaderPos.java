package com.corwinjv.achunkloader.storage;

import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

/**
 * Created by CorwinJV on 12/14/2016.
 */
public class ChunkLoaderPos implements Serializable
{
    public String ownerId = "";
    public long loginTimeStamp = 0;
    public int dimension = 0;
    public BlockPos pos = BlockPos.ORIGIN;

    public ChunkLoaderPos(String ownerId, int dimension, BlockPos pos, long loginTimeStamp)
    {
        this.ownerId = ownerId;
        this.dimension = dimension;
        this.pos = pos;
        this.loginTimeStamp = loginTimeStamp;
    }

    public String toString() {
        return "dim: " + dimension + " ownerId: " + ownerId + " {" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "} loginTimeStamp: " + loginTimeStamp;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null
                || !ChunkLoaderPos.class.isAssignableFrom(obj.getClass()))
        {
            return false;
        }
        else
        {
            ChunkLoaderPos rhs = (ChunkLoaderPos)obj;
            return this.dimension == rhs.dimension
                    && this.ownerId.equals(rhs.ownerId)
                    && this.pos.getX() == rhs.pos.getX()
                    && this.pos.getY() == rhs.pos.getY()
                    && this.pos.getZ() == rhs.pos.getZ();
            // Ignore loginTimeStamp timestamp for equality
        }
    }
}
