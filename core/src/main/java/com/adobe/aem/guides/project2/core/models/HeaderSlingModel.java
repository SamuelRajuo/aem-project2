package com.adobe.aem.guides.project2.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderSlingModel {
    
    @ValueMapValue
    private String pathField;
    
    @ValueMapValue
    private String textField;
    
    @ValueMapValue
    private String checkbox;
    @Default(values = "Hello")
    @RequestAttribute(name="htlattribute")
    private String Hello;
    
    public String getHello() {
        return Hello;
    }

    @ChildResource
    private List<HeaderChild> multiField;

    public List<HeaderChild> getMultiField() {
        return multiField;
    }

    public String getPathField() {
        return pathField;
    }

    public String getTextField() {
        return textField;
    }

    public String getCheckBox() {
        return checkbox;
    }
}
