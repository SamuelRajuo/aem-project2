package com.adobe.aem.guides.project2.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleListModel {

    @Inject
    private ResourceResolver resourceResolver;

    private List<Resource> articles;

    @Inject
    @Optional
    @Default(values = "1")
    private String pageNumber;

    @PostConstruct
    protected void init() {
        articles = fetchArticles(Integer.parseInt(pageNumber), 10);  // Fetch 10 articles per page
    }

    public List<Resource> fetchArticles(int pageNumber, int pageSize) {
        List<Resource> articles = new ArrayList<>();
        Resource resource = resourceResolver.getResource("/content/project2/us/en/dog-health-wellbeing");

        // Create a QueryBuilder instance
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);

        // Prepare predicates
        Map<String, String> predicates = new HashMap<>();
        predicates.put("path", "/content/project2/us/en/dog-health-wellbeing");
        predicates.put("type", "cq:Page");
        predicates.put("p.offset", String.valueOf((pageNumber - 1) * pageSize));
        predicates.put("p.limit", String.valueOf(pageSize));

        // Create PredicateGroup with predicates
        PredicateGroup predicateGroup = PredicateGroup.create(predicates);

        // Build the query
        Query query = queryBuilder.createQuery(predicateGroup, resourceResolver.adaptTo(Session.class));
        
        // Execute the query
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

    public List<Resource> getArticles() {
        return articles;
    }
}
