package compiler;

import java.util.HashMap;
import java.util.Map;

public class MyAsm {
    private String asm;
    private StringBuilder main;
    private HashMap<String, String> stringLabels;
    public MyAsm(){
        main = new StringBuilder();
        stringLabels = new HashMap<>();
    }

    public void addStringLabel(int type, String key, String value){
        switch (type){
            case 0:
                stringLabels.put(".NUM:", "\t.string \"%d\"");
                break;
            case 1:
                stringLabels.put(".NUM1:", "\t.string \"%d\\n\"");
                break;
            case 2:
                stringLabels.put(".STR:", "\t.string \"%s\"");
                break;
            case 3:
                stringLabels.put(".STR1:", "\t.string \"%s\\n\"");
                break;
            case 4:
                stringLabels.put(key + ":", "\t.string \"" + value + "\"");
                break;
            case 5:
                stringLabels.put(key + ":", "\t.string \"" + value + "\\n\"");
                break;
        }
    }

    public void addMainCommand(String command){
        main.append(command);
    }

    public String buildASM(String maxOffset){
        main.append("\tnop\n" +
                "\tleave\n" +
                "\tret");
        StringBuilder temp = new StringBuilder();
        temp.append(".intel_syntax noprefix\n.global main\n\n");
        for(Map.Entry<String, String> label : stringLabels.entrySet()){
            temp.append(label.getKey()).append("\n");
            temp.append("\t").append(label.getValue()).append("\n");
        }
        temp.append("main:\n" + "\tpush    rbp\n" + "\tmov     rbp, rsp\n" + "\tsub     rsp, ").append(maxOffset).append("\n");
        return temp.toString() + main.toString();
    }

}
