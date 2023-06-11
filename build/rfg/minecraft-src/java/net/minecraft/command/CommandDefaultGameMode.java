package net.minecraft.command;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSettings;

public class CommandDefaultGameMode extends CommandGameMode
{
    private static final String __OBFID = "CL_00000296";

    public String getCommandName()
    {
        return "defaultgamemode";
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.defaultgamemode.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            WorldSettings.GameType gametype = this.getGameModeFromCommand(sender, args[0]);
            this.setGameType(gametype);
            func_152373_a(sender, this, "commands.defaultgamemode.success", new Object[] {new ChatComponentTranslation("gameMode." + gametype.getName(), new Object[0])});
        }
        else
        {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        }
    }

    protected void setGameType(WorldSettings.GameType p_71541_1_)
    {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        minecraftserver.setGameType(p_71541_1_);
        EntityPlayerMP entityplayermp;

        if (minecraftserver.getForceGamemode())
        {
            for (Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator(); iterator.hasNext(); entityplayermp.fallDistance = 0.0F)
            {
                entityplayermp = (EntityPlayerMP)iterator.next();
                entityplayermp.setGameType(p_71541_1_);
            }
        }
    }
}