package ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ProcessingImage {

    Bitmap bitmap;

    public ProcessingImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ProcessingImage() {
    }

    public static byte[] receiveByteArrayUserPhoto(Bitmap userPhoto) {
        ByteArrayOutputStream imageStream = null;
        byte[] byteUserPhoto = {0};
        try {
            imageStream = new ByteArrayOutputStream();
            userPhoto.compress(Bitmap.CompressFormat.WEBP, 33, imageStream);
            byteUserPhoto = imageStream.toByteArray();
            return byteUserPhoto;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteUserPhoto;
    }

    public static byte[] receiveByteArrayUserPhoto(Bitmap userPhoto, int quality) {
        ByteArrayOutputStream imageStream = null;
        byte[] byteUserPhoto = {0};
        try {
            imageStream = new ByteArrayOutputStream();
            userPhoto.compress(Bitmap.CompressFormat.WEBP, quality, imageStream);
            byteUserPhoto = imageStream.toByteArray();
            return byteUserPhoto;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageStream != null) {
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteUserPhoto;
    }

    public static Bitmap receiveBitmapFromByteArray(byte[] byteImage) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Bitmap receiveEditedPhotoUser(Uri UriSelectedImageInGallery, Bitmap basePhotoUser, Context context) {
        Bitmap reducedImage = reduceImage(basePhotoUser);
        Bitmap finalImage = receiveRotatedImage(UriSelectedImageInGallery, reducedImage, context);
        return finalImage;
    }

    private static Bitmap reduceImage(Bitmap basicImage) {
        int WIDTH_BITMAP = basicImage.getWidth() / 2;
        int HEIGHT_BITMAP = basicImage.getHeight() / 2;

        Bitmap reducedImage = Bitmap.createScaledBitmap(basicImage, WIDTH_BITMAP, HEIGHT_BITMAP, false);
        return reducedImage;
    }

    public static Bitmap decodeSampledBitmap(Uri uriImage,
                                             int requiredWidth, int requiredHeight, Context context) throws FileNotFoundException {

        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeFile(path, options);
        //BitmapFactory.decodeByteArray(byteImage,0,byteImage.length,options);
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uriImage), new Rect(), options);
        // Вычисляем inSampleSize
        options.inSampleSize = calculateInSampleSize(options, requiredWidth,
                requiredHeight);

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;
        // return BitmapFactory.decodeFile(path, options);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uriImage), new Rect(), options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int requiredWidth, int requiredHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > requiredHeight || width > requiredWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            while ((halfHeight / inSampleSize) > requiredHeight
                    && (halfWidth / inSampleSize) > requiredWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Bitmap receiveRotatedImage(Uri selectedImageInGallery, Bitmap noRotatedBitmap, Context context) {
        try {
            ExifInterface exifInterface = new ExifInterface(context.getContentResolver().openInputStream(selectedImageInGallery));
            int rotation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {
                matrix.setRotate(rotationInDegrees);
                Bitmap finalBitmap = Bitmap.createBitmap(noRotatedBitmap, 0, 0, noRotatedBitmap.getWidth(), noRotatedBitmap.getHeight(), matrix, true);
                return finalBitmap;
            }
        } catch (IOException ex) {
            Log.e("LOG EXIF", "Failed to get Exif data", ex);
            Toast.makeText(context, "Не удалось перевернуть картинку", Toast.LENGTH_SHORT).show();
        }
        return noRotatedBitmap;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
