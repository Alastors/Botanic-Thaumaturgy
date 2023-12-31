package net.minecraft.network.play.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0EPacketClickWindow extends Packet
{
    private int field_149554_a;
    private int field_149552_b;
    private int field_149553_c;
    private short field_149550_d;
    private ItemStack field_149551_e;
    private int field_149549_f;
    private static final String __OBFID = "CL_00001353";

    public C0EPacketClickWindow() {}

    @SideOnly(Side.CLIENT)
    public C0EPacketClickWindow(int p_i45246_1_, int p_i45246_2_, int p_i45246_3_, int p_i45246_4_, ItemStack p_i45246_5_, short p_i45246_6_)
    {
        this.field_149554_a = p_i45246_1_;
        this.field_149552_b = p_i45246_2_;
        this.field_149553_c = p_i45246_3_;
        this.field_149551_e = p_i45246_5_ != null ? p_i45246_5_.copy() : null;
        this.field_149550_d = p_i45246_6_;
        this.field_149549_f = p_i45246_4_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processClickWindow(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149554_a = data.readByte();
        this.field_149552_b = data.readShort();
        this.field_149553_c = data.readByte();
        this.field_149550_d = data.readShort();
        this.field_149549_f = data.readByte();
        this.field_149551_e = data.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.field_149554_a);
        data.writeShort(this.field_149552_b);
        data.writeByte(this.field_149553_c);
        data.writeShort(this.field_149550_d);
        data.writeByte(this.field_149549_f);
        data.writeItemStackToBuffer(this.field_149551_e);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return this.field_149551_e != null ? String.format("id=%d, slot=%d, button=%d, type=%d, itemid=%d, itemcount=%d, itemaux=%d", new Object[] {Integer.valueOf(this.field_149554_a), Integer.valueOf(this.field_149552_b), Integer.valueOf(this.field_149553_c), Integer.valueOf(this.field_149549_f), Integer.valueOf(Item.getIdFromItem(this.field_149551_e.getItem())), Integer.valueOf(this.field_149551_e.stackSize), Integer.valueOf(this.field_149551_e.getItemDamage())}): String.format("id=%d, slot=%d, button=%d, type=%d, itemid=-1", new Object[] {Integer.valueOf(this.field_149554_a), Integer.valueOf(this.field_149552_b), Integer.valueOf(this.field_149553_c), Integer.valueOf(this.field_149549_f)});
    }

    public int func_149548_c()
    {
        return this.field_149554_a;
    }

    public int func_149544_d()
    {
        return this.field_149552_b;
    }

    public int func_149543_e()
    {
        return this.field_149553_c;
    }

    public short func_149547_f()
    {
        return this.field_149550_d;
    }

    public ItemStack func_149546_g()
    {
        return this.field_149551_e;
    }

    public int func_149542_h()
    {
        return this.field_149549_f;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}