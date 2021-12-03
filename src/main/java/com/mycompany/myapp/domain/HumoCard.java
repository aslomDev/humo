package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.HUMO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HumoCard.
 */
@Table("humo_card")
public class HumoCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("card_number")
    private String cardNumber;

    @Column("bank_number")
    private String bankNumber;

    @Column("sys_number")
    private String sysNumber;

    @Column("card_type")
    private HUMO cardType;

    @Column("credit")
    private Boolean credit;

    @Column("balance")
    private BigDecimal balance;

    @Column("expire_date")
    private LocalDate expireDate;

    @Column("pan")
    private String pan;

    @Column("masked_pan")
    private String maskedPan;

    @Transient
    @JsonIgnoreProperties(value = { "humoCards" }, allowSetters = true)
    private HumoClient humoClient;

    @Column("humo_client_id")
    private Long humoClientId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HumoCard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public HumoCard cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankNumber() {
        return this.bankNumber;
    }

    public HumoCard bankNumber(String bankNumber) {
        this.setBankNumber(bankNumber);
        return this;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getSysNumber() {
        return this.sysNumber;
    }

    public HumoCard sysNumber(String sysNumber) {
        this.setSysNumber(sysNumber);
        return this;
    }

    public void setSysNumber(String sysNumber) {
        this.sysNumber = sysNumber;
    }

    public HUMO getCardType() {
        return this.cardType;
    }

    public HumoCard cardType(HUMO cardType) {
        this.setCardType(cardType);
        return this;
    }

    public void setCardType(HUMO cardType) {
        this.cardType = cardType;
    }

    public Boolean getCredit() {
        return this.credit;
    }

    public HumoCard credit(Boolean credit) {
        this.setCredit(credit);
        return this;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public HumoCard balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance != null ? balance.stripTrailingZeros() : null;
    }

    public LocalDate getExpireDate() {
        return this.expireDate;
    }

    public HumoCard expireDate(LocalDate expireDate) {
        this.setExpireDate(expireDate);
        return this;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public String getPan() {
        return this.pan;
    }

    public HumoCard pan(String pan) {
        this.setPan(pan);
        return this;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getMaskedPan() {
        return this.maskedPan;
    }

    public HumoCard maskedPan(String maskedPan) {
        this.setMaskedPan(maskedPan);
        return this;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public HumoClient getHumoClient() {
        return this.humoClient;
    }

    public void setHumoClient(HumoClient humoClient) {
        this.humoClient = humoClient;
        this.humoClientId = humoClient != null ? humoClient.getId() : null;
    }

    public HumoCard humoClient(HumoClient humoClient) {
        this.setHumoClient(humoClient);
        return this;
    }

    public Long getHumoClientId() {
        return this.humoClientId;
    }

    public void setHumoClientId(Long humoClient) {
        this.humoClientId = humoClient;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HumoCard)) {
            return false;
        }
        return id != null && id.equals(((HumoCard) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HumoCard{" +
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
            "}";
    }
}
