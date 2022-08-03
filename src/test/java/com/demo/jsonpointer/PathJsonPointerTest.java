package com.demo.jsonpointer;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class PathJsonPointerTest {

    @Test
    public void createJsonNodeByPath() {
        String path1 = "";
        String path2 = "/";
        String path3 = "/a";
        String path4 = "/a/b/c";
        String path5 = "/a/b[*]/c";
        String path6 = "/a/b/c[*]";
        String path7 = "/a/b[*]/c[*]";
        String path31 = "/a/af1";
        String path32 = "/a/af2";

        List<String> paths = Arrays.asList(path1, path2, path3, path4, path5, path6, path7, path31, path32);
        JsonNode node = createJsonNodeByPath(paths.get(3));
        System.out.println(node);

        /*for (int i = 0; i < paths.size(); i++) {
            JsonNode node = createJsonNodeByPath(paths.get(i));
            System.out.println(node);
        }*/
    }

    private JsonNode createJsonNodeByPath(String path) {
        /*if (path == "") {
            return null;
        } else {
            JsonPointer ptr = JsonPointer.compile(path);
            m rootNode = new ObjectNode(JsonNodeFactory.withExactBigDecimals(true));
            while (!ptr.equals(JsonPointer.empty())) {
                String key = ptr.getMatchingProperty();
                if (!rootNode.has(key)) {
                    if (ptr.tail() == JsonPointer.empty()) {
                        rootNode.putNull(key);
                    } else if (ptr.tail().getMatchingProperty().endsWith("[*]")) {
                        String arrKey = ptr.tail().getMatchingProperty().replace("[*]", "");
                        rootNode.putArray(arrKey);
                    } else {
                        rootNode.putObject(key);
                        rootNode = rootNode.at(JsonPointer.SEPARATOR + key);
                    }
                }
                ptr = ptr.tail();
            }
            return rootNode;
        }*/
        return null;
    }
}
