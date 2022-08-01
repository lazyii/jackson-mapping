package com.demo.jsonpointer;

import jakarta.json.Json;
import jakarta.json.JsonPointer;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.json.JsonValue.ValueType;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class JakartaPointerTest {

    private static String source = "{\"taxnum\":\"taxnum0000001\",\"companyName\":\"测试有限公司\",\"extInfo\":{\"*\":\"000@abc.com\",\"email\":\"abc@abc.com\",\"phone\":\"13112341234\"},\"deptIds\":[1,2,3,4.00000000000000000000000000000000001],\"others\":[{\"key1\":\"value10\",\"key2\":\"value20\"},{\"key1\":\"value11\",\"key2\":\"value21\"}]}";
    private static String source1 = "[1,2,3,4.00000000000000000000000000000000001]";


    @Test
    public void jsonNode() {
        JsonReader jsonReader = Json.createReader(new StringReader(source));

        JsonValue jsonValue = jsonReader.readValue();
        jsonReader.close();
        JsonPointer jp = Json.createPointer("");
        JsonValue jv = null;
        if (jsonValue.getValueType()== ValueType.OBJECT) {
            jv = jp.getValue(jsonValue.asJsonObject());
        } else if(jsonValue.getValueType()== ValueType.ARRAY){
            jv = jp.getValue(jsonValue.asJsonArray());
        }
        System.out.println(jv.toString());

    }

}
