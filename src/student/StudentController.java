package student;

import connection.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Student;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StudentController implements Initializable {

    private ConnectionManager manager;
    String username;

    @FXML
    Label lblTopUser;
    @FXML
    Label name;
    @FXML
    Label phone;
    @FXML
    Label status;
    @FXML
    Label regNo;
    @FXML
    Label address;
    @FXML
    Label seat;
    @FXML
    Label room;
    @FXML
    Label department;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
    }
    @FXML
    public void logout(ActionEvent event) {

        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/login/login.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setData(String unsername) throws SQLException {
        this.username=unsername;
        Student student= manager.getStudentByReg(username);
        lblTopUser.setText("Welcome "+student.getFullName());
        name.setText(student.getFullName());
        phone.setText(student.getPhone());
        status.setText(student.getStatus());
        if (student.getStatus().equals("Due"))
            status.setStyle("-fx-text-fill: red;");
        else
            status.setStyle("-fx-text-fill: green;");

        regNo.setText(student.getRegNo());
        seat.setText(student.getSeatNo());
        room.setText(student.getRoomNo());
        department.setText(student.getDepartment());
        address.setText(student.getAddress());

    }
}
