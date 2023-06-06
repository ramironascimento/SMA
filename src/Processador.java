import java.util.Optional;

public class Processador {

  private static double tempoAtualSistema;
  private static boolean condicaoParada;
  private static Escalonador escalonador;

  public static void configProcessador(Escalonador escalonador1) {
    escalonador = escalonador1;
    condicaoParada = false;
    tempoAtualSistema = 0;
  }

  public static void start() {

    while (!condicaoParada) {

      //escalonador.printRegistros();

      if(escalonador.getQuantidadeEventosProcessados() >=100000){
        condicaoParada=true;
      }

      Optional<Evento> registroAtual = escalonador.getNext();

      registroAtual.ifPresentOrElse(Evento::processa, () -> condicaoParada = true);
    }
  }

  public static void close() {
    //escalonador.printRegistros();

    System.out.println("AGORITMO FINALIZADO:");
    System.out.println("## RESULTADOS ##");

    System.out.println("Tempo atual sistema: " + tempoAtualSistema);
//    System.out.println(Processador.filaInicial.toString());

//    System.out.println(Processador.filaInicial.getFilasFilho().get(0).toString());
  }

  public static void registraNovoEvento(TipoAcao chegada, double tempoAtual, double tempoSortiado, Fila filaOrigem, Fila filaDestino) {

    escalonador.registraNovoEvento(chegada, tempoAtual, tempoSortiado, filaOrigem, filaDestino);

  }
}
