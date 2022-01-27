package br.com.osvaldsoza;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "arquivo")
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numeroDaLinha;
    private String protocolo;
    private String descricaoMensagem;
    private String textoLinhaOriginal;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDaLinha() {
        return numeroDaLinha;
    }

    public void setNumeroDaLinha(String numeroDaLinha) {
        this.numeroDaLinha = numeroDaLinha;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getDescricaoMensagem() {
        return descricaoMensagem;
    }

    public void setDescricaoMensagem(String descricaoMensagem) {
        this.descricaoMensagem = descricaoMensagem;
    }

    public String getTextoLinhaOriginal() {
        return textoLinhaOriginal;
    }

    public void setTextoLinhaOriginal(String textoLinhaOriginal) {
        this.textoLinhaOriginal = textoLinhaOriginal;
    }
}
