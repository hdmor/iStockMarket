package i.stock.market.data.data_source.remote.dto


import com.google.gson.annotations.SerializedName

data class CompanyInfoDto(
    @SerializedName("Country")
    val country: String? = null,
    @SerializedName("Description")
    val description: String? = null,
    @SerializedName("Industry")
    val industry: String? = null,
    @SerializedName("Name")
    val name: String? = null,
    @SerializedName("Symbol")
    val symbol: String? = null
)