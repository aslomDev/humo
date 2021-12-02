package com.mycompany.myapp.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.myapp.domain.enumeration.CardType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


public class CardDTO implements Serializable {
    private String cardNumber;
    private String bankNumber;
    private String sysNumber;
    private CardType typeCard;
    private Boolean credit;
    private BigDecimal balance;
    private Date expireDate;
    @JsonIgnore
    private ClientDTO card;
    private Long cardId;


    public CardDTO() {
    }

    public CardDTO(String cardNumber, String bankNumber, String sysNumber, CardType typeCard, Boolean credit, BigDecimal balance, Date expireDate, ClientDTO card, Long cardId) {
        this.cardNumber = cardNumber;
        this.bankNumber = bankNumber;
        this.sysNumber = sysNumber;
        this.typeCard = typeCard;
        this.credit = credit;
        this.balance = balance;
        this.expireDate = expireDate;
        this.card = card;
        this.cardId = cardId;
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

    public CardType getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(CardType typeCard) {
        this.typeCard = typeCard;
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

    public ClientDTO getCard() {
        return card;
    }

    public void setCard(ClientDTO card) {
        this.card = card;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
