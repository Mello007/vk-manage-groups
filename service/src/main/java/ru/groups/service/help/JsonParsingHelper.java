package ru.groups.service.help;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;

public class JsonParsingHelper {
    public static String findValueInJson(JsonNode actualObj, String value){
        return String.valueOf(actualObj.findValue(value)).replace("\"", "");
    }
}
