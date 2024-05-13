import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Scanner;

public class s31774P01 {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        // 0 - oznacza ze jestesmy w menu podstawowym w ktorym wybieramy modul
        // 1.. czym glebiej i tak dalej
        // komendy w zaleznosci jakie wybierzemy bedą zmieniac status czyli nasze połozenie w programie

        int statusCode = 0;

        Map<Integer, String> Status = new HashMap<>(){{
            put(0, "Main");
            put(10, "Menu edycji");
            put(20, "Menu sprawdzania");
            put(11, "Dodawanie zadań");
            put(12, "");
            put(14, "Menu edycji");
        }};

        List<File> folderList = new ArrayList<>() {{
            add(new File("./src/Zadania"));
            add(new File("./src/Zadania"));
        }};

        load(folderList);

        Map<String, Command> commandMapMain = new HashMap<>() {{
            put("stop", new Command("stop", "Kończy cały program", 0));
            put("edit", new Command("edit", "edytuje", 1));
        }};

        Map<String, Command> commandMapEdit = new HashMap<>() {{
            put("add", new Command("add", "Dodaje nowe zadanie", 0));
            put("edit", new Command("edit", "edytuje", 1));
        }};

        Map<Integer, Map<String, Command>> commandMap = new HashMap<>(){{
            put(0, commandMapMain);
            put(10, commandMapEdit);
        }};

        try {
            File Zadania = new File("Zadania");
            Zadania.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        do {
            System.out.println("##########################################################################");
            System.out.println("Aktualna pozycja: " + Status.get(statusCode) + ", Aktualny status: " + statusCode);
            for(Map.Entry<String, Command> entry : commandMapMain.entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue().toString());
            }
            System.out.print("Podaj komende: ");
            String input = scanner.next();
            if (input.equals("stop")){
                break;
            }
            Command command = commandMap.get(statusCode).get(input);
            if (command != null){
                statusCode = command.StatusCode;
            }else {
                System.out.println("Nieznana komenda.");
            }
        }while (true);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void load(List<File> files) {
        for (File folder : files) {
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

//    public static void add() {
//        try {
//            File fZadania = new File("./src/Zadania");
//            if (fZadania.createNewFile()) {
//                System.out.println("Tworzenie folderu do zadań.. ");
//            } else {
//                System.out.println("Sprawdzanie folderów.. ");
//            }
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//    }
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