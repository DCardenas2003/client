import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Gameplay {

    private Client client;
    private Stage primaryStage;

    public Gameplay(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void start() {


        primaryStage.setTitle("Gameplay");
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);

        Label messageLabel = new Label("Welcome to 3 Card Poker!");

        messageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        layout.getChildren().add(messageLabel);

        Button sendMessageButton = new Button("Send message");
        sendMessageButton.setOnAction(t -> {
            String message = "Hello, server!";
            client.sendMessage(message);
        });
        layout.getChildren().add(sendMessageButton);

        Button disconnectButton = new Button("Disconnect");
        disconnectButton.setOnAction(t -> {
            client.disconnect();
            Platform.exit();
        });
        layout.getChildren().add(disconnectButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setClient(Client client) {
        this.client = client;
    }

}