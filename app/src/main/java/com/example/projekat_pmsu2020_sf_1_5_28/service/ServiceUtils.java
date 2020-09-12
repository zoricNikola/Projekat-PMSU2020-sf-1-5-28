package com.example.projekat_pmsu2020_sf_1_5_28.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.projekat_pmsu2020_sf_1_5_28.activities.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {

    //    192.168.0.14:8080/api
    public static final String SERVICE_API_PATH = "http://192.168.0.15:8080/api/";
    //  AUTH
    public static final String AUTH_LOGIN = "auth/login";
    public static final String AUTH_REGISTER = "auth/login";
    //  --- END OF AUTH ---
//  --- USERS ---
    public static final String GET_USER_BY_ID = "users/{id}";
    public static final String GET_USER_ACCOUNTS = "users/{id}/accounts";
    public static final String GET_USER_CONTACTS = "users/{id}/contacts";
    public static final String GET_USER_TAGS = "users/{id}/tags";
    public static final String CREATE_USER = "users";
    public static final String UPDATE_USER = "users/{id}";
    //  --- END OF USERS ---
//  --- ACCOUNTS ---
    public static final String GET_ACCOUNT_BY_ID = "accounts/{id}";
    public static final String GET_ACCOUNT_MESSAGES = "accounts/{id}/messages";
    public static final String GET_ACCOUNT_FOLDERS = "accounts/{id}/folders";
    public static final String CREATE_ACCOUNT = "accounts/{userId}";
    public static final String UPDATE_ACCOUNT = "accounts/{id}";
    public static final String REMOVE_ACCOUNT = "accounts/{id}";
    //  --- END OF ACCOUNTS ---
//  --- CONTACTS ---
    public static final String GET_CONTACT_BY_ID = "contacts/{id}";
    public static final String CREATE_CONTACT = "contacts/{userId}";
    public static final String UPDATE_CONTACT = "contacts/{id}";
    public static final String REMOVE_CONTACT = "contacts/{id}";
    //  --- END OF CONTACTS ---
//  --- TAGS ---
    public static final String GET_TAG_BY_ID = "tags/{id}";
    public static final String GET_TAG_MESSAGES_BY_ACCOUNT = "tags/{id}/messages";
    public static final String CREATE_TAG = "tags/{userId}";
    public static final String UPDATE_TAG = "tags/{id}";
    public static final String REMOVE_TAG = "tags/{id}";
    //  --- END OF TAGS ---
//  --- FOLDERS ---
    public static final String GET_FOLDER_BY_ID = "folders/{id}";
    public static final String GET_FOLDER_MESSAGES = "folders/{id}/messages";
    public static final String GET_FOLDER_CHILD_FOLDERS = "folders/{id}/childFolders";
    public static final String GET_FOLDER_RULES = "folders/{id}/rules";
    public static final String UPDATE_FOLDER_RULES = "folders/{id}/rules";
    public static final String CREATE_ROOT_FOLDER = "folders/{accountId}";
    public static final String CREATE_CHILD_FOLDER = "folders/{id}/childFolders";
    public static final String UPDATE_FOLDER = "folders/{id}";
    public static final String REMOVE_FOLDER = "folders/{id}";
    //  --- END OF FOLDERS ---
//  --- RULES ---
    public static final String GET_RULE_BY_ID = "rules/{id}";
    public static final String CREATE_RULE = "rules/{folderId}";
    public static final String REMOVE_RULE = "rules/{id}";
    //  --- END OF RULES ---
//  --- MESSAGES ---
    public static final String GET_MESSAGE_BY_ID = "messages/{id}";
    public static final String GET_MESSAGE_TAGS = "messages{id}/tags";
    public static final String GET_MESSAGE_ATTACHMENTS = "messages{id}/attachments";
    public static final String CREATE_MESSAGE = "messages/{accountId}";
    public static final String MARK_MESSAGE_AS_READ = "messages/{id}";
    public static final String SEND_MESSAGE = "messages/{id}";
    public static final String UPDATE_MESSAGE_TAGS = "messages/{id}";
    public static final String MOVE_MESSAGE = "messages/{id}/moveTo/{folderId}";
    public static final String REMOVE_MESSAGE = "messages/{id}";
    //  --- END OF MESSAGES ---
//  --- ATTACHMENTS ---
    public static final String GET_ATTACHMENT_BY_ID = "attachments/{id}";
    public static final String CREATE_ATTACHMENT = "attachments/{messageId}";
    public static final String REMOVE_ATTACHMENT = "attachments/{id}";
//  --- END OF ATTACHMENTS ---

    public static Context mContext;
    public static final String PREFERENCES_NAME = "MyEmailClient";

    public static OkHttpClient buildClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.addHeader("Content-Type", "application/json");

                SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
                String jwt = sharedPreferences.getString("jwt", null);
                Long userId = sharedPreferences.getLong("userId", 0);
                if (jwt != null && userId != 0) {
                    requestBuilder.addHeader("Authorization", "Bearer " + jwt);
                }
                return chain.proceed(requestBuilder.build());
            }
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).addNetworkInterceptor(headerInterceptor).build();
        return client;
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildClient())
            .build();

    public static EmailClientService emailClientService(Context context) {
        mContext = context;
        return retrofit.create(EmailClientService.class);
    }
}
