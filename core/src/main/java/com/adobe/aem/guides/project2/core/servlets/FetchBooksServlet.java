package com.adobe.aem.guides.project2.core.servlets;

import com.adobe.aem.guides.project2.core.Services.ApiService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = {Servlet.class}, property = {"sling.servlet.paths=/bin/fetchBooks"})
public class FetchBooksServlet extends SlingAllMethodsServlet {

    @Reference
    private ApiService apiService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            // Fetch and store books from the external API using the ApiService
            apiService.fetchAndStoreBooks();

            // If successful, return a success message
            response.setStatus(SlingHttpServletResponse.SC_OK);
            response.getWriter().write("{\"message\": \"Books fetched and stored successfully.\"}");

        } catch (JSONException e) {
            // Handle JSON parsing errors
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Failed to fetch and store books.\"}");
            e.printStackTrace();
        }
    }
}
