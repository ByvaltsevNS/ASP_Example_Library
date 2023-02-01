package com.byval.asp_example_library.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

//Контроллер окна "Редактор"
@Component
@FxmlView("change.fxml")
public class ChangeController {

    //--------Элементы интерфейса-------

    @FXML
    //Список примеров библиотеки
    private ComboBox<String> nameExampleChange;

    @FXML
    //Список разделов библиотеки
    private ComboBox<String> namePathChange;

    @FXML
    //Группа переключателей
    private ToggleGroup change;

    @FXML
    //Переключатель "Изменить пример"
    private RadioButton changeExample;

    @FXML
    //Переключатель "Удалить пример"
    private RadioButton deleteExample;

    @FXML
    //Переключатель "Удалить раздел"
    private RadioButton deletePath;

    @FXML
    //Кнопка "Сохранить"
    private Button saveCode;

    @FXML
    //Кнопка "Удалить"
    private Button deleteCode;

    @FXML
    //Кнопка "Домой"
    private Button home;

    @FXML
    //TextArea с исходным кодом
    private TextArea changeTextCode;


    @FXML
    void initialize(){

        //Настройка окна с информацией
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Внимание!");
        alert.setHeaderText(null);

        //Загружаем разделы в список ComboBox
        MainController.loadFolders(namePathChange,MainController.DIR);

        //Загружаем примеры в список ComboBox
       MainController.loadComboBox(changeTextCode,namePathChange,nameExampleChange,MainController.DIR);

       //Кнопка "Домой"
        home.setOnAction(actionEvent -> {
            MainController.homeButton(home);
        });

        //Действие при выборе примера из списка
        nameExampleChange.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
            } else {
                MainController.showCode(changeTextCode,namePathChange,nameExampleChange,MainController.DIR);
            }
        });


       //Кнопка "сохранить"
        saveCode.setOnAction(actionEvent -> {
            //Получаем значение переключателя
            RadioButton selectRB = (RadioButton) change.getSelectedToggle();
            String dirFile = (MainController.DIR + namePathChange.getValue()+ "/" + nameExampleChange.getValue());
            //Если не выбран нужный параметр
            if((selectRB.getText() == changeExample.getText()) && nameExampleChange.getValue()==null){
                alert.setContentText("Выберите пример из раздела!");
                alert.showAndWait();
            }

            //Если выбран не тот переключатель
            if(selectRB.getText() != changeExample.getText()){
                alert.setContentText("Выбран не тот переключатель! Выберите \"Изменить пример\" ");
                alert.showAndWait();
            }

            //Сохраняем пример
                if ((selectRB.getText() == changeExample.getText()) && nameExampleChange.getValue()!=null)
                {
                    String str = changeTextCode.getText();
                    PrintWriter writer = null;
                    try {
                        writer = new PrintWriter(dirFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    writer.println(str);
                    writer.close();
                    alert.setContentText("Пример сохранён!");
                    alert.showAndWait();
            }
        });

        //Кнопка "удалить"
        deleteCode.setOnAction(actionEvent -> {
            //Получаем значение переключателя
            RadioButton selectRB = (RadioButton) change.getSelectedToggle();
            String dirFile = (MainController.DIR + namePathChange.getValue()+ "/" + nameExampleChange.getValue());
            File file = new File(dirFile);

            //Если не выбран нужный параметр
            if((selectRB.getText() == deleteExample.getText()) && nameExampleChange.getValue() == null){
                alert.setContentText("Выберите пример из раздела!");
                alert.showAndWait();
            }
            //Если выбран не тот переключатель
            if(selectRB.getText() == changeExample.getText()){
                alert.setContentText("Выбран не тот переключатель! Выберите \"Удалить пример\" или \"Удалить раздел\"");
                alert.showAndWait();
            }



            //Удаляем пример
            if((selectRB.getText() == deleteExample.getText()) && nameExampleChange.getValue() != null) {
                if (file.delete()) {
                    alert.setContentText("Пример удалён!");
                    namePathChange.setValue(null);
                    nameExampleChange.setValue(null);
                    changeTextCode.clear();
                } else {
                    alert.setContentText("Пример НЕ удалён!");
                }
                alert.showAndWait();

                //Удаляем полностью раздел
                }else if((selectRB.getText() == deletePath.getText()) && namePathChange!=null){
                //Строим путь до раздела
                File myFile = new File(MainController.DIR + namePathChange.getValue());
                //Получаем файлы в разделе
                File[] files = myFile.listFiles();
                if(files!=null) {
                    for (int i = 0; i < files.length; i++) {
                        //Удаляем все файлы в разделе
                        files[i].delete();
                    }
                }//Удаляем сам раздел
                if(myFile.delete()){
                    alert.setContentText("Раздел удалён!");
                    MainController.clearWindow(namePathChange,nameExampleChange,changeTextCode);
                    namePathChange.getItems().clear();
                    MainController.loadFolders(namePathChange,MainController.DIR);

                }else if((selectRB.getText() == deletePath.getText()) && namePathChange.getValue()==null){
                    alert.setContentText("Выберите раздел!");}
                else{
                    alert.setContentText("Раздел НЕ удалён!");
                }
                alert.showAndWait();
            }

            });


    }


}
