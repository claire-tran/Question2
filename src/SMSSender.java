import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_DASHES;

public class SMSSender {
    private final String apiKey;
    private final String apiSecret;
    private Repository repo;
    private int maxRetry = 3;
    private int maxDailyOTP;
    private int maxDailyOTPForANumber;

    public SMSSender(String apiKey, String apiSecret, Repository repo, int maxRetry, int maxDailyOTP, int maxDailyOTPForANumber) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.repo = repo;
        this.maxRetry = maxRetry;
        this.maxDailyOTP = maxDailyOTP;
        this.maxDailyOTPForANumber = maxDailyOTPForANumber;
    }

    // create random 6-digits OTP
    public static String createRandomOTP() {
        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }

    // send OTP message to a phone number
    public void sendOTP(String from, String to) throws Exception {
        if (repo.getTotalMessageSent() >= maxDailyOTP) {
            throw new Error("Daily OTP limit exceeded(over 500 OTP)");
        }
        if (repo.getMessageSentBy(to) >= maxDailyOTPForANumber) {
            throw new Error("Daily OTP limit exceeded for this phone number (over 5 OTP)");
        }

        int attempt = 0;

        // retry if fail
        while (attempt < maxRetry) {
            try {
                sendSMS(from, to);
                repo.setDataMessageSentBy(to, repo.getMessageSentBy(to) + 1);
                break;
            } catch (Exception e) {
                attempt++;
                if (attempt == maxRetry) {
                    throw new Exception(e);
                }
            }
        }
    }

    private void sendSMS(String from, String to) throws Exception {
        // Construct data
        String url = "https://rest.nexmo.com/sms/json";
        String sender = "from=" + from;
        String message = "&text=+" + "This is your OTP: " + createRandomOTP();
        String receiver = "&to=+" + to;
        String key = "&api_key=" + apiKey;
        String secret = "&api_secret=" + apiSecret;

        // Send data
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        String data = sender + message + receiver + key + secret;
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
        conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));

        final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder stringBuffer = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        rd.close();

        String response = stringBuffer.toString();
        System.out.println("===============DEBUG================");
        System.out.println("Status code: " + conn.getResponseCode());
        System.out.println("Response body: " + response);
        System.out.println("====================================");

        // check if there is any error returned in response body
        if (conn.getResponseCode() == 200) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(LOWER_CASE_WITH_DASHES);

            SMSResponse smsResponse = gsonBuilder.create().fromJson(response, SMSResponse.class);

            for (SMSResponse.Message sms : smsResponse.getMessages()) {
                // any status other than "0" is error
                if (!sms.getStatus().equals("0")) {
                    throw new Exception(sms.getErrorText());
                }
            }
        } else {
            // error code != 200
            throw new Exception(response);
        }
    }
}
