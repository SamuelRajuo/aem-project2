package com.adobe.aem.guides.project2.core.servlets;
import java.io.IOException;
import java.util.Iterator;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
@Component(service=Servlet.class,immediate=true,
property={"sling.servlet.paths=/bin/example/tag"})
public class TagExample extends SlingAllMethodsServlet{
    public void doGet(SlingHttpServletRequest req,SlingHttpServletResponse res) throws IOException{
        ResourceResolver resolver=req.getResourceResolver();
        TagManager tagManager=resolver.adaptTo(TagManager.class);
        Tag tag=tagManager.resolve("/content/cq:tags/learning/color");
        JsonArrayBuilder jsonArray=Json.createArrayBuilder();
        Iterator<Tag> listChildren=tag.listChildren();
        JsonObjectBuilder jsonObject=Json.createObjectBuilder();
        while (listChildren.hasNext()) { 
           Tag childTag= listChildren.next();
           jsonObject.add("title", childTag.getTitle());
           jsonObject.add("path", childTag.getPath());
        }
        jsonArray.add(jsonObject);
        res.getWriter().write(jsonArray.build().toString());
    }
}
