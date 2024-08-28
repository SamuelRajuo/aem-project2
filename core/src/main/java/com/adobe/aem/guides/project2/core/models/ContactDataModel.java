package com.adobe.aem.guides.project2.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ContactDataModel {

    private List<ContactData> contacts;

    @Inject
    private Resource resource; // Inject the current resource

    @PostConstruct
    protected void init() {
        contacts = new ArrayList<>();
        ResourceResolver resolver = resource.getResourceResolver();
        Resource parentResource = resolver.getResource("/content/project2/us/en/jcr:content");

        if (parentResource != null) {
            // Iterate through each child resource (each contact node)
            for (Resource contactResource : parentResource.getChildren()) {
                ValueMap properties = contactResource.getValueMap();
                String name = properties.get("name", String.class);
                String email = properties.get("email", String.class);
                String mobile = properties.get("mobile", String.class);
                
                // Add contact data to the list
                contacts.add(new ContactData(name, email, mobile));
            }
        }
    }

    public List<ContactData> getContacts() {
        return contacts;
    }
}
