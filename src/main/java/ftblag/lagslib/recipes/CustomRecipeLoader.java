package ftblag.lagslib.recipes;

import com.google.common.collect.Maps;
import ftblag.lagslib.LagsLib;
import ftblag.lagslib.events.CustomRecipeEvent;
import ftblag.lagslib.recipe.CustomRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;
import java.util.Optional;

public class CustomRecipeLoader {

    public static void loadCustomRecipes(Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> map) {
        LagsLib.LOGGER.info("[Lag's Lib] Loading custom coded recipes..");

        CustomRecipeEvent event = new CustomRecipeEvent();
        MinecraftForge.EVENT_BUS.post(event);

        event.getRecipes().stream()
                .map(CustomRecipe::getRecipe)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(recipe -> map.computeIfAbsent(recipe.getType(), i -> Maps.newHashMap()).put(recipe.getId(), recipe));

        LagsLib.LOGGER.info("[Lag's Lib] Loaded {} custom coded recipes..", event.getRecipes().size());
    }
}
