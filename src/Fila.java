import static java.util.Comparator.comparingDouble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Fila {

  private int idFila;
  private int perdas;
  private int pessoasNaFila;
  private List<Double> temposPosicoes;
  private Set<Fila> nodos;
  private Map<Fila, Double> probabilidadeNodosFilho;

  private FilaConfig filaConfig;

  public Fila(int idFila, int capacidade, int servidores, double minChegada, double maxChegada, double minSaida, double maxSaida) {
    this.perdas = 0;
    this.pessoasNaFila = 0;

    this.idFila = idFila;
    this.temposPosicoes = new ArrayList<>(servidores + capacidade);
    this.filaConfig = new FilaConfig(minChegada, maxChegada, minSaida, maxSaida, capacidade, servidores);
    this.temposPosicoes.addAll(Collections.nCopies(10, 0.0));

  }


  public void processaChegada(Evento evento) {

    contabilizaTempo(evento);

    if (this.pessoasNaFila < this.filaConfig.getCapacidade()) {
      this.pessoasNaFila++;
      if (this.pessoasNaFila <= this.filaConfig.getServidores()) {
        if (this.nodos.isEmpty()) {
          agendaSaida();
        } else {
          agendaPassagem();
        }
      }
    } else {
      this.perdas++;
    }
    evento.setProcessado(true);

    agendaChegada();
  }

  public void processaPassagem(Evento evento) {
    contabilizaTempo(evento);

    Fila filaOrigem = evento.getFilaOrigem();
    Fila filaDestino = evento.getFilaDestino();
    filaOrigem.contabilizaPessoasNaFila(-1);
    if (filaOrigem.getPessoasNaFila() >= filaOrigem.getFilaConfig().getServidores()) {
      filaOrigem.agendaPassagem();
    }

    //TODO Fila filaDestino = filaOrigem.getProximaFila();
    // IMPLEMENTAR PROBABILIDADE DE IR PARA CADA FILA
    if (filaDestino.getPessoasNaFila() < filaDestino.getCapacidade()) {
      filaDestino.contabilizaPessoasNaFila(1);
      if (filaDestino.getPessoasNaFila() <= filaDestino.getServidores()) {
        filaDestino.agendaSaida();
      }
    }

    evento.setProcessado(true);
  }


  public void processaSaida(Evento evento) {

    contabilizaTempo(evento);

    this.pessoasNaFila--;
    if (this.pessoasNaFila >= this.filaConfig.getServidores()) {
      agendaSaida();
    }

    evento.setProcessado(true);
  }


  public void agendaChegada() {
    double tempoSorteado = filaConfig.getTempoSorteado();

    Processador.registraNovoEvento(TipoAcao.CHEGADA, Main.TEMPO_ATUAL_SISTEMA + tempoSorteado, tempoSorteado, this, null);

  }

  public void agendaSaida() {

    var tempoSortiado = filaConfig.getTempoSorteado();

    Processador.registraNovoEvento(TipoAcao.SAIDA, Main.TEMPO_ATUAL_SISTEMA + tempoSortiado, tempoSortiado, this, null);

  }

  public void agendaPassagem() {

    var tempoSortiado = filaConfig.getTempoSorteado();

    double random = Math.random(); // = 0.1

    List<Entry<Fila, Double>> entrySetOrdenada = probabilidadeNodosFilho.entrySet()
        .stream()
        .sorted(comparingDouble((Entry<Fila, Double> o) -> o.getValue()).reversed())
        .collect(Collectors.toList());

    Fila filaEscolhida = null; //TODO FAZER ALGORITMO PARA PEGAR A PROXIMA FILA

    double acumulador = 0.0;
    for (int i = 0; i < entrySetOrdenada.size(); i++) {
      if (random < entrySetOrdenada.get(i).getValue() + acumulador) {
        filaEscolhida = entrySetOrdenada.get(i).getKey();
      } else {
        acumulador += entrySetOrdenada.get(i).getValue();
      }
    }

    TipoAcao acao;
    if (filaEscolhida == null) {
      acao = TipoAcao.SAIDA;
    } else {
      acao = TipoAcao.CHEGADA;
    }

    Processador.registraNovoEvento(acao, Main.TEMPO_ATUAL_SISTEMA + tempoSortiado, tempoSortiado, this, filaEscolhida);
  }

  public void contabilizaPessoasNaFila(int i) {
    this.pessoasNaFila = +i;
  }

  @Override
  public String toString() {
    return "Fila " + idFila + "{" +
        "perda=" + perdas +
        ", pessoas=" + pessoasNaFila +
        ", tempos=" + temposPosicoes +
        '}';
  }

  private void contabilizaTempo(Evento evento) {
    this.temposPosicoes.set(pessoasNaFila, this.temposPosicoes.get(pessoasNaFila) + evento.getTempoAgendado() -
        Main.TEMPO_ATUAL_SISTEMA);
    Main.TEMPO_ATUAL_SISTEMA = evento.getTempoAgendado();
  }

  //region  getters setters

  public Map<Fila, Double> getProbabilidadeNodosFilho() {
    return probabilidadeNodosFilho;
  }

  public void setProbabilidadeNodosFilho(Map<Fila, Double> probabilidadeNodosFilho) {
    this.probabilidadeNodosFilho = probabilidadeNodosFilho;
  }

  public int getPessoasNaFila() {
    return pessoasNaFila;
  }

  public void setPessoasNaFila(int pessoasNaFila) {
    this.pessoasNaFila = pessoasNaFila;
  }

  public int getIdFila() {
    return idFila;
  }

  public void setIdFila(int idFila) {
    this.idFila = idFila;
  }

  public int getPerdas() {
    return perdas;
  }

  public void setPerdas(int perdas) {
    this.perdas = perdas;
  }

  public List<Double> getTemposPosicoes() {
    return temposPosicoes;
  }

  public void setTemposPosicoes(List<Double> temposPosicoes) {
    this.temposPosicoes = temposPosicoes;
  }

  public Set<Fila> getNodos() {
    return nodos;
  }

  public void setNodos(Set<Fila> nodos) {
    this.nodos = nodos;
  }

  public FilaConfig getFilaConfig() {
    return filaConfig;
  }

  public void setFilaConfig(FilaConfig filaConfig) {
    this.filaConfig = filaConfig;
  }

  private int getServidores() {
    return this.filaConfig.getServidores();
  }

  private int getCapacidade() {
    return this.filaConfig.getCapacidade();
  }

  public void ligaNodosFilhos(Map<Fila, Double> probabilidades) {
    this.nodos = probabilidades.keySet();
    this.probabilidadeNodosFilho = probabilidades;
  }

//endregion
}



