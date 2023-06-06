public class FilaConfig {

  private final double tempoMinChegada;
  private final double tempoMaxChegada;

  private final double tempoMinSaida;
  private final double tempoMaxSaida;

  private final int capacidade;
  private final int servidores;

  public FilaConfig(double tempoMinChegada, double tempoMaxChegada, double tempoMinSaida, double tempoMaxSaida,
      int capacidade, int servidores) {
    this.tempoMinChegada = tempoMinChegada;
    this.tempoMaxChegada = tempoMaxChegada;
    this.tempoMinSaida = tempoMinSaida;
    this.tempoMaxSaida = tempoMaxSaida;
    this.capacidade = capacidade;
    this.servidores = servidores;
  }

  public double getTempoSorteado(){
    return tempoMinChegada + (Math.random() * this.tempoMaxChegada
        - tempoMinChegada);
  }

  public double getTempoMinChegada() {
    return tempoMinChegada;
  }

  public double getTempoMaxChegada() {
    return tempoMaxChegada;
  }

  public double getTempoMinSaida() {
    return tempoMinSaida;
  }

  public double getTempoMaxSaida() {
    return tempoMaxSaida;
  }

  public int getCapacidade() {
    return capacidade;
  }

  public int getServidores() {
    return servidores;
  }
}
