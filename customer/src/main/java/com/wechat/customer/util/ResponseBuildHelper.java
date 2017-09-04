package com.wechat.customer.util;

import org.json.simple.JSONObject;

import javax.ws.rs.core.Response;

public class ResponseBuildHelper {
    public static Response buildResponse(Response.Status status, Object entity) {
        Response response = Response.status(status).entity(entity).build();
        return response;
}
    public static Response buildErrorResponse(Response.Status status, String errorMessage) {
        JSONObject result = new JSONObject();
        result.put("message",errorMessage);
        Response response = Response.status(status).entity(result).build();
        return response;
    }
}
