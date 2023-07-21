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
    int[] prioridade = new int [n_processos];

    processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada,prioridade);
    imprimir(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada,prioridade);

    while (true) {

      System.out.print(
          "Escolha o algoritmo?: [1=FCFS 2=SJF Preemptivo 3=SJF Não Preemptivo  4=Prioridade Preemptivo 5=Prioridade Não Preemptivo  6=Round Robin  7=Imprime lista de processos 8=Popular processos novamente 9=Sair]: ");

      int alg = teclado.nextInt();

      if (alg == 1) { // FCFS
        FCFS(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 2) { // SJF PREEMPTIVO
        SJF(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 3) { // SJF NÃO PREEMPTIVO
        SJF(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada);
      } else if (alg == 4) { //PRIORIDADE PREEMPTIVO
        prioridade(true, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada,prioridade);
      } else if (alg == 5) { // PRIORIDADE NÃO PREEMPTIVO
        prioridade(false, tempo_execucao, tempo_espera, tempo_restante, tempo_chegada,prioridade);
      } else if (alg == 6) { // Round_Robin
        Round_Robin(tempo_execucao, tempo_espera, tempo_restante);
      } else if (alg == 7) { // IMPRIME CONTEÚDO INICIAL DOS PROCESSOS
        processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada,prioridade);
      }else if (alg == 8) { // ATRIBUI VALORES INICIAIS
        processos(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada,prioridade);
        imprimir(tempo_execucao, tempo_espera, tempo_restante, tempo_chegada,prioridade);
      } else if (alg == 9) {
        break;
      }
    }
  }

  public static void processos(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada, int [] prioridade) {

    Random random = new Random();
    Scanner entrada = new Scanner(System.in);

    System.out.print("Será aleatório?: [1=sim 0=não] ");
    int aleatorio = entrada.nextInt();

    for (int i = 0; i < n_processos; i++) {

      if (aleatorio == 1) {
        tempo_execucao[i] = random.nextInt(10) + 1;
        tempo_chegada[i] = random.nextInt(10) + 1;
        prioridade[i] = random.nextInt(15) + 1;

      } else {
        System.out.printf("Digite o tempo de execução do processo[%d]:  ",i);
        tempo_execucao[i] = entrada.nextInt();
        System.out.printf("Digite o tempo de chegada do processo[%d]:  ",i);
        tempo_chegada[i] = entrada.nextInt();
        System.out.printf("Digite a prioridade do processo[%d]:  ",i);
        prioridade[i] = entrada.nextInt();
      }
      
      tempo_restante[i] = tempo_execucao[i];

    }
    System.out.println();
  }

  public static void imprimir(int[] tempo_execucao, int[] tempo_espera, int[] tempo_restante, int[] tempo_chegada,int []prioridade) {
    
    for (int i = 0; i < n_processos; i++) {
      
      System.out.printf("Processo[%d]: tempo execucao= %d tempo restante= %d tempo chegada= %d prioridade %d \n",i,tempo_execucao[i],tempo_restante[i],tempo_chegada[i],prioridade[i]);
      
    }
    System.out.println();
  }

  public static void imprime_stats(int[] espera) {
    int[] tempo_espera = espera.clone();
    double tempo_medio;
    int tempo=0;
    
    System.out.println();
    for (int i = 0; i < n_processos; i++) {
      System.out.printf("Processo[%d]: tempo_espera = %d \n",i,(tempo_espera[i]));
      tempo += tempo_espera[i];
    }
    tempo_medio = (double)tempo;
    tempo_medio = tempo_medio/n_processos;
    System.out.printf("Tempo médio de espera: %.1f \n", (tempo_medio));
    
  }

  public static void FCFS(int[] tmExecucao, int[] espera, int[] restante, int[] chegada) {

    int[] tempo_execucao = tmExecucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    tempo_espera[0]=0;
    
    System.out.println();
    for (int proc = 0;proc < n_processos;) {  
      for (int tempo = 1;tempo < tempo_maximo;) {
        for (int execucao = tempo_execucao[proc];execucao != 0;execucao--) { //vai diminuindo o tempo de execucao
          System.out.printf("tempo[%d]: processo [%d] restante %d \n", tempo, proc, execucao);
           tempo++;
        }
        proc++; //muda o processo
        if(proc==n_processos){ //se já acabou os processos
		    	break;
		    }else
          tempo_espera[proc]= tempo; //contabiliza o tempo de espera
      }  
    }
    imprime_stats(tempo_espera);
    System.out.println();
    
  }

  public static void SJF(boolean preemptivo, int[] execucao, int[] espera, int[] restante, int[] chegada) {

    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    int processo_em_execucao = -1;
    int proc_terminados = 0;
    int menor_tempo_restante = tempo_maximo;
    tempo_espera[0]=0;
    
    System.out.println();
    for(int tempo=0; tempo < tempo_maximo; tempo++){  
      // escolha de qual processo irá executar
      // se preemptivo, sempre entrar no for, se não preemptivo, testa se tem algum processo em execução
      if ((preemptivo) || ((!preemptivo) && (processo_em_execucao == -1))) {
        //varre a lista de processos para ver qual processo já chegou e tem o menor tempo de execucao
        for (int proc=0; proc<n_processos; proc++) {
          //se o processo ainda não começou sua execução (tempo_restante[proc] != 0) e o tempo de chegada for menor ou igual ao instante de tempo atual entra no IF para ver qual é o menor tempo de execução
          if ((tempo_restante[proc] != 0) && (tempo_chegada[proc] <= tempo)) {
            // testa para saber se o tempo de execução é menor do que o menor tempo já registrado
            if (tempo_restante[proc] < menor_tempo_restante) {
              menor_tempo_restante = tempo_restante[proc];
              processo_em_execucao = proc;
            }
          }
        }
      }
      //se a saída do laço anterior resultar em processo_em_execução = -1, significa que nenhum processo está pronto
      if (processo_em_execucao == -1) 
        System.out.printf("tempo[%d]: nenhum processo está pronto \n",tempo);
        //neste caso algum processo foi escolhido e iniciará sua execução até o fim
        
        else {
          
        //registra o tempo de espera do processo escolhido 
        if (tempo_restante[processo_em_execucao] == tempo_execucao[processo_em_execucao])
          tempo_espera[processo_em_execucao] = tempo - tempo_chegada[processo_em_execucao];
                
          //decrementa o tempo restante de execução do processo
          tempo_restante[processo_em_execucao]--;
                
          //imprime em tela as informações do processo em execução
          System.out.printf("tempo[%d]: processo[%d] restante= %d \n",tempo,processo_em_execucao,tempo_restante[processo_em_execucao]);
                
          // se já executou todo o tempo necessário, então seta as variáveis de controle para os valores iniciais, assim força a entrar no laço de escolha de processo para executar
          if (tempo_restante[processo_em_execucao] == 0) {
            processo_em_execucao = -1;
            menor_tempo_restante = tempo_maximo;
            proc_terminados++;
            //se o número de processos terminado for igual ao número de processos total, termina a aplicação
            if (proc_terminados == n_processos)
              break;
                    
          }    
        }
    }
    imprime_stats(tempo_espera);
    System.out.println();
  }

   public static void prioridade(boolean preemptivo,int[] execucao, int[] espera, int[] restante,int[] chegada,int [] temp_prioridade) {

    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();
    int[] tempo_chegada = chegada.clone();
    int[] prioridade = temp_prioridade.clone();
    int maior_prioridade = 0;
    int processo_em_execucao = -1;
    int proc_terminados = 0;

    for (int tempo = 1; tempo<tempo_maximo; tempo++) {
      // se preemptivo, sempre entrar no for, se não preemptivo, testa se tem algum processo em execução
      if ((preemptivo) || ((!preemptivo) && (processo_em_execucao == -1))) {
        //varre a lista de processos para ver qual processo já chegou e tem o menor tempo de execucao
        for (int proc=0; proc<n_processos; proc++) {
          //se o processo ainda não começou sua execução e o tempo de chegada for menor ou igual ao instante de tempo atual entra no IF para ver qual é o menor tempo de execução
          if ((tempo_restante[proc] != 0) && (tempo_chegada[proc] <= tempo)) {
            // testa para saber qual processo tem a maior prioridade
            if (prioridade[proc] > maior_prioridade) {
              maior_prioridade = prioridade[proc];
              processo_em_execucao = proc;
            }
          }
        }
      }      
      //se a saída do laço anterior resultar em processo_em_execução = -1, significa que nenhum processo está pronto
      if (processo_em_execucao == -1) 
        System.out.printf("tempo[%d]: nenhum processo está pronto \n",tempo);
            //neste caso algum processo foi escolhido e iniciará sua execução até o fim
            else {
                
              //registra o tempo de espera do processo escolhido (somente na primeira passada)
              if (tempo_restante[processo_em_execucao] == tempo_execucao[processo_em_execucao])
                tempo_espera[processo_em_execucao] = tempo - tempo_chegada[processo_em_execucao];
                
                //decrementa o tempo restante de execução do processo
                tempo_restante[processo_em_execucao]--;
                
                //imprime em tela as informações do processo em execução
                System.out.printf("tempo[%d]: processo[%d] restante=%d \n",tempo,processo_em_execucao,tempo_restante[processo_em_execucao]);
                
                // se já executou todo o tempo necessário, então seta as variáveis de controle para os valores iniciais, assim força a entrar no laço de escolha de processo para executar
                if (tempo_restante[processo_em_execucao] == 0) {
                    processo_em_execucao = -1;
                    maior_prioridade = 0;
                    proc_terminados++;
                    //se o número de processos terminado for igual ao número de processos total, termina a aplicação
                    if (proc_terminados == n_processos)
                        break;
                    
                }    
            }
          }

    imprime_stats(tempo_espera);
  }

  public static void Round_Robin(int[] execucao, int[] espera, int[] restante) {

    int[] tempo_execucao = execucao.clone();
    int[] tempo_espera = espera.clone();
    int[] tempo_restante = restante.clone();

  

    imprime_stats(tempo_espera);
  }
}