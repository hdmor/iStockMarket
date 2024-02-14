package i.stock.market.domain.repository

import i.stock.market.domain.model.CompanyInfo
import i.stock.market.domain.model.CompanyListing
import i.stock.market.domain.model.IntraDayInfo
import i.stock.market.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(fetchFromRemote: Boolean, query: String): Flow<Resource<List<CompanyListing>>>
    suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>>
    suspend fun getCompanyInfo(symbol: String) : Resource<CompanyInfo>
}