package com.deni.kameraktp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Deni Supriyatna on 10/03/2020.
 * Email : denisupriyatna01@gmail.com
 */
public class Data {
    @SerializedName("id_event")
    @Expose
    private Integer idEvent;
    @SerializedName("judul_event")
    @Expose
    private String judulEvent;
    @SerializedName("nama_tipe")
    @Expose
    private String namaTipe;
    @SerializedName("tgl_mulai")
    @Expose
    private String tglMulai;
    @SerializedName("tgl_selesai")
    @Expose
    private String tglSelesai;
    @SerializedName("jam_mulai")
    @Expose
    private String jamMulai;
    @SerializedName("jam_selesai")
    @Expose
    private String jamSelesai;
    @SerializedName("flyer")
    @Expose
    private String flyer;
    @SerializedName("rundown")
    @Expose
    private String rundown;
    @SerializedName("harga_early_bird_anggota")
    @Expose
    private Integer hargaEarlyBirdAnggota;
    @SerializedName("harga_early_bird_na")
    @Expose
    private Integer hargaEarlyBirdNa;
    @SerializedName("harga_reguler_anggota")
    @Expose
    private Integer hargaRegulerAnggota;
    @SerializedName("harga_reguler_na")
    @Expose
    private Integer hargaRegulerNa;
    @SerializedName("deskripsi_event")
    @Expose
    private String deskripsiEvent;
    @SerializedName("tgl_dibuat")
    @Expose
    private String tglDibuat;

    public Integer getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
    }

    public String getJudulEvent() {
        return judulEvent;
    }

    public void setJudulEvent(String judulEvent) {
        this.judulEvent = judulEvent;
    }

    public String getNamaTipe() {
        return namaTipe;
    }

    public void setNamaTipe(String namaTipe) {
        this.namaTipe = namaTipe;
    }

    public String getTglMulai() {
        return tglMulai;
    }

    public void setTglMulai(String tglMulai) {
        this.tglMulai = tglMulai;
    }

    public String getTglSelesai() {
        return tglSelesai;
    }

    public void setTglSelesai(String tglSelesai) {
        this.tglSelesai = tglSelesai;
    }

    public String getJamMulai() {
        return jamMulai;
    }

    public void setJamMulai(String jamMulai) {
        this.jamMulai = jamMulai;
    }

    public String getJamSelesai() {
        return jamSelesai;
    }

    public void setJamSelesai(String jamSelesai) {
        this.jamSelesai = jamSelesai;
    }

    public String getFlyer() {
        return flyer;
    }

    public void setFlyer(String flyer) {
        this.flyer = flyer;
    }

    public String getRundown() {
        return rundown;
    }

    public void setRundown(String rundown) {
        this.rundown = rundown;
    }

    public Integer getHargaEarlyBirdAnggota() {
        return hargaEarlyBirdAnggota;
    }

    public void setHargaEarlyBirdAnggota(Integer hargaEarlyBirdAnggota) {
        this.hargaEarlyBirdAnggota = hargaEarlyBirdAnggota;
    }

    public Integer getHargaEarlyBirdNa() {
        return hargaEarlyBirdNa;
    }

    public void setHargaEarlyBirdNa(Integer hargaEarlyBirdNa) {
        this.hargaEarlyBirdNa = hargaEarlyBirdNa;
    }

    public Integer getHargaRegulerAnggota() {
        return hargaRegulerAnggota;
    }

    public void setHargaRegulerAnggota(Integer hargaRegulerAnggota) {
        this.hargaRegulerAnggota = hargaRegulerAnggota;
    }

    public Integer getHargaRegulerNa() {
        return hargaRegulerNa;
    }

    public void setHargaRegulerNa(Integer hargaRegulerNa) {
        this.hargaRegulerNa = hargaRegulerNa;
    }

    public String getDeskripsiEvent() {
        return deskripsiEvent;
    }

    public void setDeskripsiEvent(String deskripsiEvent) {
        this.deskripsiEvent = deskripsiEvent;
    }

    public String getTglDibuat() {
        return tglDibuat;
    }

    public void setTglDibuat(String tglDibuat) {
        this.tglDibuat = tglDibuat;
    }
}
