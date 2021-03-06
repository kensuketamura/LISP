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
		Tokenizer b = new Tokenizer();
		Parser c = new Parser();
		Evaluator d = new Evaluator();
		LoadFile e;

		if (args.length != 0) {
			e = new LoadFile(args[0]);
			b.tokenize(e.getFileInString());
			c.parse(b.getToken());
			while (!c.startcons.isEmpty()) {
				System.out.println(d.eval(c.getStart()));
			}
		} else {
			while (!a.input().equals("END")) {
				b.tokenize(a.getInput());
				c.parse(b.getToken());
				while (!c.startcons.isEmpty()) {
					System.out.println(d.eval(c.getStart()));
				}
			}
		}
		System.out.println("-----END-----");
	}

}
