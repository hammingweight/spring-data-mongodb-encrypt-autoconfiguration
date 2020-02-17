package com.hammingweight.spring.data.mongodb.encrypt.configuration;


import com.bol.secure.Encrypted;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Widget {

    @Id
    private int id;

    @Encrypted
    private String description;

    private int price;
}
