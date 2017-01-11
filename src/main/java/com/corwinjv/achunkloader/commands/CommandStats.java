package com.corwinjv.achunkloader.commands;

import com.corwinjv.achunkloader.Reference;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Multiset;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

import java.util.Collection;
import java.util.List;

/**
 * Created by CorwinJV on 1/11/2017.
 */
public class CommandStats extends CommandBase {

    @Override
    public String getName() {
        return "ACL-stats";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "ACL-stats";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for(int id : DimensionManager.getIDs())
        {
            World world = DimensionManager.getWorld(id);
            int chunkCount = getChunkCount(ForgeChunkManager.getPersistentChunksFor(world));

            sender.sendMessage(new TextComponentString(chunkCount + " chunks loaded in dim " + id));
        }
    }

    public int getChunkCount(ImmutableSetMultimap<ChunkPos, ForgeChunkManager.Ticket> tickets)
    {
        int count = 0;
        for(ChunkPos key : tickets.asMap().keySet())
        {
            Collection<ForgeChunkManager.Ticket> ticketList = tickets.asMap().get(key);
            for(ForgeChunkManager.Ticket ticket : ticketList)
            {
                if(Reference.MOD_ID.equals(ticket.getModId()))
                {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
