package compiler;

import java.util.HashMap;
import java.util.LinkedList;

public class S_Table {
    private S_Table prevLevelTable;
    private int nextLevelTableIndex;
    private HashMap<String, My_ID> symbolTable;
    private LinkedList<S_Table> nextLevelTables;
    private static int error;//0-не было встречено ошибок, 1 были

    public S_Table() {
        prevLevelTable = null;
        nextLevelTableIndex = 0;
        symbolTable = new HashMap<>();
        symbolTable.put("System.out.println", new My_ID("System.out.println", "void", 0, true));
        symbolTable.put("System.out.print", new My_ID("System.out.print", "void", 0, true));
        symbolTable.put("System.in.read", new My_ID("System.in.read", "int/String", 0, true));
        nextLevelTables = new LinkedList<>();
        error = 0;
    }

    public S_Table(S_Table prevLevelTable) {
        this.prevLevelTable = prevLevelTable;
        nextLevelTableIndex = 0;
        symbolTable = new HashMap<>();
        nextLevelTables = new LinkedList<>();
        //error = 0;
    }

    public S_Table addNextLevelTable(){
        nextLevelTables.add(new S_Table(this));
        return nextLevelTables.getLast();
    }

    public S_Table getNextLevelTable(){
        nextLevelTableIndex++;
        return nextLevelTables.get(nextLevelTableIndex - 1);
    }

    public S_Table getPrevLevelTable(){
        if(symbolTable.size() == 0){
            symbolTable.put(null, new My_ID(null, "", 0, false));
        }
        return prevLevelTable;
    }

    public My_ID getVariable(String name){
        My_ID variable = symbolTable.get(name);
        if(prevLevelTable == null)
            return variable;
        else if(variable == null)
            return prevLevelTable.getVariable(name);

        return variable;
    }

    public String getAsmOffset(){
        return String.valueOf(My_ID.getGlobalAsmOffset());
    }

    public static void setError() {
        S_Table.error = 1;
    }

    public static int getError() {
        return error;
    }

    public boolean add(String name, My_ID variable){
        if(this.getVariable(name) != null) {
            setError();
            return false;
        }
        symbolTable.put(name, variable);
        return true;
    }

    public void print(){
        if(S_Table.error != 0)
            return;

        symbolTable.values().forEach(My_ID::print);
        nextLevelTables.forEach(table ->{
            System.out.println("---------------------------------------------------");
            table.print();
        });
    }

}
