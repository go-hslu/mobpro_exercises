package ch.hslu.sw3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.hslu.sw3.overview.OverviewFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout_fragmentContainer, OverviewFragment())
            .commit()
    }
}