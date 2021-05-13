package service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.serverregister.BehaviorActivity;
import com.example.serverregister.SharedPreferencesUserInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Set;

import entites.User;
import retrofit.ServerError;
import retrofit2.Response;

public class UserService {
    private SharedPreferencesUserInfo sharedPreferencesUserInfo = new SharedPreferencesUserInfo();
    private ServerError serverError = new ServerError();

    public void saveUser(Response<User> response, BehaviorActivity behaviorActivity) {
        int serverStatusCode = response.code();
        Context context = behaviorActivity.receiveContext();
        if (response.isSuccessful()) {
            sharedPreferencesUserInfo.setSettings(
                    context,
                    response.body().getLastname(),
                    response.body().getId(),
                    response.body().getFirstname(),
                    response.body().getPatronymic(),
                    response.body().getEmail(),
                    response.body().getPassword()
            );
        } else {
            serverError.handleError(serverStatusCode, behaviorActivity);
        }
    }

    public void loginApp(Response<User> response, BehaviorActivity behaviorActivity) {
        int serverStatusCode = response.code();
        Context context = behaviorActivity.receiveContext();
        if (response.isSuccessful()) {
            String name = String.valueOf(response.body().getFirstname()) + " " + String.valueOf(response.body().getLastname());
            Toast.makeText(context, name + "," + " поздравляем с успешной авторизацией", Toast.LENGTH_SHORT).show();
            sharedPreferencesUserInfo.setSettings(
                    context,
                    response.body().getLastname(),
                    response.body().getId(),
                    response.body().getFirstname(),
                    response.body().getPatronymic(),
                    response.body().getEmail(),
                    response.body().getPassword()
            );

        } else {
            serverError.handleError(serverStatusCode, behaviorActivity);
        }
    }

    public void updateUserData(Response<User> response, BehaviorActivity behaviorActivity) {
        int serverStatusCode = response.code();
        Context context = behaviorActivity.receiveContext();
        if (response.isSuccessful()) {
            Toast.makeText(context, "Данные успешно изменены!", Toast.LENGTH_SHORT).show();
            sharedPreferencesUserInfo.setSettings(
                    context,
                    response.body().getLastname(),
                    response.body().getId(),
                    response.body().getFirstname(),
                    response.body().getPatronymic(),
                    response.body().getEmail(),
                    response.body().getPassword()
            );
        } else {
            serverError.handleError(serverStatusCode, behaviorActivity);
        }
    }

    public User receiveUserData(Response<User> response) {
        User userData = new User();
        int id = response.body().getId();
        String firstname = response.body().getFirstname();
        String lastname = response.body().getLastname();
        String patronymic = response.body().getPatronymic();
        String email = response.body().getEmail();
        String password = response.body().getPassword();
        String aboutuser = response.body().getAboutuser();
        String phone = response.body().getPhone();
        byte[] photo = response.body().getPhoto();
        int helpcounter = response.body().getHelpcounter();
        String region = response.body().getRegion();
        String district = response.body().getDistrict();
        String city = response.body().getCity();

        userData.setId(id);
        userData.setFirstname(firstname);
        userData.setLastname(lastname);
        userData.setPatronymic(patronymic);
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setAboutuser(aboutuser);
        userData.setPhone(phone);
        userData.setPhoto(photo);
        userData.setHelpcounter(helpcounter);
        userData.setRegion(region);
        userData.setDistrict(district);
        userData.setCity(city);

        return userData;
    }

    public void createCredentials(User userRequest, String email, String password) {
        userRequest.setEmail(email);
        userRequest.setPassword(password);
    }

    public byte[] receiveByteArrayUserPhoto(Bitmap userPhoto) {
        ByteArrayOutputStream imageStream = null;
        byte[] byteUserPhoto = {0};
        try {
            imageStream = new ByteArrayOutputStream();
            userPhoto.compress(Bitmap.CompressFormat.PNG, 100, imageStream);
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

    public Bitmap receiveBitmapFromByteArray(byte[] byteImage) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bitmap receiveEditedPhotoUser(Bitmap basePhotoUser, Uri UriSelectedImageInGallery, BehaviorActivity behaviorActivity) {
        Bitmap reducedImage = reduceImage(basePhotoUser);
        Bitmap finalImage = recieveRotatedImage(UriSelectedImageInGallery, reducedImage, behaviorActivity);
        return finalImage;
    }

    private Bitmap reduceImage(Bitmap basicImage) {
        int WIDTH_BITMAP = basicImage.getWidth() / 10;
        int HEIGHT_BITMAP = basicImage.getHeight() / 10;
        if (basicImage.getWidth() > 6000) {
            WIDTH_BITMAP = basicImage.getWidth() / 30;
            HEIGHT_BITMAP = basicImage.getHeight() / 30;
        }
        Bitmap reducedImage = Bitmap.createScaledBitmap(basicImage, WIDTH_BITMAP, HEIGHT_BITMAP, false);
        return reducedImage;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Bitmap recieveRotatedImage(Uri selectedImageInGallery, Bitmap noRotatedBitmap, BehaviorActivity behaviorActivity) {
        Context context = behaviorActivity.receiveContext();
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

    public static String receiveCountParticipants(Set<User> participants) {
        int count = participants.size();
        return String.valueOf(count);
    }

    public static String receiveStringDateTime(Calendar dateTime) {
        String y = String.valueOf(dateTime.get(Calendar.YEAR));
        String m = convertIntMonthInString(dateTime.get(Calendar.MONTH) + 1);
        String day = String.valueOf(dateTime.get(Calendar.DAY_OF_MONTH));
        String h = String.valueOf(dateTime.get(Calendar.HOUR_OF_DAY));
        String mun = String.valueOf(dateTime.get(Calendar.MINUTE));
        String resultStringDateTime = day + " " + m + " " + y + " , " + h + ":" + mun;
        return resultStringDateTime;
    }

    private static String convertIntMonthInString(int monthInt) {
        String monthStr = null;
        if (monthInt == 1) {
            monthStr = "января";
        } else if (monthInt == 2) {
            monthStr = "февраля";
        } else if (monthInt == 3) {
            monthStr = "марта";
        } else if (monthInt == 4) {
            monthStr = "апреля";
        } else if (monthInt == 5) {
            monthStr = "мая";
        } else if (monthInt == 6) {
            monthStr = "июня";
        } else if (monthInt == 7) {
            monthStr = "июля";
        } else if (monthInt == 8) {
            monthStr = "августа";
        } else if (monthInt == 9) {
            monthStr = "сентября";
        } else if (monthInt == 10) {
            monthStr = "октября";
        } else if (monthInt == 11) {
            monthStr = "ноября";
        } else if (monthInt == 12) {
            monthStr = "декабря";
        }
        return monthStr;
    }
}
