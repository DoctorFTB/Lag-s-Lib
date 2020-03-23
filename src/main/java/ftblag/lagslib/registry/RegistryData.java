package ftblag.lagslib.registry;

import com.google.common.collect.Lists;
import com.mojang.datafixers.types.Type;
import ftblag.lagslib.LagsLib;
import ftblag.lagslib.events.LagsLibModEventHandler;
import ftblag.lagslib.interfaces.IScreenFactoryCommon;
import net.minecraft.block.Block;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.IContainerFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class RegistryData {

    private final List<ContainerType<?>> CONTAINERS = Lists.newArrayList();
    private final List<TileEntityType<?>> TILES = Lists.newArrayList();
    private final List<EntityType<?>> ENTITES = Lists.newArrayList();
    private final String modid;

    public RegistryData(String modid) {
        this.modid = modid;
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addGenericListener(ContainerType.class, this::onContainerRegistry);
        modBus.addGenericListener(TileEntityType.class, this::onTileRegistry);
        modBus.addGenericListener(EntityType.class, this::onEntityRegistry);
    }

    private void onContainerRegistry(RegistryEvent.Register<ContainerType<?>> event) {
        CONTAINERS.forEach(element -> {
            event.getRegistry().register(element);
            LagsLib.LOGGER.info("[Lag's Lib] Register {} container type for {}", element.getRegistryName(), modid);
        });
        LagsLib.LOGGER.info("[Lag's Lib] Finally registered {} container types for {}", CONTAINERS.size(), modid);
        CONTAINERS.clear();
    }

    private void onTileRegistry(RegistryEvent.Register<TileEntityType<?>> event) {
        TILES.forEach(element -> {
            event.getRegistry().register(element);
            LagsLib.LOGGER.info("[Lag's Lib] Register {} tile type for {}", element.getRegistryName(), modid);
        });
        LagsLib.LOGGER.info("[Lag's Lib] Finally registered {} tile types for {}", TILES.size(), modid);
        TILES.clear();
    }

    private void onEntityRegistry(RegistryEvent.Register<EntityType<?>> event) {
        ENTITES.forEach(element -> {
            event.getRegistry().register(element);
            LagsLib.LOGGER.info("[Lag's Lib] Register {} entity type for {}", element.getRegistryName(), modid);
        });
        LagsLib.LOGGER.info("[Lag's Lib] Finally registered {} entity types for {}", ENTITES.size(), modid);
        ENTITES.clear();
    }

    public <T extends Container, U extends Screen & IHasContainer<T>> ContainerTypeBuilder<T, U> container(IContainerFactory<T> factory) {
        return new ContainerTypeBuilder<>(factory);
    }

    public <T extends TileEntity> TileEntityTypeBuilder<T> tile(Supplier<? extends T> factory, Block... valid) {
        return new TileEntityTypeBuilder<>(factory, valid);
    }

    public <T extends Entity> EntityTypeBuilder<T> entity(EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
        return new EntityTypeBuilder<>(factory, classification, width, height);
    }

    public class ContainerTypeBuilder<T extends Container, U extends Screen & IHasContainer<T>> {

        private ContainerType<T> type;
        private Supplier<IScreenFactoryCommon<T, U>> supplier;

        public ContainerTypeBuilder(IContainerFactory<T> factory) {
            type = IForgeContainerType.create(factory);
        }

        public ContainerTypeBuilder<T, U> gui(Supplier<IScreenFactoryCommon<T, U>> supplier) {
            if (EffectiveSide.get().isClient()) {
                this.supplier = supplier;
            }
            return this;
        }

        public ContainerType<T> build(String name) {
            type.setRegistryName(modid, name);
            CONTAINERS.add(type);
            if (EffectiveSide.get().isClient() && supplier != null) {
                ScreenManager.registerFactory(type, supplier.get()::create);
            }
            return type;
        }
    }

    public class TileEntityTypeBuilder<T extends TileEntity> {

        private TileEntityType.Builder<T> builder;
        private Type<?> datafixerType;
        private Supplier<Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super T>>> supplier;

        public TileEntityTypeBuilder(Supplier<? extends T> factory, Block... valid) {
            builder = TileEntityType.Builder.create(factory, valid);
        }

        public TileEntityTypeBuilder<T> dataFixer(Type<?> datafixerType) {
            this.datafixerType = datafixerType;
            return this;
        }

        public TileEntityTypeBuilder<T> render(Supplier<Function<? super TileEntityRendererDispatcher, ? extends TileEntityRenderer<? super T>>> supplier) {
            if (EffectiveSide.get().isClient()) {
                this.supplier = supplier;
            }
            return this;
        }

        public TileEntityType<T> build(String name) {
            TileEntityType<T> type = builder.build(datafixerType);
            type.setRegistryName(modid, name);
            if (this.supplier != null) {
                ClientRegistry.bindTileEntityRenderer(type, supplier.get());
            }
            TILES.add(type);
            return type;
        }
    }

    public class EntityTypeBuilder<T extends Entity> {

        private EntityType.Builder<T> builder;
        private boolean hasEgg;
        private int eggPrimary, eggSecondary;
        private ResourceLocation lootTable;
        private Supplier<IRenderFactory<T>> supplier;

        public EntityTypeBuilder(EntityType.IFactory<T> factory, EntityClassification classification, float width, float height) {
            builder = EntityType.Builder.create(factory, classification).size(width, height);
        }

        public EntityTypeBuilder<T> track(int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
            builder.setTrackingRange(trackingRange)
                    .setUpdateInterval(updateFrequency)
                    .setShouldReceiveVelocityUpdates(sendsVelocityUpdates);
            return this;
        }

        public EntityTypeBuilder<T> consumer(Consumer<EntityType.Builder<T>> consumer) {
            consumer.accept(builder);
            return this;
        }

        public EntityTypeBuilder<T> render(Supplier<IRenderFactory<T>> supplier) {
            if (EffectiveSide.get().isClient()) {
                this.supplier = supplier;
            }
            return this;
        }

        public EntityTypeBuilder<T> egg(int eggPrimary, int eggSecondary) {
            hasEgg = true;
            this.eggPrimary = eggPrimary;
            this.eggSecondary = eggSecondary;
            return this;
        }

        public EntityTypeBuilder<T> lootFrom(EntityType<?> type) {
            lootTable = type.getLootTable();
            return this;
        }

        public EntityTypeBuilder<T> lootFrom(ResourceLocation lootTable) {
            this.lootTable = lootTable;
            return this;
        }

        public EntityType<T> build(String name) {
            EntityType<T> type = builder.build(modid + ":" + name);
            type.setRegistryName(modid, name);
            ENTITES.add(type);
            if (hasEgg) {
                SpawnEggItem egg = new SpawnEggItem(type, eggPrimary, eggSecondary, new Item.Properties().group(ItemGroup.MISC));
                egg.setRegistryName(modid, name + "_spawn_egg");
                LagsLibModEventHandler.ITEMS.add(egg);
            }
            if (lootTable != null) {
                type.lootTable = lootTable;
            }
            if (supplier != null) {
                RenderingRegistry.registerEntityRenderingHandler(type, supplier.get());
            }
            return type;
        }
    }
}
