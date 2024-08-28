package com.adobe.aem.guides.project2.core.servlets;

import javax.servlet.Servlet;
import java.io.IOException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;

@Component(service = Servlet.class, immediate = true, 
          property = {"sling.servlet.paths=/bin/demo/example"})
public class WorkflowServlet extends SlingAllMethodsServlet {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res) {
        String status = "";
        try {
            res.setContentType("text/plain");
            ResourceResolver resolver = req.getResourceResolver();
            WorkflowSession workflowSession = resolver.adaptTo(WorkflowSession.class);
            
            if (workflowSession != null) {
                WorkflowModel model = workflowSession.getModel("/var/workflow/models/page-version-creation");
                WorkflowData payload = workflowSession.newWorkflowData("JCR_PATH","/content/project2/us");
                status = workflowSession.startWorkflow(model, payload).getState();
                res.getWriter().write("Workflow Executed Successfully: " + status);
            } else {
                res.getWriter().write("Failed to adapt ResourceResolver to WorkflowSession");
            }
        } catch (Exception e) {
            logger.error("Error executing workflow", e);
            try {
                res.getWriter().write("Workflow execution failed: " + e.getMessage());
            } catch (IOException ioException) {
                logger.error("Error writing response", ioException);
            }
        }
    }
}
