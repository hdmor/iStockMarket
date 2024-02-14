package i.stock.market.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import i.stock.market.domain.repository.StockRepository
import i.stock.market.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intraDayInfoResult = async { repository.getIntraDayInfo(symbol) }
            state = when (val result = companyInfoResult.await()) {
                is Resource.Error -> state.copy(error = result.message, isLoading = false, company = null)
                is Resource.Loading -> state.copy(isLoading = true)
                is Resource.Success -> state.copy(company = result.data, isLoading = false, error = null)
            }
            state = when (val result = intraDayInfoResult.await()) {
                is Resource.Error -> state.copy(error = result.message, isLoading = false, company = null)
                is Resource.Loading -> state.copy(isLoading = true)
                is Resource.Success -> state.copy(stockInfoList = result.data ?: emptyList(), isLoading = false, error = null)
            }
        }
    }

}