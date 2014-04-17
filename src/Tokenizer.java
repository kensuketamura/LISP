import java.util.ArrayList;

public class Tokenizer {
	private ArrayList<String> token = new ArrayList<String>();

	public ArrayList<String> getToken() {
		return this.token;
	}

	public void tokenize(String in) {
		int i, j;
		j = 0;
		token.clear();
		for (i = 0; i < in.length(); i++) {
			if (in.charAt(i) != ' ' && in.charAt(i) != '	') {
				if (in.charAt(i) == '(' || in.charAt(i) == ')') {
					if (j != i) {
						token.add(in.substring(j, i));
					}
					token.add(String.valueOf(in.charAt(i)));
					j = i + 1;
				}
			} else {
				if (j != i) {
					token.add(in.substring(j, i));
				}
				j = i + 1;
			}
		}
	}
}
