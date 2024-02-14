package i.stock.market.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import i.stock.market.data.data_source.local.StockDao
import i.stock.market.data.data_source.local.StockDatabase
import i.stock.market.data.data_source.remote.StockApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStockApi(): StockApi =
        Retrofit.Builder().baseUrl(StockApi.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create()

    @Provides
    @Singleton
    fun provideStockDatabase(application: Application) : StockDatabase =
        Room.databaseBuilder(application, StockDatabase::class.java, "i_stock_market_db").build()

    @Provides
    @Singleton
    fun provideStockDao(stockDatabase : StockDatabase) : StockDao = stockDatabase.getStockDao()
}