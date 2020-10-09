package compiler.AST;

import compiler.S_Table;

public class NumberNode extends Node {
    private int number;
    private int line;
    private int pos;

    public NumberNode(int number){
        this.number = number;
        this.line = 0;
        this.pos = 0;
        this.type = "Number";
    }

    public NumberNode(int number, int line, int pos) {
        this.number = number;
        this.line = line;
        this.pos = pos;
        this.type = "Number";
    }

    public int getNumber() {
        return number;
    }

    public void makeSymTab(int level){
        if(!typeForCheck.equals("int") && !typeForCheck.equals("int/String")){
            S_Table.setError();
            System.out.println("Error at <" + line + ":" + pos + ">" + ": '" + number + "' is int, but expected " + typeForCheck + ".");
        }
    }

    public String makeASM(){
        return String.valueOf(number);
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[NUM] " + number);
    }
}
