package com.adobe.aem.guides.project2.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component(service = Servlet.class, property = {
        Constants.SERVICE_DESCRIPTION + "=Text to Speech API Integration Servlet",
        "sling.servlet.methods=" + "POST",
        "sling.servlet.paths=" + "/bin/integrate/tts"
})
public class TextToSpeechServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        // Reading parameters from the request (fall back to defaults if not provided)
        String text = request.getParameter("text") != null ? request.getParameter("text") : "Hey its me, Suneetha";
        String voiceCode = request.getParameter("voice_code") != null ? request.getParameter("voice_code") : "en-US-1";
        String speed = request.getParameter("speed") != null ? request.getParameter("speed") : "1.00";
        String pitch = request.getParameter("pitch") != null ? request.getParameter("pitch") : "1.00";
        String outputType = request.getParameter("output_type") != null ? request.getParameter("output_type")
                : "audio_url";

        // URL encode text to handle special characters
        String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());

        // Construct URL parameters
        String urlParameters = String.format("voice_code=%s&text=%s&speed=%s&pitch=%s&output_type=%s",
                voiceCode, encodedText, speed, pitch, outputType);

        String apiUrl = "https://cloudlabs-text-to-speech.p.rapidapi.com/synthesize";

        // Open a connection to the API
        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("X-RapidAPI-Key", "5ba86e56f2mshf3d1ae075ea0fa2p1a02b7jsnb80343e10198");
        connection.setRequestProperty("X-RapidAPI-Host", "cloudlabs-text-to-speech.p.rapidapi.com");
        connection.setDoOutput(true);

        // Send the POST request with the parameters
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = urlParameters.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Check the response status
        int status = connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            // Read and return the JSON response from the TTS API
            try (java.io.InputStream is = connection.getInputStream()) {
                String jsonResponse = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonResponse);
            }
        } else {
            // Handle error response from TTS API
            try (java.io.InputStream errorStream = connection.getErrorStream()) {
                String errorResponse = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(
                        "{\"error\": \"Failed to get response from TTS API.\", \"details\": " + errorResponse + "}");
            }
        }
    }
}
