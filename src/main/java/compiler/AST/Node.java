package compiler.AST;

import compiler.MyAsm;
import compiler.S_Table;

import java.util.LinkedList;

public abstract class Node {
    protected static S_Table symbolTable = new S_Table();
    protected static MyAsm asm = new MyAsm();
    protected static boolean isInit = false;
    protected static String initVarName = "0";
    protected static String typeForCheck = "def";
    protected static int asmStringsInc = 0;
    protected String type;
    protected static String asmIndex = "0";
    protected static int asmLabelNumber = 0;
    protected static LinkedList<Integer> whileBeginLabelNumberList = new LinkedList<>();
    protected static LinkedList<Integer> whileEndLabelNumberList = new LinkedList<>();
    protected static LinkedList<Integer> ifEndLabelNumberList = new LinkedList<>();
    protected static int beginLabelForBoolExpr;
    protected static int endLabelForBoolExpr;
    public abstract void printNode(int level);
    public abstract void makeSymTab(int level);
    public abstract String makeASM();

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void printTabs(int level){
        if(level >= 2) {
            for (int i = 0; i < level - 1; i++) {
                System.out.print("|   ");
            }
        }
        if(level >= 1){
            System.out.print("|---");
        }
    }
    public void printSymTable(){
        symbolTable.print();
    }
    public int symbolTableCheckError(){
        return S_Table.getError();
    }
    public void clearSymTable(){
        symbolTable = new S_Table();
    }

}