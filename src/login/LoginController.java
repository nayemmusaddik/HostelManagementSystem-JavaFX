package login;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dashboard.DashboardController;
import connection.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import student.StudentController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXComboBox<String> userType;
    @FXML

    private Label loginMsg;
    private ObservableList<String> userTypeList = FXCollections.observableArrayList("Student", "Employee");
    private Parent root = null;
    private FXMLLoader loader = new FXMLLoader();
    private Stage primaryStage = new Stage();
    String user;

    public void login(ActionEvent event) {
        user= userName.getText();
        String pass = password.getText();
        String type= userType.getSelectionModel().getSelectedItem();
        ConnectionManager manager = new ConnectionManager();

        try {
            boolean isLoggedIn= manager.doLogin(user, pass, type);
            if (isLoggedIn) {
                ((Node)event.getSource()).getScene().getWindow().hide();
                loginMsg.setText("Successful!");
                if (type.equals("Employee")){
                    loadEmployeeDashboard();

                }else  if(type.equals("Student")){
                    loadStudentDashboard();
                }

            } else {
                loginMsg.setText("ERROR! Invalid Username or Password or Type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userType.setPromptText("Select User Type");
        userType.setItems(userTypeList);
        userName.setText("admin");
        password.setText("admin");
    }

    private void loadEmployeeDashboard() throws SQLException {
        try {
            root = loader.load(getClass().getResource("/dashboard/dashboard.fxml").openStream());
            DashboardController dashboardController =
                    (DashboardController) loader.getController();
            dashboardController.setUI(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("Employee Dashboard");
        primaryStage.setScene(new Scene(root,700, 500));
        primaryStage.show();

    }
    private void loadStudentDashboard() throws SQLException {
        try {
            root = loader.load(getClass().getResource("/student/student.fxml").openStream());
            StudentController studentController =
                    (StudentController) loader.getController();
            studentController.setData(user);
        } catch (IOException e) {

            e.printStackTrace();
        }
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Student Dashboard");
        primaryStage.setScene(new Scene(root,700, 500));
        primaryStage.show();

    }
}
