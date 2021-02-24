package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var startButton:Button
    lateinit var coutdownDisplay:TextView
    lateinit var WorkValue:TextView
    lateinit var BreakValue:TextView
    lateinit var timer_Break:CountDownTimer
    lateinit var timer_Work:CountDownTimer


    var amountOfReps = 1
    var IsWorkTimerGoing = 0
    var IsBreakTimerGoing = 0
    var count = 0
    var timeToCountDownInMs_Work = 5000L
    var timeToCountDownInMs_Break = 5000L
    val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        WorkValue = findViewById(R.id.WorktextView)
        val seekbar1 = findViewById<View>(R.id.WorkseekBar) as SeekBar
        seekbar1.min = 1
        seekbar1.max = 300
        seekbar1.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                WorkValue.text = "Work: " + progress.toString() + " Min"
                timeToCountDownInMs_Work = progress * 60000L
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }

        })
        BreakValue = findViewById(R.id.BreaktextView)
        val seekbar2 = findViewById<View>(R.id.BreakseekBar) as SeekBar
        seekbar2.min = 0
        seekbar2.max = 60
        seekbar2.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                BreakValue.text = "Break: " + progress.toString() + " Min"
                timeToCountDownInMs_Break = progress * 60000L
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }

        })
        var reps = findViewById<EditText>(R.id.Reps)
        reps.setOnClickListener(){
            var value = reps.getText().toString().toInt()
            amountOfReps = value
        }

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            if (count == 0) {
                count = 1
                IsWorkTimerGoing = 1
                startCountDown(it)
            }
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }
    fun startCountDown(v: View){

        timer_Work = object : CountDownTimer(timeToCountDownInMs_Work,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                IsWorkTimerGoing = 0
                IsBreakTimerGoing = 1
                if(amountOfReps != 0){
                    amountOfReps -= 1
                }
                startCountDown(startButton)
            }
            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer_Break = object : CountDownTimer(timeToCountDownInMs_Break,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Pause er ferdig", Toast.LENGTH_SHORT).show()
                if(amountOfReps == 0){
                    count = 0
                    IsBreakTimerGoing = 0
                    IsWorkTimerGoing = 0
                }
                else{
                    IsWorkTimerGoing = 1
                    IsBreakTimerGoing = 0
                    startCountDown(startButton)
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        if(IsWorkTimerGoing == 1){
            timer_Work.start()
            println("Checked if IsWorkTimerGoing == 1")
            IsWorkTimerGoing = 0
        }
        if(IsBreakTimerGoing == 1){
            timer_Break.start()
            println("Checked if IsBreakTimerGoing == 1")
            IsBreakTimerGoing = 0
        }
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}