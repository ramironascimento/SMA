import static java.util.Objects.isNull;

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

  public Fila getFilaDestino() {
    return filaDestino;
  }

  public void processa() {
//    System.out.println("-> PROCESSA " + this.tipoAcao + " tempoAtual: " +Main.TEMPO_ATUAL_SISTEMA + ", origem="+(isNull(filaOrigem)?null:filaOrigem.toString(true))+", destino="+(isNull(filaDestino)?null:filaDestino.toString(true)));

    switch (this.tipoAcao) {
      case CHEGADA:
        this.filaDestino.processaChegada(this);
        break;
      case PASSAGEM:
        this.filaOrigem.processaPassagem(this);
        break;
      case SAIDA:
        this.filaOrigem.processaSaida(this);
        break;
    }
  }

  @Override
  public String toString() {
    return "Evento{" +
        "tipoAcao=" + tipoAcao +
        ", filaOrigem=" + filaOrigem +
        ", filaDestino=" + filaDestino +
        '}';
  }
}