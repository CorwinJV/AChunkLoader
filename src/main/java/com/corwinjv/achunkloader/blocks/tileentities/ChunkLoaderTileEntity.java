package com.corwinjv.achunkloader.blocks.tileentities;

import com.corwinjv.achunkloader.AChunkLoader;
import com.corwinjv.achunkloader.config.ConfigurationHandler;
import com.corwinjv.achunkloader.storage.ChunkLoaderPos;
import com.corwinjv.achunkloader.storage.ChunkLoaders;
import com.corwinjv.achunkloader.storage.SavedData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Created by CorwinJV on 12/13/2016.
 */
public class ChunkLoaderTileEntity extends TileEntity
{
    private final String UUID_TAG = "UUID_TAG";
    private final String ENABLED_TAG = "ENABLED_TAG";

    private ForgeChunkManager.Ticket ticket;

    private UUID ownerId = null;
    private boolean enabled = true;

    public void setOwnerId(UUID uuid)
    {
        ownerId = uuid;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        ownerId = compound.getUniqueId(UUID_TAG);
        enabled = compound.getBoolean(ENABLED_TAG);
        super.readFromNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setUniqueId(UUID_TAG, ownerId);
        compound.setBoolean(ENABLED_TAG, enabled);
        return super.writeToNBT(compound);
    }

    public boolean getEnabled()
    {
        return enabled;
    }

    public void enable()
    {
        if(!enabled)
        {
            //FMLLog.log(Level.INFO, "Enabling chunkloader at pos: " + pos + " for owner: " + ownerId);

            // Force chunk loading
            enabled = true;
            forceChunkLoading();
        }
    }

    public void disable()
    {
        if(enabled)
        {
            //FMLLog.log(Level.INFO, "Disabling chunk at: " + toString() + " for owner: " + ownerId);

            // Unforce chunk, release ticket
            unforceChunkLoading();

            if(ticket != null)
            {
                ForgeChunkManager.releaseTicket(ticket);
                ticket = null;
            }

            enabled = false;
        }
    }

    public void forceChunkLoading()
    {
        if(!enabled)
        {
            return;
        }

        if(ticket == null)
        {
            ticket = ForgeChunkManager.requestTicket(AChunkLoader.instance, world, ForgeChunkManager.Type.NORMAL);
        }

        if(ticket == null)
        {
            FMLLog.log(Level.ERROR, "Could not request ticket for chunk loading.");
            return;
        }

        // Set Coords on the ticket
        ticket.getModData().setInteger("xPos", getPos().getX());
        ticket.getModData().setInteger("yPos", getPos().getY());
        ticket.getModData().setInteger("zPos", getPos().getZ());

        // Save coords to world data
        SavedData data = SavedData.get(world);
        ChunkLoaders cl = data.getChunkLoaders();
        cl.addLoader(new ChunkLoaderPos(ownerId.toString(), world.provider.getDimension(), getPos(), System.currentTimeMillis()));
        data.setChunkLoaders(cl);

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
        //FMLLog.log(Level.INFO, "forceChunkLoading() at pos: " + pos.toString() + " " + chunkCount + " chunks loaded.");
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

        // Remove loader from world data
        SavedData data = SavedData.get(world);
        if(world.isRemote
                || data == null)
        {
            return;
        }

        ChunkLoaders cl = data.getChunkLoaders();
        if(world.provider != null)
        {
            cl.removeLoader(new ChunkLoaderPos(ownerId.toString(), world.provider.getDimension(), getPos(), 0));
            data.setChunkLoaders(cl);
        }

        super.invalidate();
    }

    public void unforceChunkLoading()
    {
        int size = ConfigurationHandler.chunkLoaderSize;
        if(size % 2 != 0 && size > 1)
        {
            size--;
        }
        int dist = size / 2;

        ChunkPos centerPos = new ChunkPos(pos.getX() / 16, pos.getZ() / 16);
        for(int x = centerPos.chunkXPos - dist; x <= centerPos.chunkXPos + dist; x++) {
            for (int z = centerPos.chunkZPos - dist; z <= centerPos.chunkZPos + dist; z++) {
                ChunkPos chunkPos = new ChunkPos(x, z);
                ForgeChunkManager.unforceChunk(ticket, chunkPos);
                FMLLog.log(Level.INFO, "Unchunkloading at chunk pos: " + chunkPos);
            }
        }
    }
}
