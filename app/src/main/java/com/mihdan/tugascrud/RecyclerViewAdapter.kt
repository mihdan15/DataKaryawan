package com.mihdan.tugascrud

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val listdata_teman: ArrayList<data_teman>, context: Context) :
 RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){
     private val context: Context

     inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         val Nama : TextView
         val Alamat : TextView
         val NoHP : TextView
         val Jkel : TextView
         val ListItem : LinearLayout

         init {
             Nama = itemView.findViewById(R.id.nama)
             Alamat = itemView.findViewById(R.id.alamat)
             NoHP = itemView.findViewById(R.id.no_hp)
             Jkel = itemView.findViewById(R.id.jkel)
             ListItem = itemView.findViewById(R.id.list_item)
         }


     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val V:View = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false)
        return ViewHolder(V)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Nama: String? = listdata_teman.get(holder.getAdapterPosition()).nama
        val Alamat: String? = listdata_teman.get(holder.getAdapterPosition()).alamat
        val NoHP: String? = listdata_teman.get(holder.getAdapterPosition()).jabatan
        val Jkel: String? = listdata_teman.get(holder.getAdapterPosition()).jkel

        holder.Nama.text = "Nama: $Nama"
        holder.Alamat.text = "NIP: $Alamat"
        holder.NoHP.text = "Jabatan: $NoHP"
        holder.Jkel.text = "Jkel: $Jkel"

        holder.ListItem.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                holder.ListItem.setOnLongClickListener{ view ->
                    val action = arrayOf("Update","Delete")
                    val alert : AlertDialog.Builder = AlertDialog.Builder(view.context)
                    alert.setItems(action, DialogInterface.OnClickListener{ dialog, i ->
                        when (i) { 0 -> {
                            val bundle = Bundle()
                            bundle.putString("dataNama", listdata_teman[holder.getAdapterPosition()].nama)
                            bundle.putString("dataAlamat", listdata_teman[holder.getAdapterPosition()].alamat)
                            bundle.putString("dataJabatan", listdata_teman[holder.getAdapterPosition()].jabatan)
                            bundle.putString("dataJkel", listdata_teman[holder.getAdapterPosition()].jkel)
                            bundle.putString("getPrimaryKey", listdata_teman[holder.getAdapterPosition()].key)

                            val intent = Intent(view.context, UpdateData::class.java)
                            intent.putExtras(bundle)
                            context.startActivity(intent)
                        } 1 -> {
                            listener?.onDeleteData(listdata_teman.get(holder.getAdapterPosition()), holder.getAdapterPosition())
                            deleteData(holder.adapterPosition)
                        }
                        }
                    })
                    alert.create()
                    alert.show()
                    true
                }

                return true
            }
        })

    }

    override fun getItemCount(): Int {
        return listdata_teman.size
    }

    var listener : dataListener? = null

    init {
        this.context = context
        if (context is dataListener) {
            this.listener = context
        }
    }

    interface dataListener {
        fun onDeleteData (data: data_teman?, position: Int)


    }

    fun deleteData(position: Int) {
        listdata_teman.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listdata_teman.size)
    }

}