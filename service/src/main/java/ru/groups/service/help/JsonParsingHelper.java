package ru.groups.service.help;

import com.couchbase.client.deps.com.fasterxml.jackson.databind.JsonNode;
import com.couchbase.client.deps.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@Service
public class JsonParsingHelper {

    private static final String USER_AGENT = "Mozilla/5.0";

    public static String findValueInJson(JsonNode actualObj, String value){
        return String.valueOf(actualObj.findValue(value)).replace("\"", "");
    }

    public static List<String> findValueInJson(JsonNode actualObj, String... massiveOfValues){
        List<String> values = new LinkedList<>();
        for (String massiveOfValue : massiveOfValues) {
            values.add(String.valueOf(actualObj.findValue(massiveOfValue)).replace("\"", ""));
        }
        return values;
    }

    public static JsonNode GetValueAndChangeJsonInString(String reqUrl) throws IOException{
        StringBuffer response = apiRequestForGetResponseFromServer(reqUrl);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.toString());
    }

    private static StringBuffer apiRequestForGetResponseFromServer(String reqUrl) throws IOException {
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
