import groovy.text.SimpleTemplateEngine

class GroovyJsonStringTemplateTest {


    /**
     * Use <%= ..%> if there is rendering of value.
     * Use <% .. %> if there is flow control handling ( if/else, for loop,... )
     * @param args
     */
    static void main(String[] args) {
        def text = '''
<%= name %>
<% for (other in others) { %>  for --- <%= other.key1 %>  <% } %>
<% println ''  %>
<% println '***'  %>
<% println '***'%>
<% others.each { println it.key1 } %>
'''
        def obj = [name: "xiaohong", "others": [[key1: "value1"], [key1: "value2"], [key1: "value3"]]]
        def engine = new SimpleTemplateEngine()
        def writeable = engine.createTemplate(text).make(obj)
        def result = writeable.toString()
        println result
    }


}
