import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Repository {
    private static final String url = "./data.txt";
    private final Map<String, MessageInfo> dataMessage = new HashMap<>();
    private int totalMessageSent = 0;

    public Repository() throws Exception {
        try {
            // load data from file
            FileReader fileReader = new FileReader(getClass().getResource(url).getPath());
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] input = line.split(",", -1);
                MessageInfo info = new MessageInfo(input[0], Integer.parseInt(input[1]), LocalDate.parse(input[2]));
                // if it's not today's data then delete it
                if (info.getDateSend().isBefore(LocalDateTime.now().toLocalDate())) {
                    FileWriter fileWriter = new FileWriter(getClass().getResource(url).getPath());
                    BufferedWriter writer = new BufferedWriter(fileWriter);
                    writer.write("");
                    writer.close();
                    break;
                }
                this.dataMessage.put(input[0], info);
            }
            reader.close();
            totalMessageSent = getTotalMessageSent();
        } catch (Exception e) {
            throw new Exception(e);
        }

    }


    // get total SMS sent today
    public int getTotalMessageSent() {
        int total = 0;
        for (MessageInfo data : dataMessage.values()) {
            total += data.getCount();
        }

        return total;
    }

    // set total SMS sent today
    public void setTotalMessageSent(int number) {
        totalMessageSent = number;
    }

    // get total SMS sent today by a phone number
    public int getMessageSentBy(String phoneNumber) {
        int count = 0;
        for (Map.Entry<String, MessageInfo> data : dataMessage.entrySet()) {
            String key = data.getKey();
            MessageInfo info = data.getValue();
            if (phoneNumber.equals(key)) {
                count += info.getCount();
            }
        }

        return count;
    }

    // get object MessageInfo from dataMessage if it's existed, otherwise create new one
    private MessageInfo getInfoByPhoneNumber(String phoneNumber) {
        if (!dataMessage.containsKey(phoneNumber)) {
            return new MessageInfo(phoneNumber, 0, LocalDate.now());
        }

        return dataMessage.get(phoneNumber);
    }

    // set total SMS sent today by a phone number
    public void setDataMessageSentBy(String phoneNumber, int value) throws Exception {
        // update count of this phoneNumber
        MessageInfo dataInfo = getInfoByPhoneNumber(phoneNumber);
        dataInfo.setCount(value);
        dataMessage.put(phoneNumber, dataInfo);

        // update total message sent
        setTotalMessageSent(getTotalMessageSent());

        // write back data to file
        FileWriter fileWriter = new FileWriter(getClass().getResource(url).getPath());
        BufferedWriter writer = new BufferedWriter(fileWriter);
        for (MessageInfo data : dataMessage.values()) {
            writer.write(data.toString() + '\n');
        }
        writer.close();
    }
}
