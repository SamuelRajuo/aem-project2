package com.adobe.aem.guides.project2.core.Services;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * The Interface SystemUserService.
 */
public interface SystemUserService {

    /**
     * Gets the write service resource resolver.
     *
     * @return the write service resource resolver
     * @throws LoginException the login exception
     */
    ResourceResolver getWriteServiceResourceResolver() throws LoginException;

    /**
     * Gets the read service resource resolver.
     *
     * @return the read service resource resolver
     * @throws LoginException the login exception
     */
    ResourceResolver getReadServiceResourceResolver() throws LoginException;

    /**
     * Close resource resolver.
     *
     * @param resolver the resolver
     */
    void closeResourceResolver(final ResourceResolver resolver);
}
