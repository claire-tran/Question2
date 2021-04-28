import java.time.LocalDate;

public class MessageInfo {
    private final String phoneNumber;
    private final LocalDate dateSend;
    private int count;

    public MessageInfo(String phoneNumber, int count, LocalDate dataSend) {
        this.count = count;
        this.dateSend = dataSend;
        this.phoneNumber = phoneNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LocalDate getDateSend() {
        return dateSend;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber + "," + count + "," + dateSend;
    }
}
