package i.stock.market.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CompanyListingEntity::class], exportSchema = false, version = 1)
abstract class StockDatabase : RoomDatabase() {

    abstract fun getStockDao(): StockDao
}