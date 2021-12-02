package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A HumoClient.
 */
@Table("humo_client")
public class HumoClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("phone")
    private String phone;

    @Column("address")
    private String address;

    @Transient
    @JsonIgnoreProperties(value = { "humoClient" }, allowSetters = true)
    private Set<HumoCard> humoCards = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HumoClient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HumoClient name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public HumoClient phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public HumoClient address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<HumoCard> getHumoCards() {
        return this.humoCards;
    }

    public void setHumoCards(Set<HumoCard> humoCards) {
        if (this.humoCards != null) {
            this.humoCards.forEach(i -> i.setHumoClient(null));
        }
        if (humoCards != null) {
            humoCards.forEach(i -> i.setHumoClient(this));
        }
        this.humoCards = humoCards;
    }

    public HumoClient humoCards(Set<HumoCard> humoCards) {
        this.setHumoCards(humoCards);
        return this;
    }

    public HumoClient addHumoCard(HumoCard humoCard) {
        this.humoCards.add(humoCard);
        humoCard.setHumoClient(this);
        return this;
    }

    public HumoClient removeHumoCard(HumoCard humoCard) {
        this.humoCards.remove(humoCard);
        humoCard.setHumoClient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HumoClient)) {
            return false;
        }
        return id != null && id.equals(((HumoClient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HumoClient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
