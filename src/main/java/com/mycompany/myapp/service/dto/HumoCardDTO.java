package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.HUMO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.HumoCard} entity.
 */
public class HumoCardDTO implements Serializable {

    private Long id;

    private String cardNumber;

    private String bankNumber;

    private String sysNumber;

    private HUMO cardType;

    private Boolean credit;

    private BigDecimal balance;

    private Date expireDate;

    private String pan;

    private String maskedPan;

    private HumoClientDTO humoClient;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getSysNumber() {
        return sysNumber;
    }

    public void setSysNumber(String sysNumber) {
        this.sysNumber = sysNumber;
    }

    public HUMO getCardType() {
        return cardType;
    }

    public void setCardType(HUMO cardType) {
        this.cardType = cardType;
    }

    public Boolean getCredit() {
        return credit;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public HumoClientDTO getHumoClient() {
        return humoClient;
    }

    public void setHumoClient(HumoClientDTO humoClient) {
        this.humoClient = humoClient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HumoCardDTO)) {
            return false;
        }

        HumoCardDTO humoCardDTO = (HumoCardDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, humoCardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HumoCardDTO{" +
            "id=" + getId() +
            ", cardNumber='" + getCardNumber() + "'" +
            ", bankNumber='" + getBankNumber() + "'" +
            ", sysNumber='" + getSysNumber() + "'" +
            ", cardType='" + getCardType() + "'" +
            ", credit='" + getCredit() + "'" +
            ", balance=" + getBalance() +
            ", expireDate='" + getExpireDate() + "'" +
            ", pan='" + getPan() + "'" +
            ", maskedPan='" + getMaskedPan() + "'" +
            ", humoClient=" + getHumoClient() +
            "}";
    }
}
