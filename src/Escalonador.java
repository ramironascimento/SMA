import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;

public class Escalonador {

  private PriorityQueue<Evento> eventosNaoProcessados;
  private ArrayList<Evento> eventosProcessados;

  public Escalonador(TipoAcao tipoAcao, double tempoAtual, Fila filaInicial,Fila filaDestino) {
    this.eventosNaoProcessados = new PriorityQueue<>(getEventoComparator());
    this.eventosNaoProcessados.add(new Evento(false, tipoAcao, tempoAtual, 0, filaInicial,filaDestino));
    this.eventosProcessados = new ArrayList<>();
  }

  private static Comparator<Evento> getEventoComparator() {
    return (evento1, evento2) -> {
      if (evento1.getTempoAgendado() > evento2.getTempoAgendado()) {
        return 1;
      } else if (evento1.getTempoAgendado() < evento2.getTempoAgendado()) {
        return -1;
      }
      return 0;
    };
  }

  public Optional<Evento> getNext(){
    Evento proximo = eventosNaoProcessados.poll();
    eventosProcessados.add(proximo);
    return Optional.ofNullable(proximo);
  }

  public void registraNovoEvento(TipoAcao tipoAcao, double tempoAtual, double tempoSorteado, Fila filaOrigem, Fila filaDestino) {
      eventosNaoProcessados.add(new Evento(false,tipoAcao,tempoAtual,tempoSorteado,filaOrigem,filaDestino));
  }

  public int getQuantidadeEventosProcessados() {
    return eventosProcessados.size();
  }
}