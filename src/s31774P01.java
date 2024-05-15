import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Scanner;
import java.util.stream.Stream;

public class s31774P01 implements directoryMethods, editoryInterface, studentMethods{
    public static void main(String[] args) throws IOException {

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
            stop, editmode, checkmode, add, back, edit, show, addStudent, showALL, countStudentSolution, countTaskSolution
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
            put(CommandKey.countStudentSolution, new Command("countStudentSolution", "Zwraca ilość rozwiązań studenta", 2, () -> studentMethods.countSolutions()));
            put(CommandKey.countTaskSolution, new Command("countStudentSolution", "Zwraca ilość rozwiązań danego zadania", 2, () -> studentMethods.countSolutionsTask(fileMethods.retriveTaskNames())));
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
            }
        };
    }
}
interface studentMethods{
    Scanner scanner = new Scanner(System.in);
    static ArrayList<String> retriveStudentNames(){
        ArrayList<String> names = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./src/Studenci"))) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    names.add(String.valueOf(entry.getFileName()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
    static void countSolutions(){
        int count = 0;
        System.out.print("Podaj studenta " + retriveStudentNames() +" : ");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("./src/Studenci/"+scanner.nextLine()))) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)) {
                    try(DirectoryStream<Path> subStream = Files.newDirectoryStream(entry)){
                        for(Path subEntry : subStream){
                            count++;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Ilość rozwiązań danego studenta: " + count);

        System.out.print("Press any key to leave: ");
        scanner.nextLine();
    }

    static void countSolutionsTask(ArrayList<String> taskArray){
        int count = 0;
        System.out.print("Podaj nazwe zadania " + taskArray + ": ");
        String defaultRoute = "./src/Studenci";

        String nameOfZadanie = scanner.nextLine().split("\\.")[0];

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(defaultRoute))){
            for (Path entry : stream) {
//                System.out.println(defaultRoute + "/" + entry.getFileName() + "/" + nameOfZadanie);
                if (Files.isDirectory(Path.of(defaultRoute + "/" + entry.getFileName()))) {
                    try(DirectoryStream<Path> subStream = Files.newDirectoryStream(Path.of(defaultRoute + "/" + entry.getFileName() + "/" + nameOfZadanie))){
                        for(Path subEntry : subStream){
                            count++;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Ilość rozwiązań danego zadania: " + count);
        System.out.print("Press any key to leave: ");
        scanner.nextLine();
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