package ovh.milkowski.mms_lab1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import kotlin.math.roundToInt
import android.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get handlers
        val inputHeight = findViewById<EditText>(R.id.inputHeight)
        val inputMass = findViewById<EditText>(R.id.inputWeight)
        val resultView = findViewById<TextView>(R.id.bmi)

        // handle about button click event
        val aboutEvent = findViewById<Button>(R.id.buttonAbout)
        aboutEvent.setOnClickListener{
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        // setup the alert builder
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.alert_header)
        builder.setMessage(R.string.alert_black)

        // add a button
        builder.setPositiveButton("OK", null)

        // create and show the alert dialog
        val dialog: AlertDialog = builder.create()

        // handle BMI value click event
        val showInfoEvent = findViewById<TextView>(R.id.bmi)
        showInfoEvent.setOnClickListener{
            dialog.show()
        }

        // handle Calculate button click event
        val buttonEvent = findViewById<Button>(R.id.buttonCalculate)
        buttonEvent.setOnClickListener{
            val kg = inputMass.text.toString().toDouble()
            val cm = inputHeight.text.toString().toDouble()

            var bmi: Double = kg / (cm / 100.0).pow(2.0)
            bmi = (bmi * 100.0).roundToInt() / 100.0;
            resultView.text = bmi.toString()

            when
            {
                bmi >= 30 -> {
                    resultView.setTextColor(Color.RED)
                    dialog.setMessage(getString(R.string.alert_red))
                }
                bmi >= 25 -> {
                    resultView.setTextColor(Color.YELLOW)
                    dialog.setMessage(getString(R.string.alert_yellow))
                }
                bmi >= 18.5 -> {
                    resultView.setTextColor(Color.GREEN)
                    dialog.setMessage(getString(R.string.alert_green))
                }
                bmi >= 13 -> {
                    resultView.setTextColor(Color.BLUE)
                    dialog.setMessage(getString(R.string.alert_blue))
                }
                else -> {
                    resultView.setTextColor(Color.BLACK)
                    dialog.setMessage(getString(R.string.alert_black))
                }
            }
        }
    }
}