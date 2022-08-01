package com.demo.jsonpointer;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

public class JacksonPointerTest {

    private static String source = "{\"\":\"kong\",\"taxnum\":\"taxnum0000001\",\"companyName\":\"测试有限公司\",\"extInfo\":{\"*\":\"000@abc.com\",\"email\":\"abc@abc.com\",\"phone\":\"13112341234\"},\"deptIds\":[1,2,3,4.00000000000000000000000000000000001],\"others\":[{\"key1\":\"value10\",\"key2\":\"value20\"},{\"key1\":\"value11\",\"key2\":\"value21\"}]}";
    private static String source1 = "[1,2,3,4.00000000000000000000000000000000001]";


    @Test
    public void jsonNode() {
        try {
            JacksonPointer jp = new JacksonPointer();
            JsonNode jsonNode = jp.getAllJsonPointer(source);

            System.out.println("nodeType: " + jsonNode.getNodeType() + "   " + jsonNode.isPojo());
            System.out.println(jsonNode.fieldNames());
            JsonPointer.valueOf("");
            jsonNode.at("/others");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void jsonNode1() {
        try {
            JacksonPointer jp = new JacksonPointer();
            JsonNode jsonNode = jp.getAllJsonPointer(source1);

            System.out.println("nodeType: " + jsonNode.getNodeType() + "   " + jsonNode.isPojo());
            System.out.println(jsonNode.fieldNames());
            JsonPointer.valueOf("");


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
