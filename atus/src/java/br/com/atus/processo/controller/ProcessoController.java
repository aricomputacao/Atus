/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.processo.controller;

import br.com.atus.cadastro.controller.ColaboradorController;
import br.com.atus.cadastro.controller.UsuarioController;
import br.com.atus.interfaces.Controller;
import br.com.atus.interfaces.ConsultaDeProcessos;
import br.com.atus.processo.dao.MovimentacaoDAO;
import br.com.atus.processo.dao.ProcessoDAO;
import br.com.atus.dto.ProcessoAtrasadoDTO;
import br.com.atus.dto.ProcessoGrupoDiaAtrasadoDTO;
import br.com.atus.dto.ProcessoUltimaMovimentacaoDTO;
import br.com.atus.dto.ProcessosAtrasadoRelatorioDTO;
import br.com.atus.enumerated.Perfil;
import br.com.atus.cadastro.modelo.Cliente;
import br.com.atus.cadastro.modelo.Colaborador;
import br.com.atus.cadastro.modelo.JuizoTribunal;
import br.com.atus.processo.modelo.Fase;
import br.com.atus.processo.modelo.Movimentacao;
import br.com.atus.processo.modelo.Processo;
import br.com.atus.cadastro.modelo.Usuario;
import br.com.atus.processo.modelo.Enderecamento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author ari
 */
@Stateless
public class ProcessoController extends Controller<Processo, Long> implements Serializable {

    @Inject
    private ProcessoDAO dao;
    @Inject
    private MovimentacaoDAO movimentacaoDAO;
    @Inject
    private MovimentacaoController movimentacaoController;
    @Inject
    private ColaboradorController colaboradorController;
    @Inject
    private UsuarioController usuarioController;

    @PostConstruct
    @Override
    protected void inicializaDAO() {
        setDAO(dao);
    }

    //Criar uma movimetação para corrigir processos que não tem movimentação
    public void criarMovimentacaoParaProcesso() throws Exception {
        List<Processo> lista = new ArrayList<>();
        lista = dao.consultarProcessoSemMovimentacao();
        for (Processo p : lista) {
            Movimentacao m = new Movimentacao();
            m.setDataMovimentacao(new Date());
            m.setFaseAntiga(p.getFase());
            m.setFaseNova(p.getFase());
            m.setMotivo("Ajuste do sistema");
            m.setProcesso(p);
            m.setUsuario(usuarioController.gerenciar(new Long(0)));
            movimentacaoController.salvar(m);
        }
    }

    public List<ProcessoAtrasadoDTO> processoAtrasadoUsuario(Usuario u) {
        return dao.processoAtrasadoUsuario(u);
    }

    public List<ProcessoAtrasadoDTO> processoAtrasadoGeral() {
        return dao.processoAtrasadoGeral();
    }

    public List<ProcessoGrupoDiaAtrasadoDTO> processoGrupoDiaAtrasadoGeral() {
        return dao.processoGrupoDiaAtrasadoGeral();
    }

    public List<ProcessoGrupoDiaAtrasadoDTO> processoGrupoDiaAtrasadoSetor(Usuario usuarioLogado) {
        return dao.processoGrupoDiaAtrasadoSetor(usuarioLogado);

    }

    public List<ProcessosAtrasadoRelatorioDTO> listaProcessosAtrasadosRelatorio(Cliente c) {
        if (c.getId() != null) {
            return dao.listaProcessosAtrasadosRelatorio(c);

        } else {
            return dao.listaProcessosAtrasadosRelatorio();

        }
    }

    public List<ProcessoUltimaMovimentacaoDTO> ultimasMovimentacoesDe(List<Processo> processos) {
        List<ProcessoUltimaMovimentacaoDTO> ultimaMovimentacaoDTOs = new ArrayList<>();

        for (Processo p : processos) {
            ultimaMovimentacaoDTOs.add(movimentacaoDAO.ultimaMovimentacaoDTO(p));
        }
        return ultimaMovimentacaoDTOs;
    }

    public List<ProcessoUltimaMovimentacaoDTO> listaProcessosAtrasadosRelatorio(Usuario u) {
        List<ProcessosAtrasadoRelatorioDTO> atrasados = dao.listaProcessosAtrasadosRelatorio(u);
        List<ProcessoUltimaMovimentacaoDTO> ultimaMovimentacaoDTOs = new ArrayList<>();
        for (ProcessosAtrasadoRelatorioDTO atra : atrasados) {
            if (u.getId() == null) {
                ultimaMovimentacaoDTOs.add(movimentacaoDAO.ultimaMovimentacaoDTO(atra.getProcesso()));

            } else {
                ultimaMovimentacaoDTOs.add(movimentacaoDAO.ultimaMovimentacaoDTO(atra.getProcesso(), u));

            }
        }
        return ultimaMovimentacaoDTOs;
    }

//    public List<ProcessoUltimaMovimentacaoDTO> ultimaMovimentacaoNa(Fase f) {
//        List<ProcessoUltimaMovimentacaoDTO> ultimaMovimentacaoDTOs = new ArrayList<>();
//        List<Processo> listaProcessos = new ArrayList<>();
//        listaProcessos = consultarProcessoDa(f);
//        for (Processo p : listaProcessos) {
//            ultimaMovimentacaoDTOs.add(movimentacaoDAO.ultimaMovimentacaoDTO(p));
//        }
//        return ultimaMovimentacaoDTOs;
//    }
    public List<Processo> consultarProcessoDa(Fase f, Usuario usuarioLogado) throws Exception {
        if (usuarioLogado.ehColaborador()) {
            Colaborador colaborador = colaboradorController.carregar(usuarioLogado.getReferencia());
            return dao.consultarPor(f, colaborador);
        } else {
            return dao.consultarPor(f);
        }
    }

    public List<Processo> consultarPor(Cliente c) {
        return dao.consultarPor(c);
    }

    public List<Processo> consultaLikePor(String numero) {
        return dao.consultaLikePor(numero);
    }

    public void salvarouAtualizarEditarFase(Processo processo, Fase fs, Usuario u) throws Exception {
        if (processo.getId() == null) {
            processo.setUsuarioCadastro(u);
            salvar(processo);
            movimentacaoController.addMovimentacao(processo, processo.getFase(), u);
        } else {
            //se a fase alterar add movimentação
            if (!fs.equals(processo.getFase())) {
                movimentacaoController.addMovimentacao(processo, fs, u);
            }
            salvarouAtualizar(processo);

        }

    }

    public List<Processo> listarFase() {
        return dao.listarFase();
    }

    public List<Processo> consultaPorColaborador(Colaborador colaborador) throws Exception {
        if (colaborador.getId() != null) {
            return dao.consultaPorColaborador(colaborador);

        } else {
            return dao.consultarTodos("colaborador");
        }
    }

    public List<Processo> consultarPor(Cliente cliente, Usuario usuarioLogado) throws Exception {
        ConsultaDeProcessos deProcessos;
        if (usuarioLogado.getPerfil().equals(Perfil.COLABORADOR)) {
            deProcessos = new ConsultaDeProcessoPorColaborador(dao, colaboradorController);
            return deProcessos.consultaProcessosPor(cliente, usuarioLogado);
        } else {
            deProcessos = new ConsultaDeProcessoEscritorio(dao);
            return deProcessos.consultaProcessosPor(cliente, usuarioLogado);
        }

    }

    public List<Processo> consultaLikePor(String numero, Usuario usuarioLogado) throws Exception {
        ConsultaDeProcessos deProcessos;
        if (usuarioLogado.getPerfil().equals(Perfil.COLABORADOR)) {
            deProcessos = new ConsultaDeProcessoPorColaborador(dao, colaboradorController);
            return deProcessos.consultaProcessosPor(numero, usuarioLogado);
        } else {
            deProcessos = new ConsultaDeProcessoEscritorio(dao);
            return deProcessos.consultaProcessosPor(numero, usuarioLogado);
        }
    }

    public List<Processo> consultaPorLike(String nomeDoCliente, Usuario usuarioLogado) throws Exception {
        ConsultaDeProcessos deProcessos;
        if (usuarioLogado.getPerfil().equals(Perfil.COLABORADOR)) {
            deProcessos = new ConsultaDeProcessoPorColaborador(dao, colaboradorController);
            return deProcessos.consultaProcessosPorLike(nomeDoCliente, usuarioLogado);
        } else {
            deProcessos = new ConsultaDeProcessoEscritorio(dao);
            return deProcessos.consultaProcessosPorLike(nomeDoCliente, usuarioLogado);
        }
    }

    public Processo carregarPor(Long id, Usuario usuarioLogado) throws Exception {
        ConsultaDeProcessos deProcessos;
        if (usuarioLogado.getPerfil().equals(Perfil.COLABORADOR)) {
            deProcessos = new ConsultaDeProcessoPorColaborador(dao, colaboradorController);
            return deProcessos.consultaProcessosPor(id, usuarioLogado);
        } else {
            deProcessos = new ConsultaDeProcessoEscritorio(dao);
            return deProcessos.consultaProcessosPor(id, usuarioLogado);
        }
    }

    public List<Processo> consultaPorColaborador(Colaborador colaborador, List<Fase> listaFasesSelection) {
        return dao.consultaPorColaborador(colaborador, listaFasesSelection);
    }

    public List<Processo> consultarProcessoPor(List<JuizoTribunal> listaDeJuizoTribunaisSelection) {
        return dao.consultarProcessoPor(listaDeJuizoTribunaisSelection);
    }

    public List<Processo> consultarProcessoPorEnderecamentos(List<Enderecamento> listaDeEnderecamentos) {
        return dao.consultarProcessoPorEnderecamentos(listaDeEnderecamentos);

    }

    public List<Processo> consultarProcessoPor(List<JuizoTribunal> listaDeJuizoTribunaisSelection, List<Enderecamento> listaDeEnderecamentos) {
        return dao.consultarProcessoPor(listaDeJuizoTribunaisSelection, listaDeEnderecamentos);

    }
}
