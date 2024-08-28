package com.adobe.aem.guides.project2.core.listeners;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;

@Component(
    service = EventHandler.class,
    property = {
        "event.topics=org/apache/sling/api/resource/Resource/ADDED"
    }
)
public class PageCreationEventHandler implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(PageCreationEventHandler.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public void handleEvent(Event event) {
        String path = (String) event.getProperty("path");
        log.info("Handling event for path: {}", path);

        if (path != null && path.endsWith("/jcr:content")) {
            Map<String, Object> params = new HashMap<>();
            params.put(ResourceResolverFactory.SUBSERVICE, "samuel");

            try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(params)) {
                log.info("ResourceResolver obtained for service user 'samuel'");

                // Get the page resource
                Resource pageResource = resolver.getResource(path);
                if (pageResource != null) {
                    log.info("Page resource found: {}", path);
                    String templatePath = pageResource.getValueMap().get("cq:template", String.class);
                    log.info("Template path: {}", templatePath);

                    Calendar expiryDate = Calendar.getInstance();
                    
                    // Check the template path and set the expiry date
                    if (templatePath != null && templatePath.startsWith("/conf/project2")) {
                        // Set expiry date to current date for project2 templates
                        expiryDate.add(Calendar.DAY_OF_MONTH, 0);
                        log.info("Setting expiry date to current date for template: {}", templatePath);
                    } else {
                        // Set expiry date to previous date for other templates
                        expiryDate.add(Calendar.DAY_OF_MONTH, -1);
                        log.info("Setting expiry date to previous date for template: {}", templatePath);
                    }

                    Date expiryDateValue = expiryDate.getTime();
                    String formattedDate = dateFormat.format(expiryDateValue);

                    // Set the expiry date property
                    ModifiableValueMap properties = pageResource.adaptTo(ModifiableValueMap.class);
                    if (properties != null) {
                        properties.put("expiryDate", formattedDate);
                        resolver.commit();
                        log.info("Expiry date property set and changes committed for resource: {}", path);
                    }

                    // Trigger the workflow
                    startWorkflow(resolver, path);
                } else {
                    log.warn("Page resource not found: {}", path);
                }
            } catch (LoginException | PersistenceException | WorkflowException e) {
                log.error("Error handling page creation event", e);
            }
        }
    }

    private void startWorkflow(ResourceResolver resolver, String path) throws WorkflowException {
        WorkflowSession wfSession = resolver.adaptTo(WorkflowSession.class);
        if (wfSession != null) {
            log.info("Starting workflow for path: {}", path);
            WorkflowModel wfModel = wfSession.getModel("/var/workflow/models/expirydateworkflow");
            if (wfModel != null) {
                WorkflowData wfData = wfSession.newWorkflowData("JCR_PATH", path);
                wfSession.startWorkflow(wfModel, wfData);
                log.info("Workflow started for page: {}", path);
            } else {
                log.warn("Workflow model not found at path: /var/workflow/models/expirydateworkflow");
            }
        } else {
            log.warn("Workflow session not available");
        }
    }
}
