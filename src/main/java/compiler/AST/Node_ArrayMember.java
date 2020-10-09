package compiler.AST;

import compiler.My_ID;
import compiler.S_Table;
import compiler.MyTok;

public class Node_ArrayMember extends Node {
    private MyTok token;
    private Node index;

    public Node_ArrayMember(MyTok token){
        this.token = token;
        this.index = null;
        this.type = "ArrayMember";
    }

    public void setIndex(Node index) {
        this.index = index;
    }

    public void makeSymTab(int level){
        My_ID id = symbolTable.getVariable(token.getLexeme());
        if(id == null) {
            S_Table.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't declared in this scope.");
        }else if(!id.getType().equals("int array")){
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't array.");
        }else if(!id.isInit() || token.getLexeme().equals(initVarName)) {
            S_Table.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' isn't initialized.");
        }else if(isInit){
            isInit = false;
            typeForCheck = "int";
        }else if(!typeForCheck.equals("int") && !typeForCheck.equals("int/String")){
            S_Table.setError();
            System.out.println("Error at <" + token.getLine() + ":" + token.getPos() + ">" + ": variable '" + token.getLexeme() + "' is " + id.getType() +", but expected " + typeForCheck + ".");
        }
        String tempTypeForCheck = typeForCheck;
        typeForCheck = "int";
        if(index != null)
            index.makeSymTab(level);
        typeForCheck = tempTypeForCheck;
    }

    public String makeASM(){
        if(index != null){
            My_ID id = symbolTable.getVariable(token.getLexeme());
            int arrayOffset = id.getAsmOffset();
            String addres = "";
            String indexAddress = index.makeASM();
            StringBuilder indexCommands = new StringBuilder();
            switch (index.getType()){
                case "Number":
                    int arrayMemberOffset = arrayOffset - Integer.parseInt(indexAddress) * 4;
                    addres = "DWORD PTR [rbp-" + arrayMemberOffset + "]";
                    break;
                case "Variable":
                    indexCommands.append("\tmov     eax, ").append(indexAddress).append("\n");
                case "Arith":
                    indexCommands.append("\tcdqe\n");
                    asmIndex = indexCommands.toString();
                    addres = "DWORD PTR [rbp-" + arrayOffset + "+rax*4]";
                    break;

            }
            return addres;
        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ARRAY_MEMBER] " + token.getLexeme());
        if(index != null)
            index.printNode(level + 1);
    }
}
