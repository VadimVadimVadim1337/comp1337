package compiler.AST;

public class AssignNode extends BinNode{

    public AssignNode(Node left, Node right){
        this.left = left;
        this.right = right;
        this.type = "Assign";
    }

    public void makeSymTab(int level) {
        isInit = true;
        if(left != null)
           left.makeSymTab(level);
        isInit = false;
        if(!typeForCheck.equals("def")) {
            if (right != null)
                right.makeSymTab(level);
            typeForCheck = "def";
        }
        initVarName = "0";

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
        String command;
        if(leftOperand.contains("DWORD")){
            switch (right.getType()) {
                case "Number":
                case "Variable":
                case "Arith":
                case "ArrayMember":
                    if(!rightOperandIndex.equals("0"))
                        asm.addMainCommand(rightOperandIndex);

                    asm.addMainCommand("\tmov     ebx, " + rightOperand + "\n");

                    if(!leftOperandIndex.equals("0"))
                        asm.addMainCommand(leftOperandIndex);
                    asm.addMainCommand("\tmov     " + leftOperand + ", ebx\n");
                    break;

                case "CallFunc":
                    asm.addStringLabel(0, "", "");
                    String label = ".NUM";
                    String leftOperandAddress = leftOperand.substring(10);
                    if(leftOperand.contains("rax")) {
                        asm.addMainCommand(leftOperandIndex);
                        asm.addMainCommand("\tlea     rbx, " + leftOperandAddress + "\n");
                        asm.addMainCommand("\tmov     rsi, rbx\n");
                        asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
                        asm.addMainCommand("\tmov     eax, 0\n");
                        asm.addMainCommand("\tcall    __isoc99_scanf\n");
                    }else {

                        asm.addMainCommand("\tlea     rax, " + leftOperandAddress + "\n");
                        asm.addMainCommand("\tmov     rsi, rax\n");
                        asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
                        asm.addMainCommand("\tmov     eax, 0\n");
                        asm.addMainCommand("\tcall    __isoc99_scanf\n");
                    }
                    break;
            }
        }else if(leftOperand.contains("QWORD")){
            String label;
            switch (right.getType()) {
                case "Variable":
                    asm.addMainCommand("\tmov     rax, " + rightOperand + "\n");
                    asm.addMainCommand("\tmov     " + leftOperand + ", rax\n");
                    break;
                case "StrLit":
                    label = ".LC" + asmStringsInc;
                    asmStringsInc++;
                    asm.addStringLabel(4, label, rightOperand);
                    asm.addMainCommand("\tmov     " + leftOperand + ", OFFSET FLAT:" + label + "\n");
                    break;
//                case "CallFunc":
//                    asm.addStringLabel(2, "", "");
//                    label = ".STR";
//                    asm.addMainCommand("\tmov     rax, " + leftOperand + "\n");
//                    asm.addMainCommand("\tmov     rsi, rax\n");
//                    asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
//                    asm.addMainCommand("\tmov     eax, 0\n");
//                    asm.addMainCommand("\tcall    __isoc99_scanf\n");
//                    break;
            }
        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[ASSIGN]");

        if(left != null)
            left.printNode(level + 1);
        if(right != null)
            right.printNode(level + 1);
    }
}
