package com.github.jeffw12345.draughts.models.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jeffw12345.draughts.client.Client;
import com.github.jeffw12345.draughts.models.response.ServerResponseToClient;
import lombok.Getter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Getter
public class MessageToServerService {
    private Client client;
    private HttpResponse httpResponse;

    public MessageToServerService(Client client) {
        this.client = client;
    }

    public void sendMessage(String message) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://localhost:8080/");

            StringEntity stringEntity = new StringEntity(message);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-type", "text/plain");

            // TODO - Log request and handle non 200s

            httpResponse = httpClient.execute(httpPost);

            responseLogging();

        } catch (IOException e) {
            throw new RuntimeException("Failed to send HTTP request", e);
        }
    }

    private void responseLogging() {
        //TODO - Log status and response string.
    }

    public int getStatusCodeForResponse(){
        return httpResponse.getStatusLine().getStatusCode();
    }

    public boolean isResponseStatusOK(){
        return httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }

    public String responseMessage() {
        HttpEntity httpEntity= httpResponse.getEntity();
        try {
            return httpEntity != null ? EntityUtils.toString(httpEntity) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerResponseToClient getServerResponseToClientObject() {
        HttpEntity httpEntity = httpResponse.getEntity();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (httpEntity != null) {
                String responseString = EntityUtils.toString(httpEntity);
                return objectMapper.readValue(responseString, ServerResponseToClient.class);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read server response", e);
        }
    }

}
