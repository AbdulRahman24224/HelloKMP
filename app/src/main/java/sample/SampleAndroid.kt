package sample

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import kotlinx.android.synthetic.main.activity_main.*

actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

private lateinit var manager: SplitInstallManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sample().checkMe()
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = hello()
        manager = SplitInstallManagerFactory.create(this)

        button.setOnClickListener {
            Toast.makeText(this, "${manager.installedModules}", Toast.LENGTH_SHORT).show()
            if (manager.installedModules.contains("food")) {
                Intent().setClassName(BuildConfig.APPLICATION_ID, "sample.ondemand.food.FoodActivity")
                    .also { startActivity(it) }

            } else  Toast.makeText(this, "food not installed", Toast.LENGTH_SHORT).show()

        }

        button2.setOnClickListener {

            val request = SplitInstallRequest.newBuilder()
                .addModule("food")
                .build()

            manager.startInstall(request)
                .addOnCompleteListener { Toast.makeText(this, "food complete", Toast.LENGTH_SHORT).show() }
                .addOnSuccessListener { Toast.makeText(this, "food installed", Toast.LENGTH_SHORT).show() }
                .addOnFailureListener {  Toast.makeText(this, "food failure", Toast.LENGTH_SHORT).show() }

        }

        button3.setOnClickListener {
            manager.deferredUninstall(listOf("food")).addOnSuccessListener {
                Toast.makeText(this, "food removed", Toast.LENGTH_SHORT).show()
            }  }
    }
}