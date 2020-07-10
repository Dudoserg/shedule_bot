package com.shedule.shedule_bot.service.TgBot;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shedule.shedule_bot.service.TgBot.Methods.EditMessageText_Method;
import com.shedule.shedule_bot.service.TgBot.Methods.SendMessage_Method;
import com.shedule.shedule_bot.service.TgBot.Objects.SendMessageResult;
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
     * @throws JsonProcessingException      JsonProcessingException
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
     * @param token             токен бота, от лица которого будет отправлено сообщение
     * @param sendMessageMethod объект {SendMessage}
     * @return стату отправки
     * @throws Exception Exception
     */
    public SendMessageResult sendMessage(String token, SendMessage_Method sendMessageMethod)
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
        if (sendMessageMethod.getChat_id() == null)
            throw new Exception("required field 'chat_id' is not defined");
        payload.put("chat_id", sendMessageMethod.getChat_id());

        if (sendMessageMethod.getText() == null)
            throw new Exception("required field 'chat_id' is not defined");
        payload.put("text", sendMessageMethod.getText());

        if (sendMessageMethod.getParse_mode() != null)
            payload.put("parse_mode", sendMessageMethod.getParse_mode());

        if (sendMessageMethod.getDisable_web_page_preview() != null) {
            payload.put("disable_web_page_preview", sendMessageMethod.getDisable_web_page_preview());
        }
        if (sendMessageMethod.getDisable_notification() != null) {
            payload.put("disable_notification", sendMessageMethod.getDisable_notification());
        }
        if (sendMessageMethod.getReply_to_message_id() != null) {
            payload.put("reply_to_message_id", sendMessageMethod.getReply_to_message_id());
        }
        if (sendMessageMethod.getReply_markup() != null) {
            payload.put("reply_markup", sendMessageMethod.getReply_markup());
        }


        String json = new ObjectMapper().writeValueAsString(payload);
        System.out.println(json);
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
        ObjectMapper objectMapper = new ObjectMapper();
        SendMessageResult sendMessageResult = objectMapper.readValue(response, SendMessageResult.class);
        return sendMessageResult;
    }


    public SendMessageResult editMessageText(String token, EditMessageText_Method editMessageText_method) throws Exception {
        // create request url
        URIBuilder builder = new URIBuilder()
                .setScheme("https")
                .setHost(this.BASE_URL)
                .setPath("/bot" + token + "/editMessageText");

        String url = builder.toString();

        org.apache.commons.httpclient.HttpClient client = new HttpClient();
        PostMethod httpPost = new PostMethod(url);

        // create JSON request body
        Map<String, Object> payload = new HashMap<>();
        if (editMessageText_method.getChat_id() == null)
            throw new Exception("required field 'chat_id' is not defined");
        payload.put("chat_id", editMessageText_method.getChat_id());

        if(editMessageText_method.getMessage_id() == null)
            throw new Exception("required field 'message_id' is not defined");
        payload.put("message_id", editMessageText_method.getMessage_id());

        if(editMessageText_method.getInline_message_id() != null)
            payload.put("inline_message_id", editMessageText_method.getInline_message_id());

        if (editMessageText_method.getText() == null)
            throw new Exception("required field 'chat_id' is not defined");
        payload.put("text", editMessageText_method.getText());

        if (editMessageText_method.getParse_mode() != null)
            payload.put("parse_mode", editMessageText_method.getParse_mode());

        if (editMessageText_method.getDisable_web_page_preview() != null)
            payload.put("disable_web_page_preview", editMessageText_method.getDisable_web_page_preview());

        if (editMessageText_method.getReply_markup() != null)
            payload.put("reply_markup", editMessageText_method.getReply_markup());


        String json = new ObjectMapper().writeValueAsString(payload);
        System.out.println(json);
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
        ObjectMapper objectMapper = new ObjectMapper();
        SendMessageResult sendMessageResult = objectMapper.readValue(response, SendMessageResult.class);
        return sendMessageResult;
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
