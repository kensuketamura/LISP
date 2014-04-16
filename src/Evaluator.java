import java.util.ArrayList;
import java.util.Stack;

//評価クラス
public class Evaluator {
	public ArrayList<Variable> varlist = new ArrayList<Variable>();
	public ArrayList<Function> funclist = new ArrayList<Function>();
	private Function prefunc, nowfunc;
	private Stack<Function> currentFunc = new Stack<Function>();
	private boolean skip = false;

	//実行分岐(評価)メソッド
	public int eval(Cons start){
		Cons temp;
		String value;
		int i, result = 0;
		value = start.getValue();
		if(value.equals("+") || value.equals("-") || value.equals("*") || value.equals("/")){
			result = calc(start);
			//System.out.println(result);
		} else if(value.equals("<") || value.equals(">") || value.equals("<=") || value.equals(">=") || value.equals("=")){
			comp(start);
		} else if(value.equals("if")){
			result = funcIf(start);
		} else if(value.equals("setq")){
			result = funcSetq(start);
			System.out.println(varlist.get(varlist.size() - 1).name + " = " + varlist.get(varlist.size() - 1).value);
		} else if(value.equals("defun")){
			result = funcDefun(start);
		} else if(searchFunc(value, false) != null){
			if(nowfunc == null){
				nowfunc = (Function)searchFunc(value, false).clone();
			}
			prefunc = (Function)searchFunc(value, false).clone();
			temp = start.cdr.car;
			for(i = 0; i < prefunc.parameter.size(); i++){
				prefunc.parameter.get(i).value = eval(temp);
				temp = temp.cdr;
			}
			nowfunc = prefunc;
			currentFunc.push(nowfunc);
			result = eval(nowfunc.start.cdr.cdr.car);
			if(!currentFunc.isEmpty()){
				currentFunc.pop();
				if(!currentFunc.isEmpty()){
					nowfunc = currentFunc.peek();
				}
			}
			if(start.cdr.cdr != null){
				skip = true;
			}
		} else if(searchVar(value, false) != null){
			result = searchVar(value, false).value;
		} else if(Character.isDigit(value.charAt(0))){
			result = Integer.parseInt(value);
		} else if(value.equals("(")){
			result= eval(start.car);
		}
		//System.out.println(result);
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
					skip = false;
				} else {
					token = token.cdr;
				}
			}
			if(!node.isEmpty()){
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
					skip = false;
				} else {
					token = token.cdr;
				}
			}
			if(!node.isEmpty()){
				token = node.pop();
			}
			break;
		case "*":
			ans = 1;
			while(token.cdr != null){
				ans *= calc(token.cdr);
				if(skip){
					token = token.cdr.cdr;
					skip = false;
				} else {
					token = token.cdr;
				}
			}
			if(!node.isEmpty()){
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
					skip = false;
				} else {
					token = token.cdr;
				}
			}
			if(!node.isEmpty()){
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
		int element1, element2;
		if(token.cdr.car == null){
			element1 = eval(token.cdr);
		} else {
			element1 = eval(token.cdr.car);
		}
		if(token.cdr.cdr.car == null){
			element2 = eval(token.cdr.cdr);
		} else {
			element2 = eval(token.cdr.cdr.car);
		}
		switch(token.getValue()){
		case "<":
			if(element1 < element2){
				ans = true;
			} else {
				ans = false;
			}
			break;
		case ">":
			if(element1 > element2){
				ans = true;
			} else {
				ans = false;
			}
			break;
		case "<=":
			if(element1 <= element2){
				ans = true;
			} else {
				ans = false;
			}
			break;
		case ">=":
			if(element1 >= element2){
				ans = true;
			} else {
				ans = false;
			}
			break;
		case "=":
			if(element1 == element2){
				ans = true;
			} else {
				ans = false;
			}
			break;
		}
		if(ans){
			//System.out.println("T");
		} else {
			//System.out.println("Nil");
		}
		return ans;
	}

	//if文メソッド
	public int funcIf(Cons token){
		int ans;
		if(token.cdr.getValue().equals("(")){
			token = token.cdr;	//token = "if"→"("
			if(token.cdr.getValue().equals("(")){
				if(comp(token.car)){
					if(skip){
						token = token.cdr.cdr;
						skip = false;
					} else {
						token = token.cdr;
					}
				} else {
					if(skip){
						token = token.cdr.cdr.cdr;
						skip = false;
					} else {
						token = token.cdr.cdr;
					}
				}
				ans = eval(token.car);
				//if(!outprint.contains(nowfunc.parameter.get(0).value)){
					//System.out.println("IF " + nowfunc.parameter.get(0).value + " = " + ans);
					//outprint.add(nowfunc.parameter.get(0).value);
				//}
				return ans;
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
			Variable para;
			token = token.cdr.cdr.car;
			while(token != null){
				para = new Variable(new String(token.getValue()), 0);
				a.parameter.add(para);
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
