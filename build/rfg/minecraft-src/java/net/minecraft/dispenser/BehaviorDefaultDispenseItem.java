package net.minecraft.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem
{
    private static final String __OBFID = "CL_00001195";

    /**
     * Dispenses the specified ItemStack from a dispenser.
     */
    public final ItemStack dispense(IBlockSource source, ItemStack stack)
    {
        ItemStack itemstack1 = this.dispenseStack(source, stack);
        this.playDispenseSound(source);
        this.spawnDispenseParticles(source, BlockDispenser.func_149937_b(source.getBlockMetadata()));
        return itemstack1;
    }

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
    {
        EnumFacing enumfacing = BlockDispenser.func_149937_b(source.getBlockMetadata());
        IPosition iposition = BlockDispenser.func_149939_a(source);
        ItemStack itemstack1 = stack.splitStack(1);
        doDispense(source.getWorld(), itemstack1, 6, enumfacing, iposition);
        return stack;
    }

    public static void doDispense(World worldIn, ItemStack stack, int speed, EnumFacing p_82486_3_, IPosition position)
    {
        double d0 = position.getX();
        double d1 = position.getY();
        double d2 = position.getZ();
        EntityItem entityitem = new EntityItem(worldIn, d0, d1 - 0.3D, d2, stack);
        double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
        entityitem.motionX = (double)p_82486_3_.getFrontOffsetX() * d3;
        entityitem.motionY = 0.20000000298023224D;
        entityitem.motionZ = (double)p_82486_3_.getFrontOffsetZ() * d3;
        entityitem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double)speed;
        entityitem.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double)speed;
        entityitem.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * (double)speed;
        worldIn.spawnEntityInWorld(entityitem);
    }

    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource source)
    {
        source.getWorld().playAuxSFX(1000, source.getXInt(), source.getYInt(), source.getZInt(), 0);
    }

    /**
     * Order clients to display dispense particles from the specified block and facing.
     */
    protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn)
    {
        source.getWorld().playAuxSFX(2000, source.getXInt(), source.getYInt(), source.getZInt(), this.func_82488_a(facingIn));
    }

    private int func_82488_a(EnumFacing facingIn)
    {
        return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
    }
}