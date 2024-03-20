package ch.hslu.sw5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.hslu.sw5.bands.BandsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout_body, BandsFragment())
            .addToBackStack(null)
            .commit();
    }
}