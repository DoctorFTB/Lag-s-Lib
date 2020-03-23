package ftblag.lagslib.item;

import ftblag.lagslib.events.LagsLibModEventHandler;
import ftblag.lagslib.tooltip.ToolTipBuilder;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BaseItem extends Item {

    public BaseItem(String modid, String name, Properties properties) {
        this(new ResourceLocation(modid, name), properties);
    }

    public BaseItem(ResourceLocation name, Properties properties) {
        super(properties);
        setRegistryName(name);
        if (autoRegistry()) {
            LagsLibModEventHandler.ITEMS.add(this);
        }
    }

    public boolean autoRegistry() {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addInformation(stack, ToolTipBuilder.of(tooltip), flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, ToolTipBuilder tooltip, ITooltipFlag flagIn) {
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        return onItemUse(context.getPlayer(), context.getWorld(), context.getPos(), context.getHand(), context.getFace());
    }

    public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction facing) {
        return ActionResultType.PASS;
    }
}
