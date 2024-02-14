package i.stock.market.data.repository

import i.stock.market.data.csv.CSVParser
import i.stock.market.data.data_source.local.StockDao
import i.stock.market.data.data_source.remote.StockApi
import i.stock.market.data.mapper.toCompanyInfo
import i.stock.market.data.mapper.toCompanyListing
import i.stock.market.data.mapper.toCompanyListingEntity
import i.stock.market.domain.model.CompanyInfo
import i.stock.market.domain.model.CompanyListing
import i.stock.market.domain.model.IntraDayInfo
import i.stock.market.domain.repository.StockRepository
import i.stock.market.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val dao: StockDao,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intraDayInfoParser: CSVParser<IntraDayInfo>
) : StockRepository {

    override suspend fun getCompanyListings(fetchFromRemote: Boolean, query: String): Flow<Resource<List<CompanyListing>>> =
        flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(localListings.map { it.toCompanyListing() }))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListing = try {
                val response = api.getListings()
                /** getListings() return type is ResponseBody because this api get to download a CSV file! **/
                companyListingsParser.parse(response.byteStream())
            } catch (e: IOException) { // Parsing goes wrong
                e.printStackTrace()
                emit(Resource.Error(message = e.localizedMessage ?: "Couldn't load data."))
                null
            } catch (e: HttpException) { // Invalid response
                e.printStackTrace()
                emit(Resource.Error(message = e.localizedMessage ?: "Couldn't load data."))
                null
            }

            remoteListing?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(listings.map { it.toCompanyListingEntity() })
                emit(Resource.Success(data = dao.searchCompanyListing("").map { it.toCompanyListing() }))
                emit(Resource.Loading(isLoading = false))
            }
        }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>> =
        try {
            val response = api.getIntraDayInfo(symbol)
            val result = intraDayInfoParser.parse(response.byteStream())
            Resource.Success(data = result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage ?: "Couldn't load data.")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage ?: "Couldn't load data.")
        }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> =
        try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(data = result.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage ?: "Couldn't load data.")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = e.localizedMessage ?: "Couldn't load data.")
        }
}