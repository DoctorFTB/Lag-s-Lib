package ftblag.lagslib.interfaces;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

/**
 * Copy from ScreenManager#IScreenFactory without OnlyIn annotation
 */
public interface IScreenFactoryCommon<T extends Container, U extends Screen & IHasContainer<T>> {
    U create(T container, PlayerInventory playerInventory, ITextComponent title);
}
