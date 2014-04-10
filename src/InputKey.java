
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputKey {
	private String instr;
	public String input() throws IOException{
		InputStreamReader is = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(is);

		System.out.print(">>>");

		this.instr = br.readLine();

		return this.instr;
	}
	public String getInput(){
		return this.instr;
	}
}
