package com.example.user.emilia.model;

import com.google.gson.annotations.SerializedName;

public class Crypto {
    @SerializedName("session_id")
    private String session_id;
    @SerializedName("public_key")
    private String public_key;
    @SerializedName("modulo")
    private String modulo;
    @SerializedName("plain")
    private String plain;
    @SerializedName("cipher")
    private String cipher;
    @SerializedName("cipher_aes")
    private String cipher_aes;
    @SerializedName("cipher_rsa")
    private String cipher_rsa;

    public Crypto(){}

    public Crypto(String session_id, String public_key, String modulo, String plain, String cipher, String cipher_aes, String cipher_rsa) {
        this.session_id = session_id;
        this.public_key = public_key;
        this.modulo = modulo;
        this.plain = plain;
        this.cipher = cipher;
        this.cipher_aes = cipher_aes;
        this.cipher_rsa = cipher_rsa;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getPlain() {
        return plain;
    }

    public void setPlain(String plain) {
        this.plain = plain;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }

    public String getCipher_aes() {
        return cipher_aes;
    }

    public void setCipher_aes(String cipher_aes) {
        this.cipher_aes = cipher_aes;
    }

    public String getCipher_rsa() {
        return cipher_rsa;
    }

    public void setCipher_rsa(String cipher_rsa) {
        this.cipher_rsa = cipher_rsa;
    }
}
