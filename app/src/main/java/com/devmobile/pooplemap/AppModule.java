package com.devmobile.pooplemap;

import static com.devmobile.pooplemap.MainActivity.getContextOfApplication;

import com.devmobile.pooplemap.db.jdbc.DatabaseHandlerImg;
import com.devmobile.pooplemap.db.jdbc.DatabaseImg;
import com.devmobile.pooplemap.network.services.AuthService;
import com.devmobile.pooplemap.network.services.LocationService;
import com.devmobile.pooplemap.network.services.RatingService;
import com.devmobile.pooplemap.network.services.UserService;

import com.devmobile.pooplemap.db.sqilte.DatabaseHandlerSqlite;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.sql.Connection;
import java.sql.DriverManager;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    private static final String BASE_URL = "https://poople-map-api.onrender.com/";



    @Provides
    @Singleton
    public static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }
    @Provides
    @Singleton
    public static AuthService provideIAuthService(Provider<Retrofit> retrofitProvider) {
        return retrofitProvider.get().create(AuthService.class);
    }
    @Provides
    @Singleton
    public static UserService provideIUserService(Provider<Retrofit> retrofitProvider) {
        return retrofitProvider.get().create(UserService.class);
    }
    @Provides
    @Singleton
    public static LocationService provideILocationService(Provider<Retrofit> retrofitProvider) {
        return retrofitProvider.get().create(LocationService.class);
    }
    @Provides
    @Singleton
    public static RatingService provideIRatingService(Provider<Retrofit> retrofitProvider) {
        return retrofitProvider.get().create(RatingService.class);
    }

    @Provides
    @Singleton
    public static DatabaseHandlerSqlite provideDatabaseHandler() {
        return new DatabaseHandlerSqlite(getContextOfApplication());
    }


}
