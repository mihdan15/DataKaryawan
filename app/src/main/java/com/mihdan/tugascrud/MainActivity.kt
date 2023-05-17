package com.mihdan.tugascrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mihdan.tugascrud.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth:FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logout.setOnClickListener(this)
        save.setOnClickListener(this)
        show_data.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
        setDataSpinner()
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private fun setDataSpinner() {

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.jabatan, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jabatanSpinner.adapter = adapter
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

    override fun onClick(v: View) {
        when (v.getId()) {
            R.id.save -> {
                val getUserID = auth!!.currentUser!!.uid
                val database = FirebaseDatabase.getInstance()

                val getNama: String = nama.getText().toString()
                val getAlamat: String = alamat.getText().toString()
                val getJabatan: String = jabatanSpinner.selectedItem.toString()
                val getJkel: String = getJkel()

                val getReference: DatabaseReference
                getReference = database.reference



                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getJabatan) || isEmpty(getJkel)){
                    Toast.makeText(this@MainActivity, "Data Tidak Boleh Ada Yang Ksosong", Toast.LENGTH_SHORT).show()
                } else {
                    getReference.child("Admin").child(getUserID).child("DataTeman").push()
                        .setValue(data_teman(getNama, getAlamat, getJabatan, getJkel))
                        .addOnCompleteListener(this) {
                            nama.setText("")
                            alamat.setText("")
                            jabatanSpinner.setSelection(0)
//                            no_hp.setText("")
                            lakilaki.isChecked = false
                            perempuan.isChecked = false
                            Toast.makeText(this@MainActivity, "Data Tersimpan", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            R.id.logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(object :OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            Toast.makeText(this@MainActivity, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                            intent = Intent(applicationContext, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })
            }

            R.id.show_data -> {
                startActivity(Intent(this@MainActivity, MyListData::class.java))
            }
        }
    }
}