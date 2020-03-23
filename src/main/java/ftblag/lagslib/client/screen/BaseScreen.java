package ftblag.lagslib.client.screen;

import ftblag.lagslib.container.BaseContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class BaseScreen<C extends BaseContainer> extends ContainerScreen<C> {

    protected final ResourceLocation gui;

    public BaseScreen(C container, PlayerInventory playerInv, ITextComponent text, String modid, String name, int xSize, int ySize) {
        this(container, playerInv, text, new ResourceLocation(modid, name), xSize, ySize);
    }

    public BaseScreen(C container, PlayerInventory playerInv, ITextComponent text, ResourceLocation gui, int xSize, int ySize) {
        super(container, playerInv, text);
        this.gui = gui;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public void render(int mouseX, int mouseY, float f) {
        renderBackground();
        super.render(mouseX, mouseY, f);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bindTexture(gui);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
