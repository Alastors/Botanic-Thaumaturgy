package net.minecraft.network.rcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@SideOnly(Side.SERVER)
public class RConOutputStream
{
    /** Output stream */
    private ByteArrayOutputStream byteArrayOutput;
    /** ByteArrayOutputStream wrapper */
    private DataOutputStream output;
    private static final String __OBFID = "CL_00001798";

    public RConOutputStream(int size)
    {
        this.byteArrayOutput = new ByteArrayOutputStream(size);
        this.output = new DataOutputStream(this.byteArrayOutput);
    }

    /**
     * Writes the given byte array to the output stream
     */
    public void writeByteArray(byte[] data) throws IOException
    {
        this.output.write(data, 0, data.length);
    }

    /**
     * Writes the given String to the output stream
     */
    public void writeString(String data) throws IOException
    {
        this.output.writeBytes(data);
        this.output.write(0);
    }

    /**
     * Writes the given int to the output stream
     */
    public void writeInt(int data) throws IOException
    {
        this.output.write(data);
    }

    /**
     * Writes the given short to the output stream
     */
    public void writeShort(short data) throws IOException
    {
        this.output.writeShort(Short.reverseBytes(data));
    }

    /**
     * Returns the contents of the output stream as a byte array
     */
    public byte[] toByteArray()
    {
        return this.byteArrayOutput.toByteArray();
    }

    /**
     * Resets the byte array output.
     */
    public void reset()
    {
        this.byteArrayOutput.reset();
    }
}