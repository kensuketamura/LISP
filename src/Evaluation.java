import java.util.ArrayList;
import java.util.Stack;


public class Evaluation {
	public ArrayList<Variable> varlist = new ArrayList<Variable>();
	public ArrayList<Function> funclist = new ArrayList<Function>();
	public void eval(Cons start){
		String value;
		value = start.getValue();
		if(value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")){
			System.out.println(calc(start));
		} else if(value.equals("<") || value.equals(">") || value.equals("<=") || value.equals(">=") || value.equals("=")){
			comp(start);
		} else if(value.equals("if")){
			funcIf(start);
		} else if(value.equals("setq")){
			funcSetq(start);
			System.out.println(varlist.get(varlist.size() - 1).name + " = " + varlist.get(varlist.size() - 1).value);
		} else if(value.equals("defun")){
			funcDefun(start);
		} else {
			searchFunc(value);
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
	public int funcIf(Cons token){
		if(token.cdr.getValue().equals("(")){
			token = token.cdr;	//token = "if"→"("
			if(token.cdr.getValue().equals("(")){
				if(comp(token)){
					token = token.cdr;
				} else {
					token = token.cdr.cdr;
				}
				eval(token.car);
				return 0;
			}
		}
		return -1;	//error
	}

	//変数定義メソッド
	public int funcSetq(Cons token){
		if(!token.cdr.getValue().equals("(") || !token.cdr.getValue().equals(")")){
			if(Character.isDigit(token.cdr.cdr.getValue().charAt(0))){
				Variable a=  new Variable(token.cdr.getValue(), Integer.parseInt(token.cdr.cdr.getValue()));
				varlist.add(a);
				return 1;
			}
		}
		return -1;	//Error
	}

	//関数定義メソッド
	public int funcDefun(Cons token){
		if(!token.cdr.getValue().equals("(") || !token.cdr.getValue().equals(")")){
			Function a=  new Function(token.cdr.getValue(), token.cdr.cdr.cdr.car);
			token = token.cdr.cdr.car;
			while(token != null){
				a.parameter.add(new Variable(token.getValue(), 0));
				token = token.cdr;
			}
			funclist.add(a);
			return 1;
		}
		return -1;	//Error
	}

	//変数検索メソッド
	public Variable searchVar(String name){
		int i = 0;
		for(i = 0; i < this.varlist.size(); i++){
			if(name == this.varlist.get(i).name){
				return this.varlist.get(i);
			}
		}
		System.out.println(name + " is not found");
		return null;	//Not Found
	}

	//関数検索メソッド
	public Function searchFunc(String name){
		int i = 0;
		for(i = 0; i < this.funclist.size(); i++){
			if(name == this.funclist.get(i).name){
				return this.funclist.get(i);
			}
		}
		System.out.println(name + " is not found");
		return null;	//Not Found
	}
}
