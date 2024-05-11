import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

public class s31774P01 implements directoryCommand, edytor{
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int statusCode = 0;

        Map<Integer, String> Status = new HashMap<>(){{
            put(0, "Main");
            put(10, "Menu edycji");
            put(20, "Menu sprawdzania");
        }};

        List<String> folderList = new ArrayList<>() {{
            add("./src/Zadania");
            add("./src/Studenci");
        }};

        load(folderList);

        Map<String, Command> commandMapMain = new HashMap<>() {{
            put("stop", new CustomCommand("Kończy cały program", -1, edytor.brakeApp()));
            put("edit", new CustomCommand("Moduł edycji", 10));
            put("check", new CustomCommand("Moduł sprawdzania zadań", 20));
        }};

        Map<String, Command> commandMapEdit = new HashMap<>() {{
            put("add", new CustomCommand("Dodaje nowe zadanie", 20, edytor.runEditor()));
            put("edit", new CustomCommand( "Edytuje zadanie", 20));
            put("back", new CustomCommand( "Powrót do menu głównego", 0));
        }};

        Map<Integer, Map<String, Command>> commandMap = new HashMap<>(){{
            put(0, commandMapMain);
            put(10, commandMapEdit);
        }};

        do {
            System.out.println("##########################################################################");
            System.out.println("Aktualna pozycja: " + Status.get(statusCode) + ", Aktualny status: " + statusCode);

            for(Map.Entry<String, Command> entry : commandMap.get(statusCode).entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue().toString());
            }

            System.out.print("Podaj komende: ");
            String input = scanner.next();

            Command command = commandMap.get(statusCode).get(input);
            if (command != null){
                statusCode = command.StatusCode;
                if (command.function != null) {
                    command.function.run();
                }
            }else {
                System.out.println("Nieznana komenda.");
            }
        }while (true);
    }

    public static void load(List<String> directories) {
        for (String directory : directories) {
            directoryCommand.createDirectory(directory);
        }
    }
}
abstract class Command {
    String commandDescription;
    int StatusCode;
    Runnable function;

    public Command(String commandDescription, int StatusCode) {
        this.commandDescription = commandDescription;
        this.StatusCode = StatusCode;
    }
    public Command(String commandDescription, int StatusCode, Runnable function) {
        this.commandDescription = commandDescription;
        this.StatusCode = StatusCode;
        this.function = function;
    }

    abstract void command();
    @Override
    public String toString() {
        return " - " + commandDescription + " [ StatusCode: " + StatusCode + " ]";
    }
}

class CustomCommand extends Command {
    public CustomCommand(String commandDescription, int StatusCode) {
        super(commandDescription, StatusCode);
    }
    public CustomCommand(String commandDescription, int StatusCode, Runnable function) {
        super(commandDescription, StatusCode, function);
    }

    @Override
    void command() {
        function.run();
    }

}

interface directoryCommand {
    static void createDirectory(String directoryName){
        File folder = new File(directoryName);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("Folder created successfully!");
            } else {
                System.out.println("Failed to create folder.");
            }
        } else {
            System.out.println("Folder already exists.");
        }
    }
    static void createNewZadanie(String newZadanieName){
        try {
            // Create a File object
            File myFile = new File("filename.txt");

            // Use the createNewFile method to create the file
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }

            // Create a FileWriter object
            FileWriter myWriter = new FileWriter("filename.txt");

            // Use the write method to write to the file
            myWriter.write("Hello, world!");

            // Always close the file writer
            myWriter.close();

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
interface edytor{
    static Runnable brakeApp() {
        return () -> {
            System.exit(0);
        };
    }

    static Runnable runEditor() {
        return () -> {
            Scanner scanner = new Scanner(System.in);
            String zadanieTresc = "";

            do {
                System.out.print("> ");
                String input = scanner.next();
                if (input.equals(">exit")) {
                    break;
                }
                zadanieTresc += input;
            } while (true);
        };
    }
}