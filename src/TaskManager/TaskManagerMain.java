package TaskManager;

import java.util.ArrayList;
import java.util.Scanner;

public class TaskManagerMain {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Task> tasks = new ArrayList<>();
    static ArrayList<Epic> epics = new ArrayList<>();
    static Task task = new Task(scanner);
    static Epic epic = new Epic(scanner);

    public static void taskManagerMain() {
        while (true) {
            printMenu();
            String command = scanner.next();
            switch (command) {
                case "1":
                    printListByType();
                    break;
                case "2":
                    deleteAllTasks();
                    break;
                case "3":
                    getById();
                    break;
                case "4":
                    createObjectTask();
                    break;
                case "5":
                    updateById();
                    break;
                case "6":
                    deleteById();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Введена неизвестная команда, попробуйте снова");
                    break;
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private static void printMenu() {
        System.out.println("""
                Введите команду для менеджера задач
                1. Получить список задач выбранного типа
                2. Удаление всех задач выбранного типа
                3. Получение задачи по идентификатору
                4. Создание новой задачи
                5. Обновить задачу или ее статус
                6. Удаление конкретной задачи по идентификатору
                7. Выйти из программы
                """);
    }

    private static void printTaskTypeMenu() {
        System.out.println("""
                Введите номер типа задачи для продолжения
                1. Обычная задача
                2. Эпик
                3. Подзадача к эпику
                4. Выход
                """);
    }

    //-----------------------------------------------------------------------------------------------------------------

    private static void printListByType() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    task.printListOfTasks(tasks);
                    break;
                case "2":
                    epic.printListOfEpics(epics);
                    break;
                case "3":
                    System.out.println("Введите номер эпика");
                    int id = scanner.nextInt();
                    epic.printListOfSubtasks(epics.get(id - 1));
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private static void deleteAllTasks() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    task.deleteListOfTasks(tasks);
                    break;
                case "2":
                    epic.deleteListOfEpics(epics);
                    break;
                case "3":
                    int id = scanner.nextInt();
                    epic.deleteListOfSubtasks(epics.get(id - 1));
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private static void getById() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    System.out.println("Введите id задачи:");
                    int taskId = scanner.nextInt();
                    task.printTaskById(tasks.get(taskId - 1));
                    break;
                case "2":
                    System.out.println("Введите id эпика:");
                    int epicId = scanner.nextInt();
                    epic.printEpicById(epics.get(epicId - 1));
                    break;
                case "3":
                    System.out.println("Введите id эпика из которого желаете получить информацию о подзадаче:");
                    int epicSubtaskId = scanner.nextInt();
                    epic.getSubtaskById(epics.get(epicSubtaskId - 1));
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private static void createObjectTask() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    tasks.add(task.createTask());
                    break;
                case "2":
                    epics.add(epic.createEpic());
                    break;
                case "3":
                    epic.printListOfEpics(epics);
                    System.out.println("Выберите номер нужного эпика");
                    int id = scanner.nextInt();
                    epic.addSubtask(epics.get(id - 1));
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private static void updateById() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    System.out.println("Введите id задачи:");
                    int taskId = scanner.nextInt();
                    tasks.set(taskId - 1, task.updateTask());
                    break;
                case "2":
                    System.out.println("Введите id эпика:");
                    int epicId = scanner.nextInt();
                    epics.set(epicId - 1, epic.updateEpic(epics.get(epicId - 1)));
                    break;
                case "3":
                    System.out.println("Введите id эпика из которого желаете получить информацию о подзадаче:");
                    int epicSubtaskId = scanner.nextInt();
                    epic.updateSubtask(epics.get(epicSubtaskId - 1));
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private static void deleteById() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    System.out.println("Введите id задачи:");
                    int taskId = scanner.nextInt();
                    tasks.remove(taskId - 1);
                    break;
                case "2":
                    System.out.println("Введите id эпика:");
                    int epicId = scanner.nextInt();
                    epics.remove(epicId - 1);
                    break;
                case "3":
                    System.out.println("Введите id эпика у которого желаете удалить подзадачу:");
                    int epicSubtaskId = scanner.nextInt();
                    epic.deleteById(epics.get(epicSubtaskId - 1));
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
            }
        }
    }
}

