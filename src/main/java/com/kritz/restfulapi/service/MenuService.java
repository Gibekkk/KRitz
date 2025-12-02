package com.kritz.restfulapi.service;

import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kritz.restfulapi.dto.LangkahDTO;
import com.kritz.restfulapi.dto.MenuDTO;
import com.kritz.restfulapi.dto.lists.BahanList;
import com.kritz.restfulapi.model.Bahan;
import com.kritz.restfulapi.model.BahanResep;
import com.kritz.restfulapi.model.LangkahResep;
import com.kritz.restfulapi.model.Menu;
import com.kritz.restfulapi.model.Pricelist;
import com.kritz.restfulapi.model.Toko;
import com.kritz.restfulapi.model.enums.Kategori;
import com.kritz.restfulapi.repository.BahanRepository;
import com.kritz.restfulapi.repository.BahanResepRepository;
import com.kritz.restfulapi.repository.LangkahResepRepository;
import com.kritz.restfulapi.repository.MenuRepository;
import com.kritz.restfulapi.repository.PricelistRepository;

import jakarta.transaction.Transactional;

@Service
public class MenuService {

    @Autowired
    private BahanRepository bahanRepository;

    @Autowired
    private BahanResepRepository bahanResepRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PricelistRepository pricelistRepository;

    @Autowired
    private LangkahResepRepository langkahResepRepository;

    @Autowired
    private ImageService imageService;

    @Value("${storage.upload-dir}/menu/")
    private String pathToFoto;

    @Transactional
    public void deleteMenu(Menu menu) {
        menu.getIdToko().getListMenu().remove(menu);
        menu.setIdToko(null);
        menuRepository.delete(menu);
    }

    public void flagDeleteMenu(Menu menu) {
        menu.setDeletedAt(LocalDateTime.now());
        menuRepository.save(menu);
    }

    public String getImage(Menu menu) {
        return imageService.getImage(menu.getImageUrl());
    }

    public int getMenuStock(Menu menu) {
        int stock = 100;
        for (BahanResep bahanResep : menu.getListBahanResep()) {
            Bahan bahan = bahanResep.getIdBahan();
            int bahanStock = (int) Math.floor((double) bahan.getIdStock().getStock() / bahanResep.getJumlah());
            if (bahanStock < stock) {
                stock = bahanStock;
            }
        }
        return stock;
    }


    public Menu addMenu(Toko toko, MenuDTO menuDTO) {
        Menu menu = new Menu();
        menu.setIdToko(toko);
        menu.setNama(menuDTO.getNama());
        menu.setDeskripsi(menuDTO.getDeskripsi());
        menu.setKategori(Kategori.fromString(menuDTO.getKategori()));
        menu.setCreatedAt(LocalDateTime.now());
        menu.setEditedAt(LocalDateTime.now());
        menu.setImageUrl(imageService.saveImage(menuDTO.getImage(), pathToFoto));
        menu = menuRepository.save(menu);

        Pricelist pricelist = new Pricelist();
        pricelist.setIdMenu(menu);
        pricelist.setHarga(menuDTO.getHarga());
        pricelist = pricelistRepository.save(pricelist);

        for (BahanList bahanList : menuDTO.getListBahan()) {
            Optional<Bahan> bahanOpt = bahanRepository.findById(bahanList.getIdBahan());
            if (bahanOpt.isPresent() && bahanOpt.get().getIdToko().getId().equals(toko.getId())) {
                BahanResep bahanResep = new BahanResep();
                bahanResep.setIdMenu(menu);
                bahanResep.setIdBahan(bahanOpt.get());
                bahanResep.setJumlah(bahanList.getJumlahBahan());
                bahanResepRepository.save(bahanResep);
            } else {
                throw new IllegalArgumentException("Bahan dengan ID " + bahanList.getIdBahan() + " tidak ditemukan.");
            }
        }

        for (LangkahDTO langkahDTO : menuDTO.getListLangkah()) {
            LangkahResep langkahResep = new LangkahResep();
            langkahResep.setIdMenu(menu);
            langkahResep.setDeskripsi(langkahDTO.getDeskripsi());
            langkahResep.setUrutan(langkahDTO.getUrutan());
            langkahResepRepository.save(langkahResep);
        }
        return menu;
    }

    @Transactional
    public Menu editMenu(MenuDTO menuDTO, Menu menu, Toko toko) {
        imageService.deleteImage(menu.getImageUrl());

        menu.setNama(menuDTO.getNama());
        menu.setDeskripsi(menuDTO.getDeskripsi());
        menu.setKategori(Kategori.fromString(menuDTO.getKategori()));
        menu.setEditedAt(LocalDateTime.now());
        menu.setImageUrl(imageService.saveImage(menuDTO.getImage(), pathToFoto));
        menu = menuRepository.save(menu);

        Pricelist pricelist = menu.getIdPricelist();
        pricelist.setIdMenu(menu);
        pricelist.setHarga(menuDTO.getHarga());
        pricelist = pricelistRepository.save(pricelist);

        bahanResepRepository.deleteAll(menu.getListBahanResep());
        menu.getListBahanResep().clear();

        for (BahanList bahanList : menuDTO.getListBahan()) {
            Optional<Bahan> bahanOpt = bahanRepository.findById(bahanList.getIdBahan());
            if (bahanOpt.isPresent() && bahanOpt.get().getIdToko().getId().equals(toko.getId())) {
                BahanResep bahanResep = new BahanResep();
                bahanResep.setIdMenu(menu);
                bahanResep.setIdBahan(bahanOpt.get());
                bahanResep.setJumlah(bahanList.getJumlahBahan());
                menu.getListBahanResep().add(bahanResep);
                bahanResepRepository.save(bahanResep);
            } else {
                throw new IllegalArgumentException("Bahan dengan ID " + bahanList.getIdBahan() + " tidak ditemukan.");
            }
        }

        langkahResepRepository.deleteAll(menu.getListLangkahResep());
        menu.getListLangkahResep().clear();

        for (LangkahDTO langkahDTO : menuDTO.getListLangkah()) {
            LangkahResep langkahResep = new LangkahResep();
            langkahResep.setIdMenu(menu);
            langkahResep.setDeskripsi(langkahDTO.getDeskripsi());
            langkahResep.setUrutan(langkahDTO.getUrutan());
            menu.getListLangkahResep().add(langkahResep);
            langkahResepRepository.save(langkahResep);
        }
        return menu;
    }

}
