package com.example.api_rest_call;

import android.graphics.PostProcessor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AutoService {

    /**
     * Definicion de ruta para GET (Read all)
     */
    String API_ROUTE_ALL= "app/api/read";

    /**
     * Metodo abstracto para utilizar HTTP.GET
     * @return
     */
    @GET(API_ROUTE_ALL)
    Call<List<Auto>> getAutos();

    /**
     * Definicion de ruta para GET (Read all)
     */
    String API_ROUTE= "app/api/read/{id}";

    /**
     * Metodo abstracto para utilizar HTTP.GET
     * @return
     */
    @GET(API_ROUTE)
    Call<Auto> getAuto(@Path("id") String id);


    /**
     * Definicion de ruta para POST
     */
    String API_ROUTE_CREATE= "app/api/create";

    /**
     * Metodo abstracto para utilizar HTTP.POST
     * @return
     */
    @POST(API_ROUTE_CREATE)
    @FormUrlEncoded
    Call<Auto> saveAuto(@Field("marca") String marca,
                        @Field("modelo") String modelo);


    /**
     * Definicion de ruta para PUT
     */
    String API_ROUTE_UPDATE= "app/api/update/{id}";

    /**
     * Metodo abstracto para utilizar HTTP.PUT
     * @return
     */
    @PUT(API_ROUTE_UPDATE)
    @FormUrlEncoded
    Call<Void> updateAuto(@Path("id") String id,
                                @Field("marca") String marca,
                                @Field("modelo") String modelo);

    /**
     * Definicion de ruta para DELETE
     */
    String API_ROUTE_DELETE= "app/api/delete/{id}";

    /**
     * Metodo abstracto para utilizar HTTP.PUT
     * @return
     */
    @DELETE(API_ROUTE_DELETE)
    Call<Void> deleteAuto(@Path("id") String id);

}
