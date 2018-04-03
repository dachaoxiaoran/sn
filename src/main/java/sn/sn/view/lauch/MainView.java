package sn.sn.view.lauch;


import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainView extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(this.getClass().getResource("../fxml/MainView.fxml"));
	        HBox rootLayout = (HBox) loader.load();
	        Scene scene = new Scene(rootLayout);
	        
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        primaryStage.setMaximized(true);
	        
	        TabPane tabPane = (TabPane) rootLayout.getChildren().get(0);
	        ObservableList<Tab> tabs = tabPane.getTabs();
	        for (int i = 0; i < tabs.size(); i++) {
	        	Tab tab = tabs.get(i);
	        	TextArea textArea = (TextArea) tab.getContent();
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
