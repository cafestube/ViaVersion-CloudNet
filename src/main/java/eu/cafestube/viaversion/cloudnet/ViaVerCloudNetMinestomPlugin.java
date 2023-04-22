package eu.cafestube.viaversion.cloudnet;

import dev.derklaro.aerogel.Inject;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Dependency;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import eu.cloudnetservice.wrapper.event.ServiceInfoSnapshotConfigureEvent;
import eu.cloudnetservice.wrapper.holder.ServiceInfoHolder;
import net.minestom.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

@PlatformPlugin(
        platform = "minestom",
        name = "CloudNet-ViaVersion",
        version = "{project.build.version}"
)
public class ViaVerCloudNetMinestomPlugin implements PlatformEntrypoint {

    private final int protocolVersion;

    @Inject
    public ViaVerCloudNetMinestomPlugin(
            @NonNull EventManager eventManager,
            ServiceInfoHolder holder
    ) {
        eventManager.registerListener(this);

        try {
            //We want to use the actual value of the protocol version instead of an inlined constant
            protocolVersion = MinecraftServer.class.getField("PROTOCOL_VERSION").getInt(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        holder.publishServiceInfoUpdate();
    }

    @EventListener
    public void onProperty(ServiceInfoSnapshotConfigureEvent event) {
        //noinspection deprecation
        event.serviceInfo().propertyHolder()
                .append("protocolVersion", protocolVersion);
    }

}
