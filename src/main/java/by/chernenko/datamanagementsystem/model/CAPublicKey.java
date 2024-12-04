package by.chernenko.datamanagementsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ca_public_keys")
public class CAPublicKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "key_name")
    private String name;

    @Column(name = "expiration")
    private int expiration;

    @Column(name = "exponent")
    private int exponent;

    @Column(name = "hash")
    private String hash;

    @Column(name = "index")
    private String index;

    @Column(name = "modulus", length = 512)
    private String modulus;

    @Column(name = "length")
    private String length;

    @Column(name = "rid")
    private String rid;
}
