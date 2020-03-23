package ftblag.lagslib.interfaces;

import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;

public interface ITileEntityRendererFactory<T extends TileEntity> {
    TileEntityRenderer<T> create(TileEntityRendererDispatcher dispatcher);
}
