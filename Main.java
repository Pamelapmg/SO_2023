import java.util.Scanner;
import java.util.Random;

public class Main {
  static int tempo_maximo = 65535;
  static int n_processos = 3;
  int[] pc = new int[n_processos];

  public static void main(String[] args) {
    Scanner teclado = new Scanner(System.in);

    int[] tempo_execucao = new int[n_processos];
    int[] tempo_chegada = new int[n_processos];
    int[] tempo_espera = new int[n_processos];
    int[] tempo_restante = new int[n_processos];

    processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
    imprimir(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);

    while (true) {

      System.out.print(
          "Escolha o algoritmo?: [1=FCFS 2=SJF Preemptivo 3=SJF Não Preemptivo 4=Round Robin  5=Imprime lista de processos 6=Popular processos novamente 7=Sair]: ");

      int alg = teclado.nextInt();

      if (alg == 1) { // FCFS
        FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 2) { // SJF PREEMPTIVO
        SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 3) { // SJF NÃO PREEMPTIVO
        SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 4) { // Round_Robin
        Round_Robin(tempo_execucao, tempo_espera, tempo_restante);
      } else if (alg == 5) { // IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
        processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 6) { // ATRIBUI VALORES INICIAIS
        processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
        imprimir(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 7) {
        break;
      }
    }
  }

  public static void processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada) {

    Random random = new Random();
    Scanner teclado = new Scanner(System.in);

    System.out.print("Será aleatório?: [1=sim 0=não] ");
    int aleatorio = teclado.nextInt();

    for (int i = 0; i < n_processos; i++) {

      if (aleatorio == 1) {
        tempo_execucao[i] = random.nextInt(10) + 1;
        tempo_chegada[i] = random.nextInt(10) + 1;
      } else {
        System.out.print("Digite o tempo de execução do processo[" + i + "]:  ");
        tempo_execucao[i] = teclado.nextInt();
        System.out.print("Digite o tempo de chegada do processo[" + i + "]:  ");
        tempo_chegada[i] = teclado.nextInt();
      }

      tempo_restante[i] = tempo_execucao[i];

    }
  }

  public static void imprimir(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada) {

    for (int i = 0; i < n_processos; i++) {
      
      System.out.printf("Processo[%d]: tempo execucao= %d tempo restante= %d tempo chegada= %d \n",i,tempo_execucao[i],tempo_restante[i],tempo_chegada[i]);
      
    }
    
  }

  public static void imprime_stats(int[] espera) {

    int[] tempo_espera = espera.clone();

    // Implementar o cálculo e impressão de estatísticas

  }

  public static void FCFS(int[] execucao, int[] espera, int[] restante, int[] chegada) {

    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();

    int i = 0;  
    while (i < n_processos) {
      int t = 1;
      while (t < tempo_maximo) {
        int r = tempo_execucao[i]-1;
        while (r != -1) {
          System.out.printf("tempo[%d]: processo [%d] restante %d \n", t, i, r);
          t++;
          r--;
        }
        i++;
        if(i==n_processos){
		    	break;
		    }
      }  
    }
    
    imprime_stats(tempo_espera);
  }

  public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada) {

    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();

    // preemptivo, pega o primeiro e ve se chegou algum
    int i = 0;
    while (i < n_processos) {
      int t = 1;
      while (t < tempo_maximo) {
        int r = tempo_execucao[i]-1;
        while (r != -1) {
          System.out.printf("tempo[%d]: processo [%d] restante %d \n", t, i, r);
          t++;
          r--;
        }
        if (tempo_chegada[i] == (tempo_execucao[i])) {

        }
        i++;

      }
    }

    imprime_stats(tempo_espera);

  }

  public static void Round_Robin(int[] execucao, int[] espera, int[] restante) {

    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();

    // implementar código do Round-Robin

    imprime_stats(tempo_espera);
  }
}