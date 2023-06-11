package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.world.World;

public class BlockCommandBlock extends BlockContainer
{
    private static final String __OBFID = "CL_00000219";

    public BlockCommandBlock()
    {
        super(Material.iron);
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityCommandBlock();
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!worldIn.isRemote)
        {
            boolean flag = worldIn.isBlockIndirectlyGettingPowered(x, y, z);
            int l = worldIn.getBlockMetadata(x, y, z);
            boolean flag1 = (l & 1) != 0;

            if (flag && !flag1)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, l | 1, 4);
                worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
            }
            else if (!flag && flag1)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, l & -2, 4);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        TileEntity tileentity = worldIn.getTileEntity(x, y, z);

        if (tileentity != null && tileentity instanceof TileEntityCommandBlock)
        {
            CommandBlockLogic commandblocklogic = ((TileEntityCommandBlock)tileentity).func_145993_a();
            commandblocklogic.func_145755_a(worldIn);
            worldIn.func_147453_f(x, y, z, this);
        }
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 1;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)worldIn.getTileEntity(x, y, z);

        if (tileentitycommandblock != null)
        {
            player.func_146100_a(tileentitycommandblock);
        }

        return true;
    }

    /**
     * If this returns true, then comparators facing away from this block will use the value from
     * getComparatorInputOverride instead of the actual redstone signal strength.
     */
    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    /**
     * If hasComparatorInputOverride returns true, the return value from this is used instead of the redstone signal
     * strength when this block inputs to a comparator.
     */
    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        TileEntity tileentity = worldIn.getTileEntity(x, y, z);
        return tileentity != null && tileentity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock)tileentity).func_145993_a().func_145760_g() : 0;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        TileEntityCommandBlock tileentitycommandblock = (TileEntityCommandBlock)worldIn.getTileEntity(x, y, z);

        if (itemIn.hasDisplayName())
        {
            tileentitycommandblock.func_145993_a().func_145754_b(itemIn.getDisplayName());
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }
}