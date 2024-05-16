<<<<<<< Updated upstream
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
=======
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
>>>>>>> Stashed changes
import java.util.*;
import java.util.Scanner;
import java.util.stream.Stream;

<<<<<<< Updated upstream
public class s31774P01 implements directoryCommand, edytor{
    public static void main(String[] args) {
=======
public class s31774P01 implements directoryMethods, editoryInterface{
    public static void main(String[] args) throws IOException {
>>>>>>> Stashed changes

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

<<<<<<< Updated upstream
        Map<String, Command> commandMapMain = new HashMap<>() {{
            put("stop", new CustomCommand("Kończy cały program", -1, edytor.brakeApp()));
            put("edit", new CustomCommand("Moduł edycji", 10));
            put("check", new CustomCommand("Moduł sprawdzania zadań", 20));
        }};

        Map<String, Command> commandMapEdit = new HashMap<>() {{
            put("add", new CustomCommand("Dodaje nowe zadanie", 20, edytor.runEditor()));
            put("edit", new CustomCommand( "Edytuje zadanie", 20));
            put("back", new CustomCommand( "Powrót do menu głównego", 0));
=======
        enum CommandKey {
            stop, editmode, checkmode, add, back, edit, show, addStudent, showALL
        }
        Map<CommandKey, Command> commandMapMain = new HashMap<>() {{
            put(CommandKey.stop, new Command("stop", "Kończy cały program", -1 ));
            put(CommandKey.editmode, new Command("editmode", "Wejdż do modułu edycji", 1));
            put(CommandKey.checkmode, new Command("checkmode", "Wejdź do modułu sprawdzania", 2));
        }};

        Map<CommandKey, Command> commandMapEdit = new HashMap<>() {{
            put(CommandKey.stop, new Command("stop", "Kończy cały program", -1 ));
            put(CommandKey.add, new Command("add", "Dodaje nowe zadanie", 1, () -> directoryMethods.creator(editoryInterface.runEditor(),true).run()));
            put(CommandKey.edit, new Command("edit", "edytuje", 1, () -> directoryMethods.creator(editoryInterface.runEditor(),false).run()));
            put(CommandKey.show, new Command("show", "Pokazuje zadania", 1, () -> fileMethods.showZadanie().run()));
            put(CommandKey.showALL, new Command("showALL", "Pokazuje wszystkie zadania", 1, () -> fileMethods.showAll().run()));
            put(CommandKey.back, new Command("backToMain", "Powrót do maina", 0));
        }};
        Map<CommandKey, Command> commandMapCheck = new HashMap<>() {{
            put(CommandKey.stop, new Command("stop", "Kończy cały program", -1 ));
            put(CommandKey.add, new Command("add", "Dodaje nowe rozwiązanie(studenta)", 2));
            put(CommandKey.addStudent, new Command("addStudent", "Dodaje nowego studenta do systemu", 2, directoryMethods.createStudent()));
            put(CommandKey.back, new Command("backToMain", "Powrót do maina", 0));
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    static void createNewZadanie(String newZadanieName){
        try {
            // Create a File object
            File myFile = new File("filename.txt");

            // Use the createNewFile method to create the file
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
=======
    static Runnable creator(String zadanieTresc, boolean code) {
        return () -> {
            System.out.print("Podaj nazwe Zadania: ");
            String nazwaZadania = scanner.nextLine();
            try {
                FileWriter fin = new FileWriter("./src/Zadania/" + nazwaZadania + ".txt");
                fin.write(zadanieTresc);
                fin.close();
                if (code){
                    System.out.println("Task created successfully!");
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(("./src/Studenci")))) {
                        for (Path entry : stream) {
                            // Check if the entry is a directory
                            if (Files.isDirectory(entry)) {
//                                System.out.println("Directory: " + entry.toFile());
                                createFolder(new File(entry.toFile() + "/" + nazwaZadania));                      }
                        }
                    }
                } else {
                    System.out.println("Task updated successfully!");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
>>>>>>> Stashed changes
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
interface fileMethods{
    Scanner scanner = new Scanner(System.in);
    static ArrayList<String> retriveTaskNames(){
        ArrayList<String> names = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./src/Zadania"))) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    names.add(String.valueOf(entry.getFileName()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
    static ArrayList<File> retriveTasks(){
        ArrayList<File> fileArrayList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./src/Zadania"))) {
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    fileArrayList.add(entry.toFile());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileArrayList;
    }

    static void showFile(String filePath){
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(line -> System.out.println("> " + line));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static Runnable showZadanie(){
        return () -> {
            System.out.print("Lista plików:");
            System.out.println(retriveTaskNames());
            System.out.print("Pokaż plik (nazwaPliku): ");
            String nazwaPliku = scanner.nextLine();
            showFile("./src/Zadania/" + nazwaPliku);

            System.out.print("Press any key to leave: ");
            scanner.nextLine();
        };
    }
    static Runnable showAll(){
        return () -> {
            ArrayList<File> fileList = retriveTasks();
            int currentIndex = 0;
            while (true){
                System.out.println("Obecnie wyświetlam: " + fileList.get(currentIndex));
                showFile(String.valueOf(fileList.get(currentIndex)));
                System.out.print("Poruszanie ( /previous , /exit , /next ): ");
                String input = scanner.nextLine();
                if(input.equals("/exit")) {
                    break;
                }
                if(input.equals("/next")) {
                    if(currentIndex == fileList.size() - 1) {
                        currentIndex = 0;
                    } else {
                        currentIndex++;
                    }
                }
                if(input.equals("/previous")) {
                    if(currentIndex == 0) {
                        currentIndex = fileList.size() - 1;
                    } else {
                        currentIndex--;
                    }
                }
            }
        };
    }
}