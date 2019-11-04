/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoaerolinea;

import controller.ViajeJpaController;
import controller.VueloJpaController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import model.Viaje;
import model.Vuelo;

/**
 * FXML Controller class
 *
 * @author maria
 */
public class FXMLDocumentController implements Initializable {

    VueloJpaController Vueloo = new VueloJpaController();
    ViajeJpaController Viajee = new ViajeJpaController();
    @FXML
    private ComboBox<String> cborigen;
    @FXML
    private ComboBox<String> cbdestino;
    @FXML
    private RadioButton rbroundtrip;
    @FXML
    private RadioButton rbida;
    @FXML
    private Button btnbuscar;
    @FXML
    private TableView tablevuelos;
    @FXML
    private ComboBox<String> cbfechaida;
    @FXML
    private ComboBox<String> cbfecharegreso;
    @FXML
    private ComboBox<String> cbcantdepasajeros;
    
    private boolean filtro = true;
    
    private Vuelo registroClick;
    @FXML
    private Menu menuacceder;
    @FXML
    private StackPane stackpanePrincipal;
    @FXML
    private AnchorPane anchorpanePrincipal;
    @FXML
    private MenuItem inicio;
    @FXML
    private StackPane papa;
    @FXML
    private AnchorPane hijo;
    @FXML
    private Button btncambio;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciarCombo();
        FiltrarDatos("%");
    }    

    @FXML
    private void buscarvuelos(ActionEvent event) {
         List<Vuelo> lstVuelo = Vueloo.findVueloEntities();
        for (int i = 0; i <lstVuelo.size() ; i++) {
            if(lstVuelo.get(i).getCuidadorigen().equals(cborigen.getValue())&& lstVuelo.get(i).getCiudaddestino().equals(cbdestino.getValue())){
                filtro= true;
                FiltrarDatos(cborigen.getValue());
                FiltrarDatos(cbdestino.getValue());
            }
            else{
                filtro= false;
//                FiltrarDatos(txtfiltro1.getText());
            }  
            
        }
        
        
    }
    
    
    private void iniciarCombo(){
        List<Vuelo> lstVuelo = Vueloo.findVueloEntities();
        List<Viaje> lstViaje = Viajee.findViajeEntities();
        for (int i = 0; i < lstVuelo.size(); i++) {
            cborigen.getItems().add(i, lstVuelo.get(i).getCuidadorigen());
        }
        for (int i = 0; i < lstVuelo.size(); i++) {
            cbdestino.getItems().add(i,lstVuelo.get(i).getCiudaddestino());
        }
        for (int i = 0; i < lstViaje.size(); i++) {
            cbfechaida.getItems().add(i, lstViaje.get(i).getFechasalida());
        }
        for (int i = 0; i < lstViaje.size(); i++) {
            cbfecharegreso.getItems().add(i,lstViaje.get(i).getFechallegada());
        }
    }
    
     private void FiltrarDatos(String nombre){
        
        tablevuelos.getColumns().clear();
        
        //crear la columnas
        TableColumn colciudadO = new TableColumn("Ciudad de Origen");
        colciudadO.setCellValueFactory(new PropertyValueFactory("cuidadorigen"));
        
        TableColumn colciudadD = new TableColumn("Ciudad Destino");
        colciudadD.setCellValueFactory(new PropertyValueFactory("ciudaddestino"));
        
        TableColumn colhora = new TableColumn("Hora");
        colhora.setCellValueFactory(new PropertyValueFactory("hora"));
        
        TableColumn colPrecio = new TableColumn("Precio");
        colPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
        
        TableColumn colDuracion = new TableColumn("Duracion");
        colDuracion.setCellValueFactory(new PropertyValueFactory("durcion"));
        
        TableColumn colNumVuelo = new TableColumn("Numero de Vuelo");
        colNumVuelo.setCellValueFactory(new PropertyValueFactory("idVuelo"));
        
        tablevuelos.getColumns().addAll(colciudadO, colciudadD, colhora, colPrecio, colDuracion, colNumVuelo);
        
        List<Vuelo> lstMuseo = null;
        if(filtro){   
        lstMuseo = Vueloo.findVueloEntetisFilter(nombre);
           }
        if (!filtro){
            lstMuseo = Vueloo.findVueloEntetisFilter2(nombre);
        }
        
        ObservableList items = FXCollections.observableArrayList(lstMuseo);
        
        tablevuelos.setItems(items);
        
        tablevuelos.setRowFactory(tv->{
            TableRow<Vuelo> row = new TableRow();
            row.setOnMouseClicked(event->{
                if( !row.isEmpty() && event.getButton() ==  MouseButton.PRIMARY && event.getClickCount() == 2){
                    registroClick = row.getItem();
                }
            });
            return row;
        });
        
    } 

    @FXML
    private void abririnicio(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("IniciarSesion.fxml"));
        Scene scene = papa.getScene();
//        root.translateYProperty().set(scene.getHeight());

        stackpanePrincipal.getChildren().clear();
        stackpanePrincipal.getChildren().add(root);
//        System.out.println("Si");
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("IniciarSesion.fxml"));
//        Parent root = loader.load();
//        Scene scene = btnbuscar.getScene();
//        root.translateYProperty().set(scene.getHeight());
//
//        stackpanePrincipal.getChildren().add(root);
//
//        Timeline timeline = new Timeline();
//        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
//        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
//        timeline.getKeyFrames().add(kf);
//        timeline.setOnFinished(t -> {
//            stackpanePrincipal.getChildren().remove(anchorpanePrincipal);
//        });
//        timeline.play();
    }

    @FXML
    private void cambiar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IniciarSesion.fxml"));
        Parent root = loader.load();
        Scene scene = btncambio.getScene();
        root.translateYProperty().set(scene.getHeight());

        stackpanePrincipal.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> {
            stackpanePrincipal.getChildren().remove(anchorpanePrincipal);
        });
        timeline.play();
    }

    
}
