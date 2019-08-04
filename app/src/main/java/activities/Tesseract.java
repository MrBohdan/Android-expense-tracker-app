package activities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.cv4j.core.binary.Threshold;
import com.cv4j.core.datamodel.ByteProcessor;
import com.cv4j.core.datamodel.CV4JImage;
import adapters.BillRecognizeAdapter;
import com.googlecode.tesseract.android.TessBaseAPI;


public class Tesseract extends BaseActivity{
    private static final String lang = "eng";
    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
    private static final String TAG = Tesseract.class.getSimpleName();
    private TessBaseAPI tessBaseApi;

    public void onActivityResult (Uri resultUri, int width, int height) {
        if (height > 1280 && width > 960) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.

            System.out.println("Need to resize");
        }
        Log.i(TAG, "imageWidth:" + width);
        Log.i(TAG, "imageHeight:" + height);
        startOCR(resultUri);
    }

    private void startOCR(Uri imgUri) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
            Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);

            CV4JImage cv4JImage = new CV4JImage(bitmap);
            Threshold threshold = new Threshold();
            threshold.adaptiveThresh((ByteProcessor) (cv4JImage.convert2Gray().getProcessor()), Threshold.ADAPTIVE_C_MEANS_THRESH, 12, 30, Threshold.METHOD_THRESH_BINARY);
            Bitmap newBitmap = cv4JImage.getProcessor().getImage().toBitmap(Bitmap.Config.ARGB_8888);

            String result = extractText(newBitmap);
            /**
             *  Call BillRecognizeAdapter to recognize amount and date from result
             * */
            BillRecognizeAdapter.recognizePattern(result);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    private String extractText(Bitmap bitmap) {
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tessBaseApi == null) {
                Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }

        tessBaseApi.init(DATA_PATH, lang);

        tessBaseApi.setImage(bitmap);
        String extractedText = "empty result";
        try {
            extractedText = tessBaseApi.getUTF8Text();
        } catch (Exception e) {
            Log.e(TAG, "Error in recognizing text.");
        }
        tessBaseApi.end();
        return extractedText;
    }
}
