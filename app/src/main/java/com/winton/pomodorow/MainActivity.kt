package com.winton.pomodorow

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.winton.pomodorow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var timerMain: CountDownTimer
    private lateinit var timerBreak: CountDownTimer
    private lateinit var timerTen: CountDownTimer

    private var contador:Int = 0
    private var m2:Long = 300000 //5 minutos

    private var player: MediaPlayer? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()

        setupTimerMain(1500000)

        binding.txtTimer.setOnClickListener {
            timerMain.cancel()
            setupTimerDialog()
        }
        
        timerBreak = object : CountDownTimer(m2+500,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minuto :Int = (millisUntilFinished/60000).toInt()
                val segundo : Int = (millisUntilFinished%60000/1000).toInt()

                val minutoFormatado:String = if (minuto < 10){
                    "0$minuto"
                } else
                    minuto.toString()

                val segundoFormatado:String = if (segundo < 10){
                    "0$segundo"

                } else
                    segundo.toString()
                val timer = "$minutoFormatado:$segundoFormatado"
                binding.txtTimer.text = timer
            }

            override fun onFinish() {
                playNotificationSound()
                timerMain.start()
            }
        }

        timerTen = object : CountDownTimer(600000+500,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minuto :Int = (millisUntilFinished/60000).toInt()
                val segundo : Int = (millisUntilFinished%60000/1000).toInt()

                val minutoFormatado:String = if (minuto < 10){
                    "0$minuto"
                } else
                    minuto.toString()

                val segundoFormatado:String = if (segundo < 10){
                    "0$segundo"

                } else
                    segundo.toString()
                val timer = "$minutoFormatado:$segundoFormatado"
                binding.txtTimer.text = timer
            }

            override fun onFinish() {
                playNotificationSound()
                timerMain.start()
            }
        }
        
        binding.btnIniciar.setOnClickListener {

            timerMain.start()
            timerBreak.cancel()
        }

        binding.btnIntervalo.setOnClickListener {
            contador = 0
            binding.txtContador.text = contador.toString()
            timerBreak.start()
            timerMain.cancel()
        }
    }

    private fun setupTimerDialog() {
        val builder = MaterialAlertDialogBuilder(this@MainActivity)
        val view = layoutInflater.inflate(R.layout.change_timer_layout, null)
        builder.setView(view)
            .setPositiveButton("OK") { dialog, _ ->
                val editText = view.findViewById<EditText>(R.id.et_timer)
                if (editText.text.toString() != "" && editText.text.toString() != "0") {
                    setupTimerMain((editText.text.toString().toLong()) * 60 * 1000)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Preencha o campo corretamente!", Toast.LENGTH_LONG).show()
                }
            }
            .setNegativeButton("CANCELAR") { dialog, _ ->
                dialog.dismiss()
            }
            .create().show()
    }

    private fun setupTimerMain(valor :Long) {
        timerMain = object : CountDownTimer(valor + 500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minuto: Int = (millisUntilFinished / 60000).toInt()
                val segundo: Int = (millisUntilFinished % 60000 / 1000).toInt()

                val minutoFormatado: String = if (minuto < 10) {
                    "0$minuto"
                } else
                    minuto.toString()

                val segundoFormatado: String = if (segundo < 10) {
                    "0$segundo"

                } else
                    segundo.toString()
                val timer = "$minutoFormatado:$segundoFormatado"
                binding.txtTimer.text = timer

            }

            override fun onFinish() {
                playNotificationSound()
                contador += 1
                binding.txtContador.text = contador.toString()
                if (contador % 4 == 0) {
                    val builder = MaterialAlertDialogBuilder(this@MainActivity)
                    builder.setMessage("É recomendado um descanso de 10 minutos. Aceita?")
                        .setPositiveButton("SIM") { dialog, _ ->

                            dialog.dismiss()
                            timerTen.start()
                        }
                        .setNegativeButton("NÃO") { dialog, _ ->

                            dialog.dismiss()
                            timerBreak.start()
                        }
                        .create().show()
                } else
                    timerBreak.start()

            }
        }
    }

    private fun playNotificationSound(){
        try{
            val sound = Uri.parse("android.resource://com.winton.pomodorow/"+R.raw.som_pomodoro)
            player = MediaPlayer.create(this,sound)
            player?.isLooping = false
            player?.start()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    private fun setupUI(){
        binding.txtContador.text = contador.toString()
        val timer = (1500000/60000).toString() + ":" + "00"
        binding.txtTimer.text = timer
    }
}