package com.wechat.customer.util;

import java.io.InputStream;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601Utils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class JsonHelper {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapper.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true);
		mapper.configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);

	}

	/**
	 * return jackson ObjectMapper,for internal use only
	 * 
	 * @return
	 */
	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

	/**
	 * convert object to json
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSON(Object object) {
		try {
			String jsonString = mapper.writeValueAsString(object);
			return jsonString;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * convert object to jackson json node
	 * 
	 * @param object
	 * @return
	 */
	public static JsonNode toJsonNode(Object object) {
		try {
			return mapper.convertValue(object, JsonNode.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String jsonNodeToString(JsonNode jsonNode) {
		try {
			return mapper.writeValueAsString(jsonNode);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * parse json string as jackson JsonNode
	 * 
	 * @param json
	 * @return
	 */
	public static JsonNode parseJson(String json) {
		try {
			return mapper.readTree(json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * convert object from json
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T fromJSON(String json, Class<T> clazz) {
		try {
			return (T) mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T fromJSON(InputStream is, Class<T> clazz) {
		try {
			return (T) mapper.readValue(is, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return ISO8601Utils.format(date);
	}


	public static Object parse(String jsonStr) {
			Object result = null;
			if(jsonStr != null) {
				try {
					result = new JSONParser().parse(jsonStr);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
}


