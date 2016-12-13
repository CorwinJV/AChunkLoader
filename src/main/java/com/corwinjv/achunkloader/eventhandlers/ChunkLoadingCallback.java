package com.corwinjv.achunkloader.eventhandlers;

import com.corwinjv.achunkloader.blocks.ModBlocks;
import com.corwinjv.achunkloader.blocks.tileentities.ChunkLoaderTileEntity;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.List;

/**
 * Created by CorwinJV on 12/13/2016.
 */
public class ChunkLoadingCallback implements ForgeChunkManager.LoadingCallback {

    @Override
    public void ticketsLoaded(List<Ticket> tickets, World world)
    {
        for (ForgeChunkManager.Ticket ticket : tickets)
        {
            int xPos = ticket.getModData().getInteger("xPos");
            int yPos = ticket.getModData().getInteger("yPos");
            int zPos = ticket.getModData().getInteger("zPos");

            BlockPos pos = new BlockPos(xPos, yPos, zPos);

            Block block = world.getBlockState(pos).getBlock();
            if (block == ModBlocks.getBlock(ModBlocks.CHUNK_LOADER)) {
                ChunkLoaderTileEntity chunkLoaderTile = (ChunkLoaderTileEntity) world.getTileEntity(pos);
                if(chunkLoaderTile != null)
                {
                    chunkLoaderTile.forceChunkLoading(ticket);
                }
            }
        }
    }
}
