package com.adobe.aem.guides.project2.core.listeners;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.project2.core.schedulers.PagePublishScheduler;

@Component(
    service = EventHandler.class,
    immediate = true,
    property = {
        EventConstants.EVENT_TOPIC + "=org/apache/sling/event/JobEvent"
    }
)
public class DeploymentEventListener implements EventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DeploymentEventListener.class);

    @Reference
    private PagePublishScheduler pagePublishScheduler;

    @Override
    public void handleEvent(Event event) {
        String topic = event.getTopic();
        if ("org/apache/sling/event/JobEvent".equals(topic)) {
            String jobTopic = (String) event.getProperty("job.topic");
            if (jobTopic != null && jobTopic.contains("deploy")) {
                LOG.info("Deployment event detected. Triggering scheduler.");
                pagePublishScheduler.run(); // Trigger the scheduler manually
            }
        }
    }
}
