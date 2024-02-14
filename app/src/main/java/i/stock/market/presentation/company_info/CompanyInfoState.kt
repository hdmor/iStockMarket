package i.stock.market.presentation.company_info

import i.stock.market.domain.model.CompanyInfo
import i.stock.market.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfoList: List<IntraDayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
