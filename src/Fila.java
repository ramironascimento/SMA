import static java.util.Comparator.comparingDouble;
import static java.util.Objects.isNull;

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
    this.temposPosicoes.addAll(Collections.nCopies(capacidade + servidores, 0.0));

  }


  public void processaChegada(Evento evento) {

    //evento.getFilaDestino().contabilizaTempoDaFila(evento); //fila 1, TODO fila 2

    //Main.TEMPO_ATUAL_SISTEMA = evento.getTempoAgendado();

    Main.contabilizaFilas(evento);

    if (this.pessoasNaFila < this.filaConfig.getCapacidade()) {
      this.contabilizaPessoasNaFila(+1);
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

    Main.contabilizaFilas(evento);
    Fila filaOrigem = evento.getFilaOrigem();
    Fila filaDestino = evento.getFilaDestino();
    filaOrigem.contabilizaPessoasNaFila(-1);
    if (filaOrigem.getPessoasNaFila() >= filaOrigem.getFilaConfig().getServidores()) {
      filaOrigem.agendaPassagem();
    }
    if (filaDestino.getPessoasNaFila() < filaDestino.getCapacidade()) {
      filaDestino.contabilizaPessoasNaFila(1);
      if (filaDestino.getPessoasNaFila() <= filaDestino.getServidores()) {
        filaDestino.agendaPassagem();

      }
    } else {
      filaDestino.contabilizaPerdas(1);
    }

    evento.setProcessado(true);
  }

  private void contabilizaPerdas(int i) {
    this.perdas=this.perdas+i;
  }

  private Fila escolheProximaFila() {

    Fila filaEscolhida = null;

Main.contador++;
    double random = Math.random(); // = 0.1

    List<Entry<Fila, Double>> entrySetOrdenada = probabilidadeNodosFilho.entrySet()
        .stream()
        .sorted(comparingDouble((Entry<Fila, Double> o) -> o.getValue()).reversed())
        .collect(Collectors.toList());

    double acumulador = 0.0;
    for (int i = 0; i < entrySetOrdenada.size() && isNull(filaEscolhida); i++) {
      if (random < entrySetOrdenada.get(i).getValue() + acumulador) {
        filaEscolhida = entrySetOrdenada.get(i).getKey();
      } else {
        acumulador += entrySetOrdenada.get(i).getValue();
      }
    }
    return filaEscolhida;
  }


  public void processaSaida(Evento evento) {

    //contabilizaTempoDaFila(evento);
    Main.contabilizaFilas(evento);
    //Main.TEMPO_ATUAL_SISTEMA = evento.getTempoAgendado();

    this.contabilizaPessoasNaFila(-1);
    if (this.pessoasNaFila >= this.filaConfig.getServidores()) {
//      agendaSaida();
      agendaPassagem();
    }

    evento.setProcessado(true);
  }


  public void agendaChegada() {

    double tempoSorteado = filaConfig.getTempoSorteadoChegada();

    Processador.registraNovoEvento(TipoAcao.CHEGADA, Main.TEMPO_ATUAL_SISTEMA + tempoSorteado, tempoSorteado, null, this);

  }

  public void agendaSaida() {

    var tempoSorteado = this.filaConfig.getTempoSorteadoSaida();

    Processador.registraNovoEvento(TipoAcao.SAIDA, Main.TEMPO_ATUAL_SISTEMA + tempoSorteado, tempoSorteado, this, null);

  }

  public void agendaPassagem() {

    var tempoSorteado = filaConfig.getTempoSorteadoSaida();
    Fila filaEscolhida = this.escolheProximaFila();
    if (filaEscolhida == null) {
      Processador.registraNovoEvento(TipoAcao.SAIDA, Main.TEMPO_ATUAL_SISTEMA + tempoSorteado, tempoSorteado, this, null);
    } else {
      Processador.registraNovoEvento(TipoAcao.PASSAGEM, Main.TEMPO_ATUAL_SISTEMA + tempoSorteado, tempoSorteado, this, filaEscolhida);
    }

  }

  public void contabilizaPessoasNaFila(int i) {
    this.pessoasNaFila = this.pessoasNaFila + i;
  }

  public void contabilizaTempoDaFila(Evento evento) {
    this.temposPosicoes.set(pessoasNaFila, this.temposPosicoes.get(pessoasNaFila) + evento.getTempoAgendado() -
        Main.TEMPO_ATUAL_SISTEMA);
    //System.out.println(Main.TEMPO_ATUAL_SISTEMA + " -> " + evento.getTipoAcao() + ", " + evento.getFilaOrigem() + " - " + evento.getFilaDestino());
  }


  //region  getters setters toString
  public String toString(boolean arredondado) {
    List<String> collect = null;
    int indexPrimeiroZero = temposPosicoes.indexOf(0.0);
    if (arredondado) {
      collect = temposPosicoes.subList((Math.max((indexPrimeiroZero - 100), 0)), indexPrimeiroZero).stream()
          .map(e -> Double.toString(e).substring(0, 3))
          .collect(Collectors.toList());
    } else {
      collect = temposPosicoes.subList(0, indexPrimeiroZero).stream().map(e -> Double.toString(e))
          .collect(Collectors.toList());
    }
    return "Fila" + idFila + "{" +
        "perda=" + perdas +
        ", pessoas=" + pessoasNaFila +
        ", tempos=" + collect +
        '}';
  }

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

  public void setNodosFilhosESuasRespectivasProbabilidades(Map<Fila, Double> probabilidades) {
    this.nodos = probabilidades.keySet();
    this.probabilidadeNodosFilho = probabilidades;
  }

//endregion
}



