package com.adobe.aem.guides.project2.core.listeners;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service=EventHandler.class,immediate=true,
property={EventConstants.EVENT_TOPIC+"=/org/apache/sling/api/resource/Resource/ADDED",
            EventConstants.EVENT_TOPIC+"=/org/apache/sling/api/resource/Resource/REMOVED",
            EventConstants.EVENT_TOPIC+"=/org/apache/sling/api/resource/Resource/CHANGED",
           EventConstants.EVENT_TOPIC+"=com/day/cq/replication",
           EventConstants.EVENT_FILTER+"=(|(|(path=/content/project2/us/en/*)(path=/content/we-retail/fr))(type=activate))"
})
public class EventHandlerExample implements EventHandler{
    public static final Logger Log=LoggerFactory.getLogger(EventHandlerExample.class);
    @Override
    public void handleEvent(Event event) {
    Log.info("Event Handler is Executed...");
    Log.info("Topic name{}",event.getTopic());
    String[] propertyNames=event.getPropertyNames();
    for(String name:propertyNames){
        Log.info("Property Name{},Property Value{}",name,event.getProperty(name).toString());
    }
    }

}
