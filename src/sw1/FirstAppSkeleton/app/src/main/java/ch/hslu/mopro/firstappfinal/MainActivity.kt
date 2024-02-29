package ch.hslu.mopro.firstappfinal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import ch.hslu.mopro.firstappfinal.lifecyclelog.LifecycleLogActivity


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        const val QUESTION = "question"
        const val ANSWER = "answer"
    }

    private val openQuestionActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            //TODO check if the result is ok and set the content to the textview

            if (result.resultCode == Activity.RESULT_OK) {
                val textView = findViewById<TextView>(R.id.main_textView_result);
                var answer = resources.getString(R.string.main_text_gotAnswer);

                result.data?.let { data: Intent ->
                    data.extras?.let { extra: Bundle ->
                        answer = answer.plus(extra.getString(ANSWER));
                    }
                }

                textView.text = answer;
            }
        }

    // Fires when the system first creates the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        // savedInstanceState = null; save the state of the application in a bundle
        // can be passed back to onCreate if the activity needs to be recreated (e.g., orientation change)
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.main_button_logActivity).setOnClickListener { startLogActivity() }
        findViewById<Button>(R.id.main_button_startBrowser).setOnClickListener { startBrowser() }
        findViewById<Button>(R.id.main_button_questionActivity).setOnClickListener { startQuestionActivity() }

    }


    private fun startLogActivity() {
        // TODO: start LifecylceLogActivity mit LifecycleLogFragment

        val intent = Intent(this, LifecycleLogActivity::class.java)
        startActivity(intent)
    }

    private fun startBrowser() {
        // TODO: start Browser with http://www.hslu.ch

        val hsluBrowserCall = Intent();
        hsluBrowserCall.action = Intent.ACTION_VIEW;
        hsluBrowserCall.data = Uri.parse("http://www.hslu.ch");
        startActivity(hsluBrowserCall);
    }

    private fun startQuestionActivity() {
        // TODO: launch QuestionActivity with Intent

        openQuestionActivity.launch(
            Intent(this, QuestionActivity::class.java).apply {
                putExtra(QUESTION, "Wie läufts?")
            }
        )
    }
}