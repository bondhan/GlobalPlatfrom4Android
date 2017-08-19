package com.bondhan.research.dao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Created by bondhan on 31/07/17.
 */

@Entity(indexes = {
        @Index(value = "CardName, last_modified_date DESC", unique = true)
})

public class GlobalPlatformCard {

    @NotNull
    @Id
    @Unique
    private String CardName;

    @NotNull
    private String KeyKmcAC;
    private String KeyKmcMac;
    private String KeyKmcDek;
    private String CMAid;
    private java.util.Date last_modified_date;

    @Convert(converter = ScpModeConverter.class, columnType = Integer.class)
    private ScpModeEnum scp_mode;

    @Convert(converter = ApduModeConverter.class, columnType = Integer.class)
    private ApduModeEnum apdu_mode;

    @Convert(converter = KeyDerivationModeConverter.class, columnType = Integer.class)
    private KeyDerivationModeEnum keyDerivation_mode;

    @Generated(hash = 346204934)
    public GlobalPlatformCard() {
    }

    public GlobalPlatformCard(String card_name) {
        this.CardName = card_name;
    }

    @Generated(hash = 2141023051)
    public GlobalPlatformCard(@NotNull String CardName, @NotNull String KeyKmcAC, String KeyKmcMac,
            String KeyKmcDek, String CMAid, java.util.Date last_modified_date, ScpModeEnum scp_mode,
            ApduModeEnum apdu_mode, KeyDerivationModeEnum keyDerivation_mode) {
        this.CardName = CardName;
        this.KeyKmcAC = KeyKmcAC;
        this.KeyKmcMac = KeyKmcMac;
        this.KeyKmcDek = KeyKmcDek;
        this.CMAid = CMAid;
        this.last_modified_date = last_modified_date;
        this.scp_mode = scp_mode;
        this.apdu_mode = apdu_mode;
        this.keyDerivation_mode = keyDerivation_mode;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getKeyKmcAC() {
        return KeyKmcAC;
    }

    public void setKeyKmcAC(String keyKmcAC) {
        KeyKmcAC = keyKmcAC;
    }

    public String getKeyKmcMac() {
        return KeyKmcMac;
    }

    public void setKeyKmcMac(String keyKmcMac) {
        KeyKmcMac = keyKmcMac;
    }

    public String getKeyKmcDek() {
        return KeyKmcDek;
    }

    public void setKeyKmcDek(String keyKmcDek) {
        KeyKmcDek = keyKmcDek;
    }

    public String getCMAid() {
        return CMAid;
    }

    public void setCMAid(String CMAid) {
        this.CMAid = CMAid;
    }

    public Date getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(Date last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public ScpModeEnum getScp_mode() {
        return scp_mode;
    }

    public void setScp_mode(ScpModeEnum scp_mode) {
        this.scp_mode = scp_mode;
    }

    public ApduModeEnum getApdu_mode() {
        return apdu_mode;
    }

    public void setApdu_mode(ApduModeEnum apdu_mode) {
        this.apdu_mode = apdu_mode;
    }

    public KeyDerivationModeEnum getKeyDerivation_mode() {
        return keyDerivation_mode;
    }

    public void setKeyDerivation_mode(KeyDerivationModeEnum keyDerivation_mode) {
        this.keyDerivation_mode = keyDerivation_mode;
    }
}
