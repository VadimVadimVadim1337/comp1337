package compiler.AST;

public abstract class BinNode extends Node {
    protected String op;
    protected Node left;
    protected Node right;

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public void setLeft(Node node){
        this.left = node;
    }

    public void setRight(Node node){
        this.right = node;
    }

    public void makeSymTab(int level){
        if(left != null)
            left.makeSymTab(level);
        if(right != null)
            right.makeSymTab(level);
    }

    public String makeASM(){
        return "";
    }
}
