import java.util.ArrayList;
import java.util.Stack;


public class SyntacticAnalysis {
	private Cons a, b, start;
	public Cons synana(ArrayList<String> token){
		Stack<Cons> node = new Stack<Cons>();
		int i;
		boolean change = false;
		for(i = 0; i < token.size() - 1; i++){
			//if(token.get(0) == "(" && token.get(token.size() - 1) == ")"){
				if(i == 0){
					i++;
				}
				a = new Cons(token.get(i));
				switch(a.getValue()){
				case "(":
					if(b != null){
						b.cdr = a;
					}
					node.push(a);
					change = true;
					break;
				case ")":
					a = node.pop();
					break;
				default:
					if(change){
						if(b != null){
							b.car = a;
						}
						change = false;
					} else {
						if(b != null){
							b.cdr = a;
						}
					}
					break;
				}
				b = a;
			//}
			if(i == 1){
				start = a;
			}
		}
		return start;
	}
	public Cons getStart(){
		return this.start;
	}
}
