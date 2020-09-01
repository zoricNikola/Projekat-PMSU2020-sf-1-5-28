package com.example.projekat_pmsu2020_sf_1_5_28.service;

import com.example.projekat_pmsu2020_sf_1_5_28.model.Account;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Attachment;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Contact;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Folder;
import com.example.projekat_pmsu2020_sf_1_5_28.model.LoginDTO;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Message;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Rule;
import com.example.projekat_pmsu2020_sf_1_5_28.model.Tag;
import com.example.projekat_pmsu2020_sf_1_5_28.model.TokenDTO;
import com.example.projekat_pmsu2020_sf_1_5_28.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface EmailClientService {

//  --- AUTH ---

    @POST(ServiceUtils.AUTH_LOGIN)
    Call<TokenDTO> login(@Body LoginDTO loginDTO);

    @POST(ServiceUtils.AUTH_REGISTER)
    Call<User> register(@Body User user);

//  --- END OF AUTH---
//   --- USERS ---

    @GET(ServiceUtils.GET_USER_BY_ID)
    Call<User> getUserById(@Path("id") Long id);

    @GET(ServiceUtils.GET_USER_ACCOUNTS)
    Call<List<Account>> getUserAccounts(@Path("id") Long id);

    @GET(ServiceUtils.GET_USER_CONTACTS)
    Call<List<Contact>> getUserContacts(@Path("id") Long id);

    @GET(ServiceUtils.GET_USER_TAGS)
    Call<List<Tag>> getUserTags(@Path("id") Long id);

    @POST(ServiceUtils.CREATE_USER)
    Call<User> createUser(@Body User user);

    @PUT(ServiceUtils.UPDATE_USER)
    Call<User> updateUser(@Path("id") Long id, @Body User user);

//    --- END OF USERS ---
//    --- ACCOUNTS ---

    @GET(ServiceUtils.GET_ACCOUNT_BY_ID)
    Call<Account> getAccountById(@Path("id") Long id);

    @GET(ServiceUtils.GET_ACCOUNT_MESSAGES)
    Call<List<Message>> getAccountMessages(@Path("id") Long id);

    @GET(ServiceUtils.GET_ACCOUNT_FOLDERS)
    Call<List<Folder>> getAccountFolders(@Path("id") Long id);

    @POST(ServiceUtils.CREATE_ACCOUNT)
    Call<Account> createAccount(@Path("userId") Long userId, @Body Account account);

    @PUT(ServiceUtils.UPDATE_ACCOUNT)
    Call<Account> updateAccount(@Path("id") Long id, @Body Account account);

    @DELETE(ServiceUtils.REMOVE_ACCOUNT)
    Call<Void> removeAccount(@Path("id") Long id);

//  --- END OF ACCOUNTS ---
//  --- CONTACTS ---

    @GET(ServiceUtils.GET_CONTACT_BY_ID)
    Call<Contact> getContactById(@Path("id") Long id);

    @POST(ServiceUtils.CREATE_CONTACT)
    Call<Contact> createContact(@Path("userId") Long userId, @Body Contact contact);

    @PUT(ServiceUtils.UPDATE_CONTACT)
    Call<Contact> updateContact(@Path("id") Long id, @Body Contact contact);

    @DELETE(ServiceUtils.REMOVE_CONTACT)
    Call<Void> removeContact(@Path("id") Long id);

//  --- END OF CONTACTS ---
//  --- TAGS ---

    @GET(ServiceUtils.GET_TAG_BY_ID)
    Call<Tag> getTagById(@Path("id") Long id);

    @GET(ServiceUtils.GET_TAG_MESSAGES_BY_ACCOUNT)
    Call<List<Message>> getTagMessagesByAccount(@Path("id") Long id, @Body Account account);

    @POST(ServiceUtils.CREATE_TAG)
    Call<Tag> createTag(@Path("userId") Long userId, @Body Tag tag);

    @PUT(ServiceUtils.UPDATE_TAG)
    Call<Tag> updateTag(@Path("id") Long id, @Body Tag tag);

    @DELETE(ServiceUtils.REMOVE_TAG)
    Call<Void> removeTag(@Path("id") Long id);

//  --- END OF TAGS ---
//  --- FOLDERS ---

    @GET(ServiceUtils.GET_FOLDER_BY_ID)
    Call<Folder> getFolderById(@Path("id") Long id);

    @GET(ServiceUtils.GET_FOLDER_MESSAGES)
    Call<List<Message>> getFolderMessages(@Path("id") Long id);

    @GET(ServiceUtils.GET_FOLDER_CHILD_FOLDERS)
    Call<List<Folder>> getFolderChildFolders(@Path("id") Long id);

    @GET(ServiceUtils.GET_FOLDER_RULES)
    Call<List<Rule>> getFolderRules(@Path("id") Long id);

    @PUT(ServiceUtils.UPDATE_FOLDER_RULES)
    Call<List<Rule>> updateFolderRules(@Path("id") Long id, @Body List<Rule> rules);

    @POST(ServiceUtils.CREATE_ROOT_FOLDER)
    Call<Folder> createFolder(@Path("accountId") Long accountId, @Body Folder folder);

    @POST(ServiceUtils.CREATE_CHILD_FOLDER)
    Call<Folder> createFolderChildFolder(@Path("id") Long id, @Body Folder folder);

    @PUT(ServiceUtils.UPDATE_FOLDER)
    Call<Folder> updateFolder(@Path("id") Long id, @Body Folder folder);

    @DELETE(ServiceUtils.REMOVE_FOLDER)
    Call<Void> removeFolder(@Path("id") Long id);

//  --- END OF FOLDERS ---
//  --- RULES ---

    @GET(ServiceUtils.GET_RULE_BY_ID)
    Call<Rule> getRuleById(@Path("id") Long id);

    @POST(ServiceUtils.CREATE_RULE)
    Call<Rule> createRule(@Path("folderId") Long folderId, @Body Rule rule);

    @DELETE(ServiceUtils.REMOVE_RULE)
    Call<Void> removeRule(@Path("id") Long id);

//  --- END OF RULES ---
//  --- MESSAGES ---

    @GET(ServiceUtils.GET_MESSAGE_BY_ID)
    Call<Message> getMessageById(@Path("id") Long id);

    @GET(ServiceUtils.GET_MESSAGE_TAGS)
    Call<List<Tag>> getMessageTags(@Path("id") Long id);

    @GET(ServiceUtils.GET_MESSAGE_ATTACHMENTS)
    Call<List<Attachment>> getMessageAttachments(@Path("id") Long id);

    @POST(ServiceUtils.CREATE_MESSAGE)
    Call<Message> createMessage(@Path("accountId") Long accountId, @Body Message message);

    @PUT(ServiceUtils.UPDATE_MESSAGE_TAGS)
    Call<Message> updateMessageTags(@Path("id") Long id, @Body Message message);

    @PUT(ServiceUtils.MOVE_MESSAGE)
    Call<Message> moveMessage(@Path("id") Long id, @Path("folderId") Long folderId);

    @DELETE(ServiceUtils.REMOVE_MESSAGE)
    Call<Void> removeMessage(@Path("id") Long id);

//  --- END OF MESSAGES ---
//  --- ATTACHMENTS ---

    @GET(ServiceUtils.GET_ATTACHMENT_BY_ID)
    Call<Attachment> getAttachmentById(@Path("id") Long id);

    @POST(ServiceUtils.CREATE_ATTACHMENT)
    Call<Attachment> createAttachment(@Path("messageId") Long messageId, @Body Attachment attachment);

    @DELETE(ServiceUtils.REMOVE_ATTACHMENT)
    Call<Void> removeAttachment(@Path("id") Long id);
}
