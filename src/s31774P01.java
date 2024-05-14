import com.sun.security.jgss.GSSUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Scanner;

public class s31774P01 implements directoryMethods, editoryInterface{
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int statusCode = 0;

        Map<Integer, String> Status = new HashMap<>(){{
            put(0, "Main");
            put(1, "Menu edycji");
            put(2, "Menu sprawdzania");
        }};

        List<File> folderList = new ArrayList<>() {{
            add(new File("./src/Zadania"));
            add(new File("./src/Studenci"));
        }};
        load(folderList);

        enum CommandKey {
            stop, editmode, checkmode, add, back, edit, show, addStudent
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
            put(CommandKey.back, new Command("backToMain", "Powrót do maina", 0));
        }};
        Map<CommandKey, Command> commandMapCheck = new HashMap<>() {{
            put(CommandKey.stop, new Command("stop", "Kończy cały program", -1 ));
            put(CommandKey.add, new Command("add", "Dodaje nowe rozwiązanie(studenta)", 2));
            put(CommandKey.addStudent, new Command("addStudent", "Dodaje nowego studenta do systemu", 2, directoryMethods.createStudent()));
            put(CommandKey.back, new Command("backToMain", "Powrót do maina", 0));
        }};

        Map<Integer, Map<CommandKey, Command>> commandMap = new HashMap<>(){{
            put(0, commandMapMain);
            put(1, commandMapEdit);
            put(2, commandMapCheck);
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
            directoryMethods.createFolder(folder);
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
interface editoryInterface {
    static String runEditor() {
            Scanner scanner = new Scanner(System.in);
            String zadanieTresc = "";

            while (true){
                System.out.print("> ");
                String input = scanner.nextLine();
                if (input.equals("/exit")) {
                    break;
                }
                zadanieTresc += input;
                zadanieTresc += "\n";
            }
            return zadanieTresc;
    }
}
interface directoryMethods{
    Scanner scanner = new Scanner(System.in);
    static Runnable createStudent(){
        return () -> {
            System.out.print("Podaj Imie_Nazwisko_Numer Studenta: ");
            String nazwaStudenta = scanner.nextLine();
            File folderStudenta = new File("./src/Studenci", nazwaStudenta);
            createFolder(folderStudenta);
        };
    }
    static File createFolder(File folder){
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
                } else {
                    System.out.println("Task updated successfully!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}