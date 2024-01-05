package com.example.emergencymedicalcare

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.emergencymedicalcare.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goInputActivityButton.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java) // 명시적 인텐트
//            intent.putExtra("intentMessage","응급의료정보")
            startActivity(intent)
        }

        // 데이터 삭제
        binding.deleteButton.setOnClickListener{
            deleteData()
        }

        // 전화 앱 이동
        binding.emergencyContactLayer.setOnClickListener {
            with(Intent(Intent.ACTION_VIEW)) {
                val phoneNumber = binding.CallValueTextView.text.toString()
                    .replace("-","")
                data = Uri.parse("tel:$phoneNumber")
                startActivity(this)
            } // 암시적 인텐트
        }
    }

    private fun deleteData() {
        with(getSharedPreferences(USER_INFORMATION, MODE_PRIVATE).edit()){
            clear()
            apply() // *필수
        }
        getDataUiUpdate()
        Toast.makeText(this,"초기화를 완료하였습니다.", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        getDataUiUpdate()
    }

    private fun getDataUiUpdate() {
        // 가능한 함수에 하나의 기능을 구성하도록 권장
       with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE)){
           binding.nameValueTextView.text = getString(NAME,"미정")
           binding.birthdateValueTextView.text= getString(BIRTHDATE,"미정")
           binding.BloodTypeValueTextView.text= getString(BLOOD_TYPE,"미정")
           binding.CallValueTextView.text= getString(EMERGENCY_CONTACT,"미정")

           val warning = getString(WARNING,"")
           binding.CautionTextView.isVisible = warning.isNullOrEmpty().not()
           binding.CautionValueTextView.isVisible = warning.isNullOrEmpty().not()

           if (!warning.isNullOrEmpty()){
               binding.CautionValueTextView.text = warning
           }

       }
    }
}