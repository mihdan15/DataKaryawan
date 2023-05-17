package com.mihdan.tugascrud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_update_data.*
import kotlinx.android.synthetic.main.activity_update_data.lakilaki
import kotlinx.android.synthetic.main.activity_update_data.perempuan

class UpdateData : AppCompatActivity() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNama: String? = null
    private var cekAlamat: String? = null
    private var cekJabatan: String? = null

    private fun setDataSpinner() {

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.jabatan, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        newjabatanSpinner.adapter = adapter

        // Mendapatkan jabatan dari data terkini
        val jabatan = intent.extras?.getString("dataJabatan")

        // Mendapatkan posisi jabatan dalam daftar
        val position = adapter.getPosition(jabatan)

        // Mengatur pilihan jabatan saat ini pada spinner
        newjabatanSpinner.setSelection(position)
    }


    private fun getJkel(): String {
        var gender = ""
        if (lakilaki.isChecked) {
            gender = "Laki-Laki"
        } else if (perempuan.isChecked) {
            gender = "Perempuan"
        }
        return gender
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        data
        setDataSpinner()
        update.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                cekNama = new_nama.getText().toString()
                cekAlamat = new_alamat.getText().toString()
                cekJabatan = newjabatanSpinner.selectedItem.toString()
                val getJkel: String = getJkel()

                if (isEmpty(cekNama!!) || isEmpty(cekAlamat!!) || isEmpty(cekJabatan!!) || isEmpty(getJkel!!)) {
                    Toast.makeText(this@UpdateData, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else {
                    val setdata_teman = data_teman()
                    setdata_teman.nama = new_nama.getText().toString()
                    setdata_teman.alamat = new_alamat.getText().toString()
                    setdata_teman.jabatan = newjabatanSpinner.selectedItem.toString()
                    setdata_teman.jkel = getJkel()

                    updateTeman(setdata_teman)
                }
            }
        })
    }
    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }
    private val data: Unit
        private get() {
            val getNama = intent.extras!!.getString("dataNama")
            val getAlamat = intent.extras!!.getString("dataAlamat")
            val getJabatan = intent.extras!!.getString("dataJabatan")
            val getJkel = intent.extras!!.getString("dataJkel")

            new_nama!!.setText(getNama)
            new_alamat!!.setText(getAlamat)

            if (getJkel == "Laki-Laki") {
                lakilaki.isChecked = true
            } else if (getJkel == "Perempuan") {
                perempuan.isChecked = true
            }
        }

    private fun updateTeman(setdataTeman: data_teman) {
        val userID = auth!!.uid
        val getKey = intent.extras!!.getString("getPrimaryKey")
        database!!.child("Admin")
            .child(userID!!)
            .child("DataTeman")
            .child(getKey!!)
            .setValue(setdataTeman)
            .addOnSuccessListener {
                new_nama!!.setText("")
                new_alamat!!.setText("")
//                new_nohp!!.setText("")
                lakilaki.isChecked = false
                perempuan.isChecked = false

                Toast.makeText(this@UpdateData, "Data berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                Toast.makeText(this@UpdateData, "Data gagal diubah", Toast.LENGTH_SHORT).show()
            }
    }
}