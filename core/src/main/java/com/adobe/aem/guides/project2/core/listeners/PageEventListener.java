package com.adobe.aem.guides.project2.core.listeners;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import javax.jcr.RepositoryException;
import javax.jcr.observation.ObservationManager;
import javax.jcr.observation.EventIterator;

@Component(
    service = EventHandler.class,
    immediate = true,
    property = {
        EventConstants.EVENT_TOPIC + "=com/day/cq/replication"
    }
)
public class PageEventListener implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PageEventListener.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Override
    public void handleEvent(Event event) {
        String[] paths = (String[]) event.getProperty("paths");
        LOG.info("Handling event for paths: {}", (Object[]) paths);
        if (paths != null && paths.length > 0) {
            for (String path : paths) {
                logPublishedPagePath(path);
                // No property modification needed
            }
        } else {
            LOG.warn("Page paths not found in event: {}", event);
        }
    }

    private void logPublishedPagePath(String pagePath) {
        LOG.info("Page published: {}", pagePath);
    }

    private ResourceResolver getResourceResolver() {
        ResourceResolver resolver = null;
        try {
            Map<String, Object> param = new HashMap<>();
            param.put(ResourceResolverFactory.SUBSERVICE, "samuel"); // Replace with your actual service user name
            resolver = resourceResolverFactory.getServiceResourceResolver(param);
            LOG.info("ResourceResolver obtained for service user 'samuel'");
        } catch (Exception e) {
            LOG.error("Failed to obtain ResourceResolver", e);
        }
        return resolver;
    }
}