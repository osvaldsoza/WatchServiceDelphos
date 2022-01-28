package br.com.osvaldsoza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/arquivo")
public class ArquivoController {

    @Autowired
    private ArquivoRepository arquivoRepository;

    @GetMapping
    public ResponseEntity<?> monitoraDiretorio() {

        try (WatchService service = FileSystems.getDefault().newWatchService()) {
            Map<WatchKey, Path> map = new HashMap<>();
            Path diretorioBase = Paths.get("files");
            map.put(diretorioBase.register(service,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY), diretorioBase);

            WatchKey watchKey;

            do {
                watchKey = service.take();
                Path nomeDoDiretorio = map.get(watchKey);
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent.Kind<?> watchEvenKind = event.kind();
                    Path nomeDoArquivo = (Path) event.context();

                    if (String.valueOf(watchEvenKind) == "ENTRY_MODIFY" ||
                            String.valueOf(watchEvenKind) == "ENTRY_CREATE") {
                        List<String> linhasDoArquivo = lerArquivo(nomeDoDiretorio, nomeDoArquivo);
                        salvarLinhasDoArquivoNaBase(linhasDoArquivo);
                        moveArquivoDeDiretorio(nomeDoDiretorio, nomeDoArquivo);
                    }
                    return ResponseEntity.ok("Salvo com sucesso");
                }
            } while (watchKey.reset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    private List<Arquivo> salvarLinhasDoArquivoNaBase(List<String> linhas) {
        var linhasSalvas = new ArrayList<Arquivo>();

        for (String linha : linhas) {
            var arquivo = new Arquivo();
            arquivo.setLinhaDoArquivo(linha);
            linhasSalvas.add(arquivo);

        }
        return arquivoRepository.saveAll(linhasSalvas);
    }


    private void moveArquivoDeDiretorio(Path diretorioOrigem, Path nomeDoArquivo) {

        InputStream in;
        OutputStream out;

        try {
            File caminhoDeOrigem = new File(diretorioOrigem + "/" + nomeDoArquivo.toString());

            File novoDiretorio = criaNovoDiretorio();

            File caminhoDeDestino = new File(novoDiretorio + "/" + nomeDoArquivo);

            caminhoDeDestino.createNewFile();

            in = new FileInputStream(caminhoDeOrigem);
            out = new FileOutputStream(caminhoDeDestino);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            caminhoDeOrigem.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File criaNovoDiretorio() {
        File nomeDoNovoDiretorio = new File("backup");

        if (!nomeDoNovoDiretorio.exists()) {
            nomeDoNovoDiretorio.mkdir();
        }

        return nomeDoNovoDiretorio;
    }

    private List<String> lerArquivo(Path nomeDoDiretorio, Path nomeDoArquivo) throws IOException {

        List<String> linhas = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(nomeDoDiretorio + "/" + nomeDoArquivo))) {
            stream.skip(1).forEach(linhas::add);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return linhas;
    }

//    private static List<Arquivo> lerArquivo(Path pathDir, Path fileName) throws IOException {
//        List<Arquivo> dados = new ArrayList<>();
//        FileInputStream fileInputStream = new FileInputStream(pathDir + "/" + fileName);
//        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        String linha = bufferedReader.readLine();
//
//        int index = 0;
//
//        while (linha != null) {
//            String[] linhaSplitada = linha.split(";");
//
//            armazenaDadosDeUmArquivoEmUmObjeto(dados, index, linhaSplitada);
//
//            linha = bufferedReader.readLine();
//        }
//
//        return dados;
//
//    }

//    private static void armazenaDadosDeUmArquivoEmUmObjeto(List<Arquivo> dados, int index, String[] linhaSplitada) {
//        Arquivo arquivo = new Arquivo();
//        arquivo.setNumeroDaLinha(linhaSplitada[index]);
//        index++;
//        arquivo.setProtocolo(linhaSplitada[index]);
//        index++;
//        arquivo.setDescricaoMensagem(linhaSplitada[index]);
//        index++;
//        arquivo.setTextoLinhaOriginal(linhaSplitada[index]);
//        dados.add(arquivo);
//    }

}
