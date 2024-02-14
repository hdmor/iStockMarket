package i.stock.market.domain.model

data class CompanyInfo(
    val country: String,
    val description: String,
    val industry: String,
    val name: String,
    val symbol: String
)