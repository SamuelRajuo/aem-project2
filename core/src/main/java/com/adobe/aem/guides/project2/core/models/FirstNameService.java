package com.adobe.aem.guides.project2.core.models;

import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.HashMap;
import java.util.Map;

@Component(service = FirstNameService.class, immediate = true)
public class FirstNameService {

    private static final Logger LOG = LoggerFactory.getLogger(FirstNameService.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private static final String DEFAULT_PATH = "/content/project2/us/en";

    /**
     * Updates the 'firstName' property on the jcr:content node at the specified
     * page path.
     *
     * @param pagePath the page path where the property should be added (if null,
     *                 uses the default path)
     */
    public void addFirstNameProperty(String pagePath) {
        // Use default path if pagePath is null or empty
        if (pagePath == null || pagePath.isEmpty()) {
            pagePath = DEFAULT_PATH;
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(ResourceResolverFactory.SUBSERVICE, "samuel"); // Ensure the service user mapping is correct

        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(paramMap)) {
            Resource pageResource = resolver.getResource(pagePath);
            if (pageResource != null) {
                Node node = pageResource.adaptTo(Node.class);
                if (node != null && node.hasNode("jcr:content")) {
                    Node contentNode = node.getNode("jcr:content");
                    // Setting the 'firstName' property
                    contentNode.setProperty("firstName", "Samuel");
                    resolver.commit();

                    LOG.info("FirstName property added successfully at {}", pagePath);
                } else {
                    LOG.error("Content node not found for path: {}", pagePath);
                }
            } else {
                LOG.error("Invalid page path: {}", pagePath);
            }
        } catch (Exception e) {
            LOG.error("Error updating firstName property at {}: {}", pagePath, e.getMessage(), e);
        }
    }
}
