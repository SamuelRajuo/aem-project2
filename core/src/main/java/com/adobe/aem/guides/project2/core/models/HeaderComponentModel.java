package com.adobe.aem.guides.project2.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeaderComponentModel {

    @ValueMapValue
    private String logo;

    @ChildResource(name = "tabItem")
    private List<TabItem> tabItems;

    public String getLogo() {
        return logo;
    }

    public List<TabItem> getTabItems() {
        return tabItems;
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class TabItem {
        @ValueMapValue
        private String tabTitle;

        @ValueMapValue
        private String tabLink;

        public String getTabTitle() {
            return tabTitle;
        }

        public String getTabLink() {
            return tabLink;
        }
    }
}
