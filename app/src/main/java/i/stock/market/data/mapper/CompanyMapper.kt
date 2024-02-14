package i.stock.market.data.mapper

import i.stock.market.data.data_source.local.CompanyListingEntity
import i.stock.market.data.data_source.remote.dto.CompanyInfoDto
import i.stock.market.domain.model.CompanyInfo
import i.stock.market.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing(): CompanyListing =
    CompanyListing(name = this.name, symbol = this.symbol, exchange = this.exchange)

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity =
    CompanyListingEntity(name = this.name, symbol = this.symbol, exchange = this.exchange)

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo =
    CompanyInfo(
        country = country ?: "",
        description = description ?: "",
        industry = industry ?: "",
        name = name ?: "",
        symbol = symbol ?: ""
    )