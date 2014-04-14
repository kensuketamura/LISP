import java.util.ArrayList;
import java.util.Stack;

//評価クラス
public class Evaluation {
	public ArrayList<Variable> varlist = new ArrayList<Variable>();
	public ArrayList<Function> funclist = new ArrayList<Function>();
	private Function nowfunc;
	private Stack<Function> currentFunc = new Stack<Function>();
	private boolean skip = false;

	//実行分岐(評価)メソッド
	public int eval(Cons start){
		String value;
		int i, result = 0;
		value = start.getValue();
		if(value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")){
			result = calc(start);
		} else if(value.equals("<") || value.equals(">") || value.equals("<=") || value.equals(">=") || value.equals("=")){
			comp(start);
		} else if(value.equals("if")){
			result = funcIf(start);
		} else if(value.equals("setq")){
			result = funcSetq(start);
			System.out.println(varlist.get(varlist.size() - 1).name + " = " + varlist.get(varlist.size() - 1).value);
		} else if(value.equals("defun")){
			result = funcDefun(start);
		} else if(searchFunc(value, true) != null){
			nowfunc = searchFunc(value, true);
			for(i = 0; i < nowfunc.parameter.size(); i++){
				nowfunc.parameter.get(i).value = Integer.parseInt(start.cdr.car.getValue());
			}
			currentFunc.push(nowfunc);
			result = eval(nowfunc.start.cdr.cdr.car);
			if(!currentFunc.isEmpty()){
				currentFunc.pop();
				if(!currentFunc.isEmpty()){
					nowfunc = currentFunc.pop();
				}
			} else {
				nowfunc = null;
			}
			skip = true;
		}
		System.out.println(result);
		return result;
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
				if(skip){
					token = token.cdr.cdr;
				} else {
					token = token.cdr;
				}
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
				if(skip){
					token = token.cdr.cdr;
				} else {
					token = token.cdr;
				}
			}
			if(node.isEmpty() == false){
				token = node.pop();
			}
			break;
		case "*":
			ans = 1;
			while(token.cdr != null){
				ans *= calc(token.cdr);
				if(skip){
					token = token.cdr.cdr;
				} else {
					token = token.cdr;
				}
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
				if(skip){
					token = token.cdr.cdr;
				} else {
					token = token.cdr;
				}
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
				if(searchVar(token.getValue(), false) != null){
					ans = searchVar(token.getValue(), true).value;
				}
				if(searchFunc(token.getValue(), false) != null){
					ans = eval(token);
				}
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
					if(skip){
						token = token.cdr.cdr;
					} else {
						token = token.cdr;
					}
				} else {
					if(skip){
						token = token.cdr.cdr.cdr;
					} else {
						token = token.cdr.cdr;
					}
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
			Function a=  new Function(token.cdr.getValue(), token.cdr);
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
	public Variable searchVar(String name, boolean errorprint){
		int i = 0;
		for(i = 0; i < this.nowfunc.parameter.size(); i++){
			if(name.equals(this.nowfunc.parameter.get(i).name)){
				return this.nowfunc.parameter.get(i);
			}
		}
		for(i = 0; i < this.varlist.size(); i++){
			if(name.equals(this.varlist.get(i).name)){
				return this.varlist.get(i);
			}
		}
		if(errorprint){
			System.out.println(name + " is not found");
		}
		return null;	//Not Found
	}

	//関数検索メソッド
	public Function searchFunc(String name, boolean errorprint){
		int i = 0;
		for(i = 0; i < this.funclist.size(); i++){
			if(name.equals(this.funclist.get(i).name)){
				return this.funclist.get(i);
			}
		}
		if(errorprint){
			System.out.println(name + " is not found");
		}
		return null;	//Not Found
	}
}
