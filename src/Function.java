import java.util.ArrayList;


//関数クラス
public class Function {
	public Cons start;	//変数に格納している値
	public String name;	//変数名
	public ArrayList<Variable> parameter = new ArrayList<Variable>();
	public Function(String name, Cons start) {
		this.name = name;
		this.start = start;
	}
}
