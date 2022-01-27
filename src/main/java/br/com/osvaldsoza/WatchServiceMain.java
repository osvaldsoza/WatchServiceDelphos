package br.com.osvaldsoza;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WatchServiceMain {

    public static void main(String[] args) {

        try (WatchService service = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> map = new HashMap<>();
            Path path = Paths.get("files");
            map.put(path.register(service,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY), path);

            WatchKey watchKey;

            do {
                watchKey = service.take();
                Path pathDir = map.get(watchKey);
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> watchEvenKind = event.kind();
                    Path fileName = (Path) event.context();
                    System.out.println(pathDir + ": " + watchEvenKind + ": " + fileName);

                    if (String.valueOf(watchEvenKind) == "ENTRY_MODIFY") {
                        var dados = lerArquivo(pathDir, fileName);
                        System.out.println(dados);

                    }
                }
            } while (watchKey.reset());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Arquivo> lerArquivo(Path pathDir, Path fileName) throws IOException {
        List<Arquivo> dados = new ArrayList<>();
        FileInputStream fileInputStream = new FileInputStream(pathDir + "/" + fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String linha = bufferedReader.readLine();

        int i = 0;

        while (linha != null) {
            String[] linhaSplitada = linha.split(";");
            armazenaDadosDeUmArquivoEmUmObjeto(dados, i, linhaSplitada);
            linha = bufferedReader.readLine();
        }

        return dados;
    }

    private static void armazenaDadosDeUmArquivoEmUmObjeto(List<Arquivo> dados, int index, String[] linhaSplitada) {
        Arquivo arquivo = new Arquivo();
        arquivo.setNumeroDaLinha(linhaSplitada[index]);
        arquivo.setProtocolo(linhaSplitada[index + 1]);
        arquivo.setDescricaoMensagem(linhaSplitada[index + 1]);
        arquivo.setTextoLinhaOriginal(linhaSplitada[index + 1]);
        dados.add(arquivo);
    }
}
