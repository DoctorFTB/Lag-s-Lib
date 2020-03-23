package ftblag.lagslib.events;

import ftblag.lagslib.LagsLib;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.EffectiveSide;

@Mod.EventBusSubscriber(modid = LagsLib.MODID)
public class LagsLibForgeEventHandler {

    @SubscribeEvent
    public static void onTagsUpdated(TagsUpdatedEvent event) {
        if (EffectiveSide.get().isServer()) {
            MinecraftForge.EVENT_BUS.post(new CustomTagsEvent(event.getTagManager()));
        }
    }
}
