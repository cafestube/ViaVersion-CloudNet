package eu.cafestube.viaversion.cloudnet;


import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.platform.ProtocolDetectorService;
import com.viaversion.viaversion.api.platform.ViaServerProxyPlatform;
import dev.derklaro.aerogel.Inject;
import eu.cloudnetservice.driver.document.property.DocProperty;
import eu.cloudnetservice.driver.event.EventListener;
import eu.cloudnetservice.driver.event.EventManager;
import eu.cloudnetservice.driver.event.events.service.CloudServiceLifecycleChangeEvent;
import eu.cloudnetservice.driver.event.events.service.CloudServiceUpdateEvent;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.service.ServiceLifeCycle;
import eu.cloudnetservice.ext.platforminject.api.PlatformEntrypoint;
import eu.cloudnetservice.ext.platforminject.api.stereotype.Dependency;
import eu.cloudnetservice.ext.platforminject.api.stereotype.PlatformPlugin;

@PlatformPlugin(
        platform = "velocity",
        name = "CloudNet-ViaVersion",
        version = "{project.build.version}",
        dependencies = {@Dependency(name = "CloudNet-Bridge"), @Dependency(name = "ViaVersion")}
)
public class ViaVerCloudNetProxyPlugin implements PlatformEntrypoint {

    private static final DocProperty<Integer> PROTOCOL_VERSION = DocProperty.property("protocolVersion", Integer.class);


    private final EventManager eventManager;

    @Inject
    public ViaVerCloudNetProxyPlugin(EventManager eventManager, CloudServiceProvider provider) {
        this.eventManager = eventManager;

        provider.runningServicesAsync().thenAccept(serviceInfoSnapshots -> {
            serviceInfoSnapshots.forEach(serviceInfoSnapshot -> {
                if (serviceInfoSnapshot.propertyAbsent(PROTOCOL_VERSION)) return;

                getProtocolDetectorService().setProtocolVersion(serviceInfoSnapshot.name(), serviceInfoSnapshot.readProperty(PROTOCOL_VERSION));
            });
        });
    }

    @Override
    public void onLoad() {
        eventManager.registerListener(this);
    }


    @EventListener
    public void handleServiceLifecycle(CloudServiceLifecycleChangeEvent event) {
        if(event.newLifeCycle() == ServiceLifeCycle.DELETED) {
            getProtocolDetectorService().uncacheProtocolVersion(event.serviceInfo().name());
        } else {
            if(event.serviceInfo().propertyAbsent(PROTOCOL_VERSION)) return;
            getProtocolDetectorService().setProtocolVersion(event.serviceInfo().name(), event.serviceInfo().readProperty(PROTOCOL_VERSION));
        }
    }

    private ProtocolDetectorService getProtocolDetectorService() {
        return ((ViaServerProxyPlatform<?>) Via.getPlatform()).protocolDetectorService();
    }

    @EventListener
    public void handleServiceUpdate(CloudServiceUpdateEvent event) {
        if(event.serviceInfo().lifeCycle() == ServiceLifeCycle.DELETED) {
            getProtocolDetectorService().uncacheProtocolVersion(event.serviceInfo().name());
        } else {
            if(event.serviceInfo().propertyAbsent(PROTOCOL_VERSION)) return;
            getProtocolDetectorService().setProtocolVersion(event.serviceInfo().name(), event.serviceInfo().readProperty(PROTOCOL_VERSION));
        }
    }

}
