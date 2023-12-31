package net.minecraft.entity.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.ai.EntityMinecartMobSpawner;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IMinecartCollisionHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
import net.minecraftforge.event.entity.minecart.MinecartUpdateEvent;

public abstract class EntityMinecart extends Entity
{
    private boolean isInReverse;
    private String entityName;
    /** Minecart rotational logic matrix */
    private static final int[][][] matrix = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
    /** appears to be the progress of the turn */
    private int turnProgress;
    private double minecartX;
    private double minecartY;
    private double minecartZ;
    private double minecartYaw;
    private double minecartPitch;
    @SideOnly(Side.CLIENT)
    private double velocityX;
    @SideOnly(Side.CLIENT)
    private double velocityY;
    @SideOnly(Side.CLIENT)
    private double velocityZ;
    private static final String __OBFID = "CL_00001670";

    /* Forge: Minecart Compatibility Layer Integration. */
    public static float defaultMaxSpeedAirLateral = 0.4f;
    public static float defaultMaxSpeedAirVertical = -1f;
    public static double defaultDragAir = 0.94999998807907104D;
    protected boolean canUseRail = true;
    protected boolean canBePushed = true;
    private static IMinecartCollisionHandler collisionHandler = null;

    /* Instance versions of the above physics properties */
    private float currentSpeedRail = getMaxCartSpeedOnRail();
    protected float maxSpeedAirLateral = defaultMaxSpeedAirLateral;
    protected float maxSpeedAirVertical = defaultMaxSpeedAirVertical;
    protected double dragAir = defaultDragAir;

    public EntityMinecart(World p_i1712_1_)
    {
        super(p_i1712_1_);
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.7F);
        this.yOffset = this.height / 2.0F;
    }

    /**
     * Creates a new minecart of the specified type in the specified location in the given world. par0World - world to
     * create the minecart in, double par1,par3,par5 represent x,y,z respectively. int par7 specifies the type: 1 for
     * MinecartChest, 2 for MinecartFurnace, 3 for MinecartTNT, 4 for MinecartMobSpawner, 5 for MinecartHopper and 0 for
     * a standard empty minecart
     */
    public static EntityMinecart createMinecart(World p_94090_0_, double p_94090_1_, double p_94090_3_, double p_94090_5_, int p_94090_7_)
    {
        switch (p_94090_7_)
        {
            case 1:
                return new EntityMinecartChest(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
            case 2:
                return new EntityMinecartFurnace(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
            case 3:
                return new EntityMinecartTNT(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
            case 4:
                return new EntityMinecartMobSpawner(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
            case 5:
                return new EntityMinecartHopper(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
            case 6:
                return new EntityMinecartCommandBlock(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
            default:
                return new EntityMinecartEmpty(p_94090_0_, p_94090_1_, p_94090_3_, p_94090_5_);
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0F));
        this.dataWatcher.addObject(20, new Integer(0));
        this.dataWatcher.addObject(21, new Integer(6));
        this.dataWatcher.addObject(22, Byte.valueOf((byte)0));
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        if (getCollisionHandler() != null)
        {
            return getCollisionHandler().getCollisionBox(this, entityIn);
        }
        return entityIn.canBePushed() ? entityIn.boundingBox : null;
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        if (getCollisionHandler() != null)
        {
            return getCollisionHandler().getBoundingBox(this);
        }
        return null;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return canBePushed;
    }

    public EntityMinecart(World p_i1713_1_, double p_i1713_2_, double p_i1713_4_, double p_i1713_6_)
    {
        this(p_i1713_1_);
        this.setPosition(p_i1713_2_, p_i1713_4_, p_i1713_6_);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = p_i1713_2_;
        this.prevPosY = p_i1713_4_;
        this.prevPosZ = p_i1713_6_;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return (double)this.height * 0.0D - 0.30000001192092896D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!this.worldObj.isRemote && !this.isDead)
        {
            if (this.isEntityInvulnerable())
            {
                return false;
            }
            else
            {
                this.setRollingDirection(-this.getRollingDirection());
                this.setRollingAmplitude(10);
                this.setBeenAttacked();
                this.setDamage(this.getDamage() + amount * 10.0F);
                boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;

                if (flag || this.getDamage() > 40.0F)
                {
                    if (this.riddenByEntity != null)
                    {
                        this.riddenByEntity.mountEntity(this);
                    }

                    if (flag && !this.hasCustomInventoryName())
                    {
                        this.setDead();
                    }
                    else
                    {
                        this.killMinecart(source);
                    }
                }

                return true;
            }
        }
        else
        {
            return true;
        }
    }

    public void killMinecart(DamageSource p_94095_1_)
    {
        this.setDead();
        ItemStack itemstack = new ItemStack(Items.minecart, 1);

        if (this.entityName != null)
        {
            itemstack.setStackDisplayName(this.entityName);
        }

        this.entityDropItem(itemstack, 0.0F);
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        this.setRollingDirection(-this.getRollingDirection());
        this.setRollingAmplitude(10);
        this.setDamage(this.getDamage() + this.getDamage() * 10.0F);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        super.setDead();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.getRollingAmplitude() > 0)
        {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }

        if (this.getDamage() > 0.0F)
        {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.posY < -64.0D)
        {
            this.kill();
        }

        int i;

        if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer)
        {
            this.worldObj.theProfiler.startSection("portal");
            MinecraftServer minecraftserver = ((WorldServer)this.worldObj).func_73046_m();
            i = this.getMaxInPortalTime();

            if (this.inPortal)
            {
                if (minecraftserver.getAllowNether())
                {
                    if (this.ridingEntity == null && this.portalCounter++ >= i)
                    {
                        this.portalCounter = i;
                        this.timeUntilPortal = this.getPortalCooldown();
                        byte b0;

                        if (this.worldObj.provider.dimensionId == -1)
                        {
                            b0 = 0;
                        }
                        else
                        {
                            b0 = -1;
                        }

                        this.travelToDimension(b0);
                    }

                    this.inPortal = false;
                }
            }
            else
            {
                if (this.portalCounter > 0)
                {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0)
                {
                    this.portalCounter = 0;
                }
            }

            if (this.timeUntilPortal > 0)
            {
                --this.timeUntilPortal;
            }

            this.worldObj.theProfiler.endSection();
        }

        if (this.worldObj.isRemote)
        {
            if (this.turnProgress > 0)
            {
                double d6 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
                double d7 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
                double d1 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
                double d3 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.turnProgress);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
                --this.turnProgress;
                this.setPosition(d6, d7, d1);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else
            {
                this.setPosition(this.posX, this.posY, this.posZ);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
        }
        else
        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.motionY -= 0.03999999910593033D;
            int l = MathHelper.floor_double(this.posX);
            i = MathHelper.floor_double(this.posY);
            int i1 = MathHelper.floor_double(this.posZ);

            if (BlockRailBase.func_150049_b_(this.worldObj, l, i - 1, i1))
            {
                --i;
            }

            double d0 = 0.4D;
            double d2 = 0.0078125D;
            Block block = this.worldObj.getBlock(l, i, i1);

            if (canUseRail() && BlockRailBase.func_150051_a(block))
            {
                float railMaxSpeed = ((BlockRailBase)block).getRailMaxSpeed(worldObj, this, l, i, i1);
                double maxSpeed = Math.min(railMaxSpeed, getCurrentCartSpeedCapOnRail());
                this.func_145821_a(l, i, i1, maxSpeed, getSlopeAdjustment(), block, ((BlockRailBase)block).getBasicRailMetadata(worldObj, this, l, i, i1));

                if (block == Blocks.activator_rail)
                {
                    this.onActivatorRailPass(l, i, i1, (worldObj.getBlockMetadata(l, i, i1) & 8) != 0);
                }
            }
            else
            {
                this.func_94088_b(onGround ? d0 : getMaxSpeedAirLateral());
            }

            this.func_145775_I();
            this.rotationPitch = 0.0F;
            double d8 = this.prevPosX - this.posX;
            double d4 = this.prevPosZ - this.posZ;

            if (d8 * d8 + d4 * d4 > 0.001D)
            {
                this.rotationYaw = (float)(Math.atan2(d4, d8) * 180.0D / Math.PI);

                if (this.isInReverse)
                {
                    this.rotationYaw += 180.0F;
                }
            }

            double d5 = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);

            if (d5 < -170.0D || d5 >= 170.0D)
            {
                this.rotationYaw += 180.0F;
                this.isInReverse = !this.isInReverse;
            }

            this.setRotation(this.rotationYaw, this.rotationPitch);

            AxisAlignedBB box;
            if (getCollisionHandler() != null)
            {
                box = getCollisionHandler().getMinecartCollisionBox(this);
            }
            else
            {
                box = boundingBox.expand(0.2D, 0.0D, 0.2D);
            }

            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box);

            if (list != null && !list.isEmpty())
            {
                for (int k = 0; k < list.size(); ++k)
                {
                    Entity entity = (Entity)list.get(k);

                    if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityMinecart)
                    {
                        entity.applyEntityCollision(this);
                    }
                }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead)
            {
                if (this.riddenByEntity.ridingEntity == this)
                {
                    this.riddenByEntity.ridingEntity = null;
                }

                this.riddenByEntity = null;
            }

            MinecraftForge.EVENT_BUS.post(new MinecartUpdateEvent(this, l, i, i1));
        }
    }

    /**
     * Called every tick the minecart is on an activator rail. Args: x, y, z, is the rail receiving power
     */
    public void onActivatorRailPass(int p_96095_1_, int p_96095_2_, int p_96095_3_, boolean p_96095_4_) {}

    protected void func_94088_b(double p_94088_1_)
    {
        if (this.motionX < -p_94088_1_)
        {
            this.motionX = -p_94088_1_;
        }

        if (this.motionX > p_94088_1_)
        {
            this.motionX = p_94088_1_;
        }

        if (this.motionZ < -p_94088_1_)
        {
            this.motionZ = -p_94088_1_;
        }

        if (this.motionZ > p_94088_1_)
        {
            this.motionZ = p_94088_1_;
        }

        double moveY = motionY;
        if(getMaxSpeedAirVertical() > 0 && motionY > getMaxSpeedAirVertical())
        {
            moveY = getMaxSpeedAirVertical();
            if(Math.abs(motionX) < 0.3f && Math.abs(motionZ) < 0.3f)
            {
                moveY = 0.15f;
                motionY = moveY;
            }
        }

        if (this.onGround)
        {
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        }

        this.moveEntity(this.motionX, moveY, this.motionZ);

        if (!this.onGround)
        {
            this.motionX *= getDragAir();
            this.motionY *= getDragAir();
            this.motionZ *= getDragAir();
        }
    }

    protected void func_145821_a(int p_145821_1_, int p_145821_2_, int p_145821_3_, double p_145821_4_, double p_145821_6_, Block p_145821_8_, int p_145821_9_)
    {
        this.fallDistance = 0.0F;
        Vec3 vec3 = this.func_70489_a(this.posX, this.posY, this.posZ);
        this.posY = (double)p_145821_2_;
        boolean flag = false;
        boolean flag1 = false;

        if (p_145821_8_ == Blocks.golden_rail)
        {
            flag = (worldObj.getBlockMetadata(p_145821_1_, p_145821_2_, p_145821_3_) & 8) != 0;
            flag1 = !flag;
        }

        if (((BlockRailBase)p_145821_8_).isPowered())
        {
            p_145821_9_ &= 7;
        }

        if (p_145821_9_ >= 2 && p_145821_9_ <= 5)
        {
            this.posY = (double)(p_145821_2_ + 1);
        }

        if (p_145821_9_ == 2)
        {
            this.motionX -= p_145821_6_;
        }

        if (p_145821_9_ == 3)
        {
            this.motionX += p_145821_6_;
        }

        if (p_145821_9_ == 4)
        {
            this.motionZ += p_145821_6_;
        }

        if (p_145821_9_ == 5)
        {
            this.motionZ -= p_145821_6_;
        }

        int[][] aint = matrix[p_145821_9_];
        double d2 = (double)(aint[1][0] - aint[0][0]);
        double d3 = (double)(aint[1][2] - aint[0][2]);
        double d4 = Math.sqrt(d2 * d2 + d3 * d3);
        double d5 = this.motionX * d2 + this.motionZ * d3;

        if (d5 < 0.0D)
        {
            d2 = -d2;
            d3 = -d3;
        }

        double d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        if (d6 > 2.0D)
        {
            d6 = 2.0D;
        }

        this.motionX = d6 * d2 / d4;
        this.motionZ = d6 * d3 / d4;
        double d7;
        double d8;
        double d9;
        double d10;

        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase)
        {
            d7 = (double)((EntityLivingBase)this.riddenByEntity).moveForward;

            if (d7 > 0.0D)
            {
                d8 = -Math.sin((double)(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0F));
                d9 = Math.cos((double)(this.riddenByEntity.rotationYaw * (float)Math.PI / 180.0F));
                d10 = this.motionX * this.motionX + this.motionZ * this.motionZ;

                if (d10 < 0.01D)
                {
                    this.motionX += d8 * 0.1D;
                    this.motionZ += d9 * 0.1D;
                    flag1 = false;
                }
            }
        }

        if (flag1 && shouldDoRailFunctions())
        {
            d7 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (d7 < 0.03D)
            {
                this.motionX *= 0.0D;
                this.motionY *= 0.0D;
                this.motionZ *= 0.0D;
            }
            else
            {
                this.motionX *= 0.5D;
                this.motionY *= 0.0D;
                this.motionZ *= 0.5D;
            }
        }

        d7 = 0.0D;
        d8 = (double)p_145821_1_ + 0.5D + (double)aint[0][0] * 0.5D;
        d9 = (double)p_145821_3_ + 0.5D + (double)aint[0][2] * 0.5D;
        d10 = (double)p_145821_1_ + 0.5D + (double)aint[1][0] * 0.5D;
        double d11 = (double)p_145821_3_ + 0.5D + (double)aint[1][2] * 0.5D;
        d2 = d10 - d8;
        d3 = d11 - d9;
        double d12;
        double d13;

        if (d2 == 0.0D)
        {
            this.posX = (double)p_145821_1_ + 0.5D;
            d7 = this.posZ - (double)p_145821_3_;
        }
        else if (d3 == 0.0D)
        {
            this.posZ = (double)p_145821_3_ + 0.5D;
            d7 = this.posX - (double)p_145821_1_;
        }
        else
        {
            d12 = this.posX - d8;
            d13 = this.posZ - d9;
            d7 = (d12 * d2 + d13 * d3) * 2.0D;
        }

        this.posX = d8 + d2 * d7;
        this.posZ = d9 + d3 * d7;
        this.setPosition(this.posX, this.posY + (double)this.yOffset, this.posZ);

        moveMinecartOnRail(p_145821_1_, p_145821_2_, p_145821_3_, p_145821_4_);

        if (aint[0][1] != 0 && MathHelper.floor_double(this.posX) - p_145821_1_ == aint[0][0] && MathHelper.floor_double(this.posZ) - p_145821_3_ == aint[0][2])
        {
            this.setPosition(this.posX, this.posY + (double)aint[0][1], this.posZ);
        }
        else if (aint[1][1] != 0 && MathHelper.floor_double(this.posX) - p_145821_1_ == aint[1][0] && MathHelper.floor_double(this.posZ) - p_145821_3_ == aint[1][2])
        {
            this.setPosition(this.posX, this.posY + (double)aint[1][1], this.posZ);
        }

        this.applyDrag();
        Vec3 vec31 = this.func_70489_a(this.posX, this.posY, this.posZ);

        if (vec31 != null && vec3 != null)
        {
            double d14 = (vec3.yCoord - vec31.yCoord) * 0.05D;
            d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (d6 > 0.0D)
            {
                this.motionX = this.motionX / d6 * (d6 + d14);
                this.motionZ = this.motionZ / d6 * (d6 + d14);
            }

            this.setPosition(this.posX, vec31.yCoord, this.posZ);
        }

        int j1 = MathHelper.floor_double(this.posX);
        int i1 = MathHelper.floor_double(this.posZ);

        if (j1 != p_145821_1_ || i1 != p_145821_3_)
        {
            d6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.motionX = d6 * (double)(j1 - p_145821_1_);
            this.motionZ = d6 * (double)(i1 - p_145821_3_);
        }

        if(shouldDoRailFunctions())
        {
            ((BlockRailBase)p_145821_8_).onMinecartPass(worldObj, this, p_145821_1_, p_145821_2_, p_145821_3_);
        }

        if (flag && shouldDoRailFunctions())
        {
            double d15 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (d15 > 0.01D)
            {
                double d16 = 0.06D;
                this.motionX += this.motionX / d15 * d16;
                this.motionZ += this.motionZ / d15 * d16;
            }
            else if (p_145821_9_ == 1)
            {
                if (this.worldObj.getBlock(p_145821_1_ - 1, p_145821_2_, p_145821_3_).isNormalCube())
                {
                    this.motionX = 0.02D;
                }
                else if (this.worldObj.getBlock(p_145821_1_ + 1, p_145821_2_, p_145821_3_).isNormalCube())
                {
                    this.motionX = -0.02D;
                }
            }
            else if (p_145821_9_ == 0)
            {
                if (this.worldObj.getBlock(p_145821_1_, p_145821_2_, p_145821_3_ - 1).isNormalCube())
                {
                    this.motionZ = 0.02D;
                }
                else if (this.worldObj.getBlock(p_145821_1_, p_145821_2_, p_145821_3_ + 1).isNormalCube())
                {
                    this.motionZ = -0.02D;
                }
            }
        }
    }

    protected void applyDrag()
    {
        if (this.riddenByEntity != null)
        {
            this.motionX *= 0.996999979019165D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.996999979019165D;
        }
        else
        {
            this.motionX *= 0.9599999785423279D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9599999785423279D;
        }
    }

    @SideOnly(Side.CLIENT)
    public Vec3 func_70495_a(double p_70495_1_, double p_70495_3_, double p_70495_5_, double p_70495_7_)
    {
        int i = MathHelper.floor_double(p_70495_1_);
        int j = MathHelper.floor_double(p_70495_3_);
        int k = MathHelper.floor_double(p_70495_5_);

        if (BlockRailBase.func_150049_b_(this.worldObj, i, j - 1, k))
        {
            --j;
        }

        Block block = this.worldObj.getBlock(i, j, k);

        if (!BlockRailBase.func_150051_a(block))
        {
            return null;
        }
        else
        {
            int l = ((BlockRailBase)block).getBasicRailMetadata(worldObj, this, i, j, k);

            p_70495_3_ = (double)j;

            if (l >= 2 && l <= 5)
            {
                p_70495_3_ = (double)(j + 1);
            }

            int[][] aint = matrix[l];
            double d4 = (double)(aint[1][0] - aint[0][0]);
            double d5 = (double)(aint[1][2] - aint[0][2]);
            double d6 = Math.sqrt(d4 * d4 + d5 * d5);
            d4 /= d6;
            d5 /= d6;
            p_70495_1_ += d4 * p_70495_7_;
            p_70495_5_ += d5 * p_70495_7_;

            if (aint[0][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[0][0] && MathHelper.floor_double(p_70495_5_) - k == aint[0][2])
            {
                p_70495_3_ += (double)aint[0][1];
            }
            else if (aint[1][1] != 0 && MathHelper.floor_double(p_70495_1_) - i == aint[1][0] && MathHelper.floor_double(p_70495_5_) - k == aint[1][2])
            {
                p_70495_3_ += (double)aint[1][1];
            }

            return this.func_70489_a(p_70495_1_, p_70495_3_, p_70495_5_);
        }
    }

    public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_)
    {
        int i = MathHelper.floor_double(p_70489_1_);
        int j = MathHelper.floor_double(p_70489_3_);
        int k = MathHelper.floor_double(p_70489_5_);

        if (BlockRailBase.func_150049_b_(this.worldObj, i, j - 1, k))
        {
            --j;
        }

        Block block = this.worldObj.getBlock(i, j, k);

        if (BlockRailBase.func_150051_a(block))
        {
            int l = ((BlockRailBase)block).getBasicRailMetadata(worldObj, this, i, j, k);
            p_70489_3_ = (double)j;

            if (l >= 2 && l <= 5)
            {
                p_70489_3_ = (double)(j + 1);
            }

            int[][] aint = matrix[l];
            double d3 = 0.0D;
            double d4 = (double)i + 0.5D + (double)aint[0][0] * 0.5D;
            double d5 = (double)j + 0.5D + (double)aint[0][1] * 0.5D;
            double d6 = (double)k + 0.5D + (double)aint[0][2] * 0.5D;
            double d7 = (double)i + 0.5D + (double)aint[1][0] * 0.5D;
            double d8 = (double)j + 0.5D + (double)aint[1][1] * 0.5D;
            double d9 = (double)k + 0.5D + (double)aint[1][2] * 0.5D;
            double d10 = d7 - d4;
            double d11 = (d8 - d5) * 2.0D;
            double d12 = d9 - d6;

            if (d10 == 0.0D)
            {
                p_70489_1_ = (double)i + 0.5D;
                d3 = p_70489_5_ - (double)k;
            }
            else if (d12 == 0.0D)
            {
                p_70489_5_ = (double)k + 0.5D;
                d3 = p_70489_1_ - (double)i;
            }
            else
            {
                double d13 = p_70489_1_ - d4;
                double d14 = p_70489_5_ - d6;
                d3 = (d13 * d10 + d14 * d12) * 2.0D;
            }

            p_70489_1_ = d4 + d10 * d3;
            p_70489_3_ = d5 + d11 * d3;
            p_70489_5_ = d6 + d12 * d3;

            if (d11 < 0.0D)
            {
                ++p_70489_3_;
            }

            if (d11 > 0.0D)
            {
                p_70489_3_ += 0.5D;
            }

            return Vec3.createVectorHelper(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        else
        {
            return null;
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        if (tagCompund.getBoolean("CustomDisplayTile"))
        {
            this.func_145819_k(tagCompund.getInteger("DisplayTile"));
            this.setDisplayTileData(tagCompund.getInteger("DisplayData"));
            this.setDisplayTileOffset(tagCompund.getInteger("DisplayOffset"));
        }

        if (tagCompund.hasKey("CustomName", 8) && tagCompund.getString("CustomName").length() > 0)
        {
            this.entityName = tagCompund.getString("CustomName");
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        if (this.hasDisplayTile())
        {
            tagCompound.setBoolean("CustomDisplayTile", true);
            tagCompound.setInteger("DisplayTile", this.func_145820_n().getMaterial() == Material.air ? 0 : Block.getIdFromBlock(this.func_145820_n()));
            tagCompound.setInteger("DisplayData", this.getDisplayTileData());
            tagCompound.setInteger("DisplayOffset", this.getDisplayTileOffset());
        }

        if (this.entityName != null && this.entityName.length() > 0)
        {
            tagCompound.setString("CustomName", this.entityName);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entityIn)
    {
        MinecraftForge.EVENT_BUS.post(new MinecartCollisionEvent(this, entityIn));
        if (getCollisionHandler() != null)
        {
            getCollisionHandler().onEntityCollision(this, entityIn);
            return;
        }
        if (!this.worldObj.isRemote)
        {
            if (entityIn != this.riddenByEntity)
            {
                if (entityIn instanceof EntityLivingBase && !(entityIn instanceof EntityPlayer) && !(entityIn instanceof EntityIronGolem) && canBeRidden()               && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.01D && this.riddenByEntity == null && entityIn.ridingEntity == null)
                {
                    entityIn.mountEntity(this);
                }

                double d0 = entityIn.posX - this.posX;
                double d1 = entityIn.posZ - this.posZ;
                double d2 = d0 * d0 + d1 * d1;

                if (d2 >= 9.999999747378752E-5D)
                {
                    d2 = (double)MathHelper.sqrt_double(d2);
                    d0 /= d2;
                    d1 /= d2;
                    double d3 = 1.0D / d2;

                    if (d3 > 1.0D)
                    {
                        d3 = 1.0D;
                    }

                    d0 *= d3;
                    d1 *= d3;
                    d0 *= 0.10000000149011612D;
                    d1 *= 0.10000000149011612D;
                    d0 *= (double)(1.0F - this.entityCollisionReduction);
                    d1 *= (double)(1.0F - this.entityCollisionReduction);
                    d0 *= 0.5D;
                    d1 *= 0.5D;

                    if (entityIn instanceof EntityMinecart)
                    {
                        double d4 = entityIn.posX - this.posX;
                        double d5 = entityIn.posZ - this.posZ;
                        Vec3 vec3 = Vec3.createVectorHelper(d4, 0.0D, d5).normalize();
                        Vec3 vec31 = Vec3.createVectorHelper((double)MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F), 0.0D, (double)MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F)).normalize();
                        double d6 = Math.abs(vec3.dotProduct(vec31));

                        if (d6 < 0.800000011920929D)
                        {
                            return;
                        }

                        double d7 = entityIn.motionX + this.motionX;
                        double d8 = entityIn.motionZ + this.motionZ;

                        if (((EntityMinecart)entityIn).isPoweredCart() && !isPoweredCart())
                        {
                            this.motionX *= 0.20000000298023224D;
                            this.motionZ *= 0.20000000298023224D;
                            this.addVelocity(entityIn.motionX - d0, 0.0D, entityIn.motionZ - d1);
                            entityIn.motionX *= 0.949999988079071D;
                            entityIn.motionZ *= 0.949999988079071D;
                        }
                        else if (((EntityMinecart)entityIn).isPoweredCart() && !isPoweredCart())
                        {
                            entityIn.motionX *= 0.20000000298023224D;
                            entityIn.motionZ *= 0.20000000298023224D;
                            entityIn.addVelocity(this.motionX + d0, 0.0D, this.motionZ + d1);
                            this.motionX *= 0.949999988079071D;
                            this.motionZ *= 0.949999988079071D;
                        }
                        else
                        {
                            d7 /= 2.0D;
                            d8 /= 2.0D;
                            this.motionX *= 0.20000000298023224D;
                            this.motionZ *= 0.20000000298023224D;
                            this.addVelocity(d7 - d0, 0.0D, d8 - d1);
                            entityIn.motionX *= 0.20000000298023224D;
                            entityIn.motionZ *= 0.20000000298023224D;
                            entityIn.addVelocity(d7 + d0, 0.0D, d8 + d1);
                        }
                    }
                    else
                    {
                        this.addVelocity(-d0, 0.0D, -d1);
                        entityIn.addVelocity(d0 / 4.0D, 0.0D, d1 / 4.0D);
                    }
                }
            }
        }
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int rotationIncrements)
    {
        this.minecartX = x;
        this.minecartY = y;
        this.minecartZ = z;
        this.minecartYaw = (double)yaw;
        this.minecartPitch = (double)pitch;
        this.turnProgress = rotationIncrements + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    /**
     * Sets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public void setDamage(float p_70492_1_)
    {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70492_1_));
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
    }

    /**
     * Gets the current amount of damage the minecart has taken. Decreases over time. The cart breaks when this is over
     * 40.
     */
    public float getDamage()
    {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    /**
     * Sets the rolling amplitude the cart rolls while being attacked.
     */
    public void setRollingAmplitude(int p_70497_1_)
    {
        this.dataWatcher.updateObject(17, Integer.valueOf(p_70497_1_));
    }

    /**
     * Gets the rolling amplitude the cart rolls while being attacked.
     */
    public int getRollingAmplitude()
    {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public void setRollingDirection(int p_70494_1_)
    {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_70494_1_));
    }

    /**
     * Gets the rolling direction the cart rolls while being attacked. Can be 1 or -1.
     */
    public int getRollingDirection()
    {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public abstract int getMinecartType();

    public Block func_145820_n()
    {
        if (!this.hasDisplayTile())
        {
            return this.func_145817_o();
        }
        else
        {
            int i = this.getDataWatcher().getWatchableObjectInt(20) & 65535;
            return Block.getBlockById(i);
        }
    }

    public Block func_145817_o()
    {
        return Blocks.air;
    }

    public int getDisplayTileData()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTileData() : this.getDataWatcher().getWatchableObjectInt(20) >> 16;
    }

    public int getDefaultDisplayTileData()
    {
        return 0;
    }

    public int getDisplayTileOffset()
    {
        return !this.hasDisplayTile() ? this.getDefaultDisplayTileOffset() : this.getDataWatcher().getWatchableObjectInt(21);
    }

    public int getDefaultDisplayTileOffset()
    {
        return 6;
    }

    public void func_145819_k(int p_145819_1_)
    {
        this.getDataWatcher().updateObject(20, Integer.valueOf(p_145819_1_ & 65535 | this.getDisplayTileData() << 16));
        this.setHasDisplayTile(true);
    }

    public void setDisplayTileData(int p_94092_1_)
    {
        this.getDataWatcher().updateObject(20, Integer.valueOf(Block.getIdFromBlock(this.func_145820_n()) & 65535 | p_94092_1_ << 16));
        this.setHasDisplayTile(true);
    }

    public void setDisplayTileOffset(int p_94086_1_)
    {
        this.getDataWatcher().updateObject(21, Integer.valueOf(p_94086_1_));
        this.setHasDisplayTile(true);
    }

    public boolean hasDisplayTile()
    {
        return this.getDataWatcher().getWatchableObjectByte(22) == 1;
    }

    public void setHasDisplayTile(boolean p_94096_1_)
    {
        this.getDataWatcher().updateObject(22, Byte.valueOf((byte)(p_94096_1_ ? 1 : 0)));
    }

    /**
     * Sets the minecart's name.
     */
    public void setMinecartName(String p_96094_1_)
    {
        this.entityName = p_96094_1_;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return this.entityName != null ? this.entityName : super.getCommandSenderName();
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName()
    {
        return this.entityName != null;
    }

    public String func_95999_t()
    {
        return this.entityName;
    }
    /* =================================== FORGE START ===========================================*/
    /**
     * Moved to allow overrides.
     * This code handles minecart movement and speed capping when on a rail.
     */
    public void moveMinecartOnRail(int x, int y, int z, double par4){
        double d12 = this.motionX;
        double d13 = this.motionZ;

        if (this.riddenByEntity != null)
        {
            d12 *= 0.75D;
            d13 *= 0.75D;
        }

        if (d12 < -par4)
        {
            d12 = -par4;
        }

        if (d12 > par4)
        {
            d12 = par4;
        }

        if (d13 < -par4)
        {
            d13 = -par4;
        }

        if (d13 > par4)
        {
            d13 = par4;
        }

        this.moveEntity(d12, 0.0D, d13);
    }

    /**
     * Gets the current global Minecart Collision handler if none
     * is registered, returns null
     * @return The collision handler or null
     */
    public static IMinecartCollisionHandler getCollisionHandler()
    {
        return collisionHandler;
    }

    /**
     * Sets the global Minecart Collision handler, overwrites any
     * that is currently set.
     * @param handler The new handler
     */
    public static void setCollisionHandler(IMinecartCollisionHandler handler)
    {
        collisionHandler = handler;
    }

    /**
     * This function returns an ItemStack that represents this cart.
     * This should be an ItemStack that can be used by the player to place the cart,
     * but is not necessary the item the cart drops when destroyed.
     * @return An ItemStack that can be used to place the cart.
     */
    public ItemStack getCartItem()
    {
        if (this instanceof EntityMinecartFurnace)
        {
            return new ItemStack(Items.furnace_minecart);
        }
        else if (this instanceof EntityMinecartChest)
        {
            return new ItemStack(Items.chest_minecart);
        }
        else if (this instanceof EntityMinecartTNT)
        {
            return new ItemStack(Items.tnt_minecart);
        }
        else if (this instanceof EntityMinecartHopper)
        {
            return new ItemStack(Items.hopper_minecart);
        }
        else if (this instanceof EntityMinecartCommandBlock)
        {
            return new ItemStack(Items.command_block_minecart);
        }
        return new ItemStack(Items.minecart);
    }

    /**
     * Returns true if this cart can currently use rails.
     * This function is mainly used to gracefully detach a minecart from a rail.
     * @return True if the minecart can use rails.
     */
    public boolean canUseRail()
    {
        return canUseRail;
    }

    /**
     * Set whether the minecart can use rails.
     * This function is mainly used to gracefully detach a minecart from a rail.
     * @param use Whether the minecart can currently use rails.
     */
    public void setCanUseRail(boolean use)
    {
        canUseRail = use;
    }

    /**
     * Return false if this cart should not call onMinecartPass() and should ignore Powered Rails.
     * @return True if this cart should call onMinecartPass().
     */
    public boolean shouldDoRailFunctions()
    {
        return true;
    }

    /**
     * Returns true if this cart is self propelled.
     * @return True if powered.
     */
    public boolean isPoweredCart()
    {
        return getMinecartType()== 2;
    }

    /**
     * Returns true if this cart can be ridden by an Entity.
     * @return True if this cart can be ridden.
     */
    public boolean canBeRidden()
    {
        if(this instanceof EntityMinecartEmpty)
        {
            return true;
        }
        return false;
    }

    /**
     * Getters/setters for physics variables
     */

    /**
     * Returns the carts max speed when traveling on rails. Carts going faster
     * than 1.1 cause issues with chunk loading. Carts cant traverse slopes or
     * corners at greater than 0.5 - 0.6. This value is compared with the rails
     * max speed and the carts current speed cap to determine the carts current
     * max speed. A normal rail's max speed is 0.4.
     *
     * @return Carts max speed.
     */
    public float getMaxCartSpeedOnRail()
    {
        return 1.2f;
    }

    /**
     * Returns the current speed cap for the cart when traveling on rails. This
     * functions differs from getMaxCartSpeedOnRail() in that it controls
     * current movement and cannot be overridden. The value however can never be
     * higher than getMaxCartSpeedOnRail().
     *
     * @return
     */
    public final float getCurrentCartSpeedCapOnRail()
    {
        return currentSpeedRail;
    }

    public final void setCurrentCartSpeedCapOnRail(float value)
    {
        value = Math.min(value, getMaxCartSpeedOnRail());
        currentSpeedRail = value;
    }

    public float getMaxSpeedAirLateral()
    {
        return maxSpeedAirLateral;
    }

    public void setMaxSpeedAirLateral(float value)
    {
        maxSpeedAirLateral = value;
    }

    public float getMaxSpeedAirVertical()
    {
        return maxSpeedAirVertical;
    }

    public void setMaxSpeedAirVertical(float value)
    {
        maxSpeedAirVertical = value;
    }

    public double getDragAir()
    {
        return dragAir;
    }

    public void setDragAir(double value)
    {
        dragAir = value;
    }

    public double getSlopeAdjustment()
    {
        return 0.0078125D;
    }
    /* =================================== FORGE END ===========================================*/
}