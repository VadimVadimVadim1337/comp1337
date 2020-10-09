package compiler;

import java.util.Objects;

public class MyTok {
    private String type;
    private String lexeme;
    private int line;
    private int pos;

    public MyTok(String type, String lexeme, int line, int pos) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line + 1;
        this.pos = pos + 1;
    }

    public void print(){
        System.out.println("Loc=<" + this.line + ":" + this.pos + "> " + this.type + " '" + this.lexeme + "'");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getInt() {
        switch (type) {
            case "Num":
                return Integer.parseInt(lexeme);
            case "Num_8":
                return Integer.parseInt(lexeme, 8);
            case "Num_16":
                return Integer.parseInt(lexeme.substring(2), 16);
        }

        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "MyTok{" +
                "type='" + type + '\'' +
                ", lexeme='" + lexeme + '\'' +
                ", line=" + line +
                ", pos=" + pos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyTok token = (MyTok) o;
        return line == token.line &&
                pos == token.pos &&
                type.equals(token.type) &&
                lexeme.equals(token.lexeme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, lexeme, line, pos);
    }
}
