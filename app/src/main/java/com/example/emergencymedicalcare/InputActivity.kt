package com.example.emergencymedicalcare

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.emergencymedicalcare.databinding.ActivityInputBinding
import com.example.emergencymedicalcare.databinding.ActivityMainBinding

class InputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 혈액형 선택
        binding.bloodTypeSpinner.adapter = ArrayAdapter.createFromResource( // adapter를 통한 반복 지양
            this,
            R.array.blood_types,
            android.R.layout.simple_list_item_1
        )

        // 생년월일 캘린더
        binding.birthdatelayer.setOnClickListener{
            val listener  = OnDateSetListener{ _, year, month, datOfMonth ->
                 binding.birthdateTextView.text = "$year-${month.inc()}-$datOfMonth"
            }
            DatePickerDialog(
                this,
                listener,
                2000,
                1,
                1
            ).show()
        }

        // 초반 노출값 설정
        binding.warningEditText.isVisible = binding.warningCheckBox.isChecked

        // 주의 사항 노출 여부 체크에 따른 노출
        binding.warningCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.warningEditText.isVisible = isChecked
        }

        // 저장 버튼
        binding.saveButton.setOnClickListener{
            saveData()
            finish() // 저장 후 액티비티 종료
        }

//        val message = intent.getStringExtra("intentMessage") ?: "없음"
//        Log.d("intentMessage",message)
    }

    private fun saveData(){
        with(getSharedPreferences(USER_INFORMATION, Context.MODE_PRIVATE).edit()){
            putString(NAME, binding.nameEditText.text.toString())
            putString(BLOOD_TYPE,getBloodType())
            putString(EMERGENCY_CONTACT, binding.CallValueEditText.text.toString())
            putString(BIRTHDATE, binding.birthdateTextView.text.toString())
            putString(WARNING,getWarning())
            apply() // 저장되는 동안 사용자의 행동을 막지 않도록, 이외의 스레드에서 저장작업이 필요한 경우 apply() 활용
        }
        Toast.makeText(this,"저장을 완료했습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun getBloodType(): String {
        val bloodAlphabet = binding.bloodTypeSpinner.selectedItem.toString()
        val bloodSign = if(binding.bloodTypePlus.isChecked) "+" else "-"
        return "$bloodSign$bloodAlphabet"
    }

    private fun getWarning() : String{
        return if(binding.warningCheckBox.isChecked) binding.warningEditText.text.toString() else  ""
    }


}