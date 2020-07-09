package com.shedule.shedule_bot.service.TgBot;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shedule.shedule_bot.service.TgBot.Methods.SendMessageObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TgBot {
    private final String BASE_URL = "api.telegram.org";


    /**
     * Сообщаем адрес телеграму, по которому будем принимать от него вебхуки
     *
     * @param token   токен бота
     * @param MY_HOST базовый адрес сервера
     * @return статус, результат запроса
     * @throws JsonProcessingException JsonProcessingException
     * @throws UnsupportedEncodingException UnsupportedEncodingException
     */
    public boolean setWebhook(String token, String MY_HOST) throws JsonProcessingException, UnsupportedEncodingException {
        // create request url
        URIBuilder builder = new URIBuilder()
                .setScheme("https")
                .setHost(this.BASE_URL)
                .setPath("/bot" + token + "/setwebhook");

        String url = builder.toString();

        org.apache.commons.httpclient.HttpClient client = new HttpClient();
        PostMethod httpPost = new PostMethod(url);

        // create JSON request body
        Map<String, Object> payload = new HashMap<>();

        if (token != null && !token.isEmpty())
            payload.put("url", MY_HOST + "/tg/" + token);
        else
            payload.put("url", "");

        String json = new ObjectMapper().writeValueAsString(payload);

        StringRequestEntity requestEntity = new StringRequestEntity(
                json,
                "application/json",
                "UTF-8");

        httpPost.setRequestEntity(requestEntity);

        // set headers
        httpPost.addRequestHeader("Accept", "application/json");
        httpPost.addRequestHeader("Content-type", "application/json");

        // set proxy
        //this.clientSetProxy(client);

        // execute request
        String response = "";
        try {
            response = this.exeCuteAndReturnResult(client, httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.equals("error")) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "error"
            );
//            return TgResult.createError(response);
        }

        //TgResult tgResult = mapper.readValue(response, TgResult.class);

        return true;
    }


    /**
     * Отправляет в чат сообщение (настраеваемый объект SendMessage)
     *
     * @param token токен бота, от лица которого будет отправлено сообщение
     * @param sendMessageObject объект {SendMessage}
     * @return стату отправки
     * @throws Exception Exception
     */
    public boolean sendMessage(String token, SendMessageObject sendMessageObject)
            throws Exception {
        // create request url
        URIBuilder builder = new URIBuilder()
                .setScheme("https")
                .setHost(this.BASE_URL)
                .setPath("/bot" + token + "/sendMessage");

        String url = builder.toString();

        org.apache.commons.httpclient.HttpClient client = new HttpClient();
        PostMethod httpPost = new PostMethod(url);

        // create JSON request body
        Map<String, Object> payload = new HashMap<>();
        if (sendMessageObject.getChat_id() == null)
            throw new Exception("required field 'chat_id' is not defined");
        payload.put("chat_id", sendMessageObject.getChat_id());

        if (sendMessageObject.getText() == null)
            throw new Exception("required field 'chat_id' is not defined");
        payload.put("text", sendMessageObject.getText());

        if (sendMessageObject.getParse_mode() != null)
            payload.put("parse_mode", sendMessageObject.getParse_mode());

        if (sendMessageObject.getDisable_web_page_preview() != null) {
            payload.put("disable_web_page_preview", sendMessageObject.getDisable_web_page_preview());
        }
        if (sendMessageObject.getDisable_notification() != null) {
            payload.put("disable_notification", sendMessageObject.getDisable_notification());
        }
        if (sendMessageObject.getReply_to_message_id() != null) {
            payload.put("reply_to_message_id", sendMessageObject.getReply_to_message_id());
        }
        if (sendMessageObject.getReply_markup() != null) {
            payload.put("reply_markup", sendMessageObject.getReply_markup());
        }


        String json = new ObjectMapper().writeValueAsString(payload);

        StringRequestEntity requestEntity = new StringRequestEntity(
                json,
                "application/json",
                "UTF-8");

        httpPost.setRequestEntity(requestEntity);

        // set headers
        httpPost.addRequestHeader("Accept", "application/json");
        httpPost.addRequestHeader("Content-type", "application/json");

        // execute request
        String response = "";
        try {
            response = this.exeCuteAndReturnResult(client, httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.equals("error")) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "error"
            );
//            return TgResult.createError(response);
        }
        System.out.println(response);
        //TgResult tgResult = mapper.readValue(response, TgResult.class);

        return true;
    }


    /**
     * Выполнить запрос клиента и вернуть результат
     *
     * @param client HttpClient
     * @param method HttpMethod, метод которым отправляем запрос GET\POST...
     * @return результат выполнения запроса
     * @throws IOException IOException
     */
    private String exeCuteAndReturnResult(HttpClient client, HttpMethod method) throws IOException {
        String response = "error";
        try {
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK)
                response = method.getResponseBodyAsString();
        } finally {
            method.releaseConnection();
        }
        return response;
    }
}
