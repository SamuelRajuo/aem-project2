package com.adobe.aem.guides.project2.core.Services;

import com.adobe.aem.guides.project2.core.config.ApiConfig;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = ApiService.class)
@Designate(ocd = ApiConfig.class)
public class ApiService {

    private String apiUrl;
    private String contentType;
    private String acceptHeader;

    @Reference
    private SystemUserService systemUserService;

    @Activate
    @Modified
    protected void activate(ApiConfig config) {
        this.apiUrl = config.apiUrl();
        this.contentType = config.contentType();
        this.acceptHeader = config.acceptHeader();
    }

    // Fetch and store API data in CRXDE
    public void fetchAndStoreBooks() throws JSONException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrl);

        // Add necessary headers from ApiConfig
        request.addHeader("Content-Type", contentType);
        request.addHeader("Accept", acceptHeader);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("API Response Status: " + statusCode);

            if (statusCode == 200) { // Check if the response is OK
                HttpEntity entity = response.getEntity();
                String jsonResponse = EntityUtils.toString(entity);

                // Log the raw API response body for debugging
                System.out.println("Raw API Response Body: " + jsonResponse);

                // Check if the response is empty or malformed
                if (jsonResponse == null || jsonResponse.isEmpty()) {
                    System.out.println("Empty response from API");
                    return;
                }

                // Attempt to parse the JSON response
                JSONArray booksArray = null;
                try {
                    booksArray = new JSONArray(jsonResponse);
                } catch (JSONException e) {
                    System.out.println("Failed to parse JSON as array. Trying object...");
                    try {
                        JSONObject jsonObject = new JSONObject(jsonResponse);
                        booksArray = new JSONArray(); // Convert object into an array if needed
                        booksArray.put(jsonObject);
                    } catch (JSONException jsonException) {
                        System.out.println("Failed to parse JSON: " + jsonException.getMessage());
                        throw jsonException;
                    }
                }

                // Store the parsed data in CRX
                if (booksArray != null) {
                    storeBooksInCRX(booksArray);
                }

            } else {
                System.out.println("API returned error with status code: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Store the API response data as properties in CRX nodes
    private void storeBooksInCRX(JSONArray books) throws JSONException {
        try (ResourceResolver resolver = systemUserService.getWriteServiceResourceResolver()) {
            // Check if ResourceResolver is available and live
            if (resolver != null && resolver.isLive()) {
                Resource contentResource = resolver.getResource("/content/project2/jcr:content");

                if (contentResource == null) {
                    // If the jcr:content node does not exist, create it
                    contentResource = createContentNodeStructure(resolver, "/content/project2/jcr:content");
                } else {
                    System.out.println("/content/project2/jcr:content exists, proceeding to add child nodes.");
                }

                if (contentResource != null) {
                    // Iterate over the JSONArray and store book data as JCR properties
                    for (int i = 0; i < books.length(); i++) {
                        JSONObject book = books.getJSONObject(i);
                        String id = book.optString("id", "unknown"); // Get ID with default fallback
                        String title = book.optString("title", "Untitled");
                        String body = book.optString("body", "No description available");

                        // Create or update the node for each book
                        String nodeName = id.replaceAll("[^a-zA-Z0-9]", "_"); // Sanitize node name
                        Resource bookNodeResource = resolver.getResource(contentResource.getPath() + "/" + nodeName);

                        if (bookNodeResource == null) {
                            // Create a new node if it doesn't exist
                            Map<String, Object> properties = new HashMap<>();
                            properties.put("jcr:primaryType", "nt:unstructured");
                            properties.put("id", id);
                            properties.put("title", title);
                            properties.put("body", body);  // Store the 'body' field as a JCR property

                            bookNodeResource = resolver.create(contentResource, nodeName, properties);
                            System.out.println("Created node for book ID: " + id + " with title: " + title);
                        } else {
                            // If the node exists, update its properties
                            Node bookNode = bookNodeResource.adaptTo(Node.class);
                            if (bookNode != null) {
                                bookNode.setProperty("id", id);
                                bookNode.setProperty("title", title);
                                bookNode.setProperty("body", body);  // Update the 'body' field as a JCR property
                                System.out.println("Updated node for book ID: " + id + " with title: " + title);
                            } else {
                                System.out.println("Failed to adapt resource to JCR Node for ID: " + id);
                            }
                        }
                    }

                    resolver.commit(); // Commit all changes at once
                    System.out.println("Changes committed to CRX.");
                } else {
                    System.out.println("Failed to create or retrieve content resource at path: /content/project2/jcr:content.");
                }
            } else {
                System.out.println("ResourceResolver is not live or valid.");
            }
        } catch (LoginException | PersistenceException | RepositoryException e) {
            e.printStackTrace();
            System.out.println("Error while saving the response to the content node.");
        }
    }

    // Helper method to create the node structure
    private Resource createContentNodeStructure(ResourceResolver resolver, String path) throws PersistenceException {
        String[] parts = path.split("/");
        Resource parent = resolver.getResource("/content");
        Resource current = parent;

        for (int i = 1; i < parts.length; i++) {
            if (current != null) {
                Resource child = current.getChild(parts[i]);
                if (child == null) {
                    Map<String, Object> properties = new HashMap<>();
                    properties.put("jcr:primaryType", "nt:unstructured");
                    current = resolver.create(current, parts[i], properties);
                    resolver.commit(); // Commit the node creation
                    System.out.println("Created node: " + current.getPath());
                } else {
                    current = child;
                }
            }
        }
        return current;
    }
}
