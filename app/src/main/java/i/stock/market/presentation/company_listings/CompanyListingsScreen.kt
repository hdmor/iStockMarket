package i.stock.market.presentation.company_listings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import i.stock.market.presentation.company_listings.components.CompanyItem
import i.stock.market.presentation.destinations.CompanyInfoScreenDestination

@OptIn(ExperimentalMaterialApi::class)
@Composable
@RootNavGraph(start = true) // sets this as the start destination of the default nav graph
@Destination
fun CompanyListingsScreen(navigator: DestinationsNavigator, viewModel: CompanyListingsViewModel = hiltViewModel()) {

    val state = viewModel.state
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { viewModel.onEvent(CompanyListingsEvent.Refresh) }
    )

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                viewModel.onEvent(CompanyListingsEvent.OnSearchQueryChange(it))
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = { Text(text = "Search...") },
            maxLines = 1,
            singleLine = true
        )
        Box(modifier = Modifier.pullRefresh(state = pullRefreshState)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.companies.size) { index ->
                    val company = state.companies[index]
                    CompanyItem(
                        company = company,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navigator.navigate(CompanyInfoScreenDestination(company.symbol))
                            }
                            .padding(16.dp)
                    )
                    if (index < state.companies.size)
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
            PullRefreshIndicator(state.isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}