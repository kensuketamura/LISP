import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
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
