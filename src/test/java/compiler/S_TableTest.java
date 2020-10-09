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

public class S_TableTest {
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

        assert AST != null;

        AST.makeSymTab(0);

        assertEquals(0, AST.symbolTableCheckError());
        AST.clearSymTable();
    }

    @Test
    public void test_array() {
        MyLex lexer = new MyLex();
        List<MyTok> tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("void main() {\n" +
                    "int gg = 2;\n" +
                    "int []arr = {12, gg, 33, -6};\n" +
                    "arr[gg] = 6;\n" +
                    "arr[0] = arr[gg];\n}");
            writer.close();
            tokens = lexer.lex(filename);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        MyParser parser = new MyParser(tokens);
        Node AST = parser.parse();

        assert AST != null;

        AST.makeSymTab(0);

        assertEquals(0, AST.symbolTableCheckError());
        AST.clearSymTable();
    }

    @Test
    public void test_bool() {
        MyLex lexer = new MyLex();
        List<MyTok> tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("void main() {\n" +
                    "int a = 3; int b = 5; int c = b;\n" +
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

        assert AST != null;

        AST.makeSymTab(0);

        assertEquals(0, AST.symbolTableCheckError());
        AST.clearSymTable();
    }

    @Test
    public void test_error() {
        MyLex lexer = new MyLex();
        List<MyTok> tokens = new ArrayList<>();

        try{
            FileWriter writer = new FileWriter(filename);
            writer.write("void main() {\n" +
                    "String str = a + b;\n" +
                    "int n = 2 + str;\n}");
            writer.close();
            tokens = lexer.lex(filename);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }

        MyParser parser = new MyParser(tokens);
        Node AST = parser.parse();

        assert AST != null;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {}
        }));
        AST.makeSymTab(0);

        assertEquals(1, AST.symbolTableCheckError());
        AST.clearSymTable();
    }
}
