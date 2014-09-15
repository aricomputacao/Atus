/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.managedbean;

import br.com.atus.controller.PecaController;
import br.com.atus.modelo.Peca;
import br.com.atus.util.MenssagemUtil;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author gilmario
 */
@ViewScoped
@ManagedBean
public class PecaMB extends BeanGenerico<Peca> implements Serializable {

    @EJB
    private PecaController controller;
    private UploadedFile file;
    private Peca peca;
    @Inject
    private NavegacaoMB navegacaoMB;
    private List<Peca> listaPecas;

    @PostConstruct
    public void init() {
        listaPecas = new ArrayList<>();
        peca = (Peca) navegacaoMB.getRegistroMapa("peca", new Peca());
    }

    public PecaMB() {
        super(Peca.class);
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public Peca getPeca() {
        return peca;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public List<Peca> getListaPecas() {
        return listaPecas;
    }

    public void setListaPecas(List<Peca> listaPecas) {
        this.listaPecas = listaPecas;
    }

    public void imprimir() {

    }

    public void salvar() {
        try {
            controller.salvar(peca, file);

            MenssagemUtil.addMessageInfo("Registro salvo");
        } catch (Exception e) {
            MenssagemUtil.addMessageErro("Erro ao salvar.");
        }
    }

    public static void main(String[] args) {
        File f = new File("/home/gilmario/arquivo.docx");

        try {
//            Pattern pattern = Pattern.compile("\\$\\{\\w+}");

//            WordprocessingMLPackage wordMLPackage;
//            wordMLPackage = WordprocessingMLPackage.load(f);
//            MainDocumentPart main = wordMLPackage.getMainDocumentPart();
//            List<Object> lista = main.getContent();
//            for (Object lista1 : lista) {
//                Matcher matcher = pattern.matcher(lista1.toString());
//                String parte1 = lista1.toString().replaceAll("\\$\\{\\w+\\}", "");
//                String patre2 = lista1.toString().replaceAll(lista1.toString(), parte1);
//                if (patre2.length() > 0) {
//                    System.out.println(patre2);
//                    System.out.println(lista1);
//                }
//
//            }
//            wordMLPackage.save(new java.io.File("/home/gilmario/teste" + ".docx"));
            String valor = "texto qualquer ${parametro}";
            Pattern pattern = Pattern.compile("\\$\\{\\w+}");
            String[] palavras = valor.split(" ");
            for (String palavra : palavras) {
                Matcher matcher = pattern.matcher(palavra);
                if (matcher.find()) {
                    System.out.println("Substituir " + palavra + " por text " + palavra.replaceAll(pattern.pattern(), "teste"));
                }
            }

//            System.out.println(valor.replaceAll(pattern.pattern(), ""));
//            System.out.println(pattern);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void uploadArquivo(FileUploadEvent event) {
        try {
            String pasta = requisicaoPublicacao.getId() + "-" + requisicaoPublicacao.getEmissor().getDocumento() + "-" + String.format("%td-%tm-%tY", requisicaoPublicacao.getDataRequisicao(), requisicaoPublicacao.getDataRequisicao(), requisicaoPublicacao.getDataRequisicao());
            ArquivoUtil.uploadArquivo(event, pasta);
            MenssagemUtil.addMessageInfo("Arquivo enviado com sucesso!");
        } catch (Exception ex) {
            MenssagemUtil.addMessageErro("Erro ao fazer upload do arquivo", ex, this.getClass().getName());
        }
        buscarArquivodDownload(requisicaoPublicacao);
    }

    private void buscarArquivodDownload(RequisicaoPublicacao r) {
        try {
            arquivosDownload = controller.geraListaDownload(r);
        } catch (FileNotFoundException e) {
            // O erro provavel Ã© que nÃ£o exista arquivos
            //MenssagemUtil.addMessageErro(e);
            arquivosDownload = new ArrayList<>();
        }
    }

    public List<StreamedContent> getArquivosDownload() {
        return arquivosDownload;
    }

    public void setArquivosDownload(List<StreamedContent> arquivosDownload) {
        this.arquivosDownload = arquivosDownload;
    }

    public void removeArquivo(StreamedContent streamedContent) {
        try {
            arquivosDownload.remove(streamedContent);
            ArquivoUtil.excluirAquivo(requisicaoPublicacao.getId() + "-" + requisicaoPublicacao.getEmissor().getDocumento() + "-" + String.format("%td-%tm-%tY", requisicaoPublicacao.getDataRequisicao(), requisicaoPublicacao.getDataRequisicao(), requisicaoPublicacao.getDataRequisicao()), streamedContent.getName());
            MenssagemUtil.addMessageInfo("Arquivo removido");
        } catch (Exception e) {
            MenssagemUtil.addMessageErro("Erro ao remover arquivo", e, this.getClass().getName());
        }
    }

}
