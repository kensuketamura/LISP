import java.util.ArrayList;


//関数クラス
public class Function implements Cloneable {
	public Cons start;	//関数内の処理が開始する点
	public String name;	//関数名
	public ArrayList<Variable> parameter = new ArrayList<Variable>();
	public Function(String name, Cons start) {
		this.name = name;
		this.start = start;
	}
	public Object clone(){
		int i;
		Variable dammy = new Variable("dammy", 0);
			Function copy = new Function(this.name, this.start);
			copy.parameter = new ArrayList<Variable>();
			for(i = 0; i < this.parameter.size(); i++){
				copy.parameter.add(dammy);
				copy.parameter.get(i).value = this.parameter.get(i).value;
				copy.parameter.get(i).name = new String(this.parameter.get(i).name);
			}
			return copy;
	}
}
