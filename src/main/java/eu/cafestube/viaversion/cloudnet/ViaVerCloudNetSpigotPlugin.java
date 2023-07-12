package eu.cafestube.viaversion.cloudnet;

import dev.derklaro.aerogel.Inject;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Dependency;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;
import eu.cloudnetservice.wrapper.event.ServiceInfoPropertiesConfigureEvent;
import eu.cloudnetservice.wrapper.holder.ServiceInfoHolder;
import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.NonNull;

@PlatformPlugin(
        platform = "bukkit",
        name = "CloudNet-ViaVersion",
        pluginFileNames = "plugin.yml",
        version = "{project.build.version}",
        dependencies = @Dependency(name = "CloudNet-Bridge")
)
public class ViaVerCloudNetSpigotPlugin implements PlatformEntrypoint {

    @Inject
    public ViaVerCloudNetSpigotPlugin(
            @NonNull EventManager eventManager,
            ServiceInfoHolder holder
    ) {
        eventManager.registerListener(this);
        holder.publishServiceInfoUpdate();
    }

    @EventListener
    public void onProperty(ServiceInfoPropertiesConfigureEvent event) {
        //noinspection deprecation
        event.propertyHolder()
                .append("protocolVersion", Bukkit.getUnsafe().getProtocolVersion());
    }

}
