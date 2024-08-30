package com.adobe.aem.guides.project2.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.paths=/bin/project2/articles",
        "sling.servlet.methods=GET"
    }
)
public class ArticleListServlet extends SlingAllMethodsServlet {

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        int pageNumber = Integer.parseInt(request.getParameter("page"));
        int pageSize = 10;

        ResourceResolver resourceResolver = request.getResourceResolver();
        List<Resource> articles = fetchArticles(resourceResolver, pageNumber, pageSize);

        // Convert articles to JSON and write to response
        response.setContentType("application/json");
        response.getWriter().write(convertArticlesToJson(articles));
    }

    private List<Resource> fetchArticles(ResourceResolver resourceResolver, int pageNumber, int pageSize) {
        List<Resource> articles = new ArrayList<>();

        // Prepare query predicates
        Map<String, String> predicates = new HashMap<>();
        predicates.put("path", "/content/project2/us/en/dog-health-wellbeing");
        predicates.put("type", "cq:Page");
        predicates.put("p.offset", String.valueOf((pageNumber - 1) * pageSize));
        predicates.put("p.limit", String.valueOf(pageSize));

        // Create and execute the query
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), resourceResolver.adaptTo(Session.class));
        SearchResult result = query.getResult();
        result.getHits().forEach(hit -> {
            try {
                articles.add(hit.getResource());
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        });

        return articles;
    }

    private String convertArticlesToJson(List<Resource> articles) {
        // Convert articles to JSON format
        StringBuilder json = new StringBuilder("[");
        for (Resource article : articles) {
            json.append("{\"title\":\"").append(article.getName()).append("\"},");
        }
        if (json.length() > 1) {
            json.setLength(json.length() - 1); // Remove trailing comma
        }
        json.append("]");
        return json.toString();
    }
}
