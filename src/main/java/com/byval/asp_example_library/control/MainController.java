package com.byval.asp_example_library.control;
import java.io.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

//Контроллер окна "Библиотека примеров ASP"
@Component
@FxmlView("main.fxml")
public class MainController {

    //Путь к данным библиотеки примеров
    public static final String DIR = ("src/data/");

    //Путь к исполняемому файлу системы clingo
    public static final String CLINGO = ("src/data/clingo.exe");

    //--------Элементы интерфейса-------
    @FXML
    //TextArea с исходным кодом примера
    private TextArea mainTextCode;

    @FXML
    //TextArea с результатом clingo
    private TextArea clingoText;


    @FXML
    //Список разделов библотеки
    private ComboBox<String> namePath;

    @FXML
    //Список примеров библотеки
    private ComboBox<String> nameExample;

    @FXML
    //Пункт меню "Загрузка"
    private MenuItem menuDownload;

    @FXML
    //Пункт меню "Удалить/Изменить"
    private MenuItem menuDeleteChange;

    @FXML
    //Пункт меню "Методические рекомендации"
    private MenuItem menuHelpInf;

    @FXML
    //Кнопка "Обновить"
    private Button update;

    @FXML
    //Кнопка "Выход"
    private Button exit;

    @FXML
    //Кнопка "Запуск"
    private Button runCode;

    @FXML
    //Кнопка "Вывод результата"
    private Button outResult;

    @FXML
    void initialize() {

        //Настройка окна с информацией
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);


        //-----ФУНКЦИОНАЛ MenuBar-----

        //Окно загрузки примеров
        menuDownload.setOnAction(actionEvent -> {
            clearWindow(namePath,nameExample,mainTextCode);
            clingoText.clear();
            loadWindow("/view/download.fxml");
        });

        //Окно редактирования примеров
        menuDeleteChange.setOnAction(actionEvent -> {
            clearWindow(namePath,nameExample,mainTextCode);
            clingoText.clear();
           loadWindow("/view/change.fxml");
        });

        //Окно со справкой
        menuHelpInf.setOnAction(actionEvent -> {
            clearWindow(namePath,nameExample,mainTextCode);
            clingoText.clear();
            loadWindow("/view/help.fxml");
        });

        //------ГЛАВНЫЙ ФУНКЦИОНАЛ-----

        //загружаем разделы namePath в список ComboBox
        loadFolders(namePath, DIR);

        //загружаем примеры nameExample в список ComboBox
        loadComboBox(clingoText,namePath, nameExample, DIR);

        //Вывод исходного кода в TextArea
        nameExample.valueProperty().addListener((obs, oldValue, newValue) -> {
            //Если пример nameExample не выбран - ничего не делать
            if (newValue == null) {
                //Иначе вывести исходный код
            } else {
                showCode(mainTextCode,namePath,nameExample,DIR);
            }
        });

        //Кнопка exit "Выход"
        exit.setOnAction(actionEvent -> {
            homeButton(exit);
        });


        //Кнопка "update"
        update.setOnAction(actionEvent -> {
            nameExample.getItems().clear();
            namePath.getItems().clear();
            clingoText.clear();
            mainTextCode.clear();
            loadFolders(namePath, DIR);
            loadComboBox(clingoText,namePath, nameExample, DIR);
            alert.setContentText(" Библиотека обновлена!");
            alert.showAndWait();



        });

        //Кнопка "Вывод результата"
        outResult.setOnAction(actionEvent -> {
            //Запись результа clingo в файл data.txt
            File data = new File(DIR + namePath.getValue() + "/" + "data.txt");
            clingoText.clear();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(data));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String str = null;
            //Читаем файл data.txt
            while (true) {
                try {
                    str = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (str == null) {

                    break;
                }
                //Вывод данных из data.txt в TextArea
                clingoText.appendText(str + "\n");
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        //кнопка  runCode "Запустить"
        runCode.setOnAction(actionEvent -> {

            //Создаём новый файл для записи результата выполнения примера программы в clingo
            File data = new File(DIR + namePath.getValue() + "/" + "data.txt");

            if((namePath.getValue()!= null) && (nameExample.getValue()!=null)) {
                FileWriter writer = null;
                try {
                    writer = new FileWriter(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Runtime runtime = Runtime.getRuntime();
                try {
                    //Запуск командной строки и передача ей параметров для выполнения системой clingo примера задачи
                    //и запись результата в файл data.txt
                    Process cmdStart = runtime.exec("cmd.exe /c cd " + DIR + namePath.getValue()+ "& cmd.exe /k " + "clingo " + "\""+ nameExample.getValue()+ "\"" +
                            " > data.txt");

                    writer.close();

                    if(cmdStart!=null) {
                        alert.setContentText("Пограмма выполнена! Результат записан в файл!");
                        alert.showAndWait();
                    }else{
                        alert.setContentText("Пограмма НЕ выполнена! ");
                        alert.showAndWait();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }//Если не хватает параметров для запуска программы
            else if((namePath.getValue()==null) || (nameExample.getValue()==null)){

                alert.setContentText("Не выбран пример или раздел!");
                alert.showAndWait();
            }

        });
    }

    //Получение файлов примеров из текущего раздела
    public static void getFilesFromCurrentPath(ComboBox<String> file, String path) {

        //Указываем путь до выбранного раздела
        File myFile = new File(path);
        //Получаем файлы примеров из раздела
        File[] files = myFile.listFiles();

        if(files!=null) {
            for (int j = 0; j < files.length; j++) {
                //Исключаем файлы сlingo.exe и data.txt из добавления в список ComboBox
                if((files[j].getName().equals("clingo.exe")) || (files[j].getName().equals("data.txt"))){ }
                else {
                    //Добавляем имены файлов примеров в список
                    file.getItems().add(files[j].getName());
               }
            }
        }
    }

    //Загрузка информации в список ComboBox
    public static void loadComboBox(TextArea textArea,ComboBox<String> path, ComboBox<String> example, String dir) {

        path.valueProperty().addListener((obs, oldValue, newValue) -> {
            //Если раздел namePath не выбран
            if (newValue == null) {
                //Очищаем список с названиями примеров
                example.getItems().clear();
                //Делаем НЕ видимым список с названиями примеров
                example.setDisable(true);
            } else {
                //Очищаем с TextArea от исходного кода
                textArea.clear();
                //Очищаем список с названиями примеров
                example.getItems().clear();
                // Получаем значение раздела namePath и формируем путь к файлам примеров в разделе
                getFilesFromCurrentPath(example,dir + path.getValue());
                //Делаем видимым список с названиями примеров
                example.setDisable(false);
            }
        });

        //Действие при выборе другого примера из списка
        example.valueProperty().addListener((obs, oldValue, newValue) ->{
            if(newValue == null){}
            else{
                //Очистка TextArea от исходного кода
                textArea.clear();
            }
        });



    }

    //Получение папок разделов
    public static void loadFolders(ComboBox<String> path, String dir) {
        //Указываем путь до разделов библиотеки
        File myFolder = new File(dir);
        //Получаем разделы
        File[] folders = myFolder.listFiles();
        if(folders != null) {

            for (int i = 0; i < folders.length; i++) {
                //Исключаем файл clingo.exe при добавлении имён разделов в список ComboBox
                if (folders[i].getName().equals("clingo.exe")) {
                } else {
                    //Добавляем имена разделов в список ComboBox
                    path.getItems().add(folders[i].getName());
                }
            }
        }
    }

    //Вывод ихсодного кода в TextArea
    public static void showCode(TextArea textArea, ComboBox<String> path, ComboBox<String> example, String dir){
        //Очистка TextArea
        textArea.clear();
        //Если выбраны раздел и пример
        if (path.getValue() != null && example.getValue() != null) {
            BufferedReader reader = null;
            try {
                //Читаем файл из текущего пути
                reader = new BufferedReader(new FileReader(dir + path.getValue() + "/" + example.getValue()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String str = null;
            //Считываем построчно
            while (true) {
                try {
                    if (!((str = reader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Выгружаем код в TextArea
                textArea.appendText(str + "\n");
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //Загузка окна
    public void loadWindow(String name){

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(name));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait();
    }

    //Очистка окна
    public static void clearWindow(ComboBox<String> path, ComboBox<String> example,TextArea textArea){
        path.setValue(null);
        example.setValue(null);
        textArea.clear();
    }

    //Действие для кнопки "Домой"
    public static void homeButton(Button home){
        Stage stage = (Stage) home.getScene().getWindow();
        stage.close();
    }



}

