package com.byval.asp_example_library.control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.awt.*;

//Контроллер окна "Методические рекомендации"
@Component
@FxmlView("help.fxml")
public class HelpController {

    @FXML
    //TextArea с методическими ркомендациями
    private TextArea helpText;

    @FXML
    //Кнопка "Домой"
    private Button home;

    @FXML
    void initialize() {

        //Кнопка "Домой"
        home.setOnAction(actionEvent -> {
            MainController.homeButton(home);
        });

    }
}
