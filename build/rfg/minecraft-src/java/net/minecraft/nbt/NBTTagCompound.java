package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase
{
    private static final Logger logger = LogManager.getLogger();
    /** The key-value pairs for the tag. Each key is a UTF string, each value is a tag. */
    private Map tagMap = new HashMap();
    private static final String __OBFID = "CL_00001215";

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        Iterator iterator = this.tagMap.keySet().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            NBTBase nbtbase = (NBTBase)this.tagMap.get(s);
            func_150298_a(s, nbtbase, output);
        }

        output.writeByte(0);
    }

    void func_152446_a(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        if (depth > 512)
        {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        else
        {
            this.tagMap.clear();
            byte b0;

            while ((b0 = func_152447_a(input, sizeTracker)) != 0)
            {
                String s = func_152448_b(input, sizeTracker);
                NBTSizeTracker.readUTF(sizeTracker, s); // Forge: Correctly read String length including header.
                NBTBase nbtbase = func_152449_a(b0, s, input, depth + 1, sizeTracker);
                this.tagMap.put(s, nbtbase);
            }
        }
    }

    public Set func_150296_c()
    {
        return this.tagMap.keySet();
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return (byte)10;
    }

    /**
     * Stores the given tag into the map with the given string key. This is mostly used to store tag lists.
     */
    public void setTag(String key, NBTBase value)
    {
        this.tagMap.put(key, value);
    }

    /**
     * Stores a new NBTTagByte with the given byte value into the map with the given string key.
     */
    public void setByte(String key, byte value)
    {
        this.tagMap.put(key, new NBTTagByte(value));
    }

    /**
     * Stores a new NBTTagShort with the given short value into the map with the given string key.
     */
    public void setShort(String key, short value)
    {
        this.tagMap.put(key, new NBTTagShort(value));
    }

    /**
     * Stores a new NBTTagInt with the given integer value into the map with the given string key.
     */
    public void setInteger(String key, int value)
    {
        this.tagMap.put(key, new NBTTagInt(value));
    }

    /**
     * Stores a new NBTTagLong with the given long value into the map with the given string key.
     */
    public void setLong(String key, long value)
    {
        this.tagMap.put(key, new NBTTagLong(value));
    }

    /**
     * Stores a new NBTTagFloat with the given float value into the map with the given string key.
     */
    public void setFloat(String key, float value)
    {
        this.tagMap.put(key, new NBTTagFloat(value));
    }

    /**
     * Stores a new NBTTagDouble with the given double value into the map with the given string key.
     */
    public void setDouble(String key, double value)
    {
        this.tagMap.put(key, new NBTTagDouble(value));
    }

    /**
     * Stores a new NBTTagString with the given string value into the map with the given string key.
     */
    public void setString(String key, String value)
    {
        this.tagMap.put(key, new NBTTagString(value));
    }

    /**
     * Stores a new NBTTagByteArray with the given array as data into the map with the given string key.
     */
    public void setByteArray(String key, byte[] value)
    {
        this.tagMap.put(key, new NBTTagByteArray(value));
    }

    /**
     * Stores a new NBTTagIntArray with the given array as data into the map with the given string key.
     */
    public void setIntArray(String key, int[] value)
    {
        this.tagMap.put(key, new NBTTagIntArray(value));
    }

    /**
     * Stores the given boolean value as a NBTTagByte, storing 1 for true and 0 for false, using the given string key.
     */
    public void setBoolean(String key, boolean value)
    {
        this.setByte(key, (byte)(value ? 1 : 0));
    }

    /**
     * gets a generic tag with the specified name
     */
    public NBTBase getTag(String key)
    {
        return (NBTBase)this.tagMap.get(key);
    }

    public byte func_150299_b(String key)
    {
        NBTBase nbtbase = (NBTBase)this.tagMap.get(key);
        return nbtbase != null ? nbtbase.getId() : 0;
    }

    /**
     * Returns whether the given string has been previously stored as a key in the map.
     */
    public boolean hasKey(String key)
    {
        return this.tagMap.containsKey(key);
    }

    public boolean hasKey(String key, int type)
    {
        byte b0 = this.func_150299_b(key);
        return b0 == type ? true : (type != 99 ? false : b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6);
    }

    /**
     * Retrieves a byte value using the specified key, or 0 if no such key was stored.
     */
    public byte getByte(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).func_150290_f();
        }
        catch (ClassCastException classcastexception)
        {
            return (byte)0;
        }
    }

    /**
     * Retrieves a short value using the specified key, or 0 if no such key was stored.
     */
    public short getShort(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).func_150289_e();
        }
        catch (ClassCastException classcastexception)
        {
            return (short)0;
        }
    }

    /**
     * Retrieves an integer value using the specified key, or 0 if no such key was stored.
     */
    public int getInteger(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? 0 : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).func_150287_d();
        }
        catch (ClassCastException classcastexception)
        {
            return 0;
        }
    }

    /**
     * Retrieves a long value using the specified key, or 0 if no such key was stored.
     */
    public long getLong(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? 0L : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).func_150291_c();
        }
        catch (ClassCastException classcastexception)
        {
            return 0L;
        }
    }

    /**
     * Retrieves a float value using the specified key, or 0 if no such key was stored.
     */
    public float getFloat(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? 0.0F : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).func_150288_h();
        }
        catch (ClassCastException classcastexception)
        {
            return 0.0F;
        }
    }

    /**
     * Retrieves a double value using the specified key, or 0 if no such key was stored.
     */
    public double getDouble(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? 0.0D : ((NBTBase.NBTPrimitive)this.tagMap.get(key)).func_150286_g();
        }
        catch (ClassCastException classcastexception)
        {
            return 0.0D;
        }
    }

    /**
     * Retrieves a string value using the specified key, or an empty string if no such key was stored.
     */
    public String getString(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? "" : ((NBTBase)this.tagMap.get(key)).func_150285_a_();
        }
        catch (ClassCastException classcastexception)
        {
            return "";
        }
    }

    /**
     * Retrieves a byte array using the specified key, or a zero-length array if no such key was stored.
     */
    public byte[] getByteArray(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? new byte[0] : ((NBTTagByteArray)this.tagMap.get(key)).func_150292_c();
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 7, classcastexception));
        }
    }

    /**
     * Retrieves an int array using the specified key, or a zero-length array if no such key was stored.
     */
    public int[] getIntArray(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? new int[0] : ((NBTTagIntArray)this.tagMap.get(key)).func_150302_c();
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 11, classcastexception));
        }
    }

    /**
     * Retrieves a NBTTagCompound subtag matching the specified key, or a new empty NBTTagCompound if no such key was
     * stored.
     */
    public NBTTagCompound getCompoundTag(String key)
    {
        try
        {
            return !this.tagMap.containsKey(key) ? new NBTTagCompound() : (NBTTagCompound)this.tagMap.get(key);
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 10, classcastexception));
        }
    }

    /**
     * Gets the NBTTagList object with the given name. Args: name, NBTBase type
     */
    public NBTTagList getTagList(String key, int type)
    {
        try
        {
            if (this.func_150299_b(key) != 9)
            {
                return new NBTTagList();
            }
            else
            {
                NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);
                return nbttaglist.tagCount() > 0 && nbttaglist.func_150303_d() != type ? new NBTTagList() : nbttaglist;
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 9, classcastexception));
        }
    }

    /**
     * Retrieves a boolean value using the specified key, or false if no such key was stored. This uses the getByte
     * method.
     */
    public boolean getBoolean(String key)
    {
        return this.getByte(key) != 0;
    }

    /**
     * Remove the specified tag.
     */
    public void removeTag(String key)
    {
        this.tagMap.remove(key);
    }

    public String toString()
    {
        String s = "{";
        String s1;

        for (Iterator iterator = this.tagMap.keySet().iterator(); iterator.hasNext(); s = s + s1 + ':' + this.tagMap.get(s1) + ',')
        {
            s1 = (String)iterator.next();
        }

        return s + "}";
    }

    /**
     * Return whether this compound has no tags.
     */
    public boolean hasNoTags()
    {
        return this.tagMap.isEmpty();
    }

    /**
     * Create a crash report which indicates a NBT read error.
     */
    private CrashReport createCrashReport(final String p_82581_1_, final int p_82581_2_, ClassCastException p_82581_3_)
    {
        CrashReport crashreport = CrashReport.makeCrashReport(p_82581_3_, "Reading NBT data");
        CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashreportcategory.addCrashSectionCallable("Tag type found", new Callable()
        {
            private static final String __OBFID = "CL_00001216";
            public String call()
            {
                return NBTBase.NBTTypes[((NBTBase)NBTTagCompound.this.tagMap.get(p_82581_1_)).getId()];
            }
        });
        crashreportcategory.addCrashSectionCallable("Tag type expected", new Callable()
        {
            private static final String __OBFID = "CL_00001217";
            public String call()
            {
                return NBTBase.NBTTypes[p_82581_2_];
            }
        });
        crashreportcategory.addCrashSection("Tag name", p_82581_1_);
        return crashreport;
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator iterator = this.tagMap.keySet().iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            nbttagcompound.setTag(s, ((NBTBase)this.tagMap.get(s)).copy());
        }

        return nbttagcompound;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (super.equals(p_equals_1_))
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)p_equals_1_;
            return this.tagMap.entrySet().equals(nbttagcompound.tagMap.entrySet());
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.tagMap.hashCode();
    }

    private static void func_150298_a(String name, NBTBase data, DataOutput output) throws IOException
    {
        output.writeByte(data.getId());

        if (data.getId() != 0)
        {
            output.writeUTF(name);
            data.write(output);
        }
    }

    private static byte func_152447_a(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.func_152450_a(8);
        return input.readByte();
    }

    private static String func_152448_b(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        return input.readUTF();
    }

    static NBTBase func_152449_a(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker)
    {
        sizeTracker.func_152450_a(32); //Forge: 4 extra bytes for the object allocation.
        NBTBase nbtbase = NBTBase.func_150284_a(id);

        try
        {
            nbtbase.func_152446_a(input, depth, sizeTracker);
            return nbtbase;
        }
        catch (IOException ioexception)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
            crashreportcategory.addCrashSection("Tag name", key);
            crashreportcategory.addCrashSection("Tag type", Byte.valueOf(id));
            throw new ReportedException(crashreport);
        }
    }
}