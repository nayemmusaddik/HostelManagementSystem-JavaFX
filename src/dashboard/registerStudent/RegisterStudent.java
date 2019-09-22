package dashboard.registerStudent;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import connection.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Student;

import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterStudent implements Initializable {
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
    @FXML
    JFXTextField address;
    @FXML
    JFXTextField regNo;
    @FXML
    private JFXComboBox<String> department;
    private ObservableList<String> userTypeList = FXCollections.observableArrayList("CSE", "EEE");
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
        department.setPromptText("Select Department");
        department.setItems(userTypeList);
    }
    @FXML
    private  void register(){
        Student student= new Student();
        student.setFullName(fullName.getText());
        student.setPhone(phone.getText());
        student.setRegNo(regNo.getText());
        student.setDepartment(department.getSelectionModel().getSelectedItem());
        student.setAddress(address.getText());
        student.setUserType("Student");
        student.setStatus("Due");
        student.setUserName(regNo.getText());
        student.setPassword(password.getText());
        try {
            if (validation()) {
                manager.registerStudent(student);
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
        }else if(address.getText().equals("")){
            regStatus.setText("Address Empty");
            regStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if(regNo.getText().equals("")){
            regStatus.setText("Reg. No Empty");
            regStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if(department.getSelectionModel().isEmpty()){
            regStatus.setText("Department Empty");
            regStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if (manager.getStudentByReg(regNo.getText()).getRegNo() != null){
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
        address.clear();
        regNo.clear();
    }



}
