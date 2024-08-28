package com.adobe.aem.guides.project2.core.servlets;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import javax.servlet.Servlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

@Component(
    service = Servlet.class,
    property = {
        "sling.servlet.methods=GET",
        "sling.servlet.paths=/bin/random"
    }
)
public class RandomGeneratorServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(SlingHttpServletRequest req, SlingHttpServletResponse res) throws IOException {
        String type = req.getParameter("type");

        if ("Number".equalsIgnoreCase(type)) {
            String result = generateRandomNumbers(6);
            res.setContentType("text/plain");
            res.getWriter().write(result);
        } else if ("Letters".equalsIgnoreCase(type)) {
            String result = generateRandomLetters(6);
            res.setContentType("text/plain");
            res.getWriter().write(result);
        } else if ("Random".equalsIgnoreCase(type)) {
            String result = generateRandomNumbersAndLetters(3, 3);
            res.setContentType("text/plain");
            res.getWriter().write(result);
        } else {
            res.setContentType("text/plain");
            res.getWriter().write("Invalid parameter. Use 'Number', 'Letters', or 'Random'.");
        }
    }

    private String generateRandomNumbers(int length) {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateRandomLetters(int length) {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < length; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        return sb.toString();
    }

    private String generateRandomNumbersAndLetters(int numCount, int letterCount) {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(numCount + letterCount);
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        
        for (int i = 0; i < numCount; i++) {
            sb.append(random.nextInt(10));
        }
        
        for (int i = 0; i < letterCount; i++) {
            sb.append(letters.charAt(random.nextInt(letters.length())));
        }
        return sb.toString();
    }
}
