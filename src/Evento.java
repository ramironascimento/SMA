public class Evento {

  private boolean processado;
  private TipoAcao tipoAcao;
  private double tempoAgendado;
  private double tempoSorteado;
  private Fila filaOrigem;
  private Fila filaDestino;

  public Evento(boolean processado, TipoAcao tipoAcao, double tempoAtual,
      double tempoSorteado, Fila filaOrigem, Fila filaDestino) {
    this.processado = processado;
    this.tipoAcao = tipoAcao;
    this.tempoAgendado = tempoAtual;
    this.tempoSorteado = tempoSorteado;
    this.filaOrigem = filaOrigem;
    this.filaDestino = filaDestino;
  }


  public void setProcessado(boolean processado) {
    this.processado = processado;
  }

  public double getTempoAgendado() {
    return tempoAgendado;
  }

  public Fila getFilaOrigem() {
    return filaOrigem;
  }

  public boolean isProcessado() {
    return processado;
  }

  public TipoAcao getTipoAcao() {
    return tipoAcao;
  }

  public void setTipoAcao(TipoAcao tipoAcao) {
    this.tipoAcao = tipoAcao;
  }

  public void setTempoAgendado(double tempoAgendado) {
    this.tempoAgendado = tempoAgendado;
  }

  public double getTempoSorteado() {
    return tempoSorteado;
  }

  public void setTempoSorteado(double tempoSorteado) {
    this.tempoSorteado = tempoSorteado;
  }

  public void setFilaOrigem(Fila filaOrigem) {
    this.filaOrigem = filaOrigem;
  }

  public Fila getFilaDestino() {
    return filaDestino;
  }

  public void processa() {
    switch (this.tipoAcao) {
      case CHEGADA:
        this.filaOrigem.processaChegada(this);
        break;
      case PASSAGEM:
        this.filaOrigem.processaPassagem(this);
      case SAIDA:
        this.filaOrigem.processaSaida(this);
        break;
    }
  }
}