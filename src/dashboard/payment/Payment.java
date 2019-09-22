package dashboard.payment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import connection.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Payment implements Initializable {

    private ConnectionManager manager;
    @FXML
    Label paymentStatus;
    @FXML
    JFXTextField date;
    @FXML
    JFXTextField regNoPayment;
    @FXML
    JFXTextField amount;
    @FXML
    JFXDatePicker datePicker;
    @FXML
    private JFXComboBox<String> feeType;
    private ObservableList<String> feeTypeList = FXCollections.observableArrayList("Registration Fee", "Monthly Fee");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
        feeType.setPromptText("Select Fee type");
        feeType.setItems(feeTypeList);

    }
    @FXML
    private  void  makePayment() throws SQLException {
        if(validationPayment()){
            try {
                model.Payment payment = new model.Payment();
                payment.setRegNo(regNoPayment.getText());
                payment.setAmount(Double.parseDouble(amount.getText()));
                payment.setDate(date.getText());
                payment.setFeeType(feeType.getSelectionModel().getSelectedItem());
                manager.makePayment(payment);
                paymentStatus.setText("Success");
                paymentStatus.setStyle("-fx-text-fill: green;");
                clearData();
            } catch (SQLException e) {
                paymentStatus.setText(e.toString());
                paymentStatus.setStyle("-fx-text-fill: red;");
            }
        }
    }

    @FXML
    private  void getDate(){
        LocalDate localDate = datePicker.getValue();
        System.out.println(localDate);
        date.setText(localDate.toString());
    }
    private boolean validationPayment() throws SQLException {
        if (regNoPayment.getText().equals("")){
            paymentStatus.setText("Reg. No Empty");
            paymentStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if(amount.getText().isEmpty()){
            paymentStatus.setText("Amount Empty");
            paymentStatus.setStyle("-fx-text-fill: red;");
            return false;
        }else if (date.getText().isEmpty()){
            paymentStatus.setText("Date Empty");
            paymentStatus.setStyle("-fx-text-fill: red;");
            return false;
        } else if (manager.getStudentByReg(regNoPayment.getText()).getRegNo() == null){
            paymentStatus.setText("No Student Found!");
            paymentStatus.setStyle("-fx-text-fill: red;");
            return false;
        } else if (feeType.getSelectionModel().isEmpty()){
            paymentStatus.setText("Please Select a Fee type");
            paymentStatus.setStyle("-fx-text-fill: red;");
            return false;
        } else {
            return true;
        }

    }
    private void clearData(){
        regNoPayment.clear();
        amount.clear();
        date.clear();
    }
}
