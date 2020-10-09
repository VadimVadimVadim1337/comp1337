
package compiler;

import compiler.AST.Node;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest {
    private static final String filename = "progs/test.java";

    @Test
    public void test_func() {
        MyLex lexer = new MyLex();
        List<MyTok> tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("void main() {\n" +
                        "String str = \"aello World\";\n" +
                        "int n = System.in.read();\n" +
                        "System.out.println(n);\n}");
            writer.close();
            tokens = lexer.lex(filename);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        MyParser parser = new MyParser(tokens);
        Node AST = parser.parse();

        assertNotNull(AST);
    }

    @Test
    public void test_array() {
        MyLex lexer = new MyLex();
        List<MyTok> tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("void main() {\n" +
                    "int []arr = {12, gg, 33, -6};\n" +
                    "arr[i] = 6;\n" +
                    "arr[0] = arr[i];\n}");
            writer.close();
            tokens = lexer.lex(filename);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        MyParser parser = new MyParser(tokens);
        Node AST = parser.parse();

        assertNotNull(AST);
    }

    @Test
    public void test_bool() {
        MyLex lexer = new MyLex();
        List<MyTok> tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("void main() {\n" +
                    "if(a != b || a - c > 22){\n" +
                    "int n = a + b;\n" +
                    "System.out.println(n);\n" +
                    "}else {" +
                    "System.out.println(\"-\");\n}\n}");
            writer.close();
            tokens = lexer.lex(filename);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        MyParser parser = new MyParser(tokens);
        Node AST = parser.parse();

        assertNotNull(AST);
    }

    @Test
    public void test_error() {
        MyLex lexer = new MyLex();
        List<MyTok> tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("void main() {\n" +
                    "int n = a + ;\n" +
                    "System.out.println(n)\n" +
                    "System.out.println(\"-\");\n}");
            writer.close();
            tokens = lexer.lex(filename);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {}
        }));

        MyParser parser = new MyParser(tokens);
        Node AST = parser.parse();

        assertNull(AST);
    }
}
