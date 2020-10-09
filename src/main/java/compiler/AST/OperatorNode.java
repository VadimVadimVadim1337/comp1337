package compiler.AST;

public class OperatorNode extends Node {
    private String name;

    public OperatorNode(String name){
        this.name = name;
        this.type = "Operator";
    }

    public void makeSymTab(int level){
    }

    public String makeASM(){
        switch (name){
            case "break":
                asm.addMainCommand("\tjmp     .L" + whileEndLabelNumberList.getLast() + "\n");
                break;
            case "continue":
                asm.addMainCommand("\tjmp     .L" + whileBeginLabelNumberList.getLast() + "\n");
                break;
        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[OPERATOR]" + name);
    }
}
