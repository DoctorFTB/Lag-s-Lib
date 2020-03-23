package ftblag.lagslib.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class OneLineRecipeBuilder extends RecipeBuilder {

    private OneLineRecipeBuilder(@Nonnull String modid) {
        super(modid);
    }

    public static OneLineRecipeBuilder of(@Nonnull String modid) {
        return new OneLineRecipeBuilder(modid);
    }

    public OneLineRecipeBuilder shaped(@Nonnull String name, @Nonnull IItemProvider output, @Nonnull Object... recipe) {
        return shaped(name, new ItemStack(output), recipe);
    }

    public OneLineRecipeBuilder shaped(@Nonnull String name, @Nonnull ItemStack output, @Nonnull Object... recipe) {
        nextShaped(name).output(output).oldStyle(recipe).build();
        return this;
    }

    public OneLineRecipeBuilder shapeless(@Nonnull String name, @Nonnull IItemProvider output, @Nonnull Object... recipe) {
        return shapeless(name, new ItemStack(output), recipe);
    }

    public OneLineRecipeBuilder shapeless(@Nonnull String name, @Nonnull ItemStack output, @Nonnull Object... recipe) {
        RecipeBuilder.ShapelessRecipeBuilder builder = nextShapeless(name).output(output);
        if (recipe.length == 1 && recipe[0] instanceof NonNullList) {
            builder.ingredientList((NonNullList<?>) recipe[0]);
        } else {
            builder.ingredients(recipe);
        }
        builder.build();
        return this;
    }

    public OneLineRecipeBuilder stonecutting(@Nonnull String name, @Nonnull Ingredient input, @Nonnull ItemStack output) {
        nextStonecutting(name).output(output).ingredient(input).build();
        return this;
    }

    public OneLineRecipeBuilder smelting(@Nonnull String name, @Nonnull Ingredient input, @Nonnull IItemProvider output) {
        return smelting(name, input, new ItemStack(output));
    }

    public OneLineRecipeBuilder smelting(@Nonnull String name, @Nonnull Ingredient input, @Nonnull ItemStack output) {
        nextFurnace(name).output(output).ingredient(input).build();
        return this;
    }

    public OneLineRecipeBuilder blasting(@Nonnull String name, @Nonnull Ingredient input, @Nonnull ItemStack output) {
        nextBlasting(name).output(output).ingredient(input).build();
        return this;
    }

    public OneLineRecipeBuilder smoking(@Nonnull String name, @Nonnull Ingredient input, @Nonnull ItemStack output) {
        nextSmoking(name).output(output).ingredient(input).build();
        return this;
    }

    public OneLineRecipeBuilder campfireCooking(@Nonnull String name, @Nonnull Ingredient input, @Nonnull ItemStack output) {
        nextCampfire(name).output(output).ingredient(input).build();
        return this;
    }
}
