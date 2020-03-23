package ftblag.lagslib.block;

import ftblag.lagslib.events.LagsLibModEventHandler;
import ftblag.lagslib.interfaces.Function4x;
import ftblag.lagslib.tooltip.ToolTipBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class BaseBlock extends Block {

    protected ItemGroup group;

    public BaseBlock(String modid, String name, ItemGroup group, Properties properties) {
        super(properties);
        setRegistryName(modid, name);
        this.group = group;
        if (autoRegistry()) {
            LagsLibModEventHandler.BLOCKS.add(this);
        }
    }

    public BaseBlock(ResourceLocation name, ItemGroup group, Properties properties) {
        super(properties);
        setRegistryName(name);
        LagsLibModEventHandler.BLOCKS.add(this);
        this.group = group;
    }

    public boolean autoRegistry() {
        return true;
    }

    public BlockItem getBlockItem() {
        BlockItem item = new BlockItem(this, new Item.Properties().group(group));
        item.setRegistryName(getRegistryName());
        return item;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        addInformation(stack, ToolTipBuilder.of(tooltip), flagIn);
    }

    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, ToolTipBuilder tooltip, ITooltipFlag flagIn) {
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            breakBlock(worldIn, pos);
            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    public void breakBlock(World world, BlockPos pos) {

    }

    public void openGui(PlayerEntity player, Function4x<Integer, PlayerInventory, BlockPos, Container> supplier, BlockPos pos) {
        NetworkHooks.openGui((ServerPlayerEntity) player, new SimpleNamedContainerProvider((id, playerInv, p) -> supplier.apply(id, playerInv, pos), new StringTextComponent("inventory")), pos);
    }

    public void openGui(PlayerEntity player, Function4x<Integer, PlayerInventory, BlockPos, Container> supplier, BlockPos pos, Consumer<PacketBuffer> consumer) {
        NetworkHooks.openGui((ServerPlayerEntity) player, new SimpleNamedContainerProvider((id, playerInv, p) -> supplier.apply(id, playerInv, pos), new StringTextComponent("inventory")), consumer.andThen((buf) -> buf.writeBlockPos(pos)));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        NonNullList<ItemStack> ret = NonNullList.create();
        Random rand = builder.getWorld().rand;
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, builder.get(LootParameters.TOOL));

        int count = quantityDropped(state, fortune, rand);
        for (int i = 0; i < count; i++) {
            Item item = this.getItemDropped(state, rand, fortune);
            if (item != Items.AIR) {
                ret.add(new ItemStack(item, 1));
            }
        }
        return ret;
    }

    public int quantityDropped(Random random) {
        return 1;
    }

    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    public int quantityDroppedWithBonus(int fortune, Random random) {
        return this.quantityDropped(random);
    }

    public int quantityDropped(BlockState state, int fortune, Random random) {
        return quantityDroppedWithBonus(fortune, random);
    }
}
