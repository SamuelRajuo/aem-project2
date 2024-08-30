package com.adobe.aem.guides.project2.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(
    service = { Servlet.class },
    property = {
        "sling.servlet.paths=/bin/headerdata",
        "sling.servlet.methods=GET",
        "sling.servlet.extensions=json"
    }
)
public class HeaderDataServlet extends SlingAllMethodsServlet {

    @Reference
    private ResourceResolver resourceResolverService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String path = "/content/project2/us/en/jcr:content/root/container/container/headercomponent";
        Resource resource = request.getResourceResolver().getResource(path);

        if (resource != null) {
            JSONObject jsonResponse = new JSONObject();
            try {
                jsonResponse.put("logo", resource.getValueMap().get("logo", String.class));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JSONArray tabsArray = new JSONArray();
            Resource tabsResource = resource.getChild("tabItem");
            if (tabsResource != null) {
                for (Resource tab : tabsResource.getChildren()) {
                    JSONObject tabObject = new JSONObject();
                    try {
                        tabObject.put("tabTitle", tab.getValueMap().get("tabTitle", String.class));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        tabObject.put("tabLink", tab.getValueMap().get("tabLink", String.class));
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } // External link
                    tabsArray.put(tabObject);
                }
            }
            try {
                jsonResponse.put("tabs", tabsArray);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            response.setContentType("application/json");
            response.getWriter().write(jsonResponse.toString());
        } else {
            response.sendError(SlingHttpServletResponse.SC_NOT_FOUND, "Resource not found");
        }
    }
}
