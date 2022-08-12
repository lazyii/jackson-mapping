package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.jsonpointer.JacksonUtil;
import com.jfinal.template.Directive;
import com.jfinal.template.Engine;
import com.jfinal.template.Env;
import com.jfinal.template.Template;
import com.jfinal.template.expr.ast.Expr;
import com.jfinal.template.expr.ast.ExprList;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.ParseException;
import com.jfinal.template.stat.Scope;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EnjoyTest {


    private static Engine engine = Engine.use();
    private static String source;

    @BeforeAll
    public static void init() {
        Engine.setFastMode(true);
        Engine.setChineseExpression(true);
        engine.addDirective("value", ValueDirective.class);
        engine.setDevMode(true);
        engine.setToClassPathSourceFactory();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[7];
        try (InputStream inputStream = JacksonUtil.class.getResourceAsStream("/demo.json")) {
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                baos.write(buffer, 0, length);
            }
            source = baos.toString();
        } catch (IOException e) {
            System.out.println("读取文件失败");
        }

    }

    @Test
    public void enjoy_jackson_Test() {
        try {
            Template template = engine.getTemplate("hello.enjoy");
            JSONObject json = JSON.parseObject(source);
            String result = template.renderToString(json);
            System.out.println(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static class ValueDirective extends Directive {

        private Expr valueExpr;
        private Expr patternExpr;

        public void setExprList(ExprList exprList) {
            int paraNum = exprList.length();
            if (paraNum == 0) {
                throw new ParseException("The parameter of #number directive can not be blank", location);
            }
            if (paraNum > 2) {
                throw new ParseException("Wrong number parameter of #number directive, two parameters allowed at most", location);
            }

            valueExpr = exprList.getExpr(0);
            patternExpr = (paraNum == 1 ? null : exprList.getExpr(1));
        }

        public void exec(Env env, Scope scope, Writer writer) {
            Object value = valueExpr.eval(scope);
            if (value == null) {
                write(writer, "null");
            } else {
                if (value instanceof String) {
                    value = "\"" + value + "\"";
                }
                write(writer, value.toString());
            }
        }
    }


}
