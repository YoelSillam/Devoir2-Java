package sio.devoir2graphiques;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import sio.devoir2graphiques.Tools.ConnexionBDD;
import sio.devoir2graphiques.Tools.GraphiqueController;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class Devoir2GraphiquesController implements Initializable
{
    XYChart.Series<String,Number> serieGraph1;
    XYChart.Series<String,Number> serieGraph2;
    @FXML
    private Button btnGraph1;
    @FXML
    private Button btnGraph2;
    @FXML
    private Button btnGraph3;
    @FXML
    private AnchorPane apGraph1;
    @FXML
    private LineChart graph1;
    @FXML
    private Label lblTitre;
    @FXML
    private AnchorPane apGraph2;
    @FXML
    private AnchorPane apGraph3;
    @FXML
    private PieChart graph3;
    @FXML
    private BarChart graph2;
    ConnexionBDD maCnx;
    GraphiqueController graphiqueController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblTitre.setText("Devoir : Graphique n°1");
        apGraph1.toFront();
        graph1.getData().clear();
        // A vous de jouer

        try {
            maCnx = new ConnexionBDD();
            graphiqueController = new GraphiqueController();
            Graphique1();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); //Mettre des alertes
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void menuClicked(Event event) throws SQLException {
        if(event.getSource() == btnGraph1)
        {
            lblTitre.setText("Devoir : Graphique n°1");
            apGraph1.toFront();
            Graphique1();
            // A vous de jouer

        }
        else if(event.getSource() == btnGraph2)
        {
            lblTitre.setText("Devoir : Graphique n°2");
            apGraph2.toFront();

            HashMap<String, ArrayList<String>> lesDatasDuGraph2 = graphiqueController.GetDataGraphique2();
            graph2.getData().clear();

// Je crée deux séries distinct -> une serie pour les femmes et une autre pour les hommes
            XYChart.Series<String, Integer> serieHomme = new XYChart.Series<>();
            XYChart.Series<String, Integer> serieFemme = new XYChart.Series<>();

//Je donne les noms des series -> Homme Et Femme
            serieHomme.setName("Homme");
            serieFemme.setName("Femme");

            for (String trancheAge : lesDatasDuGraph2.keySet()) {
                for (int i = 0; i < lesDatasDuGraph2.get(trancheAge).size(); i += 2) {
                    String sexe = lesDatasDuGraph2.get(trancheAge).get(i);
                    int nombre = Integer.parseInt(lesDatasDuGraph2.get(trancheAge).get(i + 1));


                    if ("Homme".equals(sexe)) {
                        serieHomme.getData().add(new XYChart.Data<>(trancheAge, nombre));
                    } else if ("Femme".equals(sexe)) {
                        serieFemme.getData().add(new XYChart.Data<>(trancheAge, nombre));
                    }
                }
            }

// Ajouter les séries au graphique
            graph2.getData().addAll(serieHomme, serieFemme);



        }
        else {
            lblTitre.setText("Devoir : Graphique n°3");
            apGraph3.toFront();

            // A vous de jouer
            graph3.getData().clear();
            HashMap<String, Integer> lesDatasDuGraph3 = graphiqueController.GetDataGraphique3();
            ObservableList<PieChart.Data> lst = FXCollections.observableArrayList();

            for (String pourcentage : lesDatasDuGraph3.keySet()) {
                lst.add(new PieChart.Data(pourcentage, lesDatasDuGraph3.get(pourcentage)));
            }
            graph3.setData(lst);

            for (PieChart.Data entry : graph3.getData()) {
                Tooltip tooltip = new Tooltip(entry.getPieValue() + ":" + entry.getName());
                tooltip.setStyle("-fx-background-color:red");
                Tooltip.install(entry.getNode(), tooltip);
            }
        }

        }
    public void Graphique1() {
        TreeMap<Integer, Integer> lesDatasDuGraph = graphiqueController.GetDataGraphique1();
        graph1.getData().clear();
        serieGraph1 = new XYChart.Series<>();
        serieGraph1.setName("Moyenne");

        for (Integer age : lesDatasDuGraph.keySet()) {
            serieGraph1.getData().add(new XYChart.Data<>(age.toString(), lesDatasDuGraph.get(age)));
        }

        graph1.getData().add(serieGraph1);
    }

}