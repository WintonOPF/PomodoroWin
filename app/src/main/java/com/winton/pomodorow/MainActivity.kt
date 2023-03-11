package com.winton.pomodorow

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

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtContador.text = contador.toString()

        timerMain = object : CountDownTimer(/*1500000*/5000+500,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minuto :Int = (millisUntilFinished/60000).toInt()
                val segundo : Int = (millisUntilFinished%60000/1000).toInt()
                val timer : String = "$minuto:$segundo"
                binding.txtTimer.text = timer

            }

            override fun onFinish() {
                contador+=1
                binding.txtContador.text = contador.toString()
                if(contador == 4){
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
        
        timerBreak = object : CountDownTimer(10000,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minuto :Int = (millisUntilFinished/60000).toInt()
                val segundo : Int = (millisUntilFinished%60000/1000).toInt()
                val timer : String = "$minuto:$segundo"
                binding.txtTimer.text = timer
            }

            override fun onFinish() {
                timerMain.start()
            }
        }

        timerTen = object : CountDownTimer(600000,1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minuto :Int = (millisUntilFinished/60000).toInt()
                val segundo : Int = (millisUntilFinished%60000/1000).toInt()
                val timer : String = "$minuto:$segundo"
                binding.txtTimer.text = timer
            }

            override fun onFinish() {
                timerMain.start()
            }
        }
        
        binding.btnIniciar.setOnClickListener {
            timerMain.start()
        }

    }

}