package com.byval.asp_example_library.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

//Контроллер окна "Загрузчик"
@Component
@FxmlView("download.fxml")
public class DownloadController {

    //--------Элементы интерфейса-------

    @FXML
    //TextArea с исходным кодом
    private TextArea loadTextCode;

    @FXML
    //Список разделов библотеки
    private ComboBox<String> namePath;

    @FXML
    //Группа переключателей
    private ToggleGroup download;

    @FXML
    //Переключатель "Существующий раздел"
    private RadioButton namePathLoad;//

    @FXML
    //Переключатель "Новый раздел"
    private RadioButton newPathLoad;

    @FXML
    //TextField для ввода нового имени раздела
    private TextField newNamePath;

    @FXML
    //TextField для ввода нового имени примера
    private TextField nameExampleLoad;

    @FXML
    //Кнопка "Загрузить"
    private Button Download;

    @FXML

    //Кнопка "Домой"
    private Button home;

    @FXML
    void initialize() {

        //настройка окна с информацией
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);

        //Загружаем разделы namePath
        MainController.loadFolders(namePath,MainController.DIR);

        //Переключатель "Существующий раздел"
        namePathLoad.setOnAction(actionEvent -> {
                //Невидимое поле для имени нового раздела
                newNamePath.setDisable(true);
                //Видимый список разделов
                namePath.setDisable(false);
                namePathLoad.setSelected(true);
                newPathLoad.setSelected(false);
        });

        //Переключатель "Новый раздел"
        newPathLoad.setOnAction(actionEvent -> {
                //Видимое поле для имени нового раздела
                newNamePath.setDisable(false);
                //Невидимый список разделов
                namePath.setDisable(true);
                newPathLoad.setSelected(true);
                namePathLoad.setSelected(false);
        });

        //Кнопка "Загрузить"
        Download.setOnAction(actionEvent -> {
            //Получаем значение переключателя
            RadioButton selectRB = (RadioButton) download.getSelectedToggle();
            //Загрузка в существующий раздел
            if((selectRB.getText() == namePathLoad.getText()) && nameExampleLoad.getText()!="" && namePath.getValue()!=null){
                //Строим путь до нового файла
                String dirFile = (MainController.DIR + namePath.getValue() + "/" + nameExampleLoad.getText() + ".lp");
                String str = loadTextCode.getText();
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(dirFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                writer.println(str);
                writer.close();
                alert.setContentText("Пример загружен!");
                alert.showAndWait();

            }else if((selectRB.getText() == namePathLoad.getText()) && (nameExampleLoad.getText()=="" || namePath.getValue()==null)){
                alert.setContentText("Не введено название примера или не выбран раздел!");
                alert.showAndWait();
            }

            //Загрузка в новый раздел
            if((selectRB.getText() == newPathLoad.getText())&& newNamePath.getText()!="" && nameExampleLoad.getText()!=""){

                new File(MainController.DIR + newNamePath.getText()).mkdirs();
                //Строим путь до нового файла
                String dirFile = (MainController.DIR + newNamePath.getText() + "/" + nameExampleLoad.getText() + ".lp");
                String str = loadTextCode.getText();

                //Копируем и вставляем исполняемый файл clingo.exe в новый раздел
                File srcClingo = new File(MainController.CLINGO);
                File pastClingo = new File(MainController.DIR + newNamePath.getText() + "/" + "clingo.exe");
                try {
                    Files.copy(srcClingo.toPath(), pastClingo.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(dirFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                writer.println(str);
                writer.close();
                alert.setContentText("Пример и раздел загружены!");
                alert.showAndWait();

            }else if((selectRB.getText() == newPathLoad.getText())&& (newNamePath.getText()=="" || nameExampleLoad.getText()=="")){
                alert.setContentText("Не введено название примера или раздела!");
                alert.showAndWait();
            }
        });

        //Кнопка "Домой"
        home.setOnAction(actionEvent -> {
            MainController.homeButton(home);
        });
    }
}
