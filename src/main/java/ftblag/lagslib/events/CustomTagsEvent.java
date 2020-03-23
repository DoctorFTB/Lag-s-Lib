package ftblag.lagslib.events;

import net.minecraft.tags.NetworkTagManager;
import net.minecraftforge.eventbus.api.Event;

public class CustomTagsEvent extends Event {

    private final NetworkTagManager manager;

    public CustomTagsEvent(NetworkTagManager manager) {
        this.manager = manager;
    }

    public NetworkTagManager getTagManager() {
        return manager;
    }
}
