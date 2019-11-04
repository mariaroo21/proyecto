/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoaerolinea;

import controller.UsuarioJpaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Usuario;

/**
 * FXML Controller class
 *
 * @author maria
 */
public class RegistrarseController implements Initializable {
    private String modalidad  = "INSERTAR";
    UsuarioJpaController Usuarioo = new UsuarioJpaController();
    @FXML
    private TextField txtnombre;
    @FXML
    private TextField txtapellido;
    @FXML
    private TextField txtdireccion;
    @FXML
    private TextField txtcorreo;
    @FXML
    private TextField txtnomusuario;
    @FXML
    private TextField txtcontrase;
    @FXML
    private TextField txttelefono;
    @FXML
    private TextField txtcelular;
    @FXML
    private TextField txtfechaNaci;
    @FXML
    private Button btnregistro;
    @FXML
    private Button btnvolver;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void registar(ActionEvent event) {
        boolean PuedeGuardar = true;
        
        if(txtnombre.getText().isEmpty()){
            PuedeGuardar = false;
        }
        if(txtapellido.getText().isEmpty()){
            PuedeGuardar = false;
        }
        if(txtcorreo.getText().isEmpty()){
            PuedeGuardar = false;
        }
        if(txtdireccion.getText().isEmpty()){
            PuedeGuardar = false;
        }
        if(txtnomusuario.getText().isEmpty()){
            PuedeGuardar = false;
        }
        if(txtcontrase.getText().isEmpty()){
            PuedeGuardar = false;
        }
        if(txtfechaNaci.getText().isEmpty()){
            PuedeGuardar = false;
        }
        if(txttelefono.getText().isEmpty()){
            PuedeGuardar = false;
        }
        if(txtcelular.getText().isEmpty()){
            PuedeGuardar = false;
        }
        
        if( !PuedeGuardar){
            Alert error = new Alert(Alert.AlertType.WARNING);
            error.setTitle("OJO A LOS DATOS");
            error.setContentText("Hay informacion que es obligatoria");
            error.showAndWait();
            return;
        }
        //aca esta validado
        
        if( modalidad.equals("INSERTAR") ){
            try {
                Usuario nuevo = new Usuario();
                nuevo.setNombre(txtnombre.getText());
                nuevo.setApellido(txtapellido.getText());
                nuevo.setDireccion(txtdireccion.getText());
                nuevo.setCorreo(txtcorreo.getText());
                nuevo.setUsuario(txtnomusuario.getText());
                nuevo.setContraseña(txtcontrase.getText());
                nuevo.setFechanaci(txtfechaNaci.getText());
                nuevo.setCelular(txtcelular.getText());
                nuevo.setTelefono(txttelefono.getText());
                
                
                Usuarioo.create(nuevo);
                
                Alert info = new Alert(Alert.AlertType.CONFIRMATION);
                info.setTitle("Bien hecho");
                info.setContentText("Los datos se guardaron correctamente");
                info.showAndWait();
                
            } catch (Exception e) {
                Alert mensaje = new Alert(Alert.AlertType.ERROR);
                mensaje.setTitle("Error");
                mensaje.setContentText("Algo salio mal");
                mensaje.showAndWait();
            } 
            txtnombre.clear();
            txtapellido.clear();
            txtcontrase.clear();
            txtnomusuario.clear();
            txtcorreo.clear();
            txtdireccion.clear();
            txtfechaNaci.clear();
            txttelefono.clear();
            txtcelular.clear();
        } 
    }

    @FXML
    private void volver(ActionEvent event) {
    }
    
}
