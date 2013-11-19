package de.fhb.sailsim.userinterface;

import java.util.List;

import com.sun.scenario.effect.impl.prism.PrImage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;

/**
*
* @author Salaxy
*/
public class GUI extends Application {

	Group root;
	Scene scene;

	
	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new Group();
//		root.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);");
		
		scene = new Scene(root, 800, 600,Color.BLACK);
		primaryStage.setScene(scene);
		
		
//		HBox hbox= new HBox(2);
////		hbox.setAlignment(Pos.BASELINE_LEFT);
//		VBox vbox = new VBox(3);
//		
//		root.getChildren().add(hbox);
//		hbox.getChildren().add(vbox);
////		hbox.setStyle("-fx-background-color: linear-gradient(#ff5400, #be1d00);");
//		vbox.getChildren().add(TextBuilder.create().text("Sailbootstate").fill(Color.BLUE).build());
//		vbox.getChildren().add(TextBuilder.create().text("Windsettings").fill(Color.RED).build());
//		vbox.getChildren().add(TextBuilder.create().text("Further Windsettings").fill(Color.RED).build());
//		
//		Group motionPane= new Group();
//		motionPane.setStyle("-fx-background-color: linear-gradient(#ff2200, #be1d00);");
////		motionPane.setMinSize(200, 300);
//		motionPane.getChildren().add(TextBuilder.create().text("motionPane").fill(Color.GREEN).build());
//		
////		motionPane.setStyle("-fx-background-image: url('water.jpg'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;");
//		hbox.getChildren().add(motionPane);
		
		AnchorPane controlbar= new AnchorPane();
		controlbar.setLayoutX(0);
		controlbar.setLayoutY(10);
		controlbar.setPrefSize(200, 600);
		controlbar.setStyle("-fx-background-color: linear-gradient(#ff2200, #000000);");
		
		FlowPane flow = new FlowPane(1, 5);

		
		flow.getChildren().add(TextBuilder.create().text("Sailbootstate").fill(Color.BLUE).build());
		flow.getChildren().add(TextBuilder.create().text("Windsettings").fill(Color.BLANCHEDALMOND).build());
		flow.getChildren().add(TextBuilder.create().text("Further Windsettings").fill(Color.CADETBLUE).build());
		
		controlbar.getChildren().add(flow);
		
		final MotionPane motionSpace= new MotionPane();
		motionSpace.setLayoutX(200);
		motionSpace.setLayoutY(50);
		motionSpace.setPrefSize(600, 600);
		motionSpace.setStyle("-fx-background-color: linear-gradient(#112200, #be1d00);");
//		motionSpace.setStyle("-fx-background-image: url('water.jpg'); -fx-background-repeat: stretch; -fx-background-size: stretch; -fx-background-position: center center;" );
		FlowPane motionFlow = new FlowPane();
		motionFlow.getChildren().add(TextBuilder.create().text("motionSpace").fill(Color.GREEN).build());
		motionSpace.getChildren().add(motionFlow);
		
		
		root.getChildren().add(controlbar);
		root.getChildren().add(motionSpace);
		
		//you can add Events only to scene or a focusalbe Node 
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			private double value;

			@Override
			public void handle(KeyEvent ke) {
				// TODO Auto-generated method stub
				
	               if (ke.getCode().isArrowKey()) {
//	                   Platform.exit();
	                   motionSpace.changeBlock();
	               }
				
			}});
		
		
		
		primaryStage.show();
		
	}
	
	/**
	 * Programmstart - keine Argumente nötig
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
