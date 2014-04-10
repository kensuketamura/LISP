import java.util.ArrayList;


public class LexicalAnalysis {
	private ArrayList<String> token = new ArrayList<String>();
	public ArrayList<String> getToken(){
		return this.token;
	}
	public void lexana(String in){
		int i, j;
		j = 0;
		for(i = 0; i < in.length(); i++){
			if(in.charAt(i) != ' '){
				if(in.charAt(i) == '(' || in.charAt(i) == ')'){
					if(j != i){
						token.add(in.substring(j, i));
					}
					token.add(String.valueOf(in.charAt(i)));
					j = i + 1;
				}
			} else {
				if(j != i){
					token.add(in.substring(j, i));
				}
				j = i + 1;
			}
		}
	}
}
