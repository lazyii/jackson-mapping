package com.demo.jsonpointer;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.StringReader;

public class JakartaJsonPointer {


    private static String source = "{\"taxnum\":\"taxnum0000001\",\"companyName\":\"测试有限公司\",\"extInfo\":{\"*\":\"000@abc.com\",\"email\":\"abc@abc.com\",\"phone\":\"13112341234\"},\"deptIds\":[1,2,3,4.00000000000000000000000000000000001],\"others\":[{\"key1\":\"value10\",\"key2\":\"value20\"},{\"key1\":\"value11\",\"key2\":\"value21\"}]}";
    private static String source1 = "[1,2,3,4.00000000000000000000000000000000001]";

    public JsonObject getAllJsonPointer(String jsonStr) {
        JsonReader jsonReader = Json.createReader(new StringReader(source));

        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

}
