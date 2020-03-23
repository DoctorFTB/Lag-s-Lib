package ftblag.lagslib.utils;

import com.google.common.collect.Maps;
import ftblag.lagslib.LagsLib;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.function.Function;

public class IngredientUtils {

    private IngredientUtils() {
    }

    private static final Map<String, ResourceLocation> oldToNew = Maps.newHashMap();
    private static final Map<Class<?>, Function<Object, Ingredient>> ingredientParsers = Maps.newHashMap();

    static {
        oldToNew.put("stickWood", new ResourceLocation("forge", "rods/wooden"));
        oldToNew.put("plankWood", new ResourceLocation("planks"));
        oldToNew.put("blockGlass", new ResourceLocation("forge", "glass"));
    }

    public static void addOldToNew(String oldName, ResourceLocation newName) {
        oldToNew.put(oldName, newName);
    }

    public static void addIngredientParser(Class<?> clazz, Function<Object, Ingredient> function) {
        ingredientParsers.put(clazz, function);
    }

    public static Ingredient getIngredient(Object obj) {
        if (obj instanceof Ingredient)
            return (Ingredient) obj;
        else if (obj instanceof ItemStack)
            return Ingredient.fromStacks(((ItemStack) obj).copy());
        else if (obj instanceof IItemProvider)
            return Ingredient.fromItems((IItemProvider) obj);
        else if (obj instanceof Tag)
            return Ingredient.fromTag((Tag) obj);
        else if (obj instanceof String) {
            String in = (String) obj;

            ResourceLocation res;
            if (in.startsWith("tag:")) {
                res = new ResourceLocation(in.substring(4));
            } else if (oldToNew.containsKey(in)) {
                res = oldToNew.get(in);
            } else {
                String modid;

                int doth = in.indexOf(":");
                if (doth != -1) {
                    modid = in.substring(0, doth);
                    in = in.substring(doth + 1);
                } else {
                    modid = "forge";
                }

                int index = 0;
                char lowerCase = 0;
                for (char chr : in.toCharArray()) {
                    if (Character.isUpperCase(chr)) {
                        lowerCase = Character.toLowerCase(chr);
                        break;
                    }
                    index++;
                }
                if (lowerCase != 0) {
                    String path = in.substring(0, index);
                    if (path.equals("block")) {
                        path = "storage_block";
                    }
                    res = new ResourceLocation(modid, String.format("%ss/%s%s", path, lowerCase, in.substring(index + 1)));
                } else {
                    res = new ResourceLocation(modid, in);
                }
            }

            Tag<Item> tag = ItemTags.getCollection().get(res);
            if (tag == null) {
                LagsLib.LOGGER.warn("[Lag's Lib] Not found ingredient for: {}!", obj);
                return null;
            }
            return Ingredient.fromTag(tag);
        }

        if (ingredientParsers.containsKey(obj.getClass())) {
            try {
                return ingredientParsers.get(obj.getClass()).apply(obj);
            } catch (Exception ex) {
                LagsLib.LOGGER.error("Failed to parse ingredient from custom parser! Object: {}", obj, ex);
                return null;
            }
        } else {
            throw new RuntimeException("Class " + obj.getClass() + " not implemented");
        }
    }

}
