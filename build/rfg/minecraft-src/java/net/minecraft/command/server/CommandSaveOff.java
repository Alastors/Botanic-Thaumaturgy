package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOff extends CommandBase
{
    private static final String __OBFID = "CL_00000847";

    public String getCommandName()
    {
        return "save-off";
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.save-off.usage";
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

                if (!worldserver.levelSaving)
                {
                    worldserver.levelSaving = true;
                    flag = true;
                }
            }
        }

        if (flag)
        {
            func_152373_a(sender, this, "commands.save.disabled", new Object[0]);
        }
        else
        {
            throw new CommandException("commands.save-off.alreadyOff", new Object[0]);
        }
    }
}