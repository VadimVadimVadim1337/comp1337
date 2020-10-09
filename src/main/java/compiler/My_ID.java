package compiler;

public class My_ID {
    private String name;
    private String type;
    private int level;
    private boolean isInit;
    private static int globalAsmOffset = 0;
    private int asmOffset;


    public My_ID(String name, String type, int level, boolean isInit) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.isInit = isInit;
        switch (type){
            case "int":
                globalAsmOffset += 4;
                this.asmOffset = globalAsmOffset;
                break;
            case "String":
                globalAsmOffset += 8;
                this.asmOffset = globalAsmOffset;
                break;
            default:
                this.asmOffset = 0;
        }
    }

    public static int getGlobalAsmOffset() {
        globalAsmOffset += 4;
        return globalAsmOffset;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getAsmOffset() {
        return asmOffset;
    }

    public void setAsmOffset(int asmOffset) {
        globalAsmOffset += asmOffset;
        this.asmOffset = globalAsmOffset;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public boolean isInit() {
        return isInit;
    }

    public int getLevel() {
        return level;
    }

    public void print(){
        if(name != null || type != null) {
            System.out.println("My_ID: " + name + " : " + type + ", level-" + level + ", offset: " + asmOffset);
        }
    }
}
