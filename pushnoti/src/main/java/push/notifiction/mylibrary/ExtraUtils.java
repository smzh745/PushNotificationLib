package push.notifiction.mylibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ExtraUtils {
    public static final String UPDATE_APP = "update app";
    public static final String PROMOTE_APP = "Promote App";
    public static final String NONE = "None";

    public static void anotherDialog(final Context context, String title, String message, final String packageName, final String buttonName, final String mode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
//        builder.setMessage("There are some changes in the App and need to update/install forcefully.\n\nPlease UPDATE_APP now?");
        builder.setMessage(message);
//        builder.setIcon(R.drawable.icon);
        builder.setPositiveButton(buttonName, (dialog, which) -> {
            dialog.dismiss();
            if (buttonName.equalsIgnoreCase("Click here")) {
                if (!isPackageExisted(packageName, context)) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }
                GetAllNotificationValues.writeModeName(context, NONE);
            } else {
                if (isPackageExisted(packageName, context)) {
                    Intent launchIntent = context.getPackageManager().
                            getLaunchIntentForPackage(packageName);
                    if (launchIntent != null) {
                        context.startActivity(launchIntent);//null pointer check in case package name was not found
                        GetAllNotificationValues.writeModeName(context, NONE);
                    }
//                    System.exit(0);
                } else {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    System.exit(0);
                }
            }
        });
        builder.setNeutralButton("Cancel", (dialog, which) -> {
            if (mode.equalsIgnoreCase(UPDATE_APP)) {
                System.exit(0);
            } else {
                GetAllNotificationValues.writeModeName(context, NONE);
            }
        });
        if (buttonName.equalsIgnoreCase("Click here")) {
            if (!isPackageExisted(packageName, context)) {
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {

            AlertDialog dialog = builder.create();
            dialog.show();
        }
//        builder.show();
    }

    public static boolean isPackageExisted(String targetPackage, Context context) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }


    public static void addToServer(final Context context) {


        try {
            ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

            final String macAddress = getMacAddress();
            Log.d("mac ", macAddress);

            if (netInfo != null && !macAddress.isEmpty()) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Log.d("Test", "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get new Instance ID token
                            final String token = Objects.requireNonNull(task.getResult()).getToken();
                            Log.d("Test", token);

                            if (GetAllNotificationValues.getisToken(context).equalsIgnoreCase("")) {
                                // Save token at server..
                                try {
                                    NetworkCalling(context, token, macAddress);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                if (!GetAllNotificationValues.getisToken(context).equalsIgnoreCase(token)) {
                                    // Token updated at server if change..
                                    try {
                                        NetworkCalling(context, token, macAddress);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void NetworkCalling(Context context, final String token, final String macAddress) throws Exception {

//        final String URL = AESUtils.decrypt(SharedPrefUtils.getStringData(context, "url"));
        final String URL = "https://pushnotification.toptrendingappstudio.com/v1/registerToken.php";
        final String brand = Build.BRAND;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("error")) {
                            // Save token into Preferences..
                            GetAllNotificationValues.writeisToken(context, token);
                        }
                        Log.d("Test", "Json response: " + jsonObject.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.d("Test", "Json error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                params.put("devicename", brand);
                params.put("packagename", context.getPackageName());
                params.put("mac", macAddress);
                return params;
            }
        };
        if (!token.isEmpty()) {
            RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
        }
    }

    public static String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    //res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void checkIfNotificationRequired(Context context) {
        String pkgName = GetAllNotificationValues.getPackage(context);
        String mode = GetAllNotificationValues.getMode(context);
        String message = GetAllNotificationValues.getMessage(context);
        String notificationTitle = GetAllNotificationValues.getTitle(context);

        if (isPackageExisted(pkgName, context)) {
            GetAllNotificationValues.writeModeName(context, NONE);
        } else {

            if (mode.equalsIgnoreCase(UPDATE_APP) || mode.equalsIgnoreCase(PROMOTE_APP)) {
                String buttonName = "Update";
                if (mode.equalsIgnoreCase(PROMOTE_APP)) {
                    buttonName = "Click here";
                }

                anotherDialog(context, notificationTitle, message, pkgName, buttonName, mode);
            }
        }

    }
}
