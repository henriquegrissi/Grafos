import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
     * Adiciona uma aresta entre dois vértices do grafo, caso os dois vértices existam no grafo.
     * Caso a aresta já exista, ou algum dos vértices não existir, o comando é ignorado e retorna FALSE.
     * Armazena na aresta o id do vértice de origem E destino
     * 
     * @param origem  Vértice de origem
     * @param destino Vértice de destino
     * @param peso    Peso da aresta
     * @return TRUE se foi inserida, FALSE caso contrário
     */    
    public boolean addArestaComOrigemDestino(int origem, int destino, int peso){
        boolean adicionou = false;
        Vertice saida = this.existeVertice(origem);
        Vertice chegada = this.existeVertice(destino);
        if (saida != null && chegada != null) {
            adicionou = (saida.addAresta(origem, destino, peso) && chegada.addAresta(destino, origem, peso));
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
            //System.out.println("A aresta não existe no grafo.");
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

        String leitura;
        String nomeVertive;
        int id;
        double latitude, longitude;
        LinkedList<Vertice> listaDeVertices = new LinkedList<Vertice>();
        HashMap<Vertice, Double> listaDeCidadesMaisPerto;

        id = 0;
        // preenchendo a lista de cidades
        while (entrada.hasNextLine()) {
            leitura = entrada.nextLine();
            id++;
            nomeVertive = leitura.split(",")[0];
            latitude  = Double.parseDouble(leitura.split(",")[1]);
            longitude = Double.parseDouble(leitura.split(",")[2]);

            Vertice newVertice = new Vertice(id, nomeVertive, latitude, longitude);
            this.addVertice(id, nomeVertive, latitude, longitude);
            listaDeVertices.add(newVertice);
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

            // adicionando as 4 cidades mais proxima da que estamos vendo
            for (int i = 0; i < 4; i++){
                Vertice menorVertice = null;
                int origem = 0, destino = 0, peso = 0;
                double distancia = Double.MAX_VALUE;
                for (Vertice melhoVerticeDeDistancia : listaDeCidadesMaisPerto.keySet()) {

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
                    vertice.addAresta(destino, peso);
                    this.addArestaComOrigemDestino(origem, destino, peso);
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

    public String dijkstra(int idVerticeOrigem, int idVerticeDestino) {
        String retorno = "";
        Vertice verticeOrigem = vertices.find(idVerticeOrigem);
        Vertice verticeDestino = vertices.find(idVerticeDestino);
        zerarVertices();

        if (verticeOrigem != null && verticeDestino != null) {
            verticeOrigem.visitar();
            LinkedList<Integer> caminhoMinimo = new LinkedList<Integer>();
            caminhoMinimo = caminhoMinimo(verticeOrigem, verticeDestino);
                    if (caminhoMinimo != null && caminhoMinimo.getLast() == verticeDestino.getId()) {
                        String saida = "";
                        for(int i : caminhoMinimo){
                            saida += " - " + i ;
                            saida += " (" + vertices.find(i).getNome() + ")";
                        }
                        
                        retorno = ("Caminho minimo: " + saida);
                    } else {
                        retorno = ("Não existe caminho para essa cidade");
                    }                                
        }

        return retorno;
    } 

    public LinkedList<Integer> caminhoMinimo(Vertice verticeOrigem, Vertice verticeDestino) { 
        LinkedList<Integer> verticesVizinhos = new LinkedList<Integer>();

        verticesVizinhos = verticeOrigem.verticesVizinhos();
        
        if (verticesVizinhos.isEmpty()) {
            System.out.println("O vertice de origem não possui vizinhos.");
            return null;
        }

        LinkedList<Integer> retorno = new LinkedList<Integer>();
        LinkedList<Integer> caminho = new LinkedList<Integer>();
        caminho.add(verticeOrigem.getId());

        while ((!verticesVizinhos.isEmpty()) ) {
            // pega o primeiro vizinho
            int vizinho = verticesVizinhos.get(0);
            
            if(vertices.find(vizinho).visitado() && verticesVizinhos.size() >= 1){
                verticesVizinhos.removeFirst();
                if(verticesVizinhos.size() == 0)
                    break;
                continue;
            }

            vertices.find(vizinho).visitar();

            // caso o vizinho não seja o que procura ele procura nos filhos de cada vertice
            if(vertices.find(vizinho).getId() == verticeDestino.getId()){
                caminho.add(vizinho);
                return caminho;
            }else {
                retorno = caminhoMinimo(vertices.find(vizinho), verticeDestino);
            }

            if(retorno.getLast() == verticeDestino.getId())
                break;
            // caso não seja o que procura ele remove da lista de viznhos
            verticesVizinhos.removeFirst();
        }
        caminho.addAll(retorno);
        return caminho;
    }

    private void zerarVertices() {
        Vertice arrayVertice[] = new Vertice[vertices.size()]; 
        vertices.allElements(arrayVertice);
        for(Vertice vertice : arrayVertice ) {
            vertice.limparVisita();
        }
    }

    //implementar uma função que recebe um vértice como parâmetro e retorna o grau desse vértice e quais são os seus vizinhos
    public String retornaGrauEVizinhosDeUmVertice(int idVertice){
        Vertice vertice = vertices.find(idVertice);
        Vertice verticeVizinho;
        StringBuilder stringFormatar = new StringBuilder();
        
        if (vertice != null) {
            Lista<Integer> vizinhos = vertice.vizinhos();
            Integer[] arrayVizinhos = new Integer[vizinhos.size()];
            arrayVizinhos = vizinhos.allElements(arrayVizinhos);

            stringFormatar.append("GRAU: " + vertice.grau());
            stringFormatar.append("\nVIZINHOS:");
            stringFormatar.append("\nId | Cidade");
            for(int id : arrayVizinhos){
                verticeVizinho = vertices.find(id);
                if(vertice != null)
                    stringFormatar.append("\n" + verticeVizinho.toString());
            }
        }

        return stringFormatar.toString();
    }

    /**
     * Retorna o resultado da geração da arvore mínima através do método PRIM
     * @param idVertice
     * @return String formatada com os vértices e arestas da árvore
     */
    public String metodoPrim(int idVertice){
        GrafoMutavel arvoreGeradoraMinima = new GrafoMutavel("Arvore Geradora Minima - Metodo Prim");

        Vertice vertice = vertices.find(idVertice);

        ABB<Vertice> conjuntoDeVerticesGrafo = this.vertices;
        ABB<Vertice> conjuntoDeVerticesSelecionados = new ABB<>();
        
        ABB<Aresta> conjuntoDeArestasAgm = new ABB<>();
        ABB<Aresta> arestasDescobertasAhPercorrer = new ABB<>();
        int pesoArestaAtual, pesoMenorAresta = 0, somatorioPesos = 0, qtdVerticesPercorridos = 0;
        Vertice verticeDestino = null;

        if(vertice != null){
            conjuntoDeVerticesSelecionados.add(idVertice, vertice);
            arvoreGeradoraMinima.addVertice(idVertice);
        }

        while(conjuntoDeVerticesGrafo.size() != qtdVerticesPercorridos){            
            Aresta arestasVerticeAtual[] = new Aresta[vertice.getAresta().size()];
            arestasVerticeAtual = vertice.getAresta().allElements(arestasVerticeAtual);
            
            for(Aresta aresta : arestasVerticeAtual){
                if(conjuntoDeVerticesSelecionados.find(aresta.destino()) == null) {
                    arestasDescobertasAhPercorrer.add(aresta.getId(), aresta);
                }
            }

            Aresta arestasPercorrer[] = new Aresta[arestasDescobertasAhPercorrer.size()];
            arestasPercorrer = arestasDescobertasAhPercorrer.allElements(arestasPercorrer);
            Aresta arestaMenorPeso = null;
            
            for(Aresta aresta : arestasPercorrer){
                pesoArestaAtual = aresta.peso();
                int idVerticeDestino = aresta.destino();
                verticeDestino = conjuntoDeVerticesSelecionados.find(idVerticeDestino);
                if((pesoArestaAtual < pesoMenorAresta || pesoMenorAresta == 0) && verticeDestino == null){
                    arestaMenorPeso = aresta;
                    pesoMenorAresta = pesoArestaAtual;

                    vertice = conjuntoDeVerticesGrafo.find(idVerticeDestino);
                    idVertice = idVerticeDestino;
                }
            }

            if(arestaMenorPeso != null){
                somatorioPesos += pesoMenorAresta;
                pesoMenorAresta = 0;
            
                conjuntoDeVerticesSelecionados.add(idVertice, vertice);
                conjuntoDeArestasAgm.add(arestaMenorPeso.getId(), arestaMenorPeso);

                arvoreGeradoraMinima.addVertice(idVertice);
                arvoreGeradoraMinima.addAresta(arestaMenorPeso.getOrigem(), arestaMenorPeso.destino(), arestaMenorPeso.peso());
                arestasDescobertasAhPercorrer.remove(arestaMenorPeso.getId());

                qtdVerticesPercorridos = conjuntoDeVerticesSelecionados.size();
            } else {                
                qtdVerticesPercorridos = conjuntoDeVerticesGrafo.size();
            }
        }

        StringBuilder stringArvoreGeradora = new StringBuilder();
        stringArvoreGeradora.append("---- ARVORE GERADORA METODO DE PRIM ----");
        stringArvoreGeradora.append("\nSomatorio dos Pesos: " + somatorioPesos);
        stringArvoreGeradora.append("\n" + arvoreGeradoraMinima.toString());

        return stringArvoreGeradora.toString();
    }
    
    /**
     * Retorna uma string com o id e nome de cada um dos vértices do grafo
     * @return
     */
    public String stringListaVertices(){
        StringBuilder stringVertices = new StringBuilder();
        Vertice[] arrayVertices = new Vertice[this.vertices.size()];
        arrayVertices = vertices.allElements(arrayVertices);

        stringVertices.append("-------------- LISTA VÉRTICES ------------");
        stringVertices.append("\nID | CIDADE");

        for(Vertice vertice : arrayVertices){
            stringVertices.append("\n" + vertice.toString());    
        }

        stringVertices.append("\n-----------------------------------------");

        return stringVertices.toString();
    }
}
