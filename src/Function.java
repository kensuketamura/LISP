import java.util.ArrayList;


//関数クラス
public class Function {
	public Cons start;	//関数内の処理が開始する点
	public String name;	//関数名
	public ArrayList<Variable> parameter = new ArrayList<Variable>();
	public Function(String name, Cons start) {
		this.name = name;
		this.start = start;
	}
}
