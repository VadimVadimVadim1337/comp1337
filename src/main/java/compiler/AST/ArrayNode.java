package compiler.AST;

import compiler.My_ID;

import java.util.List;

public class ArrayNode extends Node {
    private List<Node> members;

    public ArrayNode(List<Node> members) {
        this.members = members;
        this.type = "Array";
    }

    public void makeSymTab(int level){
        My_ID id = symbolTable.getVariable(initVarName);
        int arrayOffset = members.size() * 4;
        id.setAsmOffset(arrayOffset);
        members.forEach(member -> {if(member!=null) member.makeSymTab(level);});
    }

    public String makeASM(){
        int arrayOffset = symbolTable.getVariable(initVarName).getAsmOffset();
        for(Node member : members){
            String leftOperand = "DWORD PTR [rbp-" + arrayOffset + "]";
            String rightOperand = member.makeASM();
            switch (member.getType()){
                case "Number":
                    asm.addMainCommand("\tmov     " + leftOperand + ", " + rightOperand + "\n");
                    break;
                case "Variable":
                    asm.addMainCommand("\tmov     eax, " + rightOperand + "\n");
                    asm.addMainCommand("\tmov     " + leftOperand + ", eax\n");
                    break;
                case "Arith":
                    asm.addMainCommand("\tmov     " + leftOperand + ", " + rightOperand + "\n");
                    break;
            }
            arrayOffset -= 4;
        }
        return "";
    }

    public void printNode(int level) {
        this.printTabs(level);
        System.out.println("[ARRAY]");
        members.forEach(member -> {if(member!=null) member.printNode(level + 1);});
    }
}
