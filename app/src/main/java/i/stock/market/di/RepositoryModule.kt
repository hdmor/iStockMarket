package i.stock.market.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import i.stock.market.data.csv.CSVParser
import i.stock.market.data.csv.CompanyListingsParser
import i.stock.market.data.csv.IntraDayInfoParser
import i.stock.market.data.repository.StockRepositoryImpl
import i.stock.market.domain.model.CompanyListing
import i.stock.market.domain.model.IntraDayInfo
import i.stock.market.domain.repository.StockRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(companyListingsParser: CompanyListingsParser): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntraDayInfoParser(intraDayInfoParser: IntraDayInfoParser): CSVParser<IntraDayInfo>

    @Binds
    @Singleton
    abstract fun bindStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository
}