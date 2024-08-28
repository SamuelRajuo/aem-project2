package com.adobe.aem.guides.project2.core.models;

import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Tab3Child {
    @ValueMapValue
    private String desktopIcon;

    @ValueMapValue
    private String mobileIcon;

    @ChildResource
    private List<Tab3NestedChild> nestedMultiField;

    public String getDesktopIcon() {
        return desktopIcon;
    }

    public String getMobileIcon() {
        return mobileIcon;
    }

    public List<Tab3NestedChild> getNestedMultiField() {
        return nestedMultiField;
    }
}
