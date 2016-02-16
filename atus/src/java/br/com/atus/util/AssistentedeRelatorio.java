/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

/**
 * Classe do Projeto ******* - Criado em 16/05/2013 -
 *
 * @author Gilmário
 */
@ManagedBean
@ViewScoped
public class AssistentedeRelatorio implements Serializable {

    public AssistentedeRelatorio() {
    }

    public String getDiretorioReal(String diretorio) {
        HttpSession hSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return hSession.getServletContext().getRealPath(diretorio);
    }

    private String exportar(TipoRelatorio tipo, String relatorio, JasperPrint print) {
        String rel = null;
        try {
            if (tipo.equals(TipoRelatorio.HTML)) {
                rel = relatorio.replace(".jasper", ".html");
                JasperExportManager.exportReportToHtmlFile(print, getDiretorioReal(rel));
            } else if (tipo.equals(TipoRelatorio.PDF)) {
                rel = relatorio.replace(".jasper", ".pdf");
                JasperExportManager.exportReportToPdfFile(print, getDiretorioReal(rel));
            } else if (tipo.equals(TipoRelatorio.XML)) {
                rel = relatorio.replace(".jasper", ".xml");
                JasperExportManager.exportReportToXmlFile(print, getDiretorioReal(rel), true);
            } else if (tipo.equals(TipoRelatorio.XHTML)) {
                rel = relatorio.replace(".jasper", ".html");
                JRXhtmlExporter xhtmlExporter = new JRXhtmlExporter();
                xhtmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                xhtmlExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, getDiretorioReal(rel));
                xhtmlExporter.exportReport();
            } else if (tipo.equals(TipoRelatorio.XLSX)) {
                rel = relatorio.replace(".jasper", ".xls");
                JRXlsExporter exporter = new JRXlsExporter();
                FileOutputStream xlsReport = new FileOutputStream(getDiretorioReal(rel));
                exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
                exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, xlsReport);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, getDiretorioReal(rel));
                exporter.exportReport();
            }
        } catch (JRException | FileNotFoundException e) {
            e.printStackTrace();
            return "/erros/error.html";
        }
        return rel;
    }

    public String relatorio(String relatorio, Map<String, Object> mapa, TipoRelatorio tipo, List lista) {

        try {
            String rel = getDiretorioReal(relatorio);
            JasperPrint print = JasperFillManager.fillReport(rel, mapa, new JRBeanCollectionDataSource(lista));
            return exportar(tipo, relatorio, print);
        } catch (Exception e) {
            e.printStackTrace();
            return "/erros/error.html";
        }
    }

    public void gerarRelatorioWeb(List lista, Map<String, Object> parametros, String arquivo) {
        JRDataSource jrRS = new JRBeanCollectionDataSource(lista);
        ServletOutputStream servletOutputStream;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        try {
            servletOutputStream = response.getOutputStream();
            JasperRunManager.runReportToPdfStream(new FileInputStream(new File(getDiretorioReal(arquivo))), servletOutputStream, parametros, jrRS);
            //JasperRunManager.
            response.setContentType("application/pdf");
            servletOutputStream.flush();
            servletOutputStream.close();
            context.getApplication().getStateManager().saveView(context);
            context.renderResponse();
            context.responseComplete();
            System.out.println("Completou");
        } catch (IOException | JRException e) {
            e.printStackTrace();
        }
    }

    public void gerarRelatorioWeb2(List lista, Map<String, Object> parametros, String arquivo) {
        JRDataSource jrRS = new JRBeanCollectionDataSource(lista);
        try {
            String rel = getDiretorioReal(arquivo);
            JasperPrint print = JasperFillManager.fillReport(rel, parametros, jrRS);
            byte[] b = JasperExportManager.exportReportToPdf(print);
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "attachment;filename=arquivo.pdf");
            res.getOutputStream().write(b);
            res.getCharacterEncoding();
            FacesContext.getCurrentInstance().responseComplete();
            System.out.println("Completou");
        } catch (JRException | IOException e) {
        }
    }

    public byte[] relatorioemByte(List lista, Map<String, Object> parametros, String arquivo, String nomeRelatorio) {
        byte[] b = null;
        try {
            parametros.put("img", getDiretorioReal("resources/imagens/logo2.png"));
            parametros.put("img1", getDiretorioReal("resources/imagens/avocatus.png"));
            parametros.put("imgAt", getDiretorioReal("resources/imagens/atus.png"));

            parametros.put("imgRoda", getDiretorioReal("resources/imagens/logo.png"));

            parametros.put("relatorio", nomeRelatorio);
            parametros.put("SUBREPORT_DIR", getDiretorioReal("WEB-INF/relatorios") + "/");
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
            parametros.put("rodapeEmail", "avocatus@live.com");
            parametros.put("rodapeFone", "(88) 3611-6210");
            String rel = getDiretorioReal(arquivo);
            JRDataSource jrRS = new JRBeanCollectionDataSource(lista);
            JasperPrint print = JasperFillManager.fillReport(rel, parametros, jrRS);
            b = JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }
}
