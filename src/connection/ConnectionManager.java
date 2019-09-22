package connection;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ConnectionManager {
    private Connection connection;

    public ConnectionManager() {
        connection = DBHandler.connect();
    }

    public  boolean doLogin(String userName, String password, String userType) throws SQLException {
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        String query="select * from user where userName= ? and password= ? and userType= ? ";
        try {
            preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,userType);
            resultSet=preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
            resultSet.close();
        }
        return false;
    }

    public void registerStudent(Student student) throws SQLException {
        registerUser(student);
        PreparedStatement preparedStatement=null;
        String query="insert into student (fullName,phone,regNo,department,status,address) values (?,?,?,?,?,?) ";
        try {
            preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,student.getFullName());
            preparedStatement.setString(2,student.getPhone());
            preparedStatement.setString(3,student.getRegNo());
            preparedStatement.setString(4,student.getDepartment());
            preparedStatement.setString(5,"Due");
            preparedStatement.setString(6,student.getAddress());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
        }
    }

    public void registerEmployee(Employee employee) throws SQLException {
        registerUser(employee);
        PreparedStatement preparedStatement=null;
        String query="insert into employee (fullName,phone) values (?,?) ";
        try {
            preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,employee.getFullName());
            preparedStatement.setString(2,employee.getPhone());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
        }
    }

    public User registerUser(User user) throws SQLException {
        PreparedStatement preparedStatement=null;
        String insertQuery="insert into user (userType, userName, password) values (?,?,?)";
        try {
            preparedStatement= connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,user.getUserType());
            preparedStatement.setString(2,user.getUserName());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
        }
        return user;
    }

    public void allocateSeat(Seat seat) throws SQLException {
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;
        String query="update seat set regNo=? where roomNo=? and seatNo = ?";
        try {
            preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,seat.getRegNo());
            preparedStatement.setString(2,seat.getRoomNo());
            preparedStatement.setString(3,seat.getSeatNo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
        }
    }

    public void makePayment(Payment payment) throws SQLException {
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;
        String query="insert into payment (regNo, amount, date, feeType) values (?,?,?,?)";
        try {
            preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,payment.getRegNo());
            preparedStatement.setDouble(2,payment.getAmount());
            preparedStatement.setString(3,payment.getDate());
            preparedStatement.setString(4,payment.getFeeType());
            preparedStatement.executeUpdate();
            updatePaymentStatus(payment.getRegNo(),payment.getDate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
        }
    }

    public Employee getEmployeeByUsername(String userName) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from user  LEFT OUTER JOIN employee ON user.userName = employee.phone where user.userName= ? ";
        Employee employee= new Employee();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,userName);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                employee.setFullName(resultSet.getString(6));
                employee.setPhone(resultSet.getString(7));
                employee.setUserName(resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return employee;
    }

    public Student getStudentByReg(String regNo) throws SQLException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from student where regNo= ? ";
        Student student=new Student();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,regNo);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                student.setsId(resultSet.getInt(1));
                student.setuId(resultSet.getInt(2));
                student.setFullName(resultSet.getString(3));
                student.setPhone(resultSet.getString(4));
                student.setRegNo(resultSet.getString(5));
                student.setDepartment(resultSet.getString(6));
                student.setStatus(resultSet.getString(7));
                student.setAddress(resultSet.getString(8));
                student.setRoomNo(getSeatByReg(resultSet.getString(5)).getRoomNo());
                student.setSeatNo(getSeatByReg(resultSet.getString(5)).getSeatNo());
                student.setFloorNo(getSeatByReg(resultSet.getString(5)).getFloorNo());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return student;
    }

    public Student getSeatByReg(String regNo) throws SQLException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "select * from seat  JOIN room ON seat.roomNo = room.roomNo where regNo=? ";
        Student student=new Student();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,regNo);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                student.setRoomNo(resultSet.getString(2));
                student.setSeatNo(resultSet.getString(4));
                student.setFloorNo(resultSet.getString(7));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return student;
    }

    public ObservableList<Student> getStudent() throws SQLException{
        ObservableList<Student> students = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "select * from student ";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student=new Student();
                student.setuId(resultSet.getInt(1));
                student.setFullName(resultSet.getString(2));
                student.setPhone(resultSet.getString(3));
                student.setRegNo(resultSet.getString(4));
                student.setDepartment(resultSet.getString(5));
                student.setStatus(resultSet.getString(6));
                student.setAddress(resultSet.getString(7));
                student.setRoomNo(getSeatByReg(resultSet.getString(4)).getRoomNo());
                student.setSeatNo(getSeatByReg(resultSet.getString(4)).getSeatNo());
                student.setFloorNo(getSeatByReg(resultSet.getString(4)).getFloorNo());
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return students;
    }

    public ObservableList<Employee> getEmployee() throws SQLException{
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "select * from employee ";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Employee employee=new Employee();
                employee.setEmployeeId(resultSet.getInt(1));
                employee.setFullName(resultSet.getString(2));
                employee.setPhone(resultSet.getString(3));
                employees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return employees;
    }

    public ObservableList<Payment> getPayment() throws SQLException{
        ObservableList<Payment> paymentData = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "select * from payment ";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Payment payment=new Payment();
                payment.setpId(resultSet.getInt(1));
                payment.setRegNo(resultSet.getString(2));
                payment.setDate(resultSet.getString(3));
                payment.setAmount(resultSet.getDouble(4));
                payment.setFeeType(resultSet.getString(5));
                paymentData.add(payment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return paymentData;
    }

    public ObservableList<String> getFloorNo() throws SQLException{
        ObservableList<String> floors = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "select distinct floorNo from room";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                floors.add(String.valueOf(resultSet.getInt(1)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return floors;
    }

    public ObservableList<String> getRoomNo(String floorNo) throws SQLException{
        ObservableList<String> rooms = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "select distinct roomNo from room where floorNo= ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,floorNo);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rooms.add(String.valueOf(resultSet.getInt(1)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return rooms;
    }

    public ObservableList<String> getSeatNo(String roomNo) throws SQLException{
        ObservableList<String> seats = FXCollections.observableArrayList();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String query = "select distinct seatNo from seat where roomNo= ? and regNo=?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,roomNo);
            preparedStatement.setString(2,"0");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                seats.add(String.valueOf(resultSet.getInt(1)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return seats;
    }

    public String getAvailableSeatCount() throws SQLException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String count="";

        String query = "SELECT COUNT(*) FROM seat where regNo = 0";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count=String.valueOf(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return count;
    }

    public String getTotalStudentCount() throws SQLException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String count="";

        String query = "SELECT COUNT(*) FROM student";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count=String.valueOf(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
            resultSet.close();
        }
        return count;
    }

    public void deleteStudent(String regNo) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "delete from student where regNo = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, regNo);
            preparedStatement.executeUpdate();
            unAllocateSeat(regNo);
            deleteUser(regNo);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
        }
    }

    public void deleteEmployee(String phone) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "delete from employee where phone = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, phone);
            preparedStatement.executeUpdate();
            deleteUser(phone);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
        }
    }

    public void deleteUser(String userName) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "delete from user where userName = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
        }
    }

    public void unAllocateSeat(String regNo) throws SQLException {
        PreparedStatement preparedStatement = null;
        String query = "update seat set regNo=? where regNo=?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "0");
            preparedStatement.setString(2, regNo);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();

        }
    }

    public void updatePaymentStatus(String regNo, String paymentDate) throws SQLException{
        PreparedStatement preparedStatement = null;
        String query = "update student set status=? where regNo=?";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate=LocalDate.parse(paymentDate,formatter);
        Date date = new Date();
        LocalDate currentLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = currentLocalDate.getMonthValue();
        try {
            preparedStatement = connection.prepareStatement(query);
            if(localDate.getMonthValue()==month){
                preparedStatement.setString(1,"Paid");
            }else
            {
                preparedStatement.setString(1,"Due");
            }
            preparedStatement.setString(2,regNo);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
        }
    }

    public void updateStudent(Student student) throws SQLException{
        PreparedStatement preparedStatement = null;
        String query = "update student set fullName=?, phone=? where sId = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, student.getFullName());
            preparedStatement.setString(2, student.getPhone());
            preparedStatement.setInt(3, student.getsId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
        }
    }

}
