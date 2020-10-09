package compiler.AST;

public class MethodNode extends Node {
    private String name;
    private Node arg;

    public MethodNode(String name){
        this.name = name;
        arg = null;
        this.type = "Method";
    }

    public void setArg(Node arg) {
        this.arg = arg;
    }

    public void makeSymTab(int level){
        if(arg != null)
            arg.makeSymTab(level);
    }

    public String makeASM(){
        return "";
    }

    public String makeASM(String address){
        StringBuilder indexCommands = new StringBuilder();
        switch (name){
            case ".charAt":
                switch (arg.getType()){
                    case "Number":
                        indexCommands.append("\tmov     rax, ").append(address).append("\n");
                        indexCommands.append("\tadd     rax, ").append(arg.makeASM()).append("\n");
                        indexCommands.append("\tmovzx   eax, BYTE PTR [rax]\n");
                        indexCommands.append("\tmovsx   eax, al\n");
                        asmIndex = indexCommands.toString();
                        return "eax";
                    case "Variable":
                        indexCommands.append("\tmov     eax, ").append(arg.makeASM()).append("\n");
                        indexCommands.append("\tmovsx   rdx, eax\n");
                        indexCommands.append("\tmov     rax, ").append(address).append("\n");
                        indexCommands.append("\tadd     rax, rdx\n");
                        indexCommands.append("\tmovzx   eax, BYTE PTR [rax]\n");
                        indexCommands.append("\tmovsx   eax, al\n");
                        asmIndex = indexCommands.toString();
                        return "eax";
                }

                break;
            case ".length":
                indexCommands.append("\tmov     rax, ").append(address).append("\n");
                indexCommands.append("\tmov     rdi, rax\n");
                indexCommands.append("\tcall    strlen\n");
                asmIndex = indexCommands.toString();
                return "eax";
        }
        return "";
    }

    public void printNode(int level){
        printTabs(level);
        System.out.println("[METHOD] " + name);
        if(arg != null)
            arg.printNode(level + 1);
    }
}
