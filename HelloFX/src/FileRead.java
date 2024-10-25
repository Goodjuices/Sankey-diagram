import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileRead {
    public String getListTitle(){
        return ListTitle;
    }
    public void setListTitle(String listTitle){

        this.ListTitle = listTitle;
    }
    public List<String> getListType(){

        return ListType;
    }

    public void setListType(List<String> listType) {
        this.ListType = listType;
    }
    public List<String> getListData(){
        return ListData;
    }

    public void setListData(List<String> listData) {
        this.ListData = listData;
    }

    List<String> ListType = new ArrayList<>(); // Store the type of the expenditures
    List<String> ListData = new ArrayList<>(); // Store the amount corresponding to the expenditures
    String ListTitle ;// Store the text's first line as the diagram's title


    public void readData() {
        File file = new File("example2.txt");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    ListTitle = line;
                    System.out.println("ListTitle: " + ListTitle);
                } else if (lineNumber == 2) {
                    ListType.add(line);
                } else {
                    int lastIndex = line.lastIndexOf(' ');
                    if (lastIndex != -1) {
                        String[] parts = new String[2];
                        parts[0] = line.substring(0, lastIndex);
                        parts[1] = line.substring(lastIndex + 1);
                        ListType.add(parts[0]);
                        ListData.add(parts[1]);
                    }
                }
            }
            int sum = 0;
            for (String value : ListData) {
                    sum += Integer.parseInt(value);
            }

            ListData.add(0, String.valueOf(sum));

            System.out.println("List Type: " +ListType);
            System.out.println("List Data: " +ListData);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        FileRead fileRead = new FileRead();
        fileRead.readData();
    }
}
