package tn.esprit.setoutlife.Retrofit;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

}
