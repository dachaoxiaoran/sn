package sn.sn.launch;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sn.sn.timer.BondTimer;
import sn.sn.timer.BuySellTimer;
import sn.sn.timer.GoldTimer;
import sn.sn.timer.RateTimer;
import sn.sn.timer.XsCrmTradeTimer;

/**
 * 
 * @author 王超
 */
public class Launch extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		TextArea bond = null;
		TextArea rate = null;
		TextArea price = null;
		TextArea hand = null;
		try {
			FXMLLoader loader = new FXMLLoader();
			//loader.setLocation(new File("C:/MainView.fxml").toURL());
	        loader.setLocation(this.getClass().getResource("../view/fxml/MainView.fxml"));
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
	        	if (i == 0) bond = textArea;
	        	if (i == 1) rate = textArea;
	        	if (i == 2) price = textArea;
	        	if (i == 3) hand = textArea;
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
		
		new RateTimer(rate).run();
		new GoldTimer(price).run();
		new BondTimer(bond).run();
		new XsCrmTradeTimer().run();
		new BuySellTimer(hand).run();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
