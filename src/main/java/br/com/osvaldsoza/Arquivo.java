package br.com.osvaldsoza;

public class Arquivo {

    private String numeroDaLinha;
    private String protocolo;
    private String descricaoMensagem;
    private String textoLinhaOriginal;

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
