import java.util.Stack;


public class Evaluation {
	public void eval(Cons start){
		String value;
		value = start.getValue();
		if(value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")){
			System.out.println(calc(start));
		} else if(value.equals("<") || value.equals(">") || value.equals("<=") || value.equals(">=") || value.equals("=")){
			System.out.println(comp(start));
		}
	}
	Stack<Cons> node2 = new Stack<Cons>();
	public int calc(Cons token){
		int ans = 0;
		switch(token.getValue()){
		case "+":
			while(token.cdr != null){
				ans += calc(token.cdr);
				token = token.cdr;
			}
			if(node2.isEmpty() == false){
				token = node2.pop();
			}
			break;
		case "-":
			while(token.cdr != null){
				ans -= calc(token.cdr);
				token = token.cdr;
			}
			if(node2.isEmpty() == false){
				token = node2.pop();
			}
			break;
		case "*":
			ans = 1;
			while(token.cdr != null){
				ans *= calc(token.cdr);
				token = token.cdr;

			}
			if(node2.isEmpty() == false){
				token = node2.pop();
			}
			break;
		case "/":
			while(token.cdr != null){
				ans /= calc(token.cdr);
				token = token.cdr;
			}
			if(node2.isEmpty() == false){
				token = node2.pop();
			}
			break;
		case "(":
			node2.push(token);
			token = token.car;
			ans = calc(token);
			break;
		default:
			if(Character.isDigit(token.getValue().charAt(0))){
				ans = Integer.parseInt(token.getValue());
			} else {

			}
		}
		return ans;
	}
	public boolean comp(Cons token){
		boolean ans = false;
		switch(token.getValue()){
		case "<":
		}
		if(ans){
			System.out.println("T");
		} else {
			System.out.println("Nil");
		}
		return ans;
	}
	public void iffunc(Cons token){

	}
	public void setqfunc(Cons token){

	}
	public void defunfunc(Cons token){

	}

}
