package compiler.AST;

public class Node_While extends Node {
    private Node conditionNode;
    private BodyNode bodyNode;

    public Node_While(Node conditionNode, BodyNode bodyNode) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
        this.type = "While";
    }

    public void makeSymTab(int level) {
        typeForCheck = "int";
        if(conditionNode != null)
            conditionNode.makeSymTab(level);
        typeForCheck = "def";

        symbolTable = symbolTable.addNextLevelTable();
        //symbolTable = symbolTable.getNextLevelTable();
        if(bodyNode != null)
            bodyNode.makeSymTab(level + 1);
        symbolTable = symbolTable.getPrevLevelTable();
    }

    public String makeASM(){
        asm.addMainCommand(".L" + asmLabelNumber + ":\n");
        whileBeginLabelNumberList.add(asmLabelNumber);
        asmLabelNumber++;

        whileEndLabelNumberList.add(asmLabelNumber);
        endLabelForBoolExpr = asmLabelNumber;
        asmLabelNumber++;
        String command = "";
        if(conditionNode != null) {
            if(conditionNode.getType().equals("Bool OR")){
                beginLabelForBoolExpr = asmLabelNumber;
                asmLabelNumber++;
                command = conditionNode.makeASM();
                asm.addMainCommand("\t" + command + "     .L" + whileEndLabelNumberList.getLast() + "\n");
                asm.addMainCommand(".L" + beginLabelForBoolExpr + ":\n");
            }else {
                command = conditionNode.makeASM();
                asm.addMainCommand("\t" + command + "     .L" + whileEndLabelNumberList.getLast() + "\n");
            }
        }

        symbolTable = symbolTable.getNextLevelTable();
        if(bodyNode != null)
            bodyNode.makeASM();
        symbolTable = symbolTable.getPrevLevelTable();
        asm.addMainCommand("\tjmp     .L" + whileBeginLabelNumberList.getLast() + "\n");
        asm.addMainCommand(".L" + whileEndLabelNumberList.getLast() + ":\n");
        whileBeginLabelNumberList.removeLast();
        whileEndLabelNumberList.removeLast();
        return "";
    }

    public void printNode(int level) {
        printTabs(level);
        System.out.println("[WHILE]");
        printTabs(level + 1);
        System.out.println("[CONDITION]");

        if(conditionNode != null)
            conditionNode.printNode(level + 2);
        if(bodyNode != null)
            bodyNode.printNode(level + 1);
    }
}
