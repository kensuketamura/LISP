
public class Cons {
	private String value;
	public Cons car;
	public Cons cdr;
	public Cons(String value){
		this.value = value;
	}
	public String getValue(){
		return this.value;
	}
}
