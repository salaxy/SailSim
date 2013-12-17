package de.fhb.sailsim.userinterface.jaxafx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;

/**
 * 
 * @author Andy Klay <klay@fh-brandenburg.de>
 * 
 */
public class GUI extends Application {

	Group root;
	Scene scene;

	BoatSymbol boat;
	ControlPane controlPane;
	final MotionPane motionPane = new MotionPane();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new Group();
		scene = new Scene(root, 800, 600, Color.BLACK);
		primaryStage.setScene(scene);

		initialzeControlbar();
		initializeMotionSpace();
		root.getChildren().add(controlPane);
		root.getChildren().add(motionPane);

		// you can add Events only to scene or a focusalbe Node
		scene.setOnKeyPressed(boatEventHandler);

		boat = new BoatSymbol(motionPane, 100, 100);
		motionPane.getChildren().add(boat);
		primaryStage.show();
	}

	private void initializeMotionSpace() {
		motionPane.setLayoutX(200);
		motionPane.setLayoutY(50);
		motionPane.setPrefSize(600, 600);

		FlowPane motionFlow = new FlowPane();
		//dummy fills
		motionFlow.getChildren().add(
				TextBuilder.create().text("motionSpace").fill(Color.GREEN)
						.build());
		motionPane.getChildren().add(motionFlow);
	}

	private void initialzeControlbar() {
		controlPane = new ControlPane();
		controlPane.setLayoutX(0);
		controlPane.setLayoutY(10);
		controlPane.setPrefSize(200, 600);
		controlPane
				.setStyle("-fx-background-color: linear-gradient(#ff2200, #000000);");

		FlowPane flow = new FlowPane(1, 5);

		//dummy fills
		flow.getChildren().add(
				TextBuilder.create().text("Sailbootstate").fill(Color.BLUE)
						.build());
		flow.getChildren().add(
				TextBuilder.create().text("Windsettings")
						.fill(Color.BLANCHEDALMOND).build());
		flow.getChildren().add(
				TextBuilder.create().text("Further Windsettings")
						.fill(Color.CADETBLUE).build());

		controlPane.getChildren().add(flow);
	}

	/**
	 * Programmstart - keine Argumente nötig
	 */
	public static void main(String[] args) {
		launch(args);
	}

	private EventHandler<KeyEvent> boatEventHandler = new EventHandler<KeyEvent>() {

		@Override
		public void handle(KeyEvent ke) {

			switch (ke.getCode()) {
			case UP:
				boat.setPositionX(boat.getPositionX() + 1);
				break;
			case DOWN:
				boat.setPositionX(boat.getPositionX() - 1);
				break;
			case RIGHT:
				boat.setPositionY(boat.getPositionY() + 1);
				break;
			case LEFT:
				boat.setPositionY(boat.getPositionY() - 1);
				break;
			}

		}
	};

}
