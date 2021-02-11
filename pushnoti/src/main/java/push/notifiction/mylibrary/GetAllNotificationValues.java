package push.notifiction.mylibrary;

import android.content.Context;

public class GetAllNotificationValues {
    public static String getMessage(Context context){
       return PreferenceConnector.readString(
                context, PreferenceConnector.message, "message");
    }

    public static String getTitle(Context context){
        return PreferenceConnector.readString(
                context, PreferenceConnector.title_Notifi, "title");
    }

    public static String getPackage(Context context){
        return PreferenceConnector.readString(
                context, PreferenceConnector.packageName, "Package Name");
    }
    public static String getMode(Context context){
        return PreferenceConnector.readString(
                context, PreferenceConnector.mode, "None");
    }

    public static String getisToken(Context context){
        return PreferenceConnector.readString(
                context, PreferenceConnector.isTokenSaved, "");
    }


    public static void writeMessage(Context context, String message){
        PreferenceConnector.writeString(context,
                PreferenceConnector.message, message);
    }
    public static void writeTitle(Context context, String title){
        PreferenceConnector.writeString(context,
                PreferenceConnector.title_Notifi, title);
    }
    public static void writePackageName(Context context, String PackageName){
        PreferenceConnector.writeString(context,
                PreferenceConnector.packageName, PackageName);
    }

    public static void writeModeName(Context context, String mode){
        PreferenceConnector.writeString(context,
                PreferenceConnector.mode, mode);
    }

    public static void writeisToken(Context context, String mode){
        PreferenceConnector.writeString(context,
                PreferenceConnector.isTokenSaved, mode);
    }

}
