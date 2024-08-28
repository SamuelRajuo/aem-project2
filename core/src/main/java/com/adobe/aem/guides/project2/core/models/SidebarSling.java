package com.adobe.aem.guides.project2.core.models;

import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SidebarSling {
    // Tab 1
    @ValueMapValue
    private String logoPath;

    @ValueMapValue
    private String logoMobilePath;

    @ValueMapValue
    private String logoLink;

    @ValueMapValue
    private Boolean enableSwitch;

    // Tab 2
    @ChildResource
    private List<Tab2Child> tabMultiField;

    // Tab 3
    @ChildResource
    private List<Tab3Child> sideBarMulti;

    // Tab 4
    @ValueMapValue
    private String country;

    

    public String getLogoPath() {
        return logoPath;
    }

    public String getLogoMobilePath() {
        return logoMobilePath;
    }

    public String getLogoLink() {
        return logoLink;
    }

    public Boolean getEnableSwitch() {
        return enableSwitch;
    }

    public List<Tab2Child> getTabMultiField() {
        return tabMultiField;
    }

    public List<Tab3Child> getSideBarMulti() {
        return sideBarMulti;
    }

    public String getCountry() {
        return country;
    }
}
 