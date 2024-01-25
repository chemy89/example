package kr.jeongsejong.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.jeongsejong.feature.gate.GateActivity
import kr.jeongsejong.feature.main.MainActivity

@AndroidEntryPoint
class ParkingActivity : ComponentActivity() {

    private val viewModel by viewModels<ParkingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val targetActivity = if (viewModel.isLoggedIn) {
            MainActivity::class.java
        } else {
            GateActivity::class.java
        }
        val intent = Intent(this, targetActivity)
        startActivity(intent)

        finish()

    }
}