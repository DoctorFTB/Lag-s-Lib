package ftblag.lagslib.events;

import com.google.common.collect.Lists;
import ftblag.lagslib.recipe.CustomRecipe;
import ftblag.lagslib.recipe.RecipeBuilder;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class CustomRecipeEvent extends Event {

    private List<CustomRecipe> recipes;

    public CustomRecipeEvent() {
        this.recipes = Lists.newArrayList();
    }

    public List<CustomRecipe> getRecipes() {
        return recipes;
    }

    public void register(CustomRecipe recipe) {
        recipes.add(recipe);
    }

    public void register(RecipeBuilder builder) {
        builder.register(recipes);
    }
}
