package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.CardType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Card.
 */
@Table("card")
public class Card implements Serializable {

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

    @Column("type_card")
    private CardType typeCard;

    @Column("credit")
    private Boolean credit;

    @Column("balance")
    private BigDecimal balance;

    @Column("expire_date")
    private LocalDate expireDate;

    @Transient
    @JsonIgnoreProperties(value = { "clients" }, allowSetters = true)
    private Client card;

    @Column("card_id")
    private Long cardId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Card id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public Card cardNumber(String cardNumber) {
        this.setCardNumber(cardNumber);
        return this;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankNumber() {
        return this.bankNumber;
    }

    public Card bankNumber(String bankNumber) {
        this.setBankNumber(bankNumber);
        return this;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getSysNumber() {
        return this.sysNumber;
    }

    public Card sysNumber(String sysNumber) {
        this.setSysNumber(sysNumber);
        return this;
    }

    public void setSysNumber(String sysNumber) {
        this.sysNumber = sysNumber;
    }

    public CardType getTypeCard() {
        return this.typeCard;
    }

    public Card typeCard(CardType typeCard) {
        this.setTypeCard(typeCard);
        return this;
    }

    public void setTypeCard(CardType typeCard) {
        this.typeCard = typeCard;
    }

    public Boolean getCredit() {
        return this.credit;
    }

    public Card credit(Boolean credit) {
        this.setCredit(credit);
        return this;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public Card balance(BigDecimal balance) {
        this.setBalance(balance);
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance != null ? balance.stripTrailingZeros() : null;
    }

    public LocalDate getExpireDate() {
        return this.expireDate;
    }

    public Card expireDate(LocalDate expireDate) {
        this.setExpireDate(expireDate);
        return this;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    public Client getCard() {
        return this.card;
    }

    public void setCard(Client client) {
        this.card = client;
        this.cardId = client != null ? client.getId() : null;
    }

    public Card card(Client client) {
        this.setCard(client);
        return this;
    }

    public Long getCardId() {
        return this.cardId;
    }

    public void setCardId(Long client) {
        this.cardId = client;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        return id != null && id.equals(((Card) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Card{" +
            "id=" + getId() +
            ", cardNumber='" + getCardNumber() + "'" +
            ", bankNumber='" + getBankNumber() + "'" +
            ", sysNumber='" + getSysNumber() + "'" +
            ", typeCard='" + getTypeCard() + "'" +
            ", credit='" + getCredit() + "'" +
            ", balance=" + getBalance() +
            ", expireDate='" + getExpireDate() + "'" +
            "}";
    }
}
