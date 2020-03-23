package ftblag.lagslib.tags;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import ftblag.lagslib.events.CustomTagsEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TagsBuilder {

    protected final NetworkTagManager manager;

    private TagsBuilder(NetworkTagManager manager) {
        this.manager = manager;
    }

    public static TagsBuilder of(CustomTagsEvent event) {
        return new TagsBuilder(event.getTagManager());
    }

    public static TagsBuilder of(NetworkTagManager manager) {
        return new TagsBuilder(manager);
    }

    // ------------------------------------------------------------------------------------------

    public BaseTagsBuilder<Block> nextBlock(@Nonnull String modid, @Nonnull String name) {
        return new BaseTagsBuilder<>(manager.getBlocks().getTagMap(), new ResourceLocation(modid, name));
    }

    public BaseTagsBuilder<Item> nextItem(@Nonnull String modid, @Nonnull String name) {
        return new BaseTagsBuilder<>(manager.getItems().getTagMap(), new ResourceLocation(modid, name));
    }

    public BaseTagsBuilder<Fluid> nextFluid(@Nonnull String modid, @Nonnull String name) {
        return new BaseTagsBuilder<>(manager.getFluids().getTagMap(), new ResourceLocation(modid, name));
    }

    public BaseTagsBuilder<EntityType<?>> nextEntityType(@Nonnull String modid, @Nonnull String name) {
        return new BaseTagsBuilder<>(manager.getEntityTypes().getTagMap(), new ResourceLocation(modid, name));
    }

    public class BaseTagsBuilder<T> {

        protected final Tag<T> tag;

        public BaseTagsBuilder(Map<ResourceLocation, Tag<T>> map, ResourceLocation name) {
            if (!map.containsKey(name)) {
                map.put(name, new Tag<>(name, Sets.newLinkedHashSet(), false));
            }
            tag = map.get(name);
        }

        public BaseTagsBuilder<T> add(Tag.ITagEntry<T> entry) {
            tag.getEntries().add(entry);
            entry.populate(tag.getAllElements());
            return this;
        }

        public BaseTagsBuilder<T> add(T itemIn) {
            add(new Tag.ListEntry<>(Collections.singleton(itemIn)));
            return this;
        }

        @SafeVarargs
        public final BaseTagsBuilder<T> add(T... itemsIn) {
            add(new Tag.ListEntry<>(Lists.newArrayList(itemsIn)));
            return this;
        }

        public BaseTagsBuilder<T> add(Tag<T> tagIn) {
            add(new Tag.TagEntry<>(tagIn));
            return this;
        }

        @SafeVarargs
        public final BaseTagsBuilder<T> add(Tag<T>... tags) {
            for (Tag<T> tag : tags)
                add(tag);
            return this;
        }

        public BaseTagsBuilder<T> remove(Tag.ITagEntry<T> entry) {
            tag.getEntries().remove(entry);
            Set<T> set = Sets.newLinkedHashSet();
            entry.populate(set);
            tag.getAllElements().removeAll(set);
            return this;
        }

        public TagsBuilder done() {
            return TagsBuilder.this;
        }
    }
}
