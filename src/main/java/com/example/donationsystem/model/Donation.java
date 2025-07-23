package com.example.donationsystem.model;

import java.time.LocalDate;

public class Donation {
    private Long id;
    private String type; // ex.: "dinheiro", "alimento", "roupa"
    private int quantity;
    private double value;
    private Long donorId;
    private LocalDate donationDate;

    public Donation() {}

    public Donation(String type, int quantity, double value, Long donorId, LocalDate donationDate) {
        this.type = type;
        this.quantity = quantity;
        this.value = value;
        this.donorId = donorId;
        this.donationDate = donationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setDonorId(Long donorId) {
        this.donorId = donorId;
    }

    public LocalDate getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(LocalDate donationDate) {
        this.donationDate = donationDate;
    }
}