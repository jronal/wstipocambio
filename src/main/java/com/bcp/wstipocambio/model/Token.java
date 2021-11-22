package com.bcp.wstipocambio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.StringJoiner;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String inEstado;
    private String deIssuer;
    private String deSubject;
    private String deAudience;
    private Date feExpiration;
    private Date feNotBefore;
    private Date feIssuedAt;


}
