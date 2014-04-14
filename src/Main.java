import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		System.out.printf("\n");
		System.out.println("00     0000   0000    000000");
		System.out.println("00      00   00   00  00   00");
		System.out.println("00      00    00      00   00");
		System.out.println("00      00      00    000000");
		System.out.println("00      00   00   00  00");
		System.out.println("000000 0000    0000   00");
		System.out.printf("\n    Welcome to LISP!!\n\n");
		InputKey a = new InputKey();
		LexicalAnalysis b = new LexicalAnalysis();
		SyntacticAnalysis c = new SyntacticAnalysis();
		Evaluation d = new Evaluation();
		while(!a.input().equals("END")){
			b.lexana(a.getInput());
			c.synana(b.getToken());
			d.eval(c.getStart());
		}
		System.out.println("-----END-----");
	}

}
