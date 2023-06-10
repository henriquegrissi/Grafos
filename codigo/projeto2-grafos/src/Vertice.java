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

import java.util.Comparator;
import java.util.LinkedList;

/** Classe Vertice para um grafo */
public class Vertice {

    private ABB<Aresta> arestas;
    private final int id;
    private boolean visitado;
    private String nome;
    private double latitude, longitude;

    /**
     * Construtor para criação de vértice identificado
     * 
     * @param id Número/id do vértice a ser criado (atributo final).
     */
    public Vertice(int id) {
        this.id = id;
        init("cidade", 0, 0);
    }

    public Vertice(int id, String nome, double latitude, double longitude) {
        this.id = id;
        init(nome, latitude, longitude);
    }

    public void init(String nome, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.nome = nome;
        this.arestas = new ABB<Aresta>();
        this.visitado = false;
    }


    /**
     * Retorna o id do vértice, caso seja necessário para verificações próprias
     * 
     * @return Identificador do vértice (int)
     */
    public int getId() {
        return this.id;
    }

    /**
     * Retorna todas as arestas de um vértice
     * 
     * @return Árvore com todas as arestas
     */
    public ABB<Aresta> getAresta() {
        return arestas;
    }

    /**
     * @param destino Vértice de destino
     * @return TRUE se aresta foi inserida, FALSE caso já existisse e não foi inserida
     */
    public boolean addAresta(int destino) {
        return this.arestas.add(destino, new Aresta(destino));
    }

    /**
     * Adiciona uma aresta ponderada neste vértice para um destino
     * 
     * @param peso Peso da aresta
     * @param destino Vértice de destino
     * @return TRUE se foi inserida, FALSE caso já existisse e não foi inserida.
     */
    public boolean addAresta(int destino, int peso) {
        return this.arestas.add(destino, new Aresta(peso, destino));
    }

    /**
     * Valida se a aresta existe no grafo ou não
     * 
     * @param destino Vértice de destino
     * @return Aresta encontrada ou null caso não exista
     */
    public Aresta existeAresta(int destino) {
        return this.arestas.find(destino);
    }

    /**
     * Remove e retorna a aresta para o destino indicado. Retorna null caso não
     * exista a aresta.
     * 
     * @param destino Destino da aresta a ser removida.
     * @return A aresta removida, ou null se não existir.
     */
    public Aresta removeAresta(int destino) {
        return this.arestas.remove(destino);
    }

    /**
     * Retorna o grau do vértice
     * 
     * @return Grau do vértice (número de arestas incidentes)
     */
    public int grau() {
        return this.arestas.size();
    }

    /**
     * Marca o vértice como visitado
     */
    public void visitar() {
        this.visitado = true;
    }

    /**
     * Marca o vértice como não visitado
     */
    public void limparVisita() {
        this.visitado = false;
    }

    /**
     * Indica se o vértice foi visitado (TRUE) ou não (FALSE)
     * 
     * @return TRUE/FALSE conforme o vértice tenha sido ou não visitado.
     */
    public boolean visitado() {
        return this.visitado;
    }

    /**
     * Retorna todos os vizinhos de um vertice
     * 
     * @return Lista com o par de todos os vértices vizinhos
     */
    public Lista<Integer> vizinhos() {
        Lista<Integer> vizinhosList = new Lista<Integer>();

        if (arestas.size() != 0) {
            Aresta[] arestas;
            arestas = new Aresta[this.arestas.size()];
            arestas = this.arestas.allElements(arestas);
            for (int i = 0; i < this.arestas.size(); i++) {
                vizinhosList.add(arestas[i].destino());
            }
        }
        return vizinhosList;
    }

    public LinkedList<Integer> verticesVizinhos() {
        LinkedList<Integer> vizinhosList = new LinkedList<Integer>();

        if (arestas.size() != 0) {
            Aresta[] arestas;
            arestas = new Aresta[this.arestas.size()];
            arestas = this.arestas.allElements(arestas);
            for (int i = 0; i < this.arestas.size(); i++) {
                vizinhosList.add(arestas[i].destino());
            }
        }
        return vizinhosList;
    }


    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double calcularDistancia(double latitude2, double longitude2) {
        double latitude1 = this.latitude;
        double longitude1 = this.longitude;

        double diferencaLatitudes = Math.toRadians(latitude2 - latitude1);
        double diferencaLongitudes = Math.toRadians(longitude2 - longitude1);

        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);

        double formulaHaversine = Math.sqrt(Math.pow(Math.sin(diferencaLatitudes/2), 2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.pow(Math.sin(diferencaLongitudes/2), 2));
        double distancia = 2 * 6371 * Math.asin(formulaHaversine);
        
        return distancia;
    }

    public Object getNome() {
        return this.nome;
    }
}
