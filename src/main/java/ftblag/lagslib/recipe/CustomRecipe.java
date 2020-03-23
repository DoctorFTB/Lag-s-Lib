package ftblag.lagslib.recipe;

import net.minecraft.item.crafting.IRecipe;

import java.util.Optional;

public class CustomRecipe {

    private final Optional<IRecipe<?>> recipe;

    private CustomRecipe(Optional<IRecipe<?>> recipe) {
        this.recipe = recipe;
    }

    public Optional<IRecipe<?>> getRecipe() {
        return recipe;
    }

    public static CustomRecipe of(IRecipe<?> recipeIn) {
        return new CustomRecipe(Optional.ofNullable(recipeIn));
    }
}
