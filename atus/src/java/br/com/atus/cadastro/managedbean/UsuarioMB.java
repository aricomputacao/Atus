/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.atus.cadastro.managedbean;

import br.com.atus.cadastro.controller.AdvogadoController;
import br.com.atus.cadastro.controller.ClienteController;
import br.com.atus.cadastro.controller.ColaboradorController;
import br.com.atus.cadastro.controller.UsuarioController;
import br.com.atus.enumerated.Perfil;
import br.com.atus.util.managedbean.BeanGenerico;
import br.com.atus.util.managedbean.NavegacaoMB;
import br.com.atus.cadastro.modelo.Advogado;
import br.com.atus.cadastro.modelo.Cliente;
import br.com.atus.cadastro.modelo.Colaborador;
import br.com.atus.cadastro.modelo.Usuario;
import br.com.atus.util.AssistentedeRelatorio;
import br.com.atus.util.CriptografiaSenha;
import br.com.atus.util.MenssagemUtil;
import br.com.atus.util.RelatorioSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

/**
 *
 * @author ari
 */
@ManagedBean
@ViewScoped
public class UsuarioMB extends BeanGenerico<Usuario> implements Serializable {

    @Inject
    private NavegacaoMB navegacaoMB;
    @EJB
    private UsuarioController controller;
    @EJB
    private AdvogadoController advogadoController;
    @EJB
    private ClienteController clienteController;
    @EJB
    private ColaboradorController colaboradorController;
    private List<Usuario> listaUsuarios;
    private Usuario usuario;
    private String senhaAtual;
    private String novaSenha;
    private String confirmeSenha;
    private CriptografiaSenha cs;
    private boolean renderAdvogado;
    private boolean renderCliente;
    private boolean renderEstagiario;
    private boolean renderColaborador;
    private boolean renderAdmin;
    private boolean renderNome;
    private Advogado advogado;
    private Colaborador colaborador;
    private Cliente cliente;

    @PostConstruct
    public void init() {
        try {
            cs = new CriptografiaSenha();
            advogado = new Advogado();
            renderCliente = false;
            renderAdvogado = false;
            renderAdmin = true;
            renderColaborador = false;
            renderEstagiario = false;
            renderNome = true;
            usuario = (Usuario) navegacaoMB.getRegistroMapa("usuario", new Usuario());
            if (usuario.getId() == null) {
                usuario.setSenha(cs.criptografarSenha("123456"));
                usuario.setAtivo(true);
            } else {
                if (usuario.getPerfil().equals(Perfil.ADVOGADO)) {
                    advogado = advogadoController.carregar(usuario.getReferencia());
                }
                if (usuario.getPerfil().equals(Perfil.CLIENTE)) {
                    cliente = clienteController.carregar(usuario.getReferencia());
                }
                if (usuario.getPerfil().equals(Perfil.COLABORADOR)) {
                    colaborador = colaboradorController.carregar(usuario.getReferencia());
                }

            }
            listaUsuarios = new ArrayList<>();
        } catch (Exception ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void validateSenhaAtual(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Usuario u = navegacaoMB.getUsuarioLogado();
        senhaAtual = (String) value;
        if (!u.getSenha().equals(cs.criptografarSenha(senhaAtual))) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_WARN, "Senha atual incorreta!", ""));
        }
    }

    public void resetarSenha() {
        try {
            usuario.setSenha(cs.criptografarSenha("123456"));
            controller.atualizar(usuario);
            MenssagemUtil.addMessageInfo("Senha alterada com sucesso!");

        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("falha", MenssagemUtil.MENSAGENS), ex, "Usuario");
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void alterarSenha() {
        try {
            Usuario u = navegacaoMB.getUsuarioLogado();
            controller.alterarSenha(u, senhaAtual, novaSenha, confirmeSenha);
            MenssagemUtil.addMessageInfo("Senha alterada com sucesso!");
        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(ex.getMessage(), ex, this.getClass().getName());
        } finally {
            novaSenha = "";
            confirmeSenha = "";
            senhaAtual = "";
        }
    }

    public void salvar() {
        try {
            if (renderAdvogado) {
                usuario.setReferencia(advogado.getId());
                usuario.setNome(advogado.getNome());
            }
            if (renderColaborador) {
                usuario.setReferencia(colaborador.getId());
                usuario.setNome(colaborador.getPessoa().getNome());
            }
            if (renderCliente) {
                usuario.setReferencia(cliente.getId());
                usuario.setNome(cliente.getPessoa().getNome());
            }
            controller.salvarouAtualizar(usuario);
            init();
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("salvar", MenssagemUtil.MENSAGENS));
        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("falha", MenssagemUtil.MENSAGENS), ex, "Usuario");
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listar() {
        try {
            if (getValorBusca() == null || getValorBusca().equals("")) {
                listaUsuarios = controller.consultarTodos("nome");
            } else {
                listaUsuarios = controller.consultarLike("nome", getValorBusca());
                MenssagemUtil.addMessageWarn(NavegacaoMB.getMsg("consulta.vazia", MenssagemUtil.MENSAGENS));

            }
        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("consulta.vazia", MenssagemUtil.MENSAGENS));
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluir(Usuario ee) {
        try {
            ee = controller.gerenciar(ee.getId());
            controller.excluir(ee);
            listaUsuarios.remove(ee);
            MenssagemUtil.addMessageInfo(NavegacaoMB.getMsg("excluir", MenssagemUtil.MENSAGENS));

        } catch (Exception ex) {
            MenssagemUtil.addMessageErro(NavegacaoMB.getMsg("excluir.falha", MenssagemUtil.MENSAGENS));
            Logger.getLogger(EspecieEventoMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean renderizarAdv() {
        return renderAdvogado;
    }

    public void renderGrd() {
        if (usuario.getPerfil().equals(Perfil.ADMINISTRADOR)) {
            renderCliente = false;
            renderAdvogado = false;
            renderAdmin = true;
            renderColaborador = false;
            renderEstagiario = false;
            renderNome = true;
        }
        if (usuario.getPerfil().equals(Perfil.ADVOGADO)) {
            renderCliente = false;
            renderAdvogado = true;
            renderAdmin = false;
            renderColaborador = false;
            renderEstagiario = false;
            renderNome = false;

        }
        if (usuario.getPerfil().equals(Perfil.CLIENTE)) {
            renderCliente = true;
            renderAdvogado = false;
            renderAdmin = false;
            renderColaborador = false;
            renderEstagiario = false;
            renderNome = false;

        }
        if (usuario.getPerfil().equals(Perfil.COLABORADOR)) {
            renderCliente = false;
            renderAdvogado = false;
            renderAdmin = false;
            renderColaborador = true;
            renderEstagiario = false;
            renderNome = false;

        }
        if (usuario.getPerfil().equals(Perfil.ESTAGIÁRIO)) {
            renderCliente = false;
            renderAdvogado = false;
            renderAdmin = false;
            renderColaborador = false;
            renderEstagiario = true;
            renderNome = true;

        }
    }

    public void imprimir() {
        if (!listaUsuarios.isEmpty()) {
            Map<String, Object> m = new HashMap<>();
            byte[] rel = new AssistentedeRelatorio().relatorioemByte(listaUsuarios, m, "WEB-INF/relatorios/rel_tipo_contrato.jasper", "Relatório de Tipos de Contratos");
            RelatorioSession.setBytesRelatorioInSession(rel);
        }

    }

    public Perfil[] listaPerfis() {
        return Perfil.values();
    }

    public UsuarioMB() {
        super(Usuario.class);
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmeSenha() {
        return confirmeSenha;
    }

    public void setConfirmeSenha(String confirmeSenha) {
        this.confirmeSenha = confirmeSenha;
    }

    public boolean isRenderAdvogado() {
        return renderAdvogado;
    }

    public void setRenderAdvogado(boolean renderAdvogado) {
        this.renderAdvogado = renderAdvogado;
    }

    public boolean isRenderCliente() {
        return renderCliente;
    }

    public void setRenderCliente(boolean renderCliente) {
        this.renderCliente = renderCliente;
    }

    public boolean isRenderEstagiario() {
        return renderEstagiario;
    }

    public void setRenderEstagiario(boolean renderEstagiario) {
        this.renderEstagiario = renderEstagiario;
    }

    public boolean isRenderColaborador() {
        return renderColaborador;
    }

    public void setRenderColaborador(boolean renderColaborador) {
        this.renderColaborador = renderColaborador;
    }

    public boolean isRenderAdmin() {
        return renderAdmin;
    }

    public void setRenderAdmin(boolean renderAdmin) {
        this.renderAdmin = renderAdmin;
    }

    public Advogado getAdvogado() {
        return advogado;
    }

    public void setAdvogado(Advogado advogado) {
        this.advogado = advogado;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean isRenderNome() {
        return renderNome;
    }

    public void setRenderNome(boolean renderNome) {
        this.renderNome = renderNome;
    }

}
