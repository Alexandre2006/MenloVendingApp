import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.menlovending.ui.widgets.MenloVendingScaffold
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _currentStatus = mutableStateOf("Loading...")
    val currentStatus = _currentStatus

    private var isInitialized = false

    fun startLoading(context: Context, navController: NavHostController) {
        if (!isInitialized) {
            viewModelScope.launch {
                loadApp(
                    context = context,
                    updateProgress = { status ->
                        _currentStatus.value = status
                    },
                    navController = navController
                )
            }
            isInitialized = true
        }
    }
}

@Composable
fun LoadingScreen(context: Context, navController: NavHostController) {
    val viewModel: MainViewModel = viewModel()
    MenloVendingScaffold(content = {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(viewModel.currentStatus.value, style = MaterialTheme.typography.bodyLarge)
        }
    })
    LaunchedEffect(Unit) {
        viewModel.startLoading(context, navController)
    }
}

fun loadApp(updateProgress: (String) -> Unit, context: Context, navController: NavHostController) {
    // 1. Permission Check
    updateProgress("Checking for permissions...")
    if (!checkForPermissions(context)) {
        // Push permission check view
        navController.navigate("permission")
    }

    // Push permission check view

    // 2. Initialize Stripe Terminal
    updateProgress("Initializing Stripe Terminal...")
}