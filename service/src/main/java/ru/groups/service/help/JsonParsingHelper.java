package ru.groups.service.help;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class JsonParsingHelper {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String API_VERSION = "5.21";

    public static String findValueInJson(JsonNode actualObj, String value){
        return String.valueOf(actualObj.findValue(value)).replace("\"", "");
    }

    public static List<String> findValueInJson(JsonNode actualObj, String... massivOfValues){
        List<String> values = new LinkedList<>();
        for (int i = 0; i < massivOfValues.length; i++) {
            values.add(String.valueOf(actualObj.findValue(massivOfValues[i])).replace("\"", ""));
        }
        return values;
    }

    public static JsonNode GetValueAndChangeJsonInString(String reqUrl) throws IOException{
        StringBuffer response = apiRequestForGetResponseFromServer(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.toString());
        return actualObj;
    }

    public static StringBuffer apiRequestForGetResponseFromServer(String reqUrl) throws IOException {
        URL obj = new URL(reqUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("UserVk-Agent", USER_AGENT);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response;
    }

}
