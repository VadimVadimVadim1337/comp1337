package compiler.AST;

import compiler.S_Table;
import compiler.MyTok;

public class StrLitNode extends Node {
    private String str;
    private MyTok token;

    public StrLitNode(String str, MyTok token) {
        this.str = str;
        this.token = token;
        this.type = "StrLit";
    }

    public String getStr() {
        return str;
    }
    public void makeSymTab(int level) {
        if(!typeForCheck.equals("String") && !typeForCheck.equals("int/String")){
            S_Table.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": '" + token.getLexeme() + "' is String Literal, but expected " + typeForCheck + ".");
        }
    }

    public String makeASM(){
        return str.replace("\"", "");
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[STR_LIT] " + str);
    }
}
