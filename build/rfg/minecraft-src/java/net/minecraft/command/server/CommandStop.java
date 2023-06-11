package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStop extends CommandBase
{
    private static final String __OBFID = "CL_00001132";

    public String getCommandName()
    {
        return "stop";
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.stop.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (MinecraftServer.getServer().worldServers != null)
        {
            func_152373_a(sender, this, "commands.stop.start", new Object[0]);
        }

        MinecraftServer.getServer().initiateShutdown();
    }
}