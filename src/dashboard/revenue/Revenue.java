package dashboard.revenue;

import connection.ConnectionManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Payment;
import model.Student;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Revenue implements Initializable {
    private ConnectionManager manager;
    @FXML
    Label lblTotal;
    @FXML
    TableView<Payment> revenueTable;
    @FXML
    private TableColumn<Payment, String> col_regNo;
    @FXML
    private TableColumn<Payment, String> col_date;
    @FXML
    private TableColumn<Payment, String> col_amount;
    @FXML
    private TableColumn<Payment, String> col_feeType;
    private ObservableList<Payment> paymentData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = new ConnectionManager();
        col_regNo.setCellValueFactory(new PropertyValueFactory<Payment, String>("regNo"));
        col_date.setCellValueFactory(new PropertyValueFactory<Payment, String>("date"));
        col_amount.setCellValueFactory(new PropertyValueFactory<Payment, String>("amount"));
        col_feeType.setCellValueFactory(new PropertyValueFactory<Payment, String>("feeType"));

        try {
            paymentData = manager.getPayment();
            revenueTable.setItems(paymentData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double total=0;
        for (Payment paymentDatum : paymentData) {
            total += paymentDatum.getAmount();
        }
        lblTotal.setText(String.valueOf(total)+" BDT");
    }

    @FXML
     public void refresh() throws SQLException {
        paymentData.clear();
        paymentData=manager.getPayment();
        revenueTable.setItems(paymentData);
    }

}
