package com.kritz.restfulapi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pengisian_stock")
public class PengisianStock {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_toko", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_pengisianStockToko"))
    private Toko idToko;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "edited_at", nullable = false)
    private LocalDateTime editedAt;

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;
    
}
