/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	txtResult.clear();
    	
    	Actor partenza = boxAttore.getValue();
    	
    	if(partenza==null) {
    		txtResult.appendText("Errore-Prima scegliere attore da tendina");
    		return;
    	}
    	
    	List<Actor> result = model.doAttoriSimili(partenza);
    	
    	txtResult.appendText("ATTORI SIMILI A: "+partenza);
    	for(Actor a : result)
    		txtResult.appendText("\n"+a);

    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	
    	String genre = boxGenere.getValue();
    	
    	if(genre==null) {
    		txtResult.appendText("Errore-Prima scegliere un genere!");
    		return;
    	}
    	
    	model.creaGrafo(genre);
    	
    	txtResult.appendText("Grafo creato!");
    	txtResult.appendText("\n#VERTICI: "+model.nVertici());
    	txtResult.appendText("\n#ARCHI: "+model.nArchi());
    	
    	boxAttore.getItems().clear();
    	boxAttore.getItems().addAll(model.getActors());

    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	txtResult.clear();
    	
    	if(!model.grafoCreato()) {
    		txtResult.appendText("Errore-prima creare il grafo");
    		return;
    	}
    	
    	try {
    		
    		int n = Integer.parseInt(txtGiorni.getText());
    		String result = model.simula(n);
    		
    		txtResult.appendText(result);
    		
    	}catch(NumberFormatException e) {
    		txtResult.appendText("Errore-inserire un numero interno di giorni");
    		return;
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	boxGenere.getItems().clear();
    	boxGenere.getItems().addAll(model.getGenres());
    }
}
