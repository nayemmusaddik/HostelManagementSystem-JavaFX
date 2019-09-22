package dashboard.registerEmployee;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connection.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Employee;
import model.Student;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterEmployee implements Initializable {
    private ConnectionManager manager;

    @FXML
    Label regStatus;
    @FXML
    JFXTextField fullName;
    @FXML
    JFXTextField phone;
    @FXML
    JFXPasswordField password;
    @FXML
    JFXPasswordField confirmPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
    }
    @FXML
    private  void register(){
        Employee employee= new Employee();
        employee.setFullName(fullName.getText());
        employee.setPhone(phone.getText());
        employee.setUserType("Employee");
        employee.setUserName(phone.getText());
        employee.setPassword(password.getText());
        try {
            if (validation()) {
                manager.registerEmployee(employee);
                regStatus.setText("Success");
                regStatus.setStyle("-fx-text-fill: green;");
                clearData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            regStatus.setText(e.toString());
            regStatus.setStyle("-fx-text-fill: red;");
        }
    }
    private boolean validation() throws SQLException {
        if (fullName.getText().equals("")){
            regStatus.setText("Full Name Empty");
            regStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if(phone.getText().equals("")){
            regStatus.setText("Phone Empty");
            regStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if(password.getText().equals("")){
            regStatus.setText("Password Empty");
            regStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if(!Objects.equals(confirmPassword.getText(), password.getText())){
            regStatus.setText("Password Didn't Match");
            regStatus.setStyle("-fx-text-fill: red;");
            return false;
        } else if (manager.getEmployeeByUsername(phone.getText()).getPhone() != null){
            regStatus.setText("Student Already Exists!");
            regStatus.setStyle("-fx-text-fill: red;");
            return false;
        }
        else {
            return true;
        }

    }
    private void clearData(){
        fullName.clear();
        phone.clear();
        password.clear();
        confirmPassword.clear();
    }

}
