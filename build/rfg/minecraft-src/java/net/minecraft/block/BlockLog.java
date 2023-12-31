package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLog extends BlockRotatedPillar
{
    @SideOnly(Side.CLIENT)
    protected IIcon[] field_150167_a;
    @SideOnly(Side.CLIENT)
    protected IIcon[] field_150166_b;
    private static final String __OBFID = "CL_00000266";

    public BlockLog()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(2.0F);
        this.setStepSound(soundTypeWood);
    }

    public static int func_150165_c(int p_150165_0_)
    {
        return p_150165_0_ & 3;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 1;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(this);
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        byte b0 = 4;
        int i1 = b0 + 1;

        if (worldIn.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1))
        {
            for (int j1 = -b0; j1 <= b0; ++j1)
            {
                for (int k1 = -b0; k1 <= b0; ++k1)
                {
                    for (int l1 = -b0; l1 <= b0; ++l1)
                    {
                        Block block = worldIn.getBlock(x + j1, y + k1, z + l1);
                        if (block.isLeaves(worldIn, x + j1, y + k1, z + l1))
                        {
                            block.beginLeavesDecay(worldIn, x + j1, y + k1, z + l1);
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon(int p_150163_1_)
    {
        return this.field_150167_a[p_150163_1_ % this.field_150167_a.length];
    }

    @SideOnly(Side.CLIENT)
    protected IIcon getTopIcon(int p_150161_1_)
    {
        return this.field_150166_b[p_150161_1_ % this.field_150166_b.length];
    }

    @Override
    public boolean canSustainLeaves(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean isWood(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }
}