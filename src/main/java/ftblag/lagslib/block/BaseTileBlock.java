package ftblag.lagslib.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public abstract class BaseTileBlock extends BaseBlock {
    public BaseTileBlock(String modid, String name, ItemGroup group, Properties properties) {
        super(modid, name, group, properties);
    }

    public BaseTileBlock(ResourceLocation name, ItemGroup group, Properties properties) {
        super(name, group, properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}
