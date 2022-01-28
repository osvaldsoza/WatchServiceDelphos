package br.com.osvaldsoza;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "arquivo")
public class Arquivo {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private UUID id;

    private String linhaDoArquivo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLinhaDoArquivo() {
        return linhaDoArquivo;
    }

    public void setLinhaDoArquivo(String linhaDoArquivo) {
        this.linhaDoArquivo = linhaDoArquivo;
    }

    //    private String numeroDaLinha;
//    private String protocolo;
//    private String descricaoMensagem;
//    private String textoLinhaOriginal;
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getNumeroDaLinha() {
//        return numeroDaLinha;
//    }
//
//    public void setNumeroDaLinha(String numeroDaLinha) {
//        this.numeroDaLinha = numeroDaLinha;
//    }
//
//    public String getProtocolo() {
//        return protocolo;
//    }
//
//    public void setProtocolo(String protocolo) {
//        this.protocolo = protocolo;
//    }
//
//    public String getDescricaoMensagem() {
//        return descricaoMensagem;
//    }
//
//    public void setDescricaoMensagem(String descricaoMensagem) {
//        this.descricaoMensagem = descricaoMensagem;
//    }
//
//    public String getTextoLinhaOriginal() {
//        return textoLinhaOriginal;
//    }
//
//    public void setTextoLinhaOriginal(String textoLinhaOriginal) {
//        this.textoLinhaOriginal = textoLinhaOriginal;
//    }
}
