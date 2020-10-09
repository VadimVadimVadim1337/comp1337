package compiler.AST;

import compiler.My_ID;
import compiler.S_Table;
import compiler.MyTok;

public class ArrayPointerNode extends VariableNode{

    public ArrayPointerNode(MyTok token) {
        super(token);
        this.type = "ArrayPointer";
    }


    public void makeSymTab(int level){
        if(symbolTable.getVariable(token.getLexeme()) == null) {
            S_Table.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't declared in this scope.");
        }
    }

    public void makeSymTab(int level, String type){
        if(!type.equals("int")){
            S_Table.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos()  + ">" + ": array '" + token.getLexeme() + "' has incorrect type.");
            return;
        }
        typeForCheck = type;
        type = type + " array";
        initVarName = token.getLexeme();
        My_ID id = new My_ID(token.getLexeme(), type, level, isInit);
        if(!symbolTable.add(token.getLexeme(), id))
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos()  + ">" + ": variable '" + token.getLexeme() + "' is already exist in this scope.");
    }

    public String makeASM(){
        initVarName = token.getLexeme();
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ARRAY_POINTER] " + token.getLexeme());
    }
}
