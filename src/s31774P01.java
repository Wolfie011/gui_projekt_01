import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Scanner;

public class s31774P01 implements directoryMethods, edtoryInterface{
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int statusCode = 0;

        Map<Integer, String> Status = new HashMap<>(){{
            put(0, "Main");
            put(10, "Menu edycji");
            put(11, "Dodawanie zadań");
            put(12, "");
            put(20, "Menu sprawdzania");
            put(21, "Zadania");
        }};

        List<File> folderList = new ArrayList<>() {{
            add(new File("./src/Zadania"));
            add(new File("./src/Studenci"));
        }};

        load(folderList);
        enum CommandKey {
            STOP,
            EDITMODE,
            ADD,
            BACK,
            EDIT
        }
        Map<CommandKey, Command> commandMapMain = new HashMap<>() {{
            put(CommandKey.STOP, new Command("stop", "Kończy cały program", -1 ));
            put(CommandKey.EDITMODE, new Command("edit", "Moduł edycji", 10));
        }};

        Map<CommandKey, Command> commandMapEdit = new HashMap<>() {{
            put(CommandKey.ADD, new Command("add", "Dodaje nowe zadanie", 10, edtoryInterface.runEditor()));
            put(CommandKey.EDIT, new Command("edit", "edytuje", 10));
            put(CommandKey.BACK, new Command("backToMain", "Powrót do maina", 0));
        }};

        Map<Integer, Map<CommandKey, Command>> commandMap = new HashMap<>(){{
            put(0, commandMapMain);
            put(10, commandMapEdit);
        }};

        do {
            if (statusCode == -1) System.exit(0);
            System.out.println("##########################################################################");
            System.out.println("Aktualna pozycja: " + Status.get(statusCode) + ", Aktualny status: " + statusCode);
            for(Map.Entry<CommandKey, Command> entry : commandMap.get(statusCode).entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue().toString());
            }
            System.out.print("Podaj komende: ");
            String input = scanner.next();

            Command command = commandMap.get(statusCode).get(CommandKey.valueOf(input));
            if (command != null){
                statusCode = command.StatusCode;
                if (command.function != null) command.function.run();
            }else {
                System.out.println("Nieznana komenda.");
            }
        }while (true);
    }

    public static void load(List<File> files) {
        for (File folder : files) {
            directoryMethods.createDirectory(folder);
        }
    }
}
abstract class Task{
    int taskNum;

    public Task(int taskNum) {
        this.taskNum = taskNum;
    }

    abstract void add();
    abstract void show();
}
class theTask extends Task{
    String taskName;
    String taskContents;
    String taskPhoto;
    public theTask(int taskNum, String taskName, String taskContents, String taskPhoto) {
        super(taskNum);
        this.taskName = taskName;
        this.taskContents = taskContents;
        this.taskPhoto = taskPhoto;
    }
    @Override
    void add() {
    }
    @Override
    void show() {
    }
}
class theSolution extends Task{
    String soluition;
    public theSolution(int taskNum, String soluition) {
        super(taskNum);
        this.soluition = soluition;
    }
    @Override
    void add() {

    }
    @Override
    void show() {

    }
}
class Command {
    String commandName;
    String commandDescription;
    int StatusCode;
    Runnable function;
    public Command(String commandName, String commandDescription, int StatusCode, Runnable function) {
        this.commandDescription = commandDescription;
        this.commandName = commandName;
        this.StatusCode = StatusCode;
        this.function = function;
    }
    public Command(String commandName, String commandDescription, int StatusCode) {
        this.commandDescription = commandDescription;
        this.commandName = commandName;
        this.StatusCode = StatusCode;
    }
    @Override
    public String toString() {
        return " - " + commandDescription + " [ StatusCode: " + StatusCode + " ]";
    }
}
interface edtoryInterface {
    static Runnable runEditor() {
        return () -> {
            Scanner scanner = new Scanner(System.in);
            String zadanieTresc = "";

            while (true){
                System.out.print("> ");
                String input = scanner.nextLine();
                if (input.equals(">exit")) {
                    System.out.println("Utworz oraz zapisz? [Y/N]: ");
                    if(input.equals("Y")){
                        //TUTAJ ROBIMY ZAPUIS
                        break;
                    } else {
                        break;
                    }
                }
                zadanieTresc += input;
                zadanieTresc += "\n";
            }
            System.out.println(zadanieTresc);
        };
    }
}
interface directoryMethods{
    static File createDirectory(File folder){
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

        return folder;
    }
    static void creator(String name){
        File nowyKatalogZadania = createDirectory(new File("./src/Zadania/" + name));

    }
}