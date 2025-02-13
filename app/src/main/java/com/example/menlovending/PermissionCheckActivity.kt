import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.menlovending.ui.widgets.MenloVendingScaffold

// Required permissions for API level 30 and below
val permissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.BLUETOOTH,
    Manifest.permission.BLUETOOTH_ADMIN
)

// Required permissions for API level 31 and above
@RequiresApi(Build.VERSION_CODES.S)
val permissionsS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT
)

// Map of permission names
val permissionNames = mapOf(
    Manifest.permission.ACCESS_FINE_LOCATION to "Location",
    Manifest.permission.BLUETOOTH to "Bluetooth",
    Manifest.permission.BLUETOOTH_ADMIN to "Bluetooth Admin",
    Manifest.permission.BLUETOOTH_SCAN to "Bluetooth Scan",
    Manifest.permission.BLUETOOTH_CONNECT to "Bluetooth Connect"
)

@Composable
fun PermissionCheckScreen(
    context: Context,
    navHostController: NavHostController
) {

    // Keep track of permissions granted, missing, and permanently denied
    val permissionsGranted = remember { mutableStateOf(false) }
    val missingPermissions = remember { mutableStateOf(emptyArray<String>()) }
    val permanentlyDenied = remember { mutableStateOf(emptyArray<String>()) }

    // Keep track of navigation stack (to prevent popping too many screens)
    val navigationComplete = remember { mutableStateOf(false) }

    // Create a permission launcher (to request NON-PERMANENTLY DENIED permissions)
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.containsValue(false)) {
            // Not all permissions granted
            permissionsGranted.value = false

            // Re-check which are missing and permanently denied
            missingPermissions.value = getMissingPermissions(context)
            permanentlyDenied.value = getBlockedPermissions(context)
        } else {
            // All permissions granted
            permissionsGranted.value = true
        }
    }

    // Create disposable effect to check permissions when the screen is opened
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val validEvents = setOf(Lifecycle.Event.ON_START, Lifecycle.Event.ON_RESUME)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event in validEvents) {
                permissionsGranted.value = checkForPermissions(context)
                missingPermissions.value = getMissingPermissions(context)
                permanentlyDenied.value = getBlockedPermissions(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Pop back to previous screen if permissions are granted
    if (permissionsGranted.value) {
        if (!navigationComplete.value) {
            navigationComplete.value = true
            navHostController.popBackStack()
        }
    } else {
        MenloVendingScaffold ("Permission Check") {
            Column(modifier = Modifier.fillMaxSize().padding(it), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                // Subtitle
                Text("Missing Permissions:", style = MaterialTheme.typography.headlineSmall)

                // List of missing permissions
                missingPermissions.value.forEach { permission ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(("- " + permissionNames[permission]), style = MaterialTheme.typography.bodyLarge)
                }

                // Button to request permissions
                Spacer(modifier = Modifier.height(8.dp))
                if (permanentlyDenied.value.isNotEmpty()) {
                    // Open settings if permissions are permanently denied
                    Button(onClick = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }) {
                        Text("Open Settings")
                    }
                } else {
                    // No permissions are permanently denied
                    Button(onClick = { requestPermissionLauncher.launch(missingPermissions.value) }) {
                        Text("Request Permissions")
                    }
                }
            }
        }
    }
}

fun checkForPermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        permissionsS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    } else {
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}

fun getMissingPermissions(context: Context): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        permissionsS.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
    } else {
        permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
    }
}

fun getBlockedPermissions(context: Context): Array<String> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        permissionsS.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED &&
                    !shouldShowRequestPermissionRationale(context as Activity, it)
        }.toTypedArray()
    } else {
        permissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED &&
                    !shouldShowRequestPermissionRationale(context as Activity, it)
        }.toTypedArray()
    }
}