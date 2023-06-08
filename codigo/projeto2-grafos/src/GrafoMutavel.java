import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class GrafoMutavel extends Grafo {

    public GrafoMutavel(String nome) {
        super(nome);
    }

    /**
     * Adiciona um vértice com o id especificado. Ignora a ação e retorna false se
     * já existir um vértice com este id
     * 
     * @param id O identificador do vértice a ser criado/adicionado
     * @return TRUE se houve a inclusão do vértice, FALSE se já existia vértice com
     *         este id
     */
    public boolean addVertice(int id, String nome, double latitude, double longitude) {
        Vertice novo = new Vertice(id, nome, latitude, longitude);
        return this.vertices.add(id, novo);
    }    
    

    /**
     * Adiciona um vértice com o id especificado. Ignora a ação e retorna false se
     * já existir um vértice com este id
     * 
     * @param id O identificador do vértice a ser criado/adicionado
     * @return TRUE se houve a inclusão do vértice, FALSE se já existia vértice com
     *         este id
     */
    public boolean addVertice(int id) {
        Vertice novo = new Vertice(id);
        return this.vertices.add(id, novo);
    }    

    public Vertice removeVertice(int id) {
        Vertice vertice = vertices.find(id);
        if (vertice != null) {
            vertices.remove(id);
            return vertice;
        }
        return null;
    }

    /**
     * Adiciona uma aresta entre dois vértices do grafo, caso os dois vértices existam no grafo.
     * Caso a aresta já exista, ou algum dos vértices não existir, o comando é ignorado e retorna FALSE.
     * 
     * @param origem  Vértice de origem
     * @param destino Vértice de destino
     * @param peso    Peso da aresta
     * @return TRUE se foi inserida, FALSE caso contrário
     */
    public boolean addAresta(int origem, int destino, int peso) {
        boolean adicionou = false;
        Vertice saida = this.existeVertice(origem);
        Vertice chegada = this.existeVertice(destino);
        if (saida != null && chegada != null) {
            adicionou = (saida.addAresta(destino, peso) && chegada.addAresta(origem, peso));
        }
        return adicionou;
    }

    /**
     * Método para remover a aresta com origem e destino de acordo com os parâmetros recebidos
     * 
     * @param origem Vértice de origem
     * @param destino Vértice de destino
     * @return Aresta removida caso exista e null caso não exista aresta
     */
    public Aresta removeAresta(int origem, int destino) {
        Vertice verticeO = vertices.find(origem);
        Aresta arestaR = this.existeAresta(origem, destino);

        if (arestaR == null) {
            System.out.println("A aresta não existe no grafo.");
            return null;
        }else{
           return verticeO.removeAresta(destino);
        }
    }
 
    /**
     * Carregar um arquivo de Grafo
     * @param nomeArquivo
     * @throws FileNotFoundException
     * @throws EOFException
     */
    public void carregar() throws FileNotFoundException, EOFException {
        File file = new File("./codigo/projeto2-grafos/arquivos/br.csv");
        Scanner entrada = new Scanner(file, "UTF-8");

        String leitura = entrada.nextLine();
        String nomeVertive;
        int id;
        double latitude, longitude;
        LinkedList<Vertice> listaDeVertices = new LinkedList<Vertice>();
        HashMap<Vertice, Double> listaDeCidadesMaisPerto;

        id = 0;
        // preenchendo a lista de cidades
        while (id < 115) {
            id++;
            nomeVertive = leitura.split(",")[0];
            latitude  = Double.parseDouble(leitura.split(",")[1]);
            longitude = Double.parseDouble(leitura.split(",")[2]);

            Vertice newVertice = new Vertice(id, nomeVertive, latitude, longitude);
            this.addVertice(id, nomeVertive, latitude, longitude);
            listaDeVertices.add(newVertice);

            leitura = entrada.nextLine();
        }
        entrada.close();
        
        // percorrendo cada vertice da lista e adiciona a distancia com o vertice
        for (Vertice vertice : listaDeVertices) {
            listaDeCidadesMaisPerto = new HashMap<Vertice, Double>(200);
            for (Vertice outroVertice : listaDeVertices) {
                if (outroVertice.getId()!=vertice.getId()){
                    double distancia = vertice.calcularDistancia(outroVertice.getLatitude(), outroVertice.getLongitude());
                    listaDeCidadesMaisPerto.put(outroVertice, distancia);
                }
            }

            // ordena a lista de vertices
            Collections.sort(listaDeVertices, (a,b) -> {
                            double distanciaA, distanciaB;
                            distanciaA = vertice.calcularDistancia(a.getLatitude(), a.getLongitude());
                            distanciaB = vertice.calcularDistancia(b.getLatitude(), b.getLongitude());
                            return Double.compare(distanciaA, distanciaB); 
            });

            // adicionando as 4 cidades mais proxima da que estamos vendo
            for (int i=0; i<4 ;i++){
                Vertice menorVertice = null;
                int origem = 0, destino = 0, peso = 0;
                for (Vertice melhoVerticeDeDistancia : listaDeCidadesMaisPerto.keySet()) {
                    double distancia = Double.MAX_VALUE;

                    // verifica qual o menor caminho(a cidade mais perto)
                    if (listaDeCidadesMaisPerto.get(melhoVerticeDeDistancia) < distancia){
                        menorVertice = melhoVerticeDeDistancia;
                        
                        origem = vertice.getId();
                        destino = melhoVerticeDeDistancia.getId();
                        distancia = listaDeCidadesMaisPerto.get(melhoVerticeDeDistancia);
                        peso = (int) distancia;
                    }
                }
                // verifica se tem algum valor, caso tenha cria uma aresta para os vertices
                if(origem != 0 && destino != 0){
                    this.addAresta(origem, destino, peso);
                    // remove da lista a cidade já usada
                    listaDeCidadesMaisPerto.remove(menorVertice);
                }
            }
        }
        entrada.close();
    }

    /**
     * Salvar grafo em um arquivo
     * 
     * @param nomeArquivo nome do arquivo de destino
     * @throws IOException
     */
    public void salvar(String nomeArquivo) throws IOException {
        FileWriter arq = new FileWriter("./codigo/projeto2-grafos/arquivos/" + nomeArquivo + ".csv");
        PrintWriter gravarArq = new PrintWriter(arq);

        StringBuilder idVert = new StringBuilder();
        StringBuilder idArest = new StringBuilder();

        Vertice arrayVertice[] = new Vertice[vertices.size()];
        vertices.allElements(arrayVertice);

        for (int i = 0; i < arrayVertice.length; i++) {
            Vertice vertice = vertices.find(arrayVertice[i].getId());
            idVert.append(vertice.getId());
            if (i+1 < arrayVertice.length)
                idVert.append(",");

            Aresta arestas[] = new Aresta[vertice.getAresta().size()];
            vertice.getAresta().allElements(arestas);

            for (int j = 0; j < arestas.length; j++) {
                Aresta aresta = vertice.existeAresta(arestas[j].destino());
                if (aresta != null) {
                    idArest.append(vertice.getId());
                    idArest.append("-");
                    idArest.append(aresta.destino());
                    idArest.append("-");
                    idArest.append(aresta.peso());
                    idArest.append(",");
                }
            }
        }
        String idArestStr = idArest.toString();

        gravarArq.write("vertice;");
        gravarArq.write(idVert.toString() + ";");
        gravarArq.write("\naresta;");

        if(idArestStr.length() > 0)
            gravarArq.write(idArestStr.substring(0, idArestStr.length() - 1) + ";");

        arq.close();
    }

}