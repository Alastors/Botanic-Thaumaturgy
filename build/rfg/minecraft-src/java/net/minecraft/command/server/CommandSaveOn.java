package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOn extends CommandBase
{
    private static final String __OBFID = "CL_00000873";

    public String getCommandName()
    {
        return "save-on";
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.save-on.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        boolean flag = false;

        for (int i = 0; i < minecraftserver.worldServers.length; ++i)
        {
            if (minecraftserver.worldServers[i] != null)
            {
                WorldServer worldserver = minecraftserver.worldServers[i];

                if (worldserver.levelSaving)
                {
                    worldserver.levelSaving = false;
                    flag = true;
                }
            }
        }

        if (flag)
        {
            func_152373_a(sender, this, "commands.save.enabled", new Object[0]);
        }
        else
        {
            throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
        }
    }
}