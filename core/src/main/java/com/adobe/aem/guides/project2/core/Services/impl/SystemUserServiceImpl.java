package com.adobe.aem.guides.project2.core.Services.impl;


import java.util.HashMap;
import java.util.Map;

import com.adobe.aem.guides.project2.core.Services.SystemUserService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * It is to define some methods to read / write the restriction resource
 */
@Component(service = SystemUserService.class, immediate = true)
public class SystemUserServiceImpl implements SystemUserService {

    /**
     * The Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(SystemUserServiceImpl.class);

    /**
     * The resolver factory.
     */
    @Reference
    private ResourceResolverFactory resolverFactory;

    /**
     * The Constant READ_SUB_SERVICE.
     */
    private static final String READ_SUB_SERVICE = "samuel";

    /**
     * The Constant WRITE_SUB_SERVICE.
     */
    private static final String WRITE_SUB_SERVICE = "samuel";

    /**
     * 
     */
    @Override
    public ResourceResolver getWriteServiceResourceResolver() throws LoginException {
        final Map<String, Object> authenticationInfo = new HashMap<>();
        authenticationInfo.put(ResourceResolverFactory.SUBSERVICE, WRITE_SUB_SERVICE);
        LOG.debug("Getting write access serviceResourceResolver for param {}", authenticationInfo);
        return resolverFactory.getServiceResourceResolver(authenticationInfo);
    }

    /**
     * 
     */
    @Override
    public ResourceResolver getReadServiceResourceResolver() throws LoginException {
        final Map<String, Object> authenticationInfo = new HashMap<>();
        authenticationInfo.put(ResourceResolverFactory.SUBSERVICE, READ_SUB_SERVICE);
        LOG.debug("Getting read access serviceResourceResolver for param {}", authenticationInfo);
        return resolverFactory.getServiceResourceResolver(authenticationInfo);
    }

    /**
     * 
     */
    @Override
    public void closeResourceResolver(final ResourceResolver resolver) {
        if (resolver != null && resolver.isLive()) {
            resolver.close();
        }
    }
}
