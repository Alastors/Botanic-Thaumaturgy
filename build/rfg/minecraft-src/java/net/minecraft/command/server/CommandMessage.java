package net.minecraft.command.server;

import java.util.Arrays;
import java.util.List;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class CommandMessage extends CommandBase
{
    private static final String __OBFID = "CL_00000641";

    public List getCommandAliases()
    {
        return Arrays.asList(new String[] {"w", "msg"});
    }

    public String getCommandName()
    {
        return "tell";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.message.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.message.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP entityplayermp = getPlayer(sender, args[0]);

            if (entityplayermp == null)
            {
                throw new PlayerNotFoundException();
            }
            else if (entityplayermp == sender)
            {
                throw new PlayerNotFoundException("commands.message.sameTarget", new Object[0]);
            }
            else
            {
                IChatComponent ichatcomponent = func_147176_a(sender, args, 1, !(sender instanceof EntityPlayer));
                ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("commands.message.display.incoming", new Object[] {sender.func_145748_c_(), ichatcomponent.createCopy()});
                ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("commands.message.display.outgoing", new Object[] {entityplayermp.func_145748_c_(), ichatcomponent.createCopy()});
                chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.valueOf(true));
                chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.GRAY).setItalic(Boolean.valueOf(true));
                entityplayermp.addChatMessage(chatcomponenttranslation);
                sender.addChatMessage(chatcomponenttranslation1);
            }
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}