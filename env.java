import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

class Main {

	public static String readFile(String path, Charset encoding) throws IOException {
		return Files.readString(Paths.get(path), encoding);
	}

	public static void main(String[] args) {

		String filename = args[0];
		String modified = null;
		String content = null;
		String localhome = System.getProperty("user.home");
		try {
			content = readFile(filename, StandardCharsets.UTF_8);
			modified = content.replaceAll("__HOME__", localhome);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
//		System.out.println(modified);
		Path output = Path.of("stopgo-java" + File.separator + ".classpath");
		Files.writeString(output, modified);
		}
		catch (IOException e)  {e.printStackTrace();}
	}
}