/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoaerolinea;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author maria
 */
public class IniciarSesionController implements Initializable {

    @FXML
    private StackPane stackpaneinicio;
    @FXML
    private AnchorPane anchorpaneinicio;
    @FXML
    private PasswordField txtcontra;
    @FXML
    private TextField txtnombreusuario;
    @FXML
    private Label lblregistro;
    @FXML
    private Button btninicio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void registrarse(MouseEvent event) {
    }

    @FXML
    private void iniciarsesion(ActionEvent event) {
    }
    
}
