package com.corwinjv.achunkloader.blocks.tileentities;

import com.corwinjv.achunkloader.AChunkLoader;
import com.corwinjv.achunkloader.config.ConfigurationHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

/**
 * Created by CorwinJV on 12/13/2016.
 */
public class ChunkLoaderTileEntity extends TileEntity implements ITickable
{
    private ForgeChunkManager.Ticket ticket;

    public void forceChunkLoading()
    {
        if(ticket == null)
        {
            ticket = ForgeChunkManager.requestTicket(AChunkLoader.instance, worldObj, ForgeChunkManager.Type.NORMAL);
        }

        if(ticket == null)
        {
            FMLLog.log(Level.ERROR, "Could not request ticket for chunk loading.");
            return;
        }

        ticket.getModData().setInteger("xPos", getPos().getX());
        ticket.getModData().setInteger("yPos", getPos().getY());
        ticket.getModData().setInteger("zPos", getPos().getZ());

        int size = ConfigurationHandler.chunkLoaderSize;
        if(size % 2 != 0 && size > 1)
        {
            size--;
        }
        int dist = size / 2;

        //FMLLog.log(Level.INFO, "chunk loader size: " + ConfigurationHandler.chunkLoaderSize);
        int chunkCount = 0;
        ChunkPos centerPos = new ChunkPos(pos.getX() / 16, pos.getZ() / 16);
        for(int x = centerPos.chunkXPos - dist; x <= centerPos.chunkXPos + dist; x++)
        {
            for(int z = centerPos.chunkZPos - dist; z <= centerPos.chunkZPos + dist; z++)
            {
                ChunkPos chunkPos = new ChunkPos(x, z);
                ForgeChunkManager.forceChunk(ticket, chunkPos);
                //FMLLog.log(Level.INFO, "Chunk #" + chunkCount + " loaded at chunk pos: " + chunkPos);
                chunkCount++;
            }
        }
    }

    public void forceChunkLoading(ForgeChunkManager.Ticket aTicket)
    {
        if(ticket != null
                && ticket != aTicket)
        {
            ForgeChunkManager.releaseTicket(ticket);
        }
        ticket = aTicket;
        forceChunkLoading();
    }

    @Override
    public void invalidate() {
        if(ticket != null)
        {
            ForgeChunkManager.releaseTicket(ticket);
            ticket = null;
        }
        super.invalidate();
    }

    public void unforceChunkLoading()
    {
        ChunkPos chunkPos = new ChunkPos(pos.getX() / 16, pos.getZ() / 16);
        FMLLog.log(Level.INFO, "Unchunkloading at chunk pos: " + chunkPos);
        ForgeChunkManager.unforceChunk(ticket, chunkPos);
    }

    @Override
    public void update() {
        //FMLLog.log(Level.INFO, "I'm ticking at pos: " + pos);
    }
}
