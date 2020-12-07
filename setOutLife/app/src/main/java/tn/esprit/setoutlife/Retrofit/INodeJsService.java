package tn.esprit.setoutlife.Retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tn.esprit.setoutlife.entities.Tag;

public interface INodeJsService {

    @POST("register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("firstName") String firstName,
                                    @Field("lastName") String lastName,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("address") String address,
                                    @Field("phone") String phone);
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email,
                                  @Field("password") String password);


    //tags
    @POST("add_tag")
    @FormUrlEncoded
    Observable<String> addTag(@Field("tagName") String tagName,
                                 @Field("color") String color);

    @GET("all_tags")
    Call<String> getAllTags();
}
