package com.example.privatepr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientDto {
    private int id;

    @NotEmpty(message = "{validation.hotelBook.client.name.not-empty}")
    @NotNull(message = "{validation.hotelBook.client.name.not-null}")
    @Size(min = 3, max = 50, message = "{validation.hotelBook.client.name.size}")
    private String name;

    @NotEmpty(message = "{validation.hotelBook.client.login.not-empty}")
    @NotNull(message = "{validation.hotelBook.client.login.not-null}")
    @Size(min = 3, max = 50, message = "{validation.hotelBook.client.login.size}")
    private String login;


    @NotNull(message = "{validation.hotelBook.client.birthdate.not-null}")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthdate;
}
