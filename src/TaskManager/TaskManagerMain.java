package TaskManager;

import java.util.ArrayList;
import java.util.Scanner;

public class TaskManagerMain {
    Scanner scanner = new Scanner(System.in);
    ArrayList<Task> tasks = new ArrayList<>();
    ArrayList<Epic> epics = new ArrayList<>();
    Condition condition;
    Task task = new Task();
    Epic epic = new Epic(scanner);

    public void taskManagerMain() {
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

                    break;
                case "6":

                    break;
                case "7":
                    return;
                default:
                    System.out.println("Введена неизвестная команда, попробуйте снова");
                    break;
            }
        }
    }

    private void printMenu() {
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

    private void printTaskTypeMenu() {
        System.out.println("""
                Введите номер типа задачи для продолжения
                1. Обычная задача
                2. Эпик
                3. Подзадача к эпику
                """);
    }

    //-----------------------------------------------------------------------------------------------------------------

    private void printListByType() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    printListOfTasks();
                    break;
                case "2":
                    printListOfEpics();
                    break;
                case "3":
                    int id = scanner.nextInt();
                    printListOfSubtasks(id);
                    break;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
                    break;
            }
        }
    }

    private void printListOfEpics() {
        epic.printListOfEpics(epics);
    }

    private void printListOfTasks() {
        task.printListOfTasks(tasks);
    }

    private void printListOfSubtasks(int id) {
        epic.printListOfSubtasks(epics.get(id - 1));
    }

    //-----------------------------------------------------------------------------------------------------------------

    public void deleteAllTasks() {
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
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
                    break;
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    public void getById() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    System.out.println("Введите id задачи:");
                    int taskId = scanner.nextInt();
                    System.out.println(tasks.get(taskId - 1));
                    break;
                case "2":
                    System.out.println("Введите id эпика:");
                    int epicId = scanner.nextInt();
                    System.out.println(epics.get(epicId - 1));
                    break;
                case "3":
                    System.out.println("Введите id эпика из которого желаете получить информацию о подзадаче:");
                    int epicSubtaskId = scanner.nextInt();
                    epic.getSubtaskById(epics.get(epicSubtaskId - 1));
                    break;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
                    break;
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------

    private void createObjectTask() {
        while (true) {
            printTaskTypeMenu();
            String taskTypeCommand = scanner.next();
            switch (taskTypeCommand) {
                case "1":
                    createTask();
                    break;
                case "2":
                    createEpic();
                    break;
                case "3":
                    addSubtask();
                    break;
                default:
                    System.out.println("Введена неверная команда, повторите ввод");
                    break;
            }
        }
    }

    private void createTask() {
        System.out.println("Название:");
        String name = scanner.nextLine();
        System.out.println("Описание");
        String description = scanner.nextLine();
        System.out.println("Статус задачи");
        while (true) {
            String conditionType = scanner.next();
            switch (conditionType) {
                case "NEW":
                    condition = Condition.NEW;
                    break;
                case "IN_PROGRESS":
                    condition = Condition.IN_PROGRESS;
                    break;
                case "DONE":
                    condition = Condition.DONE;
                    break;
                default:
                    System.out.println("Введен неверный статус");
                    break;
            }
        }
        tasks.add(new Task(name, description, condition));
    }

    private void createEpic() {
        System.out.println("Название:");
        String name = scanner.nextLine();
        System.out.println("Описание");
        String description = scanner.nextLine();
        System.out.println("Статус эпика");
        while (true) {
            String conditionType = scanner.next();
            switch (conditionType) {
                case "NEW":
                    condition = Condition.NEW;
                    break;
                case "IN_PROGRESS":
                    condition = Condition.IN_PROGRESS;
                    break;
                case "DONE":
                    condition = Condition.DONE;
                    break;
                default:
                    System.out.println("Введен неверный статус");
                    break;
            }
        }
        epics.add(new Epic(name, description, condition));
    }

    private void addSubtask() {
        epic.printListOfEpics(epics);
        System.out.println("Выберите номер нужного эпика");
        int id = scanner.nextInt();
        epic.addSubtask(epics.get(id - 1));
    }

    //-----------------------------------------------------------------------------------------------------------------

}

