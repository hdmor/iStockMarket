package i.stock.market.presentation.company_listings

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import i.stock.market.domain.repository.StockRepository
import i.stock.market.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/** We are not use any USE-CASE in this project but still project architecture is most like clean-architecture MVVM! **/
@HiltViewModel
class CompanyListingsViewModel @Inject constructor(private val repository: StockRepository) : ViewModel() {

    var state by mutableStateOf(CompanyListingsState())
    private var searchJob: Job? = null

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingsEvent) {
        when (event) {
            is CompanyListingsEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    /** After 500 milliseconds without entering new character for search trigger this job **/
                    delay(500L)
                    getCompanyListings()
                }
            }

            CompanyListingsEvent.Refresh -> getCompanyListings(fetchFromRemote = true)
        }
    }

    private fun getCompanyListings(query: String = state.searchQuery.lowercase(), fetchFromRemote: Boolean = false) {
        viewModelScope.launch {
            repository.getCompanyListings(fetchFromRemote, query).collect { result ->
                when (result) {
                    is Resource.Error -> Log.e("TAG", "error: ${result.message}")
                    is Resource.Loading ->
                        state = state.copy(isLoading = result.isLoading, isRefreshing = result.isLoading)

                    is Resource.Success -> result.data?.let { listings ->
                        state = state.copy(companies = listings)
                    }
                }
            }
        }
    }
}