import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.ArrayList;

public class FileParser {
    private File csvFile = null;
    private List<String> parsed_data = new ArrayList<>();
    private String line = "";
    final private String csvSplitBy = "\n";
    BufferedReader br = null;

    public FileParser(File csvFile) {
        this.csvFile = csvFile;
    }

    public List<String> getParsed_data() {
        return parsed_data;
    }

    public void parse_data() {
        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                parsed_data.add(line);
            }
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
