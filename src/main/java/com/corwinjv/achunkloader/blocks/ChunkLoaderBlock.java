package com.corwinjv.achunkloader.blocks;

import com.corwinjv.achunkloader.blocks.tileentities.ChunkLoaderTileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by CorwinJV on 12/11/2016.
 */
public class ChunkLoaderBlock extends ModBlock implements ITileEntityProvider {
    ChunkLoaderBlock()
    {
        super(Material.ROCK);
        setHardness(2.0f);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if(!worldIn.isRemote)
        {
            ChunkLoaderTileEntity chunkLoaderTileEntity = (ChunkLoaderTileEntity)worldIn.getTileEntity(pos);
            if(chunkLoaderTileEntity != null)
            {
                chunkLoaderTileEntity.forceChunkLoading();
            }
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        if(!worldIn.isRemote)
        {
            ChunkLoaderTileEntity chunkLoaderTileEntity = (ChunkLoaderTileEntity)worldIn.getTileEntity(pos);
            if(chunkLoaderTileEntity != null)
            {
                chunkLoaderTileEntity.unforceChunkLoading();
            }
        }
        super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote)
        {
            ChunkLoaderTileEntity chunkLoaderTileEntity = (ChunkLoaderTileEntity)worldIn.getTileEntity(pos);
            if(chunkLoaderTileEntity != null)
            {
                chunkLoaderTileEntity.unforceChunkLoading();
            }
        }
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ChunkLoaderTileEntity();
    }
}
