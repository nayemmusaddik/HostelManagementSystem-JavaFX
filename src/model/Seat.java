package model;

public class Seat extends Room {

    private int seatId;
    private String seatNo;
    private String regNo;

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String studentId) {
        this.regNo = studentId;
    }
}
