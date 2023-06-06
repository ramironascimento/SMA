import java.util.Map;

public class Main {

    public static double TEMPO_ATUAL_SISTEMA = 0;

    public static void main(String[] args) {

        Fila fila1 = new Fila(1, 1, 1, 1, 4, 1, 1.5);
        Fila fila2 = new Fila(2, 5, 3, 0, 0, 5, 10);
        Fila fila3 = new Fila(3,8,2,0,0,10,20);

        fila1.ligaNodosFilhos(Map.of(fila2,0.8,fila3,0.2));
        fila2.ligaNodosFilhos(Map.of(fila1,0.3,fila3,0.5));
        fila3.ligaNodosFilhos(Map.of(fila2,0.7));

        Escalonador escalonador = new Escalonador(TipoAcao.CHEGADA, 2.5, fila1, null);
        Processador.configProcessador(escalonador);
        Processador.start();

        Processador.close();
    }
}