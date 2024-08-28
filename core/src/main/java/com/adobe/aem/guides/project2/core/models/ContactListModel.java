package com.adobe.aem.guides.project2.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class)
public class ContactListModel {

    @Self
    private Resource resource;

    private List<Map<String, String>> contacts;

    @PostConstruct
    protected void init() {
        contacts = new ArrayList<>();
        ResourceResolver resolver = resource.getResourceResolver();
        Resource parentResource = resolver.getResource("/content/project2/us/en/jcr:content");

        if (parentResource != null) {
            for (Resource contactResource : parentResource.getChildren()) {
                ValueMap valueMap = contactResource.getValueMap();
                Map<String, String> contact = new HashMap<>();

                for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
                    contact.put(entry.getKey(), entry.getValue() != null ? entry.getValue().toString() : null);
                }

                contacts.add(contact);
            }
        }
    }

    public List<Map<String, String>> getContacts() {
        return contacts;
    }
}