package com.adobe.aem.guides.project2.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "API Configuration", description = "Configuration for the external API calls")
public @interface ApiConfig {

    @AttributeDefinition(
        name = "API URL",
        description = "The URL of the API to fetch data from",
        defaultValue = "https://jsonplaceholder.typicode.com/posts"
    )
    String apiUrl() default "https://jsonplaceholder.typicode.com/posts";

    @AttributeDefinition(
        name = "Content Type",
        description = "The Content-Type header for the API request",
        defaultValue = "application/json"
    )
    String contentType() default "application/json";

    @AttributeDefinition(
        name = "Accept Header",
        description = "The Accept header for the API request",
        defaultValue = "application/json"
    )
    String acceptHeader() default "application/json";
}
