/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicauna.util;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

/**
 *
 * @author ccarranza
 */
public class Mensaje {

    public static void show(AlertType tipo, String titulo, String mensaje) {

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
//        if(!SettingsController.idioma){
//          alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
//         }else{
//            System.out.println("skljf");
//           alert.getButtonTypes().set(0, new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE));  
//        }
        alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
        alert.show();
    }
    
    public static void showAndWait(AlertType tipo, String titulo, String mensaje) {

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
//        if(!SettingsController.idioma){
//          alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
//         }else{
//            System.out.println("skljf");
//           alert.getButtonTypes().set(0, new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE));  
//        }
        alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
        alert.showAndWait();
    }

    public static void showImage(AlertType tipo, String titulo, String mensaje) {
        ImageView image = new ImageView("juegoPreguntados/resources/ciencia.png");
        image.setFitHeight(100);
        image.setFitWidth(100);
        image.setPreserveRatio(true);
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.setGraphic(image);
        alert.show();
    }

    public static void showModal(AlertType tipo, String titulo, Window padre, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.initOwner(padre);
        alert.setContentText(mensaje);
//        if(!SettingsController.idioma){
//          alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
//         }else{
//            System.out.println("skljf");
//           alert.getButtonTypes().set(0, new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE));  
//        }
        alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
        alert.showAndWait();
    }

    public static Boolean showConfirmation(String titulo, Window padre, String mensaje) {
        Alert alert = new Alert(AlertType.CONFIRMATION, mensaje, new ButtonType("OK", ButtonBar.ButtonData.YES), new ButtonType("Cancel", ButtonBar.ButtonData.NO));
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.initOwner(padre);
        alert.setContentText(mensaje);
//        if(!SettingsController.idioma){
//          alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.YES));
//          alert.getButtonTypes().set(1, new ButtonType("Cancel", ButtonBar.ButtonData.NO));
//         }else{
//            System.out.println("skljf");
//           alert.getButtonTypes().set(0, new ButtonType("Aceptar", ButtonBar.ButtonData.YES)); 
//            alert.getButtonTypes().set(1, new ButtonType("Cancelar", ButtonBar.ButtonData.NO)); 
//        }
        alert.getButtonTypes().set(0, new ButtonType("OK", ButtonBar.ButtonData.YES));
        Optional<ButtonType> result = alert.showAndWait();
        System.out.println("Ei " + result.get());
        return result.get().getButtonData() == ButtonBar.ButtonData.YES;
    }
}
