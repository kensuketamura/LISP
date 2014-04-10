import java.util.Stack;


public class Evaluation {
	public void eval(Cons start){
		String value;
		value = start.getValue();
		if(value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")){
			System.out.println(calc(start));
		} else if(value.equals("<") || value.equals(">") || value.equals("<=") || value.equals(">=") || value.equals("=")){
			comp(start);
		} else if(value.equals("if")){
			funcif(start);
		}
	}
	Stack<Cons> node = new Stack<Cons>();	//car部分に新しい枝が発生したときの発生源(ノード)を保存するスタック

	//四則演算メソッド
	public int calc(Cons token){
		//"token"は現在注目している要素(Consクラス)
		boolean firstnum = false;	//減算、除算のときの最初の値がtokenに入っているかどうか
		int ans = 0;
		switch(token.getValue()){
		case "+":
			while(token.cdr != null){
				ans += calc(token.cdr);
				token = token.cdr;
			}
			if(node.isEmpty() == false){
				token = node.pop();
			}
			break;
		case "-":
			firstnum = true;
			while(token.cdr != null){
				if(firstnum){
					ans = calc(token.cdr);
					firstnum = false;
				} else {
					ans -= calc(token.cdr);
				}
				token = token.cdr;
			}
			if(node.isEmpty() == false){
				token = node.pop();
			}
			break;
		case "*":
			ans = 1;
			while(token.cdr != null){
				ans *= calc(token.cdr);
				token = token.cdr;

			}
			if(node.isEmpty() == false){
				token = node.pop();
			}
			break;
		case "/":
			firstnum = true;
			while(token.cdr != null){
				if(firstnum){
					ans = calc(token.cdr);
					firstnum = false;
				} else {
					ans /= calc(token.cdr);
				}
				token = token.cdr;
			}
			if(node.isEmpty() == false){
				token = node.pop();
			}
			break;
		case "(":
			node.push(token);
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

	//比較演算メソッド
	public boolean comp(Cons token){
		boolean ans = false;
		switch(token.getValue()){
		case "<":
			if(Integer.parseInt(token.cdr.getValue()) < Integer.parseInt(token.cdr.cdr.getValue())){
				ans = true;
			} else {
				ans = false;
			}
			break;
		case ">":
			if(Integer.parseInt(token.cdr.getValue()) > Integer.parseInt(token.cdr.cdr.getValue())){
				ans = true;
			} else {
				ans = false;
			}
			break;
		case "<=":
			if(Integer.parseInt(token.cdr.getValue()) <= Integer.parseInt(token.cdr.cdr.getValue())){
				ans = true;
			} else {
				ans = false;
			}
			break;
		case ">=":
			if(Integer.parseInt(token.cdr.getValue()) >= Integer.parseInt(token.cdr.cdr.getValue())){
				ans = true;
			} else {
				ans = false;
			}
			break;
		case "=":
			if(Integer.parseInt(token.cdr.getValue()) == Integer.parseInt(token.cdr.cdr.getValue())){
				ans = true;
			} else {
				ans = false;
			}
			break;
		}
		if(ans){
			System.out.println("T");
		} else {
			System.out.println("Nil");
		}
		return ans;
	}

	//if文メソッド
	public int funcif(Cons token){
		if(token.cdr.getValue().equals("(")){
			token = token.cdr;	//token = "if"→"("
			if(comp(token)){
				token = token.cdr;
			} else {
				token = token.cdr.cdr;
			}
			eval(token.car);
			return 0;
		} else {
			return -1;
		}
	}

	//変数定義メソッド
	public void funcsetq(Cons token){

	}

	//関数定義メソッド
	public void funcdefun(Cons token){

	}

}
