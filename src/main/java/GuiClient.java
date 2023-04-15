
import java.util.HashMap;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiClient extends Application{


	TextField s1,s2,s3,s4, c1;
	Button clientChoice,b1;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
//	Server serverConnection;
	Client clientConnection;

	ListView<String> listItems, listItems2;


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("The Networked Client/Server GUI Example");

		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");

		this.buttonBox = new HBox(400, clientChoice);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);

		startScene = new Scene(startPane, 800, 800);

		listItems = new ListView<String>();
		listItems2 = new ListView<String>();

		c1 = new TextField();
		b1 = new Button("Send");

		sceneMap = new HashMap<String, Scene>();

		sceneMap.put("client",  createClientGui());

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});



		clientChoice.setOnAction(e-> {
			Stage clientStage = new Stage();
			VBox root = new VBox();
			root.setAlignment(Pos.CENTER); // Center aligns all child elements
			root.setSpacing(10);
			root.setPadding(new Insets(10));

			// Host input
			TextField hostField = new TextField();
			Label hostLabel = new Label("Host name or IP address:");
			root.getChildren().addAll(hostLabel, hostField);

			// Port input
			TextField portField = new TextField();
			Label portLabel = new Label("Port number:");
			root.getChildren().addAll(portLabel, portField);

			// Connect button
			Button connectButton = new Button("Connect");
			connectButton.setOnAction(event -> {
				String host = hostField.getText();
				String portString = portField.getText();

				// Validate inputs
				if (!host.equals("localhost") && !portString.equals("5555")) {
					Alert alert = new Alert(Alert.AlertType.ERROR, "The host or port number you entered is incorrect. Please try again.");
					alert.showAndWait();
					return;
				}

				int port = 0;
				try {
					port = Integer.parseInt(portString);
				} catch (NumberFormatException ex) {
					Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid port number.");
					alert.showAndWait();
					return;
				}

				// Create the client object and connect to server
				if (host.equals("localhost") && port == 5555) {

					// Start gameplay
					primaryStage.setScene(sceneMap.get("client"));
					primaryStage.setTitle("This is a client");

					clientConnection = new Client(data -> {
						Platform.runLater(() -> {
							listItems2.getItems().add(data.toString());
						});
					}, "localhost", 5555);

					clientConnection.start();
					primaryStage.setScene(sceneMap.get("client"));
					primaryStage.setTitle("This is a client");
					Gameplay gameplay = new Gameplay(primaryStage);
					gameplay.setClient(clientConnection); // Pass the client object to the gameplay object
					gameplay.start();

					// Close the VBox and the stage after the connection is established
					clientStage.close();

				} else {
					Alert alert = new Alert(Alert.AlertType.ERROR, "The host or port number you entered is incorrect. Please try again.");
					alert.showAndWait();
					return;
				}
			});
			root.getChildren().add(connectButton);

			clientStage.setScene(new Scene(root));
			clientStage.setTitle("Three Card Poker");

			// Add a big title in the top middle
			Label titleLabel = new Label("Welcome to Three Card Poker");
			titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
			root.getChildren().add(0, titleLabel);
			VBox.setMargin(titleLabel, new Insets(0, 0, 20, 0)); // Adds some margin below the title

			clientStage.show();
		});




		primaryStage.setScene(startScene);
		primaryStage.show();
	}



	public Scene createClientGui() {

		clientBox = new VBox(10, c1,b1,listItems2);
		clientBox.setStyle("-fx-background-color: blue");
		return new Scene(clientBox, 400, 300);

	}

}
