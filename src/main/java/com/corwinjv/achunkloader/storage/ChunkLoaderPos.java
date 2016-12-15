package com.corwinjv.achunkloader.storage;

import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

/**
 * Created by CorwinJV on 12/14/2016.
 */
public class ChunkLoaderPos implements Serializable
{
    public int dimension = 0;
    public BlockPos pos = BlockPos.ORIGIN;
    public String playerUUID = "";

    public ChunkLoaderPos(int dimension, BlockPos pos)
    {
        this.dimension = dimension;
        this.pos = pos;
    }

    public String toString() {
        return "dim: " + dimension + " {" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "}";
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
                    && this.pos.getX() == rhs.pos.getX()
                    && this.pos.getY() == rhs.pos.getY()
                    && this.pos.getZ() == rhs.pos.getZ();
        }
    }
}
