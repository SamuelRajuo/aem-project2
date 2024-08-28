package com.adobe.aem.guides.project2.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.apache.sling.api.resource.Resource;
@Component(service=Servlet.class,immediate=true,
property={"sling.servlet.paths=/bin/example/demo"})
public class JcrServlet extends SlingAllMethodsServlet{
    public void doGet(SlingHttpServletRequest req,SlingHttpServletResponse res) throws IOException
    {
        try{
        String name=req.getParameter("name");
        String password=req.getParameter("password");
        String email=req.getParameter("email");
        ResourceResolver resolver=req.getResourceResolver();
        Map<String,Object> map= new HashMap<>();
        map.put("name",name);
        map.put("password",password);
        map.put("email",email);
       Resource parentResource= resolver.getResource("/content/project2/us/en/jcr:content");
       resolver.create(parentResource, "jcrnode", map);
       res.getWriter().write("jcr Node created...");
       resolver.commit();
    }catch(Exception e){
        res.getWriter().write("getting Error...");
    }

}

}
