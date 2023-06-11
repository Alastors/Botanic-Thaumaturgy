package botanicthaumaturgy.main;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import botanicthaumaturgy.renderers.blockrenderers.BlockAlphaPedestalRenderer;
import botanicthaumaturgy.renderers.blockrenderers.BlockMatrixAlphaRenderer;
import botanicthaumaturgy.renderers.blockrenderers.BlockPillarAlphaRenderer;
import botanicthaumaturgy.renderers.blockrenderers.RenderBigPool;
import botanicthaumaturgy.renderers.tileentityrenderers.RenderTileBigPool;
import botanicthaumaturgy.renderers.tileentityrenderers.TileAlphaPedestalRenderer;
import botanicthaumaturgy.renderers.tileentityrenderers.TileMatrixAlphaRenderer;
import botanicthaumaturgy.renderers.tileentityrenderers.TilePillarAlphaRenderer;
import botanicthaumaturgy.tileentity.TileBigPool;
import botanicthaumaturgy.tileentity.TileEntityInfusionMatrixAlpha;
import botanicthaumaturgy.tileentity.TileEntityInfusionPillarAlpha;
import botanicthaumaturgy.tileentity.TileEntityPedestalAlpha;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        specialRenderers();
    }

    public void registerTileEntitySpecialRenderer(Class<? extends TileEntity> tile,
            TileEntitySpecialRenderer renderer) {
        ClientRegistry.bindTileEntitySpecialRenderer(tile, renderer);
    }

    public void registerBlockRenderer(ISimpleBlockRenderingHandler renderer) {
        RenderingRegistry.registerBlockHandler(renderer);
    }

    public void specialRenderers() {

        this.registerTileEntitySpecialRenderer(TileEntityInfusionMatrixAlpha.class, new TileMatrixAlphaRenderer(0));
        Config.blockStoneDeviceRI = RenderingRegistry.getNextAvailableRenderId();
        this.registerBlockRenderer(new BlockMatrixAlphaRenderer());

        this.registerTileEntitySpecialRenderer(TileEntityInfusionPillarAlpha.class, new TilePillarAlphaRenderer());
        Config.blockStoneDeviceTwoRI = RenderingRegistry.getNextAvailableRenderId();
        this.registerBlockRenderer(new BlockPillarAlphaRenderer());

        // TODO: FIX THIS
        Config.blockStoneDeviceThreeRI = RenderingRegistry.getNextAvailableRenderId();
        this.registerTileEntitySpecialRenderer(TileEntityPedestalAlpha.class, new TileAlphaPedestalRenderer());
        this.registerBlockRenderer(new BlockAlphaPedestalRenderer());

        Config.blockStoneDeviceFourRI = RenderingRegistry.getNextAvailableRenderId();
        this.registerTileEntitySpecialRenderer(TileBigPool.class, new RenderTileBigPool());
        this.registerBlockRenderer(new RenderBigPool());
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}
