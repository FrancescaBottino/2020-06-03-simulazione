/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Avversario;
import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.TopPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	double numero;
    	
    	try {
    		numero = Double.parseDouble(txtGoals.getText());
    		
    	}catch(NumberFormatException e) {
    		
    		txtResult.appendText("Devi inserire un numero decimale");
    		return;
    	}
    	
    	model.creaGrafo(numero);
    	
    	if(model.getGrafo()==null) {
    		txtResult.setText("Errore: grafo non creato");
    		return;
    	}
    	else {
    		
    		txtResult.appendText("Grafo creato."+"\nNumero di vertici: "+model.getNVertici()+"\nNumero di archi: "+model.getNArchi());
    	}

    }

    @FXML
    void doDreamTeam(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	Integer numero;
    	
    	try {
    		numero = Integer.parseInt(txtK.getText());
    		
    	}catch(NumberFormatException e) {
    		
    		txtResult.appendText("Devi inserire un numero decimale");
    		return;
    	}	
    	
    	txtResult.appendText("Calcolo ricorsione..\n");
    	
    	model.doRicorsione(numero);
    	

    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	
    	txtResult.clear();
    	
    	if(model.getGrafo() == null) {
    		txtResult.appendText("Devi prima inserire il grafo");
    		return;
    	}
    	
    	
    	TopPlayer tp = model.getTopPlayer();
    	
    	txtResult.appendText("Giocatore migliore: "+tp.getTop().toString());
    	txtResult.appendText("\nLista avversari: \n");
    
    	for(Avversario a: tp.getAvversari()) {
    		txtResult.appendText(a.getPlayer().toString()+" | peso: "+a.getPeso()+"\n");
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
