package ftblag.lagslib;

import ftblag.lagslib.recipes.CustomRecipeLoader;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Mod(LagsLib.MODID)
public class LagsLib {

    public static final String MODID = "lagslib";
    public static final Logger LOGGER = LogManager.getLogger();

    public LagsLib() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::onServerAboutToStart);
    }

    private void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        IReloadableResourceManager manager = event.getServer().getResourceManager();
        manager.addReloadListener((IResourceManagerReloadListener) resourceManager -> {
            RecipeManager recipeManager = event.getServer().getRecipeManager();
            recipeManager.recipes = new HashMap(recipeManager.recipes);
            for (IRecipeType<?> type : recipeManager.recipes.keySet()) {
                recipeManager.recipes.put(type, new HashMap(recipeManager.recipes.get(type)));
            }
            CustomRecipeLoader.loadCustomRecipes(recipeManager.recipes);
        });
    }
}
