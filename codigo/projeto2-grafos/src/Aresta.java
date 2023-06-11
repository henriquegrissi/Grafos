/** 
 * MIT License
 *
 * Copyright(c) 2021 João Caram <caram@pucminas.br>
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

public class Aresta {

    private int peso;
    private int destino;
    private int origem;
    private int id;
    private static int parseId = 0;
    private boolean visitada;

    /**
     * Construtor para arestas com peso. A aresta é criada como não-visitada.
     * @param peso Peso da aresta
     * @param dest Vértice de destino
     */
    public Aresta(int peso, int destino){
       init(parseId++, peso, 0, destino);
    }

    /**
     * Construtor para arestas com peso, origem e destino. A aresta é criada como não-visitada.
     * @param peso Peso da aresta
     * @param origem Vértice de origem
     * @param destino Vértice de destino
     */
    public Aresta(int peso, int origem, int destino){
       init(parseId++, peso, origem, destino);
    }

    /**
     * Construtor para arestas sem peso. A aresta é criada como não-visitada.
     * @param dest Vértice de destino
     */
    public Aresta(int destino){
        init(parseId++, 0, 0, destino);
    }

    /**
     * Método inicializador para ser utilizado pelos construtores que recebe o peso e o vértice de destino da aresta
     * 
     * @param peso
     * @param destino
     */
    private void init(int id, int peso, int origem, int destino){
        this.id = id;
        this.peso = peso;
        this.origem = origem;
        this.destino = destino;
        this.visitada = false;
    }

    /**
     * Método get para retornar o id da aresta
     * @return id da aresta
     */
    public int getId(){
        return this.id;
    }

    /**
     * Método get para retornar a origem da aresta
     * @return id da aresta
     */
    public int getOrigem(){
        return this.origem;
    }
    
    /**
     * Método de acesso para o peso da aresta
     * @return Peso da aresta (int)
     */
    public int peso() {
        return this.peso;
    }
   
    /**
     * Método de acesso para o destino da aresta
     * @return Id do vértice de destino
     */
    public int destino() {
        return this.destino;
    }

    /**
     * Marca a aresta como visitada
     */
    public void visitar(){
        this.visitada = true;
    }

    /**
     * Marca a aresta como não visitada
     */
    public void limparVisita(){
        this.visitada = false;
    }

    /**
     * Indica se a aresta foi visitada (TRUE) ou não (FALSE)
     * @return TRUE/FALSE conforme a aresta foi visitada ou não.
     */
    public boolean visitada(){
        return this.visitada;
    }
}
