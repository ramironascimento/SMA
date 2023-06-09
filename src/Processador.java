import static java.util.Objects.isNull;

import java.util.Optional;

public class Processador {

  private static boolean condicaoParada;
  private static Escalonador escalonador;

  public static void configProcessador(Escalonador escalonador1) {
    escalonador = escalonador1;
    condicaoParada = false;
  }

  public static void start() {

    while (!condicaoParada) {

      if (condicaoDeParada()) {
        condicaoParada = true;
      }

      //pega o proximo evento nao executado que tenha o menor tempo
      Optional<Evento> registroAtual = escalonador.getNext();


      registroAtual.ifPresentOrElse(
          Evento::processa,
          () -> condicaoParada = true);
    }
  }

  private static boolean condicaoDeParada() {
    return Main.contador >= 100000;
  }

  public static void close() {



    //escalonador.printRegistros();


//    System.out.println(Processador.filaInicial.getFilasFilho().get(0).toString());
  }

  public static void registraNovoEvento(TipoAcao tipoAcao, double tempoAtual, double tempoSorteado, Fila filaOrigem, Fila filaDestino) {

//    System.out.println("*agenda* " + tipoAcao +",tempoAtualSistema="+Double.toString(tempoAtual-tempoSorteado).substring(0,3) + ",tempoQueSeraRodado=" + Double.toString(tempoAtual).substring(0,3) + ",tempoSorteado=" + Double.toString(tempoSorteado).substring(0,3) + ",filaOrigem=" + (isNull(filaOrigem)?null:filaOrigem.toString(true)) + ", filaDestino = " + (isNull(filaDestino)?null:filaDestino.toString(true)));
    escalonador.registraNovoEvento(tipoAcao, tempoAtual, tempoSorteado, filaOrigem, filaDestino);

  }
}
