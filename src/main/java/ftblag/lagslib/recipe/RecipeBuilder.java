package ftblag.lagslib.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ftblag.lagslib.LagsLib;
import ftblag.lagslib.interfaces.ICookingFactory;
import ftblag.lagslib.utils.IngredientUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"WeakerAccess", "unused", "unchecked", "UnusedReturnValue"})
public class RecipeBuilder {

    protected String modid;

    protected RecipeBuilder(@Nonnull String modid) {
        this.modid = modid;
        groups.add(MutablePair.of(Maps.newHashMap(), null));
    }

    public static RecipeBuilder of(@Nonnull String modid) {
        return new RecipeBuilder(modid);
    }

    // ------------------------------------------------------------------------------------------

    protected List<CustomRecipe> recipes = new ArrayList<>();

    public void register(List<CustomRecipe> list) {
        list.addAll(recipes);
        recipes.clear();
    }

    // ------------------------------------------------------------------------------------------

    protected List<Pair<Map<Character, Ingredient>, String[]>> groups = new ArrayList<>();

    public RecipeBuilder createGroup() {
        groups.add(MutablePair.of(Maps.newHashMap(getLast().getKey()), getLast().getRight()));
        return this;
    }

    public RecipeBuilder endGroup() {
        if (groups.size() == 1) {
            throw new RuntimeException("Cant remove default group");
        } else {
            groups.remove(groups.size() - 1);
        }
        return this;
    }

    public RecipeBuilder groupKey(@Nonnull Character chr, @Nonnull Object in) {
        Ingredient ing = IngredientUtils.getIngredient(in);
        if (ing == null) {
            invalidRecipe = true;
        }
        if (ing == Ingredient.EMPTY) {
            LagsLib.LOGGER.warn("[Lag's Lib] Not found ingredient for: {}!", in);
        }

        getLast().getLeft().put(chr, ing);
        return this;
    }

    public RecipeBuilder removeGroupKey(@Nonnull Character chr) {
        getLast().getLeft().remove(chr);
        return this;
    }

    public RecipeBuilder groupPattern(@Nonnull String... pattern) {
        getLast().setValue(pattern);
        return this;
    }

    protected Pair<Map<Character, Ingredient>, String[]> getLast() {
        return groups.get(groups.size() - 1);
    }

    // ------------------------------------------------------------------------------------------

    protected Map<String, Pair<Map<Character, Ingredient>, String[]>> namedGroups = new HashMap<>();

    public RecipeBuilder namedGroupKey(@Nonnull String groupName, @Nonnull Character chr, @Nonnull Object in) {
        Ingredient ing = IngredientUtils.getIngredient(in);
        if (ing == null) {
            invalidRecipe = true;
        }
        if (ing == Ingredient.EMPTY) {
            LagsLib.LOGGER.warn("[Lag's Lib] Not found ingredient for: {}!", in);
        }

        getNamedGroup(groupName).getLeft().put(chr, ing);
        return this;
    }

    public RecipeBuilder removeNamedGroupKey(@Nonnull String groupName, @Nonnull Character chr) {
        getNamedGroup(groupName).getLeft().remove(chr);
        return this;
    }

    public RecipeBuilder namedGroupPattern(@Nonnull String groupName, @Nonnull String... pattern) {
        getNamedGroup(groupName).setValue(pattern);
        return this;
    }

    protected Pair<Map<Character, Ingredient>, String[]> getNamedGroup(@Nonnull String groupName) {
        if (!namedGroups.containsKey(groupName)) {
            namedGroups.put(groupName, MutablePair.of(Maps.newHashMap(), null));
        }
        return namedGroups.get(groupName);
    }

    // ------------------------------------------------------------------------------------------

    protected boolean started;
    protected String name;

    protected boolean invalidRecipe;

    protected ResourceLocation getName() {
        if (name == null) {
            throw new RuntimeException("Cant make registryName because name is null");
        }
        return new ResourceLocation(modid, name);
    }

    protected void resetConstants() {
        started = false;
        name = null;
        invalidRecipe = false;
    }

    // ------------------------------------------------------------------------------------------

    public class BaseRecipeBuilder<T extends BaseRecipeBuilder<T>> {
        protected String group = "";
        protected ItemStack output = ItemStack.EMPTY;

        public T group(ResourceLocation group) {
            if (!started) {
                throw new RuntimeException("Cant set group if recipe not started");
            }
            if (group != null) {
                this.group = group.toString();
            }
            return (T) this;
        }

        public T output(@Nonnull IItemProvider provider) {
            return output(new ItemStack(provider));
        }

        public T output(@Nonnull IItemProvider provider, int count) {
            return output(new ItemStack(provider, count));
        }

        public T output(@Nonnull ItemStack stack) {
            if (!started) {
                throw new RuntimeException("Cant set output if recipe not started");
            }
            output = stack.copy();
            return (T) this;
        }
    }

    public class BaseSingleIngredientRecipeBuilder<T extends BaseSingleIngredientRecipeBuilder<T>> extends BaseRecipeBuilder<T> {
        protected List<Ingredient> ingredients = Lists.newArrayList();

        public T ingredient(@Nonnull Object in) {
            if (!started) {
                throw new RuntimeException("Cant add ingredient if recipe not started");
            }
            Ingredient ing = IngredientUtils.getIngredient(in);
            if (ing == null) {
                invalidRecipe = true;
            }
            if (ing == Ingredient.EMPTY) {
                LagsLib.LOGGER.warn("[Lag's Lib] Not found ingredient for: {}!", in);
            }
            ingredients.set(0, ing);
            return (T) this;
        }
    }

    public class BaseIngredientRecipeBuilder<T extends BaseIngredientRecipeBuilder<T>> extends BaseRecipeBuilder<T> {
        protected List<Ingredient> ingredients = Lists.newArrayList();

        public T ingredientList(@Nonnull NonNullList<?> in) {
            for (Object o : in) {
                ingredient(o);
            }
            return (T) this;
        }

        public T ingredients(@Nonnull Object... in) {
            for (Object o : in) {
                ingredient(o);
            }
            return (T) this;
        }

        public T ingredient(@Nonnull Object in) {
            if (!started) {
                throw new RuntimeException("Cant add ingredient if recipe not started");
            }
            Ingredient ing = IngredientUtils.getIngredient(in);
            if (ing == null) {
                invalidRecipe = true;
            }
            if (ing == Ingredient.EMPTY) {
                LagsLib.LOGGER.warn("[Lag's Lib] Not found ingredient for: {}!", in);
            }
            ingredients.add(ing);
            return (T) this;
        }
    }

    // ------------------------------------------------------------------------------------------

    public class ShapedRecipeBuilder extends BaseRecipeBuilder<ShapedRecipeBuilder> {
        protected final Map<Character, Ingredient> keys = Maps.newHashMap();
        protected String[] pattern;
        protected String groupName;

        public ShapedRecipeBuilder useNamedGroup(@Nonnull String groupName) {
            this.groupName = groupName;
            return this;
        }

        public ShapedRecipeBuilder oldStyle(@Nonnull Object... recipe) {
            if (!started) {
                throw new RuntimeException("Cant set output if recipe not started");
            }
            List<String> pattern = Lists.newArrayList();
            int index = 0;
            for (Object obj : recipe) {
                if (obj instanceof String) {
                    pattern.add((String) obj);
                    index++;
                } else {
                    break;
                }
            }

            for (; index < recipe.length; index += 2) {
                key((Character) recipe[index], recipe[index + 1]);
            }

            pattern(pattern.toArray(new String[0]));
            return this;
        }

        public ShapedRecipeBuilder key(@Nonnull Character chr, @Nonnull Object in) {
            if (!started) {
                throw new RuntimeException("Cant add key if recipe not started");
            }
            Ingredient ing = IngredientUtils.getIngredient(in);
            if (ing == null) {
                invalidRecipe = true;
            }
            if (ing == Ingredient.EMPTY) {
                LagsLib.LOGGER.warn("[Lag's Lib] Not found ingredient for: {}!", in);
            }
            keys.put(chr, ing);
            return this;
        }

        public ShapedRecipeBuilder keys(@Nonnull Object... keys) {
            if (!started) {
                throw new RuntimeException("Cant set output if recipe not started");
            }
            for (int index = 0; index < keys.length; index += 2) {
                key((Character) keys[index], keys[index + 1]);
            }
            return this;
        }

        public ShapedRecipeBuilder pattern(@Nonnull String... pattern) {
            if (!started) {
                throw new RuntimeException("Cant set pattern if recipe not started");
            }
            int len = pattern[0].length();
            for (String s : pattern) {
                if (len != s.length()) {
                    throw new RuntimeException("Wrong width for pattern");
                }
            }
            this.pattern = pattern;
            return this;
        }

        public RecipeBuilder build() {
            recipes.add(buildRecipe());
            resetConstants();
            return RecipeBuilder.this;
        }

        public CustomRecipe buildRecipe() {
            if (!started) {
                throw new RuntimeException("Cant build recipe if recipe not started");
            }

            if (pattern == null) {
                if (groupName != null) {
                    if (namedGroups.containsKey(groupName)) {
                        pattern = namedGroups.get(groupName).getRight();
                    } else {
                        throw new RuntimeException("Used group with name \"" + groupName + "\" but group not found!");
                    }
                } else {
                    pattern = getLast().getRight();
                }
                if (pattern == null) {
                    throw new RuntimeException("Pattern not defined for recipe");
                }
            }
            if (output.isEmpty()) {
                throw new RuntimeException("Output not defined for recipe");
            }

            if (invalidRecipe) {
                return CustomRecipe.of(null);
            }

            int width = pattern[0].length();
            int height = pattern.length;

            int size = 0;
            for (String s : pattern) {
                size += s.length();
            }
            if (width * height != size) {
                throw new RuntimeException("Invalid recipe, width * height pattern != pattern size");
            }

            NonNullList<Ingredient> input = NonNullList.withSize(width * height, Ingredient.EMPTY);
            int index = -1;
            for (String s : pattern) {
                for (char chr : s.toCharArray()) {
                    index++;
                    if (chr == ' ') {
                        continue;
                    }
                    Ingredient ing = keys.get(chr);
                    if (ing == null) {
                        if (groupName != null) {
                            if (namedGroups.containsKey(groupName)) {
                                pattern = namedGroups.get(groupName).getRight();
                            } else {
                                throw new RuntimeException("Used group with name \"" + groupName + "\" but group not found!");
                            }
                        } else {
                            ing = getLast().getKey().get(chr);
                        }
                        if (ing == null) {
                            throw new RuntimeException("Symbol '" + chr + "' not defined");
                        }
                    }
                    input.set(index, ing);
                }
            }

            return CustomRecipe.of(new ShapedRecipe(getName(), group, width, height, input, output));
        }
    }

    // ------------------------------------------------------------------------------------------

    public class ShapelessRecipeBuilder extends BaseIngredientRecipeBuilder<ShapelessRecipeBuilder> {

        public RecipeBuilder build() {
            recipes.add(buildRecipe());
            resetConstants();
            return RecipeBuilder.this;
        }

        public CustomRecipe buildRecipe() {
            if (!started) {
                throw new RuntimeException("Cant build recipe if recipe not started");
            }
            if (output.isEmpty()) {
                throw new RuntimeException("Output not defined for recipe");
            }
            if (ingredients.size() == 0) {
                throw new RuntimeException("Ingredients not defined for recipe");
            }

            if (invalidRecipe) {
                return CustomRecipe.of(null);
            }

            return CustomRecipe.of(new ShapelessRecipe(getName(), group, output, NonNullList.from(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0]))));
        }
    }

    // ------------------------------------------------------------------------------------------

    public class StonecuttingRecipeBuilder extends BaseSingleIngredientRecipeBuilder<StonecuttingRecipeBuilder> {

        public RecipeBuilder build() {
            recipes.add(buildRecipe());
            resetConstants();
            return RecipeBuilder.this;
        }

        public CustomRecipe buildRecipe() {
            if (!started) {
                throw new RuntimeException("Cant build recipe if recipe not started");
            }
            if (output.isEmpty()) {
                throw new RuntimeException("Output not defined for recipe");
            }
            if (ingredients.size() == 0) {
                throw new RuntimeException("Ingredient not defined for recipe");
            }

            if (invalidRecipe) {
                return CustomRecipe.of(null);
            }

            return CustomRecipe.of(new StonecuttingRecipe(getName(), group, ingredients.get(0), output));
        }
    }

    // ------------------------------------------------------------------------------------------

    public class CookingRecipeBuilder<T extends AbstractCookingRecipe> extends BaseSingleIngredientRecipeBuilder<CookingRecipeBuilder<T>> {
        protected float experience;
        protected int cookTime;

        protected final ICookingFactory<T> factory;

        public CookingRecipeBuilder(ICookingFactory<T> factory, int defaultCookTime) {
            this.factory = factory;
            cookTime = defaultCookTime;
        }

        public CookingRecipeBuilder experience(float experience) {
            if (!started) {
                throw new RuntimeException("Cant set experience if recipe not started");
            }
            this.experience = experience;
            return this;
        }

        public CookingRecipeBuilder cookTime(int cookTime) {
            if (!started) {
                throw new RuntimeException("Cant set cookTime if recipe not started");
            }
            this.cookTime = cookTime;
            return this;
        }

        public RecipeBuilder build() {
            recipes.add(buildRecipe());
            resetConstants();
            return RecipeBuilder.this;
        }

        public CustomRecipe buildRecipe() {
            if (!started) {
                throw new RuntimeException("Cant build recipe if recipe not started");
            }
            if (output.isEmpty()) {
                throw new RuntimeException("Output not defined for recipe");
            }
            if (ingredients.size() == 0) {
                throw new RuntimeException("Ingredient not defined for recipe");
            }

            if (invalidRecipe) {
                return CustomRecipe.of(null);
            }

            return CustomRecipe.of(this.factory.create(getName(), group, ingredients.get(0), output, experience, cookTime));
        }
    }

    // ------------------------------------------------------------------------------------------

    protected void next(@Nonnull String name) {
        if (started) {
            throw new RuntimeException("Cant start new recipe if previous not finished");
        }
        started = true;
        this.name = name;
    }

    public ShapedRecipeBuilder nextShaped(@Nonnull String name) {
        next(name);
        return new ShapedRecipeBuilder();
    }

    public ShapelessRecipeBuilder nextShapeless(@Nonnull String name) {
        next(name);
        return new ShapelessRecipeBuilder();
    }

    public StonecuttingRecipeBuilder nextStonecutting(@Nonnull String name) {
        next(name);
        return new StonecuttingRecipeBuilder();
    }

    public CookingRecipeBuilder<FurnaceRecipe> nextFurnace(@Nonnull String name) {
        next(name);
        return new CookingRecipeBuilder<>(FurnaceRecipe::new, 200);
    }

    public CookingRecipeBuilder<BlastingRecipe> nextBlasting(@Nonnull String name) {
        next(name);
        return new CookingRecipeBuilder<>(BlastingRecipe::new, 100);
    }

    public CookingRecipeBuilder<SmokingRecipe> nextSmoking(@Nonnull String name) {
        next(name);
        return new CookingRecipeBuilder<>(SmokingRecipe::new, 100);
    }

    public CookingRecipeBuilder<CampfireCookingRecipe> nextCampfire(@Nonnull String name) {
        next(name);
        return new CookingRecipeBuilder<>(CampfireCookingRecipe::new, 100);
    }
}
