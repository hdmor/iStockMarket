package i.stock.market

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import i.stock.market.presentation.NavGraphs
import i.stock.market.ui.theme.IStockMarketTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IStockMarketTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Your API key is: 7VI72827RPA74E6K.
                    // https://www.alphavantage.co/
                    // https://www.alphavantage.co/query?function=OVERVIEW&symbol=IBM&apikey=7VI72827RPA74E6K
                    DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }
}