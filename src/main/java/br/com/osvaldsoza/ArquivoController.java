package br.com.osvaldsoza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/arquivo")
public class ArquivoController {

    @Autowired
    private ArquivoRepository arquivoRepository;

    @GetMapping
    public ResponseEntity<?> lerArquivo(){
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

                        dados.remove(0);

                        arquivoRepository.saveAll(dados);
                        System.out.println(dados);
                        return  ResponseEntity.ok(dados);
                    }
                }
            } while (watchKey.reset());
        } catch (Exception e) {

            e.printStackTrace();
        }
            return  ResponseEntity.notFound().build();
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
        index++;
        arquivo.setProtocolo(linhaSplitada[index]);
        index++;
        arquivo.setDescricaoMensagem(linhaSplitada[index]);
        index++;
        arquivo.setTextoLinhaOriginal(linhaSplitada[index]);
        dados.add(arquivo);
    }

}
