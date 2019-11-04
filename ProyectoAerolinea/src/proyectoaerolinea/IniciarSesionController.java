/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoaerolinea;

import clinicauna.util.AppContext;
import clinicauna.util.Mensaje;
import controller.UsuarioJpaController;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import model.Usuario;

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

    UsuarioJpaController usuService = new UsuarioJpaController();
    
    @FXML
    private void iniciarsesion(ActionEvent event) {
        if(txtnombreusuario.getText().equals(null)||txtnombreusuario.getText().isEmpty()||txtnombreusuario.getText().equals("")||txtcontra.getText().equals(null)||txtcontra.getText().isEmpty()||txtcontra.getText().equals("")){
            Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :( ", "Faltan datos por ingresar");
        }else{
           
           List<Usuario> lista1=new ArrayList<Usuario>();
           lista1=usuService.findUsuarioEntetisFilter(txtnombreusuario.getText());
           
           List<Usuario> lista2=new ArrayList<Usuario>();
           lista2=usuService.findUsuarioEntetisFilter2(txtcontra.getText());
           
           
           Usuario log = new Usuario();
           if(lista1!=null && lista2!=null){
               for(int i=0; i<lista1.size(); i++){
                   for(int j=0; j<lista2.size(); j++){
                        if(lista1.get(i).equals(lista2.get(j))){
                            log=lista1.get(i);
                            AppContext.getInstance().set("UsuarioLoggeado", log);
                        }   
                    }
               }
           }else{
               Mensaje.showAndWait(Alert.AlertType.ERROR, "Opps :( ", "Usuario o contraseña incorrecta");
           }
                
                
        }
    }
    
//    //Para obtener el usuario loggeado en otra parte del proyecto:
//    Usuario usu=(Usuario) AppContext.getInstance().get("UsuarioLoggeado");
    
}
