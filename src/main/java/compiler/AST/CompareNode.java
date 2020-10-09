package compiler.AST;

public class CompareNode extends BinNode {

    public CompareNode(String op){
        this.op = op;
        right = null;
        left = null;
        this.type = "Compare";
    }

    public CompareNode(String op, Node left, Node right) {
        this.op = op;
        this.left = left;
        this.right = right;
        this.type = "Compare";
    }

    public String makeASM(){
        String leftOperand = left.makeASM();
        String leftOperandIndex = "0";
        if(!asmIndex.equals("0"))
            leftOperandIndex = asmIndex;
        asmIndex = "0";
        String rightOperand = right.makeASM();
        String rightOperandIndex = "0";
        if(!asmIndex.equals("0"))
            rightOperandIndex = asmIndex;
        asmIndex = "0";

        if(!leftOperandIndex.equals("0"))
            asm.addMainCommand(leftOperandIndex);
        asm.addMainCommand("\tmov     ebx, " + leftOperand + "\n");
        if(!rightOperandIndex.equals("0"))
            asm.addMainCommand(rightOperandIndex);
        asm.addMainCommand("\tmov     eax, " + rightOperand + "\n");
        asm.addMainCommand("\tcmp     ebx, eax\n");
        switch (op){
            case "==":
                return "jne";
            case "!=":
                return "je";
            case "<=":
                return "jg";
            case "<":
                return "jge";
            case ">=":
                return "jl";
            case ">":
                return "jle";
        }
        return "";
    }
    public void printNode(int level){
        printTabs(level);
        System.out.println("[COMPARE] " + op);

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
