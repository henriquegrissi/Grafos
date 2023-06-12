import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/** 
 * MIT License
 *
 * Copyright(c) 2021-23 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Classe básica para um Grafo simples não direcionado.
 */
public class Grafo {
    private final String nome;
    protected ABB<Vertice> vertices;

    /**
     * Construtor. Cria um grafo vazio com um nome escolhido pelo usuário. Em caso
     * de nome não informado
     * (string vazia), recebe o nome genérico "Grafo"
     */
    public Grafo(String nome) {
        if (nome.length() == 0)
            this.nome = "Grafo";
        else
            this.nome = nome;
        this.vertices = new ABB<>();
    }

    /**
     * Retorna o nome do grafo (string), caso seja necessário em outras
     * classes/sistemas
     * 
     * @return O nome do grafo (uma string)
     */
    public String nome() {
        return this.nome;
    }

    /**
     * Verifica se existe vértice com o id passado como parametro
     * 
     * @param idVertice
     * @return null caso não exista vértice e o vertice caso exista
     */
    public Vertice existeVertice(int idVertice) {
        Vertice vertice = vertices.find(idVertice);
        if (vertice != null)
            return vertice;
        return null;
    }

    /**
     * Valida se existe aresta entre o vertice A e B
     * 
     * @param verticeA
     * @param verticeB
     * @return Aresta caso exista e null caso não exista
     */
    public Aresta existeAresta(int verticeA, int verticeB) {
        Vertice verA = this.existeVertice(verticeA);
        Vertice verB = this.existeVertice(verticeB);

        if (verA != null && verB != null) {
            return verA.existeAresta(verticeB);
        }
        return null;
    }

    /**
     * Valida se o grafo gerado é completo ou não
     * 
     * @return TRUE caso grafo completo e FALSE caso contrário
     */
    public boolean completo() {
        int ordem = this.ordem();

        for (int i = 1; i <= ordem; i++) {
            for (int j = i + 1; j <= ordem; j++) {
                if (this.existeAresta(i, j) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Retorna o tamanho do grafo
     * 
     * @return tamanho do grafo
     */
    public int tamanho() {
        int vertices = this.ordem();
        int arestas = 0;

        for (int i = 1; i <= vertices; i++) {
            for (int j = i + 1; j <= vertices; j++) {
                if (this.existeAresta(i, j) != null) {
                    arestas++;
                }
            }
        }
        return arestas + vertices;
    }

    /**
     * Método que retorna a ordem do grafo (quantidade de vértices)
     * 
     * @return ordem do grafo
     */
    public int ordem() {
        return this.vertices.size();
    }

    /**
     * Realiza a busca em largura no grafo a partir de um vértice de inicio
     * 
     * @param idVerticeInicio Vértice a partir do qual a busca ira iniciar
     * @return Grafo com o resultado da busca (Representação da busca)
     */
    public String bfs(int idVerticeInicio) {
        Queue<Vertice> queue = new LinkedList<>();
        StringBuilder str = new StringBuilder(idVerticeInicio + " - ");
        str.append(this.vertices.find(idVerticeInicio).getNome() + "\n");
        queue.add(this.vertices.find(idVerticeInicio));
        this.vertices.find(idVerticeInicio).visitar();

        while (!queue.isEmpty()) {
            Lista<Integer> vizinhosList = queue.remove().vizinhos();
            Integer[] vizinhosListArray = new Integer[vizinhosList.size()];
            vizinhosListArray = vizinhosList.allElements(vizinhosListArray);

            for (int i = 0; i < vizinhosList.size(); i++) {
                if (!this.vertices.find(vizinhosListArray[i]).visitado()) {
                    this.vertices.find(vizinhosListArray[i]).visitar();
                    queue.add(this.vertices.find(vizinhosListArray[i]));
                    str.append(vizinhosListArray[i]);
                    str.append(" - ");
                    str.append(this.vertices.find(vizinhosListArray[i]).getNome());
                    str.append("\n");
                }
            }
            idVerticeInicio = vizinhosListArray[0];
        }

        return str.toString();
    }

    /**
     * Realiza a busca em profundidade no grafo a partir de um vértice de inicio
     * 
     * @param idVerticeInicio Vértice a partir do qual a busca ira iniciar
     * @return String em ordem
     */
    public String dfs(int idVerticeInicio) {
        for (int i = 1; i < this.vertices.size(); i++) { 
            this.vertices.find(i).limparVisita(); // Reseta todos os nós novamente para que a busca possa ser executada
        }
        StringBuilder str = new StringBuilder(idVerticeInicio + " ");
        this.vertices.find(idVerticeInicio).visitar();
        Stack stack = new Stack<Integer>();
        stack.push(idVerticeInicio);
        
        // se cria uma lista de vizinhos do vertice inicial
        while (!stack.empty()) {

            Integer index = search_dfs((Integer) stack.peek());
            if (index == -1) { // não existe ? Remove um nó da pilha
                stack.pop();
            } else { // existe
                this.vertices.find(index).visitar(); // executa os passos da regra 1: visite, marque como visitado e
                                                     // empilhe
                str.append(index);
                str.append(" ");
                stack.push(index);
            }
        }

        for (int i = 1; i < this.vertices.size(); i++) { // Se chegou aqui é porque não haviam mais nós na pilha
            this.vertices.find(i).limparVisita(); // Reseta todos os nós novamente para que a busca possa ser executada
            // depois
        }
        return str.toString();
    }

    /**
     * Método recursivo para realizar a busca em profundidade
     * 
     * @param vertice
     * @param str
     * @param i
     */
    public int search_dfs(Integer id) {
        // Lista<Integer> vizinhos = this.vertices.find(id).vizinhos();
        Integer[] arrayVizinhos = new Integer[this.vertices.find(id).vizinhos().size()];
        arrayVizinhos = this.vertices.find(id).vizinhos().allElements(arrayVizinhos);
        int tamanho = arrayVizinhos.length;
        int tamanho2 = arrayVizinhos.length;
        for (int k = 0; k < arrayVizinhos.length; k++) {
            if (this.vertices.find(arrayVizinhos[k]).visitado()) {
                tamanho2--;
            }
        }
        for (int i = 0; i < tamanho; i++) {
            int j = arrayVizinhos[i];
            if (j > tamanho2 && !this.vertices.find(j).visitado()) { // utiliza a matriz de adjacências para determinar
                                                                      // se para um nó existem nós adjacentes a ele
                return j; // caso exista, retorna a posição deste nó no array de nós
            }
            arrayVizinhos = this.vertices.find(j).vizinhos().allElements(arrayVizinhos);
        }
        return -1;
    }

    /**
     * Realiza a subtração do grafo por aresta
     * 
     * @param vertice vertice, null se nao usar
     * @param origem  origem da aresta, null se nao usar
     * @param destino destino da aresta, null se nao usar
     * @return grafo subtraido
     * @throws CloneNotSupportedException
     * @throws IllegalArgumentException
     */
    public Grafo subtracaoGrafo(int idVertice, Integer origem, Integer destino)
            throws CloneNotSupportedException, IllegalArgumentException {
        GrafoMutavel subGrafoMutavel = (GrafoMutavel) this.clone();
        Integer x = null;
        Integer y = null;

        if (origem != null && destino != null) 
            x = destino;

        if (idVertice != 0)
            y = idVertice;
            
        if(x == null && y == null)
            throw new IllegalArgumentException();
        if (x != null) {
            subGrafoMutavel.removeAresta(origem, destino);
            subGrafoMutavel.removeAresta(destino, origem);
            for (int i = x; i <= subGrafoMutavel.vertices.size(); i++) {
                if (subGrafoMutavel.vertices.find(i) != null) {
                    subGrafoMutavel.removeAresta(i, i + 1);
                    subGrafoMutavel.removeAresta(i + 1, i);
                    subGrafoMutavel.removeVertice(i);
                    subGrafoMutavel.removeVertice(i + 1);
                }
            }
        }
        if (y != null) {
            for (int i = 0; i <= subGrafoMutavel.vertices.size(); i++) {
                subGrafoMutavel.removeAresta(y, i);
                subGrafoMutavel.removeAresta(i, y);
            }
            subGrafoMutavel.removeVertice(y);
        }

        return subGrafoMutavel;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Cloning não permitido.");
            return this;
        }
    }

    @Override
    /**
     * Método toString para converter um grafo em uma string
     * 
     * @return Vértices e arestas do grafo em uma string ex:
     *         Grafo: vertice;1,2,3
     *         aresta;1-2-0,1-3-0
     */
    public String toString() {
        StringBuilder idVert = new StringBuilder();
        StringBuilder idArest = new StringBuilder();

        StringBuilder grafoString = new StringBuilder("Grafo: ");

        Vertice arrayVertice[] = new Vertice[vertices.size()];
        vertices.allElements(arrayVertice);

        for (int i = 0; i < arrayVertice.length; i++) {
            Vertice vertice = vertices.find(arrayVertice[i].getId());
            idVert.append(vertice.getId());
            if (i + 1 < arrayVertice.length)
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

        grafoString.append("vertice;");
        grafoString.append(idVert.toString() + ";");
        grafoString.append("\naresta;");
        if (idArestStr.length() > 0)
            grafoString.append(idArestStr.substring(0, idArestStr.length() - 1) + ";");

        return grafoString.toString();
    }

}