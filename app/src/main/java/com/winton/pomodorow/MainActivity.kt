package com.winton.pomodorow

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.winton.pomodorow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var timerMain: CountDownTimer
    private lateinit var timerBreak: CountDownTimer
    private lateinit var timerTen: CountDownTimer

    private var contador:Int = 0
    private var m1:Long = 5000//1500000 //25 minutos
    private var m2:Long = 300000 //5 minutos

    private var player: MediaPlayer? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtContador.text = contador.toString()

        timerMain = object : CountDownTimer(m1+500,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minuto :Int = (millisUntilFinished/60000).toInt()
                val segundo : Int = (millisUntilFinished%60000/1000).toInt()
                val timer = "$minuto:$segundo"
                binding.txtTimer.text = timer

            }

            override fun onFinish() {
                playNotificationSound()
                contador+=1
                binding.txtContador.text = contador.toString()
                if(contador % 4 == 0){
                    val builder = MaterialAlertDialogBuilder(this@MainActivity)
                    builder.setMessage("É recomendado um descanso de 10 minutos. Aceita ?")
                        .setPositiveButton("SIM"){dialog,_->
                            /*setar o timer de 10min*/
                            dialog.dismiss()
                            timerTen.start()
                        }
                        .setNegativeButton("NÃO"){dialog,_->
                            //continuar o timer comum de 5  min
                            dialog.dismiss()
                            timerBreak.start()
                        }
                        .create().show()
                }
                else
                    timerBreak.start()

            }
        }
        
        timerBreak = object : CountDownTimer(m2+500,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minuto :Int = (millisUntilFinished/60000).toInt()
                val segundo : Int = (millisUntilFinished%60000/1000).toInt()
                val timer = "$minuto:$segundo"
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
                val timer = "$minuto:$segundo"
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
}