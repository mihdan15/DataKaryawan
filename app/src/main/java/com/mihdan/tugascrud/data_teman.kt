package com.mihdan.tugascrud

class data_teman {

    var nama: String? = null
    var alamat: String? = null
    var jabatan:String? = null
    var jkel:String? = null
    var key: String? = null

    constructor()

    constructor(nama: String?, alamat:String?, jabatan:String?, jkel:String?){
        this.nama = nama
        this.alamat = alamat
        this.jabatan = jabatan
        this.jkel = jkel
    }
}