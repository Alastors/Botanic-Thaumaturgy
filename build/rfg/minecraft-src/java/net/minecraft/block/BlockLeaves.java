package net.minecraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public abstract class BlockLeaves extends BlockLeavesBase implements IShearable
{
    int[] field_150128_a;
    @SideOnly(Side.CLIENT)
    protected int field_150127_b;
    protected IIcon[][] field_150129_M = new IIcon[2][];
    private static final String __OBFID = "CL_00000263";

    public BlockLeaves()
    {
        super(Material.leaves, false);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHardness(0.2F);
        this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
    }

    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        double d0 = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d0, d1);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @SideOnly(Side.CLIENT)
    public int getRenderColor(int meta)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        int l = 0;
        int i1 = 0;
        int j1 = 0;

        for (int k1 = -1; k1 <= 1; ++k1)
        {
            for (int l1 = -1; l1 <= 1; ++l1)
            {
                int i2 = worldIn.getBiomeGenForCoords(x + l1, z + k1).getBiomeFoliageColor(x + l1, y, z + k1);
                l += (i2 & 16711680) >> 16;
                i1 += (i2 & 65280) >> 8;
                j1 += i2 & 255;
            }
        }

        return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        byte b0 = 1;
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

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote)
        {
            int l = worldIn.getBlockMetadata(x, y, z);

            if ((l & 8) != 0 && (l & 4) == 0)
            {
                byte b0 = 4;
                int i1 = b0 + 1;
                byte b1 = 32;
                int j1 = b1 * b1;
                int k1 = b1 / 2;

                if (this.field_150128_a == null)
                {
                    this.field_150128_a = new int[b1 * b1 * b1];
                }

                int l1;

                if (worldIn.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1))
                {
                    int i2;
                    int j2;

                    for (l1 = -b0; l1 <= b0; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (j2 = -b0; j2 <= b0; ++j2)
                            {
                                Block block = worldIn.getBlock(x + l1, y + i2, z + j2);

                                if (!block.canSustainLeaves(worldIn, x + l1, y + i2, z + j2))
                                {
                                    if (block.isLeaves(worldIn, x + l1, y + i2, z + j2))
                                    {
                                        this.field_150128_a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -2;
                                    }
                                    else
                                    {
                                        this.field_150128_a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = -1;
                                    }
                                }
                                else
                                {
                                    this.field_150128_a[(l1 + k1) * j1 + (i2 + k1) * b1 + j2 + k1] = 0;
                                }
                            }
                        }
                    }

                    for (l1 = 1; l1 <= 4; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (j2 = -b0; j2 <= b0; ++j2)
                            {
                                for (int k2 = -b0; k2 <= b0; ++k2)
                                {
                                    if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1] == l1 - 1)
                                    {
                                        if (this.field_150128_a[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
                                        {
                                            this.field_150128_a[(i2 + k1 - 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] == -2)
                                        {
                                            this.field_150128_a[(i2 + k1 + 1) * j1 + (j2 + k1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] == -2)
                                        {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 - 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] == -2)
                                        {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1 + 1) * b1 + k2 + k1] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] == -2)
                                        {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + (k2 + k1 - 1)] = l1;
                                        }

                                        if (this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] == -2)
                                        {
                                            this.field_150128_a[(i2 + k1) * j1 + (j2 + k1) * b1 + k2 + k1 + 1] = l1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                l1 = this.field_150128_a[k1 * j1 + k1 * b1 + k1];

                if (l1 >= 0)
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, l & -9, 4);
                }
                else
                {
                    this.removeLeaves(worldIn, x, y, z);
                }
            }
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.canLightningStrikeAt(x, y + 1, z) && !World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && random.nextInt(15) == 1)
        {
            double d0 = (double)((float)x + random.nextFloat());
            double d1 = (double)y - 0.05D;
            double d2 = (double)((float)z + random.nextFloat());
            worldIn.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    private void removeLeaves(World p_150126_1_, int p_150126_2_, int p_150126_3_, int p_150126_4_)
    {
        this.dropBlockAsItem(p_150126_1_, p_150126_2_, p_150126_3_, p_150126_4_, p_150126_1_.getBlockMetadata(p_150126_2_, p_150126_3_, p_150126_4_), 0);
        p_150126_1_.setBlockToAir(p_150126_2_, p_150126_3_, p_150126_4_);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(Blocks.sapling);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, 1.0f, fortune);
    }

    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_) {}

    protected int func_150123_b(int p_150123_1_)
    {
        return 20;
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World worldIn, EntityPlayer player, int x, int y, int z, int meta)
    {
        {
            super.harvestBlock(worldIn, player, x, y, z, meta);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta & 3;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return !this.field_150121_P;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public abstract IIcon getIcon(int side, int meta);

    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */
    @SideOnly(Side.CLIENT)
    public void setGraphicsLevel(boolean p_150122_1_)
    {
        this.field_150121_P = p_150122_1_;
        this.field_150127_b = p_150122_1_ ? 0 : 1;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, meta & 3);
    }

    public abstract String[] func_150125_e();


    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public ArrayList<ItemStack> onSheared(ItemStack item, IBlockAccess world, int x, int y, int z, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, world.getBlockMetadata(x, y, z) & 3));
        return ret;
    }

    @Override
    public void beginLeavesDecay(World world, int x, int y, int z)
    {

        int i2 = world.getBlockMetadata(x, y, z);

        if ((i2 & 8) == 0)
        {
            world.setBlockMetadataWithNotify(x, y, z, i2 | 8, 4);
        }
        world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) | 8, 4);
    }

    @Override
    public boolean isLeaves(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }


    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        int chance = this.func_150123_b(metadata);

        if (fortune > 0)
        {
            chance -= 2 << fortune;
            if (chance < 10) chance = 10;
        }

        if (world.rand.nextInt(chance) == 0)
            ret.add(new ItemStack(this.getItemDropped(metadata, world.rand, fortune), 1, this.damageDropped(metadata)));

        chance = 200;
        if (fortune > 0)
        {
            chance -= 10 << fortune;
            if (chance < 40) chance = 40;
        }

        this.captureDrops(true);
        this.func_150124_c(world, x, y, z, metadata, chance); // Dammet mojang
        ret.addAll(this.captureDrops(false));
        return ret;
    }
}