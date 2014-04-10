import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		InputKey a = new InputKey();
		LexicalAnalysis b = new LexicalAnalysis();
		b.lexana(a.input());
		SyntacticAnalysis c = new SyntacticAnalysis();
		c.synana(b.getToken());
		Evaluation d = new Evaluation();
		d.eval(c.getStart());
		System.out.println("-----END-----");
	}

}
