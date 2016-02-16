/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.processo.dao;

import br.com.atus.util.dao.DAO;
import br.com.atus.dto.ProcessoAtrasadoDTO;
import br.com.atus.dto.ProcessoGrupoDiaAtrasadoDTO;
import br.com.atus.dto.ProcessosAtrasadoRelatorioDTO;
import br.com.atus.cadastro.modelo.Cliente;
import br.com.atus.cadastro.modelo.Colaborador;
import br.com.atus.cadastro.modelo.JuizoTribunal;
import br.com.atus.processo.modelo.Fase;
import br.com.atus.processo.modelo.Processo;
import br.com.atus.cadastro.modelo.Usuario;
import br.com.atus.processo.modelo.Enderecamento;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

/**
 *
 * @author ari
 */
@Stateless
public class ProcessoDAO extends DAO<Processo, Long> implements Serializable {

    public ProcessoDAO() {
        super(Processo.class);
    }

    public List<ProcessoAtrasadoDTO> processoAtrasadoUsuario(Usuario u) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM ProcessoAtrasadoDTO p WHERE p.fase.usuario = :u ORDER BY p.fase.nome", ProcessoAtrasadoDTO.class)
                .setParameter("u", u);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }

    }

    public List<ProcessoAtrasadoDTO> processoAtrasadoGeral() {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM ProcessoAtrasadoDTO p ORDER BY p.fase.nome", ProcessoAtrasadoDTO.class);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }

    }

    public List<ProcessoGrupoDiaAtrasadoDTO> processoGrupoDiaAtrasadoGeral() {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM ProcessoGrupoDiaAtrasadoDTO p ORDER BY p.fase.nome", ProcessoGrupoDiaAtrasadoDTO.class);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }

    }

    public List<ProcessoGrupoDiaAtrasadoDTO> processoGrupoDiaAtrasadoSetor(Usuario usuarioLogado) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM ProcessoGrupoDiaAtrasadoDTO p WHERE p.fase.usuario = :usr ORDER BY p.fase.nome", ProcessoGrupoDiaAtrasadoDTO.class)
                .setParameter("usr", usuarioLogado);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultarPor(Fase f) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.fase  = :f ORDER BY p.fase.usuario,p.fase", Processo.class)
                .setParameter("f", f);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultarProcessoSemMovimentacao() {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.id  NOT IN (SELECT m.processo.id FROM Movimentacao m)", Processo.class);

        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultarPor(Fase f, Colaborador colaborador) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.fase  = :f and p.colaborador = :col ORDER BY p.fase.usuario,p.fase", Processo.class)
                .setParameter("f", f)
                .setParameter("col", colaborador);

        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultarPor(Cliente c) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.cliente  = :c ORDER BY p.fase,p.fase.usuario", Processo.class)
                .setParameter("c", c);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultaLikePor(String numero) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE UPPER(p.numero) like :c ORDER BY p.fase.usuario,p.fase", Processo.class)
                .setParameter("c", "%" + numero.toUpperCase() + "%");
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<ProcessosAtrasadoRelatorioDTO> listaProcessosAtrasadosRelatorio() {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM ProcessosAtrasadoRelatorioDTO p  ORDER BY p.processo.fase.usuario,p.processo.fase", ProcessosAtrasadoRelatorioDTO.class);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<ProcessosAtrasadoRelatorioDTO> listaProcessosAtrasadosRelatorio(Cliente c) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM ProcessosAtrasadoRelatorioDTO p  where p.processo.cliente = :c ORDER BY p.processo.fase.usuario,p.processo.fase", ProcessosAtrasadoRelatorioDTO.class)
                .setParameter("c", c);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<ProcessosAtrasadoRelatorioDTO> listaProcessosAtrasadosRelatorio(Usuario u) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM ProcessosAtrasadoRelatorioDTO p  where p.processo.fase.usuario = :c ORDER BY p.processo.fase.usuario,p.processo.fase", ProcessosAtrasadoRelatorioDTO.class)
                .setParameter("c", u);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> listarFaseNula() {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.fase IS NULL", Processo.class);

        return q.getResultList().isEmpty() ? new ArrayList<>() : q.getResultList();
    }

    public List<Processo> listarFase() {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p ", Processo.class);

        return q.getResultList().isEmpty() ? new ArrayList<>() : q.getResultList();
    }

    public List<Processo> consultaPorColaborador(Colaborador colaborador) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.colaborador  = :c ORDER BY p.colaborador", Processo.class)
                .setParameter("c", colaborador);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultaProcessosPor(Cliente cliente, Colaborador colaborador) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.cliente  = :c AND p.colaborador = :col ORDER BY p.fase,p.fase.usuario", Processo.class)
                .setParameter("c", cliente)
                .setParameter("col", colaborador);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultarLikePor(String numero, Colaborador colaborador) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE UPPER(p.numero) like :c and p.colaborador = :col ORDER BY p.fase.usuario,p.fase", Processo.class)
                .setParameter("c", "%" + numero.toUpperCase() + "%")
                .setParameter("col", colaborador);

        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public Processo carregarPor(Long id, Colaborador colaborador) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.id  = :id AND p.colaborador = :col ", Processo.class)
                .setParameter("id", id)
                .setParameter("col", colaborador);
        if (q.getResultList().isEmpty()) {
            return new Processo();
        } else {
            return (Processo) q.getSingleResult();
        }
    }

    public List<Processo> consultaProcessosPorLike(String nomeCliente) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE UPPER(p.cliente.pessoa.nome) like :c  ORDER BY p.fase.usuario,p.fase", Processo.class)
                .setParameter("c", "%" + nomeCliente.toUpperCase() + "%");

        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultaPorColaborador(Colaborador colaborador, List<Fase> listaFasesSelection) {
        TypedQuery q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.colaborador  = :c AND p.fase IN (:fs) ORDER BY p.colaborador,p.fase", Processo.class)
                .setParameter("c", colaborador)
                .setParameter("fs", listaFasesSelection);
        if (q.getResultList().isEmpty()) {
            return new ArrayList<>();
        } else {
            return q.getResultList();
        }
    }

    public List<Processo> consultarProcessoPor(List<JuizoTribunal> listaDeJuizoTribunaisSelection) {
        TypedQuery<Processo> q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.juizoTribunal IN  (:juiz)  ORDER BY p.juizoTribunal,p.advogado", Processo.class)
                .setParameter("juiz", listaDeJuizoTribunaisSelection);

        return (List<Processo>) (q.getResultList().isEmpty() ? new ArrayList<>() : q.getResultList());
    }

    public List<Processo> consultarProcessoPorEnderecamentos(List<Enderecamento> listaDeEnderecamentos) {
       TypedQuery<Processo> q;
        q = getEm().createQuery("SELECT p FROM Processo p WHERE p.enderecamento IN  (:end)  ORDER BY p.enderecamento,p.advogado", Processo.class)
                .setParameter("end", listaDeEnderecamentos);

        return (List<Processo>) (q.getResultList().isEmpty() ? new ArrayList<>() : q.getResultList());
    }
}


