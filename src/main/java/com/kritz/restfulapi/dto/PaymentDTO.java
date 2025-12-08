package com.kritz.restfulapi.dto;

import lombok.Setter;

import java.util.Optional;

import com.kritz.restfulapi.model.enums.TipePembayaran;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String namaPelanggan;
    private String tipePembayaran;


    public void checkDTO() {
        trim();
        if(this.namaPelanggan == null) throw new IllegalArgumentException("ID Menu Tidak Boleh Bernilai NULL");
        if(this.tipePembayaran == null) throw new IllegalArgumentException("Tipe Pembayaran Tidak Boleh Bernilai NULL");
        TipePembayaran.fromString(tipePembayaran);
    }

    public void checkLength() {
        boolean namaPelanggan = Optional.ofNullable(this.namaPelanggan)
                .map(s -> s.length() <= 25)
                .orElse(true);

        if (!namaPelanggan)
            throw new IllegalArgumentException("Nama Pelanggan Melewati Batas Karakter");
    }

    public void trim() {
        this.namaPelanggan = Optional.ofNullable(this.namaPelanggan).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
        this.tipePembayaran = Optional.ofNullable(this.tipePembayaran).map(String::trim).filter(s -> !s.isBlank()).orElse(null);
    }

}

