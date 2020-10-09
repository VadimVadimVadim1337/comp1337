package compiler.AST;

public class IfElseNode extends Node {
    private Node conditionNode;
    private BodyNode bodyNode;
    private IfElseNode elseNode;
    private int nodeType;//0-if, 1- else if, 2-else

    public IfElseNode(Node conditionNode, BodyNode bodyNode, int nodeType) {
        this.conditionNode = conditionNode;
        this.bodyNode = bodyNode;
        this.nodeType = nodeType;
        this.type = "IfElse";
    }

    public void setElseNode(IfElseNode elseNode) {
        this.elseNode = elseNode;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public IfElseNode getElseNode() {
        return elseNode;
    }

    public Node getConditionNode() {
        return conditionNode;
    }

    public BodyNode getBodyNode() {
        return bodyNode;
    }


    public void makeSymTab(int level){
        typeForCheck = "int";
        if(conditionNode != null)
            conditionNode.makeSymTab(level);
        typeForCheck = "def";

        symbolTable = symbolTable.addNextLevelTable();
        if(bodyNode != null)
            bodyNode.makeSymTab(level + 1);
        symbolTable = symbolTable.getPrevLevelTable();

        if(elseNode != null)
            elseNode.makeSymTab(level);
    }

    public String makeASM() {
        if(nodeType == 0){
            ifEndLabelNumberList.add(asmLabelNumber);
            asmLabelNumber++;
        }
        int endBodyLabelNumber = asmLabelNumber;
        asmLabelNumber++;
        if(elseNode != null)
            endLabelForBoolExpr = endBodyLabelNumber;
        else
            endLabelForBoolExpr = ifEndLabelNumberList.getLast();
        String command = "";
        if (conditionNode != null) {
            if(conditionNode.getType().equals("Bool OR")){
                beginLabelForBoolExpr = asmLabelNumber;
                asmLabelNumber++;
                command = conditionNode.makeASM();
                asm.addMainCommand("\t" + command + "     .L" + endLabelForBoolExpr + "\n");
                asm.addMainCommand(".L" + beginLabelForBoolExpr + ":\n");
            }else {
                command = conditionNode.makeASM();
                asm.addMainCommand("\t" + command + "     .L" + endLabelForBoolExpr + "\n");
            }
        }

        symbolTable = symbolTable.getNextLevelTable();
        if (bodyNode != null)
            bodyNode.makeASM();
        symbolTable = symbolTable.getPrevLevelTable();

        if (elseNode != null){
            asm.addMainCommand("\tjmp     .L" + ifEndLabelNumberList.getLast() +"\n");
            asm.addMainCommand(".L" + endBodyLabelNumber + ":\n");
            elseNode.makeASM();
        }else
            asm.addMainCommand(".L" + ifEndLabelNumberList.getLast() + ":\n");
        if(nodeType == 0)
            ifEndLabelNumberList.removeLast();
        return "";
    }

    public void printNode(int level) {
        printTabs(level);
        switch (nodeType){
            case 0:
                System.out.println("[IF]");
                break;
            case 1:
                System.out.println("[ELSE IF]");
                break;
            case 2:
                System.out.println("[ELSE]");
                break;
        }

        if(conditionNode != null) {
            printTabs(level + 1);
            System.out.println("[CONDITION]");
            conditionNode.printNode(level + 2);
        }
        if(bodyNode != null)
            bodyNode.printNode(level + 1);
        if(elseNode != null)
            elseNode.printNode(level + 1);
    }
}
