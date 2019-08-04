package adapters;

import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BillRecognizeAdapter  extends AppCompatActivity {
    private static final String TAG = BillRecognizeAdapter.class.getSimpleName();

    private static String amountScanned;
    private static String dateScanned;

    private static Pattern p, p_0,p_1, p_2, p_3,p_4, p_5,p_6,p_7,p_8,p_9,p_10,p_11,p2_2, p2_3, p3, dateP1, dateP2;

    private static Matcher m,m_0,m_1, m_2,m_3,m_4,m_5,m_6,m_7,m_8,m_9,m_10,m_11,m2_2,m2_3,m3,dateM1,dateM2;

    public static void recognizePattern(String resultScan) {
        /**             ?------------------------------EXPLANATION------------------------------?
         * \s* matches any whitespace character (equal to [\r\n\t\f\v ])
         * * Quantifier — Matches between zero and unlimited times, as many times as possible, giving back as needed (greedy)
         * \:* matches the character : literally (case insensitive)
         * * Quantifier — Matches between zero and unlimited times, as many times as possible, giving back as needed (greedy)
         * \s* matches any whitespace character (equal to [\r\n\t\f\v ])
         * * Quantifier — Matches between zero and unlimited times, as many times as possible, giving back as needed (greedy)
         * \-* matches the character - literally (case insensitive)
         * * Quantifier — Matches between zero and unlimited times, as many times as possible, giving back as needed (greedy)
         * \s* matches any whitespace character (equal to [\r\n\t\f\v ])
         * * Quantifier — Matches between zero and unlimited times, as many times as possible, giving back as needed (greedy)
         * \>* matches the character > literally (case insensitive)
         * * Quantifier — Matches between zero and unlimited times, as many times as possible, giving back as needed (greedy)
         * \s* matches any whitespace character (equal to [\r\n\t\f\v ])
         * * Quantifier — Matches between zero and unlimited times, as many times as possible, giving back as needed (greedy)
         * \$? matches the character $ literally (case insensitive)
         * ? Quantifier — Matches between zero and one times, as many times as possible, giving back as needed (greedy)
         * \£? matches the character £ literally (case insensitive)
         * \€? matches the character € literally (case insensitive)
         * \d+ matches a digit (equal to [0-9])
         * Non-capturing group (?:[-.,‘_]\d+)?
         * ? Quantifier — Matches between zero and one times, as many times as possible, giving back as needed (greedy)
         * Match a single character present in the list below [-.,‘_]
         * \d+ matches a digit (equal to [0-9])
         * */

        Log.e(TAG, "Tesseract result:" + resultScan);

        /**-----------"Total" regular patterns----------*/
        p = Pattern.compile("Total\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_0 = Pattern.compile("Grand Total\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\d+(?:[-.,‘_]\\d+)?");
        p_1 = Pattern.compile("To‘al\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_2 = Pattern.compile("Total\\s*RM\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\d+(?:[-.,‘_]\\d+)?");
        p_3 = Pattern.compile("SUBTOTAL\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\d+(?:[-.,‘_]\\d+)?");
        p_4 = Pattern.compile("To‘al\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_5 = Pattern.compile("Tota!\\s*\\:*\\s*\\,*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_6 = Pattern.compile("Amount\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_7 = Pattern.compile("Balance\\s*Due\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_8 = Pattern.compile("SUB\\s*TOTAL\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_9 = Pattern.compile("Amount\\s*Due\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_10 = Pattern.compile("Paid\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p_11 = Pattern.compile("Tmal\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\s*\\d+(?:[-.,‘_]\\d+)?");
        p2_2 = Pattern.compile("SUBTOTAL\\\"*\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\d+(?:[-.,‘_]\\d+)?");
        p2_3 = Pattern.compile("SUBTOTAL\\s*\\\"*\\s*\\:*\\s*\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\d+(?:\\s*[-.,‘_]\\d+)?");
        p3 = Pattern.compile("Total\\s*\\:*\\s*CHF\\-*\\s*\\>*\\s*\\$?\\£?\\€?\\d+(?:[-.,‘_]\\d+)?");
        /**-----------"Dates" regular patterns----------*/
        dateP1 = Pattern.compile("(\\d+\\/\\d+\\/\\d+)");
        dateP2 = Pattern.compile("(\\d+\\.\\d+\\.\\d+)");
        /**-----------"Total" regular patterns----------*/
        m = p.matcher(resultScan);
        m_0 = p_0.matcher(resultScan);
        m_1 = p_1.matcher(resultScan);
        m_2 = p_2.matcher(resultScan);
        m_3 = p_3.matcher(resultScan);
        m_4 = p_4.matcher(resultScan);
        m_5 = p_5.matcher(resultScan);
        m_6 = p_6.matcher(resultScan);
        m_7 = p_7.matcher(resultScan);
        m_8 = p_8.matcher(resultScan);
        m_9 = p_9.matcher(resultScan);
        m_10 = p_10.matcher(resultScan);
        m_11 = p_11.matcher(resultScan);
        m2_2 = p2_2.matcher(resultScan);
        m2_3 = p2_3.matcher(resultScan);
        m3 = p3.matcher(resultScan);
        /**-----------"Dates" regular patterns----------*/
        dateM1 = dateP1.matcher(resultScan);
        dateM2 = dateP2.matcher(resultScan);

        while (m.find()) {
            System.out.println("recognizePattern result Amount:" + m.group());
            amountScanned = m.group();
            Log.e(TAG, "recognizePattern result Amount:" + m.group());
        }
        while (m_0.find()) {
            System.out.println("recognizePattern result Amount:" + m_0.group());
            amountScanned = m_1.group();
            Log.e(TAG, "recognizePattern result Amount:" + m_0.group());
        }
        while (m_1.find()) {
            System.out.println("recognizePattern result Amount:" + m_1.group());
            amountScanned = m_1.group();
            Log.e(TAG, "recognizePattern result Amount:" + m_1.group());
        }
        while (m_2.find()) {
            System.out.println("recognizePattern result Amount:" + m_2.group());
            amountScanned = m_2.group();
            Log.e(TAG, "recognizePattern result Amount:" + m_2.group());
        }
        while (m_3.find()) {
            System.out.println("recognizePattern result Amount:" + m_3.group());
            amountScanned = m_3.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_3.group());
        }
        while (m_4.find()) {
            System.out.println("recognizePattern result Amount:" + m_4.group());
            amountScanned = m_4.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_4.group());
        }
        while (m_5.find()) {
            System.out.println("recognizePattern result Amount:" + m_5.group());
            amountScanned = m_5.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_5.group());
        }
        while (m_6.find()) {
            System.out.println("recognizePattern result Amount:" + m_6.group());
            amountScanned = m_5.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_6.group());
        }
        while (m_7.find()) {
            System.out.println("recognizePattern result Amount:" + m_7.group());
            amountScanned = m_7.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_7.group());
        }
        while (m_8.find()) {
            System.out.println("recognizePattern result Amount:" + m_8.group());
            amountScanned = m_8.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_8.group());
        }
        while (m_9.find()) {
            System.out.println("recognizePattern result Amount:" + m_9.group());
            amountScanned = m_9.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_9.group());
        }
        while (m_10.find()) {
            System.out.println("recognizePattern result Amount:" + m_10.group());
            amountScanned = m_10.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_10.group());
        }
        while (m_11.find()) {
            System.out.println("recognizePattern result Amount:" + m_11.group());
            amountScanned = m_11.group();
            Log.e(TAG, "recognizePattern result Amount :" + m_11.group());
        }
        while (m2_2.find()) {
            System.out.println("recognizePattern result Amount:" + m2_2.group());
            amountScanned = m2_2.group();
            Log.e(TAG, "recognizePattern result Amount:" + m2_2.group());
        }
        while (m2_3.find()) {
            System.out.println("recognizePattern result Amount:" + m2_3.group());
            amountScanned = m2_3.group();
            Log.e(TAG, "recognizePattern result Amount:" + m2_3.group());
        }
        while (m3.find()) {
            System.out.println("recognizePattern result Amount:" + m3.group());
            amountScanned = m3.group();
            Log.e(TAG, "recognizePattern result Amount:" + m3.group());
        }
        /**-----------"Dates" regular patterns----------*/
        while (dateM1.find()) {
            System.out.println("recognizePattern result:" + dateM1.group());
            dateScanned = dateM1.group();
            Log.e(TAG, "recognizePattern result:" + dateM1.group());
        }
        while (dateM2.find()) {
            System.out.println("recognizePattern result:" + dateM2.group());
            dateScanned = dateM2.group();
            Log.e(TAG, "recognizePattern result:" + dateM2.group());
        }
        BillRecognizeAdapter billRecognizeAdapter = new BillRecognizeAdapter();
        billRecognizeAdapter.setDateScanned(dateScanned);
        billRecognizeAdapter.setAmountScanned(amountScanned);
    }

    public String getDateScanned() {
        return dateScanned;
    }

    public void setDateScanned(String dateScanned) {
        this.dateScanned = dateScanned;
    }

    public String getAmountScanned() {
        return amountScanned;
    }

    public void setAmountScanned(String amountScanned) {
        this.amountScanned = amountScanned;
    }

}
