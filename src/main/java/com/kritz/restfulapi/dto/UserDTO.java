package com.kritz.restfulapi.dto;

import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String nama;
    private String email;
    private String nomorTelepon;
    private String perusahaan;
    private String password;
    private String provinsi;
    private String kota;
    private String kelurahan;
    private String kecamatan;
    private String alamat;
    private String tanggalLahir;
    private String fcmToken;

    public boolean checkDTO() {
        trim();
        if (this.nama == null)
            throw new IllegalArgumentException("Nama Tidak Boleh Bernilai NULL");
        if (this.nomorTelepon == null)
            throw new IllegalArgumentException("Nomor Telepon Tidak Boleh Bernilai NULL");
        if (this.perusahaan == null)
            throw new IllegalArgumentException("Perusahaan Tidak Boleh Bernilai NULL");
        return this.nama != null &&
                this.nomorTelepon != null &&
                this.perusahaan != null && checkLength();
    }

    public boolean checkFullDTO() {
        if (this.password == null)
            throw new IllegalArgumentException("Password Tidak Boleh Bernilai NULL");
        if (this.fcmToken == null)
            throw new IllegalArgumentException("Token FCM Tidak Boleh Bernilai NULL");
        if (this.email == null)
            throw new IllegalArgumentException("Email Tidak Boleh Bernilai NULL");
        return checkDTO() && password != null && this.email != null && this.fcmToken != null;
    }

    public boolean checkLength() {
        boolean nama = Optional.ofNullable(this.nama).map(s -> s.length() <= 50).orElse(true);
        boolean email = Optional.ofNullable(this.email)
                .map(s -> s.length() <= 50 && s.matches("^[a-zA-Z0-9. _%+-]+@[a-zA-Z0-9. -]+\\.[a-zA-Z]{2,}$"))
                .orElse(true);
        boolean nomorTelepon = Optional.ofNullable(this.nomorTelepon)
                .map(s -> s.length() <= 255 && s.matches("^[0-9+]+$")).orElse(true);
        boolean perusahaan = Optional.ofNullable(this.perusahaan).map(s -> s.length() <= 255).orElse(true);
        boolean provinsi = Optional.ofNullable(this.provinsi).map(s -> s.length() <= 255).orElse(true);
        boolean kota = Optional.ofNullable(this.kota).map(s -> s.length() <= 255).orElse(true);
        boolean alamat = Optional.ofNullable(this.alamat).map(s -> s.length() <= 255).orElse(true);
        boolean kelurahan = Optional.ofNullable(this.kelurahan).map(s -> s.length() <= 255).orElse(true);
        boolean kecamatan = Optional.ofNullable(this.kecamatan).map(s -> s.length() <= 255).orElse(true);
        boolean tanggalLahir = Optional.ofNullable(this.tanggalLahir).map(s -> dateCheck(s))
                .orElse(true);

        if (!nama)
            throw new IllegalArgumentException("Nama Tidak Valid atau Melewati Batas Karakter");
        if (!email)
            throw new IllegalArgumentException("Email Tidak Valid atau Melewati Batas Karakter");
        if (!nomorTelepon)
            throw new IllegalArgumentException("Nomor Telepon Tidak Valid atau Melewati Batas Karakter");
        if (!perusahaan)
            throw new IllegalArgumentException("Perusahaan Tidak Valid atau Melewati Batas Karakter");
        if (!provinsi)
            throw new IllegalArgumentException("Provinsi Tidak Valid atau Melewati Batas Karakter");
        if (!kota)
            throw new IllegalArgumentException("Kota Tidak Valid atau Melewati Batas Karakter");
        if (!alamat)
            throw new IllegalArgumentException("Alamat Tidak Valid atau Melewati Batas Karakter");
        if (!kelurahan)
            throw new IllegalArgumentException("Kelurahan Tidak Valid atau Melewati Batas Karakter");
        if (!kecamatan)
            throw new IllegalArgumentException("Kecamatan Tidak Valid atau Melewati Batas Karakter");
        if (!tanggalLahir)
            throw new IllegalArgumentException("Tanggal Lahir Tidak Valid");

        return nama &&
                email &&
                nomorTelepon &&
                perusahaan &&
                provinsi &&
                kota &&
                tanggalLahir &&
                kelurahan &&
                kecamatan &&
                alamat;
    }

    public boolean dateCheck(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public void trim() {
        this.nama = Optional.ofNullable(this.nama).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.email = Optional.ofNullable(this.email).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.nomorTelepon = Optional.ofNullable(this.nomorTelepon).map(String::trim).filter(s -> !s.isBlank())
                .orElse(null);
        this.perusahaan = Optional.ofNullable(this.perusahaan).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.password = Optional.ofNullable(this.password).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.provinsi = Optional.ofNullable(this.provinsi).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.kota = Optional.ofNullable(this.kota).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.tanggalLahir = Optional.ofNullable(this.tanggalLahir).map(String::trim).filter(s -> !s.isBlank())
                .orElse(null);
        this.alamat = Optional.ofNullable(this.alamat).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.kelurahan = Optional.ofNullable(this.kelurahan).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.kecamatan = Optional.ofNullable(this.kecamatan).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.fcmToken = Optional.ofNullable(this.fcmToken).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
    }
}
