package ftblag.lagslib.tooltip;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.KeybindTextComponent;
import net.minecraft.util.text.NBTTextComponent;
import net.minecraft.util.text.ScoreTextComponent;
import net.minecraft.util.text.SelectorTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class ToolTipBuilder {

    private final List<ITextComponent> tooltip;

    private ToolTipBuilder(List<ITextComponent> tooltip) {
        this.tooltip = tooltip;
    }

    public static ToolTipBuilder of(List<ITextComponent> tooltip) {
        return new ToolTipBuilder(tooltip);
    }

    public List<ITextComponent> getTooltip() {
        return tooltip;
    }

    public TranslationTextComponent translate(String translationKey, Object... args) {
        TranslationTextComponent component = new TranslationTextComponent(translationKey, args);
        tooltip.add(component);
        return component;
    }

    public StringTextComponent string(String msg) {
        StringTextComponent component = new StringTextComponent(msg);
        tooltip.add(component);
        return component;
    }

    public KeybindTextComponent keybind(String keybind) {
        KeybindTextComponent component = new KeybindTextComponent(keybind);
        tooltip.add(component);
        return component;
    }

    public NBTTextComponent.Block nbtBlock(String nbt, boolean interpret, String block) {
        NBTTextComponent.Block component = new NBTTextComponent.Block(nbt, interpret, block);
        tooltip.add(component);
        return component;
    }

    public NBTTextComponent.Entity nbtEntity(String nbt, boolean interpret, String entity) {
        NBTTextComponent.Entity component = new NBTTextComponent.Entity(nbt, interpret, entity);
        tooltip.add(component);
        return component;
    }

    public ScoreTextComponent score(String nameIn, String objectiveIn) {
        ScoreTextComponent component = new ScoreTextComponent(nameIn, objectiveIn);
        tooltip.add(component);
        return component;
    }

    public SelectorTextComponent selector(String selectorIn) {
        SelectorTextComponent component = new SelectorTextComponent(selectorIn);
        tooltip.add(component);
        return component;
    }

    public ITextComponent component(ITextComponent component) {
        tooltip.add(component);
        return component;
    }
}
