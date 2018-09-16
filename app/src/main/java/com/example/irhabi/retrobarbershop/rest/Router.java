package com.example.irhabi.retrobarbershop.rest;

/**
 * Created BY Programmer Jalan on January 2018
 */

import com.example.irhabi.retrobarbershop.model.Absen;
import com.example.irhabi.retrobarbershop.model.Absenarray;
import com.example.irhabi.retrobarbershop.model.AlluserRespons;
import com.example.irhabi.retrobarbershop.model.Barang;
import com.example.irhabi.retrobarbershop.model.BarangArray;
import com.example.irhabi.retrobarbershop.model.BarangDetail;
import com.example.irhabi.retrobarbershop.model.BarangDetailArray;
import com.example.irhabi.retrobarbershop.model.ModelTambahAKun;
import com.example.irhabi.retrobarbershop.model.Responhapuskaryawan;
import com.example.irhabi.retrobarbershop.model.Upload;
import com.example.irhabi.retrobarbershop.model.User;
import com.example.irhabi.retrobarbershop.model.Usr;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Router {

    //user endpoint
    @POST("user/login")
    Call<User> Login(@Body User user);
    @PUT("user/{id}")
    Call<User> Updateuser(@Body User user, @Path("id")
                String id);
    @GET("user/{id}")
    Call<Usr> retro(@Path("id") int Id);
    @GET("user")
    Call<AlluserRespons> getalluser();
    @POST("user")
    Call<ModelTambahAKun> Postuser(@Body ModelTambahAKun akun);
    @DELETE("user/{id}")
    Call<Responhapuskaryawan> delete(@Path("id")int id);

    //absen endpoint
    @GET("absen/{id}")
    Call<Absenarray> absenbarberman(@Path("id") int Id);
    @GET("absen/{id}/{from}/{to}")
    Call<Absenarray> rangedataabsen(@Path("id") int Id, @Path("from") String from ,@Path("to") String to);
    @POST("absen")
    Call<Absen>post(@Body Absen a);

    @POST("absen/izin")
    Call<Absen>postizin(@Body Absen a);


    //upload endpoint
    @Multipart
    @POST("upload/android")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part photo,
                                   @Part MultipartBody.Part phot,
                                   @PartMap Map<String,RequestBody> text);
    @POST("upload/namephoto")
    Call<Upload> uploadnameImage(@Body Upload upload);


    //barang endpoin
    @POST("barang/{usergrup}/{id}")
    Call<Barang>postbarang(@Body Barang a,
                           @Path("usergrup") String usergrup,
                           @Path("id") String id);

    @PUT("barang/{usergrup}/{id}/{idbarang}")
    Call<Barang>Updatebarang(@Body Barang a,
                           @Path("usergrup") String usergrup,
                           @Path("id") String id,
                             @Path("idbarang") int idbarang);

    @GET("barang/{usergrup}/{page}")
    Call<BarangArray>Getbarang(@Path("usergrup") String usergrup, @Path("page") String page);

    @DELETE("barang/{usergrup}/delete/{idbarang}")
    Call<Barang>Deletebarang(@Path("usergrup") String usergrup, @Path("idbarang") int idbarang );


   //barangdetail
    @GET("barangdetail/{usergrup}/{page}/detail/{code_category}")
    Call<BarangDetailArray> Get_Barang_Detail(@Path("usergrup") String usergrup,
                                              @Path("page") String s,
                                              @Path("code_category") int kt);
    @POST("barangdetail/{usergrup}")
    Call<BarangDetail> PostBarangDetail(@Body BarangDetail s,
                                        @Path("usergrup") String usergrup);

    @DELETE("barangdetail/{usergrup}/delete/{idbarang}")
    Call<BarangDetail> DeleteBarangDetail(@Path("usergrup") String usergrup,
                                    @Path("idbarang") int idbarang);

    @PUT("barangdetail/{usergrup}/update/{idbarang}")
    Call<BarangDetail> UpdateBarangDetail(@Body BarangDetail barangDetail ,
                                          @Path("usergrup")String usergrup,
                                          @Path("idbarang") int idbarang);
}
