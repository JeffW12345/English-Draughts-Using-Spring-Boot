package com.github.jeffw12345.draughts.client.controller;

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

public class MessageToServerService {

    @Getter
    private HttpResponse httpResponse;

    public void sendMessage(String message) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://localhost:8080/");

            StringEntity stringEntity = new StringEntity(message);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-type", "text/plain");

            // TODO - Log request

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

    @Override
    public String toString() {
        HttpEntity httpEntity= httpResponse.getEntity();
        try {
            return httpEntity != null ? EntityUtils.toString(httpEntity) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
