package compiler.AST;

public class CallFuncNode extends Node {
    private String name;
    private Node arg;

    public CallFuncNode(String name){
        this.name = name;
        arg = null;
        this.type = "CallFunc";
    }

    public void setArg(Node arg) {
        this.arg = arg;
    }

    public void makeSymTab(int level){
        typeForCheck = "int/String";
        if(arg != null)
            arg.makeSymTab(level);
        typeForCheck = "def";
    }

    public String makeASM(){
        if(arg != null){
            switch (arg.getType()){
                case "Variable":
                case "ArrayMember":
                    String argAddress = arg.makeASM();
                    String argIndex = asmIndex;
                    if(!argIndex.equals("0"))
                        asm.addMainCommand(argIndex);
                    asmIndex = "0";
                    if(argAddress.contains("DWORD")){
                        String label = "";
                        switch (name){
                            case "System.out.print":
                                asm.addStringLabel(0, "", "");
                                label = ".NUM";
                                break;
                            case "System.out.println":
                                asm.addStringLabel(1, "", "");
                                label = ".NUM1";
                                break;
                        }
                        asm.addMainCommand("\tmov     eax, " + argAddress + "\n");
                        asm.addMainCommand("\tmov     esi, eax\n");
                        asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
                        asm.addMainCommand("\tmov     eax, 0\n");
                        asm.addMainCommand("\tcall    printf\n");
                    }else if(argAddress.contains("QWORD")){
                        String label = "";
                        switch (name){
                            case "System.out.print":
                                asm.addStringLabel(2, "", "");
                                label = ".STR";
                                break;
                            case "System.out.println":
                                asm.addStringLabel(3, "", "");
                                label = ".STR1";
                                break;
                        }
                        asm.addMainCommand("\tmov     rax, " + argAddress + "\n");
                        asm.addMainCommand("\tmov     rsi, rax\n");
                        asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
                        asm.addMainCommand("\tmov     eax, 0\n");
                        asm.addMainCommand("\tcall    printf\n");
                    }
                    break;
                case "Number":
                case "StrLit":
                    String argValue = arg.makeASM();
                    String label = ".LC" + String.valueOf(asmStringsInc);
                    asmStringsInc++;
                    switch (name){
                        case "System.out.print":
                            asm.addStringLabel(4, label, argValue);
                            break;
                        case "System.out.println":
                            asm.addStringLabel(5, label, argValue);
                            break;
                    }
                    asm.addMainCommand("\tmov     edi, OFFSET FLAT:" + label + "\n");
                    asm.addMainCommand("\tmov     eax, 0\n");
                    asm.addMainCommand("\tcall    printf\n");
                    break;
            }
        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[CALL] " + name);
        if(arg != null)
            arg.printNode(level + 1);
    }
}
