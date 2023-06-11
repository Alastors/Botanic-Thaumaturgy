/**
 * This class was created by <Vazkii>. It's distributed as part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 26, 2014, 12:25:06 AM (GMT)]
 */
package botanicthaumaturgy.renderers.blockrenderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import botanicthaumaturgy.main.Config;
import botanicthaumaturgy.renderers.tileentityrenderers.RenderTileBigPool;
import botanicthaumaturgy.tileentity.TileBigPool;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBigPool implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        RenderTileBigPool.forceMeta = metadata;
        RenderTileBigPool.forceMana = RenderTileBigPool.forceMana | metadata == 1;
        TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileBigPool(), 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
            RenderBlocks renderer) {
        return false;
    }

    @Override
    public int getRenderId() {
        return Config.blockStoneDeviceFourRI;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

}
