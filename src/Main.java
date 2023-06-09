import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static double TEMPO_ATUAL_SISTEMA = 0;
    public static List<Fila> filas;
    public static int contador = 0;

    public static void main(String[] args) {

        //instancia as 3 filas
        Fila fila1 = new Fila(1, 1000000, 1, 1, 4, 1, 1.5);
        Fila fila2 = new Fila(2, 5, 3, 0, 0, 5, 10);
        Fila fila3 = new Fila(3,8,2,0,0,10,20);

        //faz as ligacoes
        fila1.setNodosFilhosESuasRespectivasProbabilidades(Map.of(fila2,0.8,fila3,0.2));
        fila2.setNodosFilhosESuasRespectivasProbabilidades(Map.of(fila1,0.3,fila3,0.5));
        fila3.setNodosFilhosESuasRespectivasProbabilidades(Map.of(fila2,0.7));
        //obs: a porcentagem que falta para completar 100% é saida da fila

        filas = new ArrayList<>();
        filas.addAll(List.of(fila1,fila2,fila3));

        //configura ambiente
        Escalonador escalonador = new Escalonador(TipoAcao.CHEGADA, 1.0, null,fila1);
        Processador.configProcessador(escalonador);

        //inicia
        Processador.start();

        Processador.close();


        System.out.println("======================================================================");
        System.out.println("================================ REPORT ==============================");
        System.out.println("======================================================================");
        System.out.println("**********************************************************************");
        for(Fila fila:List.of(fila1, fila2,fila3)){
            System.out.println("Queue: " +fila.getIdFila() + " (G/G/"+fila.getFilaConfig().getServidores()+"/"+fila.getFilaConfig().getCapacidade()+")");
            System.out.println("Arrival:" + fila.getFilaConfig().getTempoMinChegada() + " ... "+fila.getFilaConfig().getTempoMaxChegada());
            System.out.println("Service:" + fila.getFilaConfig().getTempoMinSaida() + " ... "+fila.getFilaConfig().getTempoMaxSaida());
        }


        System.out.println("\n\nALGORITMO FINALIZADO:");
        System.out.println("## RESULTADOS ##");

        System.out.println("fila1 = " + fila1.toString(false));
        System.out.println("fila2 = " + fila2.toString(false));
        System.out.println("fila3 = " + fila3.toString(false));
        System.out.println("\nTempo atual sistema: " + TEMPO_ATUAL_SISTEMA);
    }
    public static void contabilizaFilas(Evento evento){
        Main.filas.forEach(fila -> fila.contabilizaTempoDaFila(evento));
        Main.TEMPO_ATUAL_SISTEMA = evento.getTempoAgendado();
    }
}