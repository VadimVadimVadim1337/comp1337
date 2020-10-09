package compiler;

import compiler.AST.Node;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main{
    public static void main(String []args) {
        String filename = "";
        String option = "";
        if(args.length != 1 && args.length != 2) {
            System.out.println("Wrong arguments!!");
            System.out.println("USAGE: $compiler [Options] <input_file>");
            System.exit(0);
        }else if(args.length == 2) {
            filename = args[1];
            option = args[0];
        }else
            filename = args[0];
        try{
            MyLex lexer = new MyLex();
            List<MyTok> toks = lexer.lex(filename);

            if(option.equals("--dump-tokens")) {
                lexer.print_Tokens();
                return;
            }
            MyParser parser = new MyParser(toks);
            Node AST_tree = parser.parse();
            if(AST_tree == null)
                return;

            AST_tree.makeSymTab(0);

            if(option.equals("--dump-ast")) {
                parser.printAST();
                AST_tree.printSymTable();
                return;
            }
            if(AST_tree.symbolTableCheckError() == 1)
                return;

            String asm = AST_tree.makeASM();
            if(option.equals("--dump-asm"))
                System.out.println(asm);
            else {
                Process compile = Runtime.getRuntime().exec("mkdir MyAsm");
                compile.waitFor();
                compile.destroy();
                FileWriter fileWriter = new FileWriter("MyAsm/prog.s");
                fileWriter.write(asm);
                fileWriter.close();
                compile = Runtime.getRuntime().exec("mkdir bin");
                compile.waitFor();
                compile.destroy();
                compile = Runtime.getRuntime().exec("gcc -no-pie MyAsm/prog.s -o bin/prog");
                compile.waitFor();
                compile.destroy();

            }

        }catch(IOException | InterruptedException er){
            System.err.println(er);
        }
    }
}
