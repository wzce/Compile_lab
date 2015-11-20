package lab1;

public class MAIN {
	public static void main(String args[]){
		Lex le=new Lex("program1.txt");
		le.analyse();
		le.output();
	}
}
