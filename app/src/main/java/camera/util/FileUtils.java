package camera.util;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * Save Bitmap to SD card
     * @param b
     * @param savePath
     */
    public static void saveBitmap(Bitmap b, String savePath){
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap rotatedBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
            FileOutputStream fileOutputStream = new FileOutputStream(savePath);
            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "Bitmap : save succeeded");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "Bitmap: save failure");
            e.printStackTrace();
        }
    }

    /**
     * Get the file path according to the file Uri
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String path = null;
        if ( scheme == null )
            path = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            path = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        path = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return path;
    }

}
