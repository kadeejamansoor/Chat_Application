package Resourse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ClientFormController extends Thread {
    //    public static String ClientName;
    int[] emojiIcons = {0x1F606, 0x1F601, 0x1F602, 0x1F609, 0x1F618, 0x1F610, 0x1F914, 0x1F642, 0x1F635, 0x1F696, 0x1F636, 0x1F980, 0x1F625, 0x1F634, 0x1F607, 0x2764};
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;
    @FXML
    private ScrollPane content;
    @FXML
    private AnchorPane ClientPane;
    @FXML
    private VBox msgContent;
    @FXML
    private JFXTextField txtmessage;
    @FXML
    private Label lblClientName;
    @FXML
    private ScrollPane spEmojiIcons;
    @FXML
    private GridPane gpEmojiIcons;
    private FileChooser fileChooser;
    private File filePath;

    public void initialize() {
        String clientName = LoginFormController.ClientName;
        this.lblClientName.setText(clientName);
        setEmojisToPane();

        spEmojiIcons.setVisible(false);
        spEmojiIcons.setStyle("-fx-background-color: purple");
        try {
            socket = new Socket("localhost", 2500);
            System.out.println("Socket is connected with server!");
            System.out.println(LoginFormController.ClientName + " Enter the Chat");
            System.out.println("____________________");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        client.listenForMessage(msgContent);
//        client.clientSendMessage("");
    }

    @Override
    public void run() {
        try {
            while (true) {

                String messages = reader.readLine();
                String[] tokens = messages.split(" ");
                String cmd = tokens[0];


                StringBuilder fullMsg = new StringBuilder();
                for (int i = 1; i < tokens.length; i++) {
                    fullMsg.append(tokens[i] + " ");
                }


                String[] messageToArr = messages.split(" ");
                String st = "";
                for (int i = 0; i < messageToArr.length - 1; i++) {
                    st += messageToArr[i + 1] + " ";
                }


                Text text = new Text(st);
                String firstChars = "";
                if (st.length() > 3) {
                    firstChars = st.substring(0, 3);

                }


                if (firstChars.equalsIgnoreCase("img")) {
                    //for the Images.
                    st = st.substring(3, st.length() - 1);
                    File file = new File(st);
                    Image image = new Image(file.toURI().toString());

                    ImageView imageView = new ImageView(image);

                    imageView.setFitHeight(120);
                    imageView.setFitWidth(150);


                    HBox hBox = new HBox(10);
                    hBox.setAlignment(Pos.BOTTOM_RIGHT);


                    if (!cmd.equalsIgnoreCase(lblClientName.getText())) {

                        msgContent.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);


                        Text text1 = new Text("  " + cmd + " :");
                        hBox.getChildren().add(text1);
                        hBox.getChildren().add(imageView);

                    } else {
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(imageView);
                        Text text1 = new Text(": Me ");
                        hBox.getChildren().add(text1);


                    }

                    Platform.runLater(() -> msgContent.getChildren().addAll(hBox));


                } else {

                    TextFlow tempFlow = new TextFlow();
                    if (!cmd.equalsIgnoreCase(lblClientName.getText() + ":")) {
                        Text txtName = new Text(cmd + " ");
                        txtName.getStyleClass().add("txtName");
                        tempFlow.getChildren().add(txtName);

                        tempFlow.setStyle("-fx-color:black;" +
                                "-fx-background-color:#D361DD;" +
                                " -fx-background-radius: 10px");
                        tempFlow.setPadding(new Insets(3, 10, 3, 10));

                    }

                    tempFlow.getChildren().add(text);
                    tempFlow.setMaxWidth(200);

                    TextFlow flow = new TextFlow(tempFlow);

                    HBox hBox = new HBox(12);


                    if (!cmd.equalsIgnoreCase(lblClientName.getText() + ":")) {


                        msgContent.setAlignment(Pos.TOP_LEFT);
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        hBox.getChildren().add(flow);
                        hBox.setPadding(new Insets(10, 10, 10, 10));

                    } else {

                        Text text2 = new Text(fullMsg + ": Me");
                        TextFlow flow2 = new TextFlow(text2);
                        hBox.setAlignment(Pos.BOTTOM_RIGHT);
                        hBox.getChildren().add(flow2);
                        hBox.setPadding(new Insets(2, 5, 2, 10));

                        flow2.setStyle("-fx-color: rgb(239,242,255);" +
                                "-fx-background-color:#840190;" +
                                "-fx-background-radius: 10px");
                        flow2.setPadding(new Insets(10, 10, 10, 10));

                    }

                    Platform.runLater(() -> msgContent.getChildren().addAll(hBox));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEmojisToPane() {
        int EMOJI_INDEX = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                Label text = new Label(new String(Character.toChars(emojiIcons[EMOJI_INDEX++])));
                text.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        txtmessage.appendText(text.getText());
                    }
                });
                text.setStyle("-fx-font-size: 25px;" +
                        " -fx-font-family: 'Noto Emoji';" +
                        "-fx-text-alignment: center;");
                gpEmojiIcons.add(text, i, j);
            }
        }
    }

    @FXML
    void btnSendOnAction(MouseEvent event) {
        sendMessage();

    }

    public void sendMessage() {
        spEmojiIcons.setVisible(false);
        String msg = txtmessage.getText();
//        System.out.println(LoginFormController.ClientName+":"+msg);
        writer.println(lblClientName.getText() + ": " + msg);
//        System.out.println(LoginFormController.ClientName+msg);
        txtmessage.clear();

        if (msg.equalsIgnoreCase("!Bye") || (msg.equalsIgnoreCase("logout"))) {
            System.exit(0);

        }
//        client.clientSendMessage(message);
    }

    @FXML
    void messageOnAction(ActionEvent event) {

        sendMessage();
    }

    @FXML
    void btnimageOnAction(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        this.filePath = fileChooser.showOpenDialog(stage);
        writer.println(lblClientName.getText() + " " + "img" + filePath.getPath());

    }

    @FXML
    void emojiOnAction(MouseEvent event) {
        if (spEmojiIcons.isVisible()) {
            spEmojiIcons.setVisible(false);
        } else {
            spEmojiIcons.setVisible(true);
            Text text = new Text(new String(Character.toChars(emojiIcons[4])));
            text.setStyle("-fx-font-size: 25px; -fx-font-family: 'Noto Emoji';");
            gpEmojiIcons.add(text, 0, 0);
        }
    }


}
