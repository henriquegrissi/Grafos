import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {
    static Scanner teclado = new Scanner(System.in);

    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Enter para continuar.");
        teclado.nextLine();
    }

    public static int menuGrafos() {
        limparTela();
        System.out.println("Menu");
        System.out.println("==========================");
        System.out.println("1 - Fazer pesquisa em profundidade");
        System.out.println("2 - Fazer pesquisa em largura");
        System.out.println("3 - Encontra o caminho minimo entre 2 vertices");
        System.out.println("4 - Retornar o grau e vizinhos de um vertice especifico");
        System.out.println("5 - Realizar a subtracao de um vertice/aresta");
        System.out.println("6 - Gerar a AGM a partir de um vertice (Metodo de Prim)");
        System.out.println("0 - Sair");
        System.out.print("\nDigite sua opção: ");
        int opcao = Integer.parseInt(teclado.nextLine());

        return opcao;
    }

    public static void main(String[] args) throws Exception {
        limparTela();

        String verticeEntradaTeclado;

        GrafoMutavel grafoMutavel = new GrafoMutavel("");

        try {
            grafoMutavel.carregar();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não existente, você deve primeiro gerar e salvar o grafo " + e);
        }

        try {
            grafoMutavel.salvar("Grafo cidades");
        } catch (Exception e) {
            System.out.println("Não existe grafo para ser salvo, você deve primeiro gerar o grafo " + e);
        }

        int opcao = -1;

        do {
            opcao = menuGrafos();
            limparTela();
            switch (opcao) {
                case 1:
                    verticeEntradaTeclado = "";
                    System.out.println("\nDigite o vertice: ");
                    verticeEntradaTeclado = teclado.nextLine();
                    System.out.println("Raizes da pesquisa em profundidade: ");
                    System.out.println(grafoMutavel.dfs(Integer.parseInt(verticeEntradaTeclado)));
                    break;

                case 2:
                    System.out.println("\nDigite o vertice: ");
                    verticeEntradaTeclado = teclado.nextLine();
                    System.out.println("Raizes da pesquisa em largura: ");
                    System.out.println(grafoMutavel.bfs(Integer.parseInt(verticeEntradaTeclado)));
                    break;
                
                case 3:
                    caminhoMinimo(grafoMutavel);
                    break;

                case 4:
                    System.out.println(grafoMutavel.stringListaVertices());

                    System.out.print("ID do vértice para exibir o grau e lista de vizinhos: ");
                    int idVertice = Integer.parseInt(teclado.nextLine());
                    System.out.println(grafoMutavel.retornaGrauEVizinhosDeUmVertice(idVertice));
                    break;

                case 5:
                    //subtração de vértice aresta
                    break;

                case 6:
                    System.out.println(grafoMutavel.stringListaVertices());

                    System.out.print("ID do vértice para gerar a arvore geradora minima com metodo de PRIM: ");
                    int idVer = Integer.parseInt(teclado.nextLine());
                    System.out.println(grafoMutavel.metodoPrim(idVer));
                    break;                

                default:
                    break;
            }
            pausa();
        } while (opcao != 0);
        System.out.println("Saindo...");
    }

    private static void caminhoMinimo(GrafoMutavel grafoMutavel) {
        System.out.println(grafoMutavel.stringListaVertices());

        System.out.print("Digite o ID do vertice de origem: ");
        int idVerOrigem = Integer.parseInt(teclado.nextLine());

        System.out.print("Digite o ID do vertice de destino: ");
        int idVerDestino = Integer.parseInt(teclado.nextLine());
        
        System.out.println(grafoMutavel.dijkstra(idVerOrigem, idVerDestino));
    }
}
