package ftblag.lagslib.events;

import com.google.common.collect.Lists;
import ftblag.lagslib.LagsLib;
import ftblag.lagslib.block.BaseBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = LagsLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LagsLibModEventHandler {

    public static final List<Item> ITEMS = Lists.newArrayList();
    public static final List<BaseBlock> BLOCKS = Lists.newArrayList();

    @SubscribeEvent
    public static void onBlockRegistry(RegistryEvent.Register<Block> event) {
        BLOCKS.forEach(element -> {
            event.getRegistry().register(element);
            LagsLib.LOGGER.info("[Lag's Lib] Register {} block", element.getRegistryName());
        });
        LagsLib.LOGGER.info("[Lag's Lib] Finally registered {} blocks", BLOCKS.size());
    }

    @SubscribeEvent
    public static void onItemRegistry(RegistryEvent.Register<Item> event) {
        ITEMS.forEach(element -> {
            event.getRegistry().register(element);
            LagsLib.LOGGER.info("[Lag's Lib] Register {} items", element.getRegistryName());
        });
        LagsLib.LOGGER.info("[Lag's Lib] Finally registered {} item", ITEMS.size());

        BLOCKS.stream().map(BaseBlock::getBlockItem).forEach(element -> {
            event.getRegistry().register(element);
            LagsLib.LOGGER.info("[Lag's Lib] Register {} item block", element.getRegistryName());
        });
        LagsLib.LOGGER.info("[Lag's Lib] Finally registered {} item blocks", BLOCKS.size());

        ITEMS.clear();
        BLOCKS.clear();
    }
}
