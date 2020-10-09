package compiler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

public class MyLex {
    private List<MyTok> tokens;
    private int index;
    private int line;
    private String filestr;
    private static final String DELIM = "[\\s\\[\\]=+*/\\-<>!|&%,;{}()]";

    MyLex(){
        this.tokens = new ArrayList<>();
        this.line = 0;
    }


    public void print_Tokens(){
        for(MyTok tok : this.tokens){
            tok.print();
        }
    }

    public List<MyTok> lex(String str) throws FileNotFoundException{
        FileReader fr = new FileReader(str);
        Scanner scan = new Scanner(fr);
        for ( ; scan.hasNextLine(); this.line++) {
            this.index = 0;
            this.filestr = scan.nextLine();
            for( ; this.index < filestr.length(); this.index++) {
                MyTok tok = this.getTok();
                if(tok != null) {
                    tokens.add(tok);
				}
            }
        }
        tokens.add(new MyTok("EOF", "", this.line - 1, this.index));
        return tokens;
    }

    public MyTok getTok() {
        char c;
        for(; this.index < this.filestr.length(); this.index++) {
            c = this.filestr.charAt(this.index);
            if(c == ' '){
                if(this.index + 1 == this.filestr.length())
                    return null;
                continue;
            }else if(c ==','){
                return new MyTok("Col", ",", this.line, this.index);
            }else if(c =='/'){
                if(this.index + 1 != this.filestr.length() && this.filestr.charAt(this.index + 1) == '/'){
                    this.index = this.filestr.length();
                    return null;
                }else {
                    return new MyTok("OP_DIV", "/", this.line, this.index);
                }
            }else if(c =='%'){
                return new MyTok("OP_DIV2", "%", this.line, this.index);
            }else if(c ==';'){
                return new MyTok("Semi", ";", this.line, this.index);
            }else if(c =='+'){
                return new MyTok("OP_ADD", "+", this.line, this.index);
            }else if(c =='-'){
                return new MyTok("OP_SUB", "-", this.line, this.index);
            }else if(c =='*'){
                return new MyTok("OP_MUL", "*", this.line, this.index);

            }else if(c == '{'){
                return new MyTok("L_brace", "{", this.line, this.index);
            }else if(c == '}'){
                return new MyTok("R_brace", "}", this.line, this.index);
            }else if(c == '('){
                return new MyTok("L_paren", "(", this.line, this.index);
            }else if(c == ')'){
                return new MyTok("R_paren", ")", this.line, this.index);
            }else if(c == '['){
                return new MyTok("L_square", "[", this.line, this.index);
            }else if(c == ']'){
                return new MyTok("R_square", "]", this.line, this.index);

            }else if(c == '>'){
                if(this.index + 1 != this.filestr.length() && this.filestr.charAt(this.index + 1) == '='){
                    this.index++;
                    return new MyTok("OP_MORE/EQ", ">=", this.line, this.index - 1);
                }else {
                    return new MyTok("OP_MORE", ">", this.line, this.index);
                }
            }else if(c == '<'){
                if(this.index + 1 != this.filestr.length() && this.filestr.charAt(this.index + 1) == '='){
                    this.index++;
                    return new MyTok("OP_LESS/EQ", "<=", this.line, this.index - 1);
                }else {
                    return new MyTok("OP_LESS", "<", this.line, this.index);
                }
            }else if(c == '!'){
                if(this.index + 1 != this.filestr.length() && this.filestr.charAt(this.index + 1) == '='){
                    this.index++;
                    return new MyTok("OP_NOTEQ", "!=", this.line, this.index - 1);
                }else{
                    return new MyTok("Unknown_token", "!", this.line, this.index);
                }
            }else if(c == '='){
                if(this.index + 1 != this.filestr.length() && this.filestr.charAt(this.index + 1) == '='){
                    this.index++;
                    return new MyTok("OP_EQ", "==", this.line, this.index - 1);
                }else {
                    return new MyTok("Assign", "=", this.line, this.index);
                }
            }else if(c == '|'){
                if(this.index + 1 != this.filestr.length() && this.filestr.charAt(this.index + 1) == '|'){
                    this.index++;
                    return new MyTok("OR", "||", this.line, this.index - 1);
                }else{
                    return new MyTok("Unknown_token", "|", this.line, this.index);
                }
            }else if(c == '&'){
                if(this.index + 1 != this.filestr.length() && this.filestr.charAt(this.index + 1) == '&'){
                    this.index++;
                    return new MyTok("AND", "&&", this.line, this.index - 1);
                }else{
                    return new MyTok("Unknown_token", "&", this.line, this.index);
                }
            }else if(c == '"'){
				return this.get_literal();
            }
			Pattern p = Pattern.compile(DELIM);
			Matcher m = p.matcher(this.filestr);
			int next;
			if(m.find(this.index)) {
                next = m.start();
            }else
				next = this.filestr.length();
				
			String substr = this.filestr.substring(this.index, next);
			
			if(Character.isDigit(c)) {
				return this.get_num(substr);
			}else if(c == '.'){
				return this.get_method(substr);
			}else{
				return this.get_id(substr);
			}

		}
        return null;
    }

    public MyTok get_id(String lexeme){
        int start = this.index;
        char c = lexeme.charAt(0);
        if(!Character.isLetter(c) && c != '_') {
			this.index += lexeme.length();
			return new MyTok("Unknown_token", lexeme, this.line, start);
		}
        for(int i = 1; i < lexeme.length(); i++, this.index++) {
            c = lexeme.charAt(i);
            
            if(c == '.' && !lexeme.substring(0, i).equals("System") && !lexeme.substring(0, i).equals("System.out")
			  && !lexeme.substring(0, i).equals("System.in")) {
                lexeme = lexeme.substring(0, i);
                break;
            }
        }
        if(lexeme.indexOf('.') != -1){
            switch (lexeme) {
                case "System.out.print":
                case "System.in.read":
                case "System.out.println":
                    return new MyTok("Identifier", lexeme, this.line, start);
                default:
                    return new MyTok("Unknown_token", lexeme, this.line, start);
            }
        }
        switch (lexeme){
            case "if":
                return new MyTok("if", "if", this.line, start);
            case "else":
                return new MyTok("else", "else", this.line, start);
            case "while":
                return new MyTok("while", "while", this.line, start);
            case "int":
                return new MyTok("int", "int", this.line, start);
            case "break":
                return new MyTok("break", "break", this.line, start);
            case "continue":
                return new MyTok("continue", "continue", this.line, start);
            case "String":
                return new MyTok("String", "String", this.line, start);
            case "void":
                return new MyTok("void", "void", this.line, start);

            default:
				Pattern p = Pattern.compile("[^A-Za-z0-9_]");
				Matcher m = p.matcher(lexeme);
				if(m.find())
					return new MyTok("Unknown_token", lexeme, this.line, start);
                return new MyTok("Identifier", lexeme, this.line, start);
        }
    }

    public MyTok get_method(String lexeme){
        int start = this.index;
        this.index += lexeme.length() - 1;
        switch (lexeme){
            case ".length":
                return new MyTok("method", ".length", this.line, start);
            case ".charAt":
                return new MyTok("method", ".charAt", this.line, start);

            default:
                return new MyTok("Unknown_token", lexeme, this.line, start);
        }
    }

    public MyTok get_num(String lexeme){
        int start = this.index;
        this.index += lexeme.length() - 1;
        if(lexeme.matches("0x[0-9A-Fa-f]+"))
            return new MyTok("Num_16", lexeme, this.line, start);
        else if(lexeme.matches("0[0-7]+"))
            return new MyTok("Num_8", lexeme, this.line, start);
        else if(lexeme.matches("[1-9]\\d*") || lexeme.equals("0"))
            return new MyTok("Num", lexeme, this.line, start);
        else
            return new MyTok("Unknown_token", lexeme, this.line, start);
    }

    public MyTok get_literal(){
        int start = this.index;
        StringBuilder lexeme = new StringBuilder();
        lexeme.append(this.filestr.charAt(this.index));
        this.index++;
        for(; this.index < this.filestr.length(); this.index++) {
            char c = this.filestr.charAt(this.index);
            char p = this.filestr.charAt(this.index - 1);
            lexeme.append(c);
            if(c == '"' && p != '\\')
                return new MyTok("Str_lit", lexeme.toString(), this.line, start);
        }
        this.index--;
        return new MyTok("Unknown_token", lexeme.toString(), this.line, start);
    }

}
