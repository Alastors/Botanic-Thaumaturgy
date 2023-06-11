package net.minecraft.network.play.server;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S0EPacketSpawnObject extends Packet
{
    private int field_149018_a;
    private int field_149016_b;
    private int field_149017_c;
    private int field_149014_d;
    private int field_149015_e;
    private int field_149012_f;
    private int field_149013_g;
    private int field_149021_h;
    private int field_149022_i;
    private int field_149019_j;
    private int field_149020_k;
    private static final String __OBFID = "CL_00001276";

    public S0EPacketSpawnObject() {}

    public S0EPacketSpawnObject(Entity p_i45165_1_, int p_i45165_2_)
    {
        this(p_i45165_1_, p_i45165_2_, 0);
    }

    public S0EPacketSpawnObject(Entity p_i45166_1_, int p_i45166_2_, int p_i45166_3_)
    {
        this.field_149018_a = p_i45166_1_.getEntityId();
        this.field_149016_b = MathHelper.floor_double(p_i45166_1_.posX * 32.0D);
        this.field_149017_c = MathHelper.floor_double(p_i45166_1_.posY * 32.0D);
        this.field_149014_d = MathHelper.floor_double(p_i45166_1_.posZ * 32.0D);
        this.field_149021_h = MathHelper.floor_float(p_i45166_1_.rotationPitch * 256.0F / 360.0F);
        this.field_149022_i = MathHelper.floor_float(p_i45166_1_.rotationYaw * 256.0F / 360.0F);
        this.field_149019_j = p_i45166_2_;
        this.field_149020_k = p_i45166_3_;

        if (p_i45166_3_ > 0)
        {
            double d0 = p_i45166_1_.motionX;
            double d1 = p_i45166_1_.motionY;
            double d2 = p_i45166_1_.motionZ;
            double d3 = 3.9D;

            if (d0 < -d3)
            {
                d0 = -d3;
            }

            if (d1 < -d3)
            {
                d1 = -d3;
            }

            if (d2 < -d3)
            {
                d2 = -d3;
            }

            if (d0 > d3)
            {
                d0 = d3;
            }

            if (d1 > d3)
            {
                d1 = d3;
            }

            if (d2 > d3)
            {
                d2 = d3;
            }

            this.field_149015_e = (int)(d0 * 8000.0D);
            this.field_149012_f = (int)(d1 * 8000.0D);
            this.field_149013_g = (int)(d2 * 8000.0D);
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149018_a = data.readVarIntFromBuffer();
        this.field_149019_j = data.readByte();
        this.field_149016_b = data.readInt();
        this.field_149017_c = data.readInt();
        this.field_149014_d = data.readInt();
        this.field_149021_h = data.readByte();
        this.field_149022_i = data.readByte();
        this.field_149020_k = data.readInt();

        if (this.field_149020_k > 0)
        {
            this.field_149015_e = data.readShort();
            this.field_149012_f = data.readShort();
            this.field_149013_g = data.readShort();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.field_149018_a);
        data.writeByte(this.field_149019_j);
        data.writeInt(this.field_149016_b);
        data.writeInt(this.field_149017_c);
        data.writeInt(this.field_149014_d);
        data.writeByte(this.field_149021_h);
        data.writeByte(this.field_149022_i);
        data.writeInt(this.field_149020_k);

        if (this.field_149020_k > 0)
        {
            data.writeShort(this.field_149015_e);
            data.writeShort(this.field_149012_f);
            data.writeShort(this.field_149013_g);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleSpawnObject(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("id=%d, type=%d, x=%.2f, y=%.2f, z=%.2f", new Object[] {Integer.valueOf(this.field_149018_a), Integer.valueOf(this.field_149019_j), Float.valueOf((float)this.field_149016_b / 32.0F), Float.valueOf((float)this.field_149017_c / 32.0F), Float.valueOf((float)this.field_149014_d / 32.0F)});
    }

    @SideOnly(Side.CLIENT)
    public int func_149001_c()
    {
        return this.field_149018_a;
    }

    @SideOnly(Side.CLIENT)
    public int func_148997_d()
    {
        return this.field_149016_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_148998_e()
    {
        return this.field_149017_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_148994_f()
    {
        return this.field_149014_d;
    }

    @SideOnly(Side.CLIENT)
    public int func_149010_g()
    {
        return this.field_149015_e;
    }

    @SideOnly(Side.CLIENT)
    public int func_149004_h()
    {
        return this.field_149012_f;
    }

    @SideOnly(Side.CLIENT)
    public int func_148999_i()
    {
        return this.field_149013_g;
    }

    @SideOnly(Side.CLIENT)
    public int func_149008_j()
    {
        return this.field_149021_h;
    }

    @SideOnly(Side.CLIENT)
    public int func_149006_k()
    {
        return this.field_149022_i;
    }

    @SideOnly(Side.CLIENT)
    public int func_148993_l()
    {
        return this.field_149019_j;
    }

    @SideOnly(Side.CLIENT)
    public int func_149009_m()
    {
        return this.field_149020_k;
    }

    public void func_148996_a(int p_148996_1_)
    {
        this.field_149016_b = p_148996_1_;
    }

    public void func_148995_b(int p_148995_1_)
    {
        this.field_149017_c = p_148995_1_;
    }

    public void func_149005_c(int p_149005_1_)
    {
        this.field_149014_d = p_149005_1_;
    }

    public void func_149003_d(int p_149003_1_)
    {
        this.field_149015_e = p_149003_1_;
    }

    public void func_149000_e(int p_149000_1_)
    {
        this.field_149012_f = p_149000_1_;
    }

    public void func_149007_f(int p_149007_1_)
    {
        this.field_149013_g = p_149007_1_;
    }

    @SideOnly(Side.CLIENT)
    public void func_149002_g(int p_149002_1_)
    {
        this.field_149020_k = p_149002_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}