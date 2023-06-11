package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.IPlantable;

public class BlockCactus extends Block implements IPlantable
{
    @SideOnly(Side.CLIENT)
    private IIcon field_150041_a;
    @SideOnly(Side.CLIENT)
    private IIcon field_150040_b;
    private static final String __OBFID = "CL_00000210";

    protected BlockCactus()
    {
        super(Material.cactus);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.isAirBlock(x, y + 1, z))
        {
            int l;

            for (l = 1; worldIn.getBlock(x, y - l, z) == this; ++l)
            {
                ;
            }

            if (l < 3)
            {
                int i1 = worldIn.getBlockMetadata(x, y, z);

                if (i1 == 15)
                {
                    worldIn.setBlock(x, y + 1, z, this);
                    worldIn.setBlockMetadataWithNotify(x, y, z, 0, 4);
                    this.onNeighborBlockChange(worldIn, x, y + 1, z, this);
                }
                else
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, i1 + 1, 4);
                }
            }
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)((float)(y + 1) - f), (double)((float)(z + 1) - f));
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        float f = 0.0625F;
        return AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)(y + 1), (double)((float)(z + 1) - f));
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.field_150041_a : (side == 0 ? this.field_150040_b : this.blockIcon);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 13;
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return !super.canPlaceBlockAt(worldIn, x, y, z) ? false : this.canBlockStay(worldIn, x, y, z);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!this.canBlockStay(worldIn, x, y, z))
        {
            worldIn.func_147480_a(x, y, z, true);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        if (worldIn.getBlock(x - 1, y, z).getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.getBlock(x + 1, y, z).getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.getBlock(x, y, z - 1).getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.getBlock(x, y, z + 1).getMaterial().isSolid())
        {
            return false;
        }
        else
        {
            Block block = worldIn.getBlock(x, y - 1, z);
            return block.canSustainPlant(worldIn, x, y - 1, z, ForgeDirection.UP, this);
        }
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        entityIn.attackEntityFrom(DamageSource.cactus, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.field_150041_a = reg.registerIcon(this.getTextureName() + "_top");
        this.field_150040_b = reg.registerIcon(this.getTextureName() + "_bottom");
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z)
    {
        return EnumPlantType.Desert;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z)
    {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z)
    {
        return -1;
    }
}