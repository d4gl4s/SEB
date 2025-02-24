package com.SEB.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum Currency {
    USD("US dollar", "🇺🇸", "$"),
    JPY("Japanese yen", "🇯🇵", "¥"),
    BGN("Bulgarian lev", "🇧🇬", "лв"),
    CZK("Czech koruna", "🇨🇿", "Kč"),
    DKK("Danish krone", "🇩🇰", "kr"),
    GBP("Pound sterling", "🇬🇧", "£"),
    HUF("Hungarian forint", "🇭🇺", "Ft"),
    PLN("Polish zloty", "🇵🇱", "zł"),
    RON("Romanian leu", "🇷🇴", "lei"),
    SEK("Swedish krona", "🇸🇪", "kr"),
    CHF("Swiss franc", "🇨🇭", "Fr"),
    ISK("Icelandic krona", "🇮🇸", "kr"),
    NOK("Norwegian krone", "🇳🇴", "kr"),
    TRY("Turkish lira", "🇹🇷", "₺"),
    AUD("Australian dollar", "🇦🇺", "$"),
    BRL("Brazilian real", "🇧🇷", "R$"),
    CAD("Canadian dollar", "🇨🇦", "$"),
    CNY("Chinese yuan renminbi", "🇨🇳", "¥"),
    HKD("Hong Kong dollar", "🇭🇰", "$"),
    IDR("Indonesian rupiah", "🇮🇩", "Rp"),
    ILS("Israeli shekel", "🇮🇱", "₪"),
    INR("Indian rupee", "🇮🇳", "₹"),
    KRW("South Korean won", "🇰🇷", "₩"),
    MXN("Mexican peso", "🇲🇽", "$"),
    MYR("Malaysian ringgit", "🇲🇾", "RM"),
    NZD("New Zealand dollar", "🇳🇿", "$"),
    PHP("Philippine peso", "🇵🇭", "₱"),
    SGD("Singapore dollar", "🇸🇬", "$"),
    THB("Thai baht", "🇹🇭", "฿"),
    ZAR("South African rand", "🇿🇦", "R");

    private final String currencyName;
    private final String flag;
    private final String symbol;

    public static Currency fromCode(String code) {
        try {
            return Currency.valueOf(code.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported currency code: " + code);
        }
    }
}