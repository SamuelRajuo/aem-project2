package com.adobe.aem.guides.project2.core.models;


import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CarouselItemModel {

    @ValueMapValue
    private String text;

    @ValueMapValue
    private String path;

    @ChildResource
    public List<NestedMultifieldModel>nestedMultifield;

    public List<NestedMultifieldModel> getNestedMultifield() {
        return nestedMultifield;
    }

    public String getText() {
        return text;
    }

    public String getPath() {
        return path;
    }
}
