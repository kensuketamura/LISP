import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class LoadFile {
	private String fileInString;
	public LoadFile(String filepath) {
		try{
			File file = new File(filepath);
			if(checkFileOpen(file)){
				BufferedReader br = new BufferedReader(new FileReader(file));
				String buffer;
				StringBuilder in = new StringBuilder();
				System.out.println("-----Input-----");
				while((buffer = br.readLine()) != null){
					in.append(buffer);
					System.out.println(buffer);
				}
				this.fileInString = new String(in);
				br.close();
				System.out.println("---------------");
			} else {
				System.out.println(file + " is not found");
			}
		} catch(FileNotFoundException ex){
			System.out.println(ex);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	private boolean checkFileOpen(File file){
		if(file.exists()){
			if(file.isFile() && file.canRead()){
				return true;
			}
		}
		return false;
	}
	public String getFileInString() {
		return this.fileInString;
	}
}
