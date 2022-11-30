package com.calcontrol.app.data.api

import com.calcontrol.app.data.model.apimodel.RemoteExercise
import com.calcontrol.app.data.model.apimodel.RemoteIngredient
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FitnessApi {

    @GET("/ingredients/search/")
    suspend fun searchIngredients(@Query("name") name: String): Response<MutableList<RemoteIngredient>>

    @GET("/exercises/search/")
    suspend fun searchExercises(@Query("name") name: String): Response<MutableList<RemoteExercise>>

}