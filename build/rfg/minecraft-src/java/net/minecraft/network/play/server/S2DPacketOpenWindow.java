package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S2DPacketOpenWindow extends Packet
{
    private int field_148909_a;
    private int field_148907_b;
    private String field_148908_c;
    private int field_148905_d;
    private boolean field_148906_e;
    private int field_148904_f;
    private static final String __OBFID = "CL_00001293";

    public S2DPacketOpenWindow() {}

    public S2DPacketOpenWindow(int p_i45184_1_, int p_i45184_2_, String p_i45184_3_, int p_i45184_4_, boolean p_i45184_5_)
    {
        this.field_148909_a = p_i45184_1_;
        this.field_148907_b = p_i45184_2_;
        this.field_148908_c = p_i45184_3_;
        this.field_148905_d = p_i45184_4_;
        this.field_148906_e = p_i45184_5_;
    }

    public S2DPacketOpenWindow(int p_i45185_1_, int p_i45185_2_, String p_i45185_3_, int p_i45185_4_, boolean p_i45185_5_, int p_i45185_6_)
    {
        this(p_i45185_1_, p_i45185_2_, p_i45185_3_, p_i45185_4_, p_i45185_5_);
        this.field_148904_f = p_i45185_6_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleOpenWindow(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_148909_a = data.readUnsignedByte();
        this.field_148907_b = data.readUnsignedByte();
        this.field_148908_c = data.readStringFromBuffer(32);
        this.field_148905_d = data.readUnsignedByte();
        this.field_148906_e = data.readBoolean();

        if (this.field_148907_b == 11)
        {
            this.field_148904_f = data.readInt();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.field_148909_a);
        data.writeByte(this.field_148907_b);
        data.writeStringToBuffer(this.field_148908_c);
        data.writeByte(this.field_148905_d);
        data.writeBoolean(this.field_148906_e);

        if (this.field_148907_b == 11)
        {
            data.writeInt(this.field_148904_f);
        }
    }

    @SideOnly(Side.CLIENT)
    public int func_148901_c()
    {
        return this.field_148909_a;
    }

    @SideOnly(Side.CLIENT)
    public int func_148899_d()
    {
        return this.field_148907_b;
    }

    @SideOnly(Side.CLIENT)
    public String func_148902_e()
    {
        return this.field_148908_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_148898_f()
    {
        return this.field_148905_d;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_148900_g()
    {
        return this.field_148906_e;
    }

    @SideOnly(Side.CLIENT)
    public int func_148897_h()
    {
        return this.field_148904_f;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}