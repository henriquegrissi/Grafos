import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.EOFException;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;

public class GrafoTest {
    GrafoMutavel meuGrafo;
    Grafo grafo;

    @BeforeEach
    public void prepare() {
        grafo = new Grafo("Meu Grafo Para Testes");
        meuGrafo = new GrafoMutavel(" ");
    }

    @Test
    public void deveGerarGrafoComNomeBaseCasoNaoSejaInformadoPeloUsuario() {
        Grafo grafoNomeVazio = new Grafo("");
        assertEquals(grafoNomeVazio.nome(), "Grafo");
    }

    @Test
    public void deveGerarGrafoComNomeInformadoPeloUsuario() {
        assertEquals(grafo.nome(), "Meu Grafo Para Testes");
    }

    @Test
    public void deveRetornarOrdemCorreta() {
        meuGrafo.addVertice(1);
        meuGrafo.addVertice(2);

        assertEquals(2, meuGrafo.ordem());
    }

    @Test
    public void deveRetornarFalsoCasoTenteAdicionarArestaQueJaExista() {
        meuGrafo.addVertice(4);
        meuGrafo.addVertice(5);
        meuGrafo.addAresta(4, 5, 1);

        assertFalse(meuGrafo.addAresta(4, 5, 5));
    }

    @Test
    public void deveRetornarFalsoCasoTenteAdicionarArestaEmUmVerticeInexistente() {
        meuGrafo.addVertice(5);
        assertFalse(meuGrafo.addAresta(4, 5, 5));
    }

    @Test
    public void deveRetornarTrueCasoTenteAdicionarNovaAresta() {
        meuGrafo.addVertice(2);
        meuGrafo.addVertice(3);
        assertTrue(meuGrafo.addAresta(2, 3, 1));
    }

    @Test
    public void deveRetornarFalsoCasoTenteAdicionarVerticeComMesmoId() {
        meuGrafo.addVertice(1);
        assertFalse(meuGrafo.addVertice(1));
    }

    @Test
    public void deveAdicionarUmVerticeComIdUnico() {
        assertTrue(meuGrafo.addVertice(9));
    }

    @Test
    public void deveAdicionarArestaPonderadaComSucesso() {
        meuGrafo.addVertice(1);
        meuGrafo.addVertice(2);
        meuGrafo.addAresta(1, 2, 5);

        Aresta arestaAdicionada = meuGrafo.existeAresta(1, 2);

        assertEquals(5, arestaAdicionada.peso());
        ;
    }

    @Test
    public void deveRetornarPesoZeroCasoAdicioneArestaNaoPonderada() {
        meuGrafo.addVertice(1);
        meuGrafo.addVertice(2);
        meuGrafo.addAresta(1, 2, 0);

        Aresta arestaAdicionada = meuGrafo.existeAresta(1, 2);

        assertEquals(0, arestaAdicionada.peso());
        ;
    }

    @Test
    public void deveRemoverVertice() {
        meuGrafo.addVertice(1);
        meuGrafo.addVertice(2);
        meuGrafo.removeVertice(2);
        assertNull(meuGrafo.existeVertice(2));
    }

    @Test
    public void deveRemoverAresta() {
        meuGrafo.addVertice(1);
        meuGrafo.addVertice(2);
        meuGrafo.addAresta(1, 2, 0);
        meuGrafo.removeAresta(1, 2);
        assertNull(meuGrafo.existeAresta(1, 2));
    }

    @Test
    public void calculaTamanho() {
        meuGrafo.addVertice(1);
        meuGrafo.addVertice(2);
        meuGrafo.addVertice(3);

        meuGrafo.addAresta(1, 2, 0);
        meuGrafo.addAresta(1, 3, 0);

        assertEquals(5, meuGrafo.tamanho());
    }

      @Test
      public void deveRealizarBuscaEmProfundidade() {
         GrafoMutavel grafoB = new GrafoMutavel("4");

        grafoB.addVertice(1);
        grafoB.addVertice(2);
        grafoB.addVertice(3);
        grafoB.addVertice(4);

        grafoB.addAresta(1, 2, 0);
        grafoB.addAresta(2, 3, 0);
        grafoB.addAresta(3, 1, 0);
        grafoB.addAresta(4, 3, 0);
     
        assertEquals("1 3 4 2 ", grafoB.dfs(1));
     }

    @Test
    public void retornaGraudoVertice3() {
        meuGrafo.addVertice(1);
        meuGrafo.addVertice(2);
        meuGrafo.addVertice(3);
        meuGrafo.addVertice(4);

        meuGrafo.addAresta(1, 2, 0);
        meuGrafo.addAresta(1, 3, 0);
        meuGrafo.addAresta(1, 4, 0);

        assertEquals(3, meuGrafo.existeVertice(1).grau());

    }

    @Test
    public void deveRealizarBuscaEmLargura() {
        GrafoMutavel grafoB = new GrafoMutavel("grafob");

        grafoB.addVertice(1);
        grafoB.addVertice(2);
        grafoB.addVertice(3);

        grafoB.addAresta(1, 2, 0);
        grafoB.addAresta(2, 3, 0);
        grafoB.addAresta(3, 1, 0);
        
        assertEquals("1 - cidade\n2 - cidade\n3 - cidade\n", grafoB.bfs(1));
    }

    @Test
    public void deveCalcularAgmComMetodoDePrim() {
        GrafoMutavel grafoB = new GrafoMutavel("grafob");

        grafoB.addVertice(1);
        grafoB.addVertice(2);
        grafoB.addVertice(3);
        grafoB.addVertice(4);
        grafoB.addVertice(5);
        grafoB.addVertice(6);
        grafoB.addVertice(7);
        grafoB.addVertice(8);
        grafoB.addVertice(9);

        grafoB.addArestaComOrigemDestino(1,4,1);
        grafoB.addArestaComOrigemDestino(1,2,8);
        grafoB.addArestaComOrigemDestino(2,3,4);
        grafoB.addArestaComOrigemDestino(2,5,5);
        grafoB.addArestaComOrigemDestino(3,4,7);
        grafoB.addArestaComOrigemDestino(3,6,1);
        grafoB.addArestaComOrigemDestino(4,7,5);
        grafoB.addArestaComOrigemDestino(5,6,2);
        grafoB.addArestaComOrigemDestino(5,9,1);
        grafoB.addArestaComOrigemDestino(6,7,8);
        grafoB.addArestaComOrigemDestino(6,8,6);
        grafoB.addArestaComOrigemDestino(7,8,9);
        grafoB.addArestaComOrigemDestino(8,9,7);

        System.out.println(grafoB.metodoPrim(1));
        assertTrue(grafoB.metodoPrim(1).contains("Somatorio dos Pesos: 27"));
    }

    @Test
    public void deveCalcularAgmDeGrafoComMaisDeUmComponenteComMetodoDePrim() {
        GrafoMutavel grafoB = new GrafoMutavel("grafob");

        grafoB.addVertice(1);
        grafoB.addVertice(2);
        grafoB.addVertice(3);
        grafoB.addVertice(4);
        grafoB.addVertice(5);
        grafoB.addVertice(6);
        grafoB.addVertice(7);
        grafoB.addVertice(8);
        grafoB.addVertice(9);

        grafoB.addArestaComOrigemDestino(1,2,1);
        grafoB.addArestaComOrigemDestino(1,3,8);
        grafoB.addArestaComOrigemDestino(2,5,8);
        grafoB.addArestaComOrigemDestino(3,4,4);
        grafoB.addArestaComOrigemDestino(4,5,5);
        grafoB.addArestaComOrigemDestino(4,6,7);
        grafoB.addArestaComOrigemDestino(7,8,1);

        System.out.println(grafoB.metodoPrim(1));
        assertTrue(grafoB.metodoPrim(1).contains("Somatorio dos Pesos: 25"));
    }

    @Test
    public void deveSubtrairVertice() throws CloneNotSupportedException {
        GrafoMutavel grafoB = new GrafoMutavel("grafob");
        Grafo grafoF = new GrafoMutavel("grafof");
        grafoB.addVertice(1);
        grafoB.addVertice(2);
        grafoB.addVertice(3);

        grafoB.addAresta(1, 2, 0);
        grafoB.addAresta(2, 3, 0);
        grafoB.addAresta(3, 1, 0);

        grafoF = grafoB.subtracaoGrafo(2, null, null);
        assertEquals("Grafo: vertice;1,3;\naresta;1-3-0,3-1-0;", grafoF.toString());
    }

    @Test
    public void deveSubtrairAresta() throws CloneNotSupportedException {
        GrafoMutavel grafoB = new GrafoMutavel("grafob");
        Grafo grafoF = new GrafoMutavel("grafof");
        grafoB.addVertice(1);
        grafoB.addVertice(2);
        grafoB.addVertice(3);
        grafoB.addVertice(4);

        grafoB.addAresta(1, 2, 0);
        grafoB.addAresta(2, 3, 0);
        grafoB.addAresta(3, 1, 0);
        grafoB.addAresta(4, 3, 0);

        grafoF = grafoB.subtracaoGrafo(0, 2, 3);
        assertEquals("Grafo: vertice;1,2;\naresta;1-2-0,1-3-0,2-1-0;", grafoF.toString());
    }

    @Test
    public void deveRetornarCaminhoMinimoEntreDoisVertices() throws FileNotFoundException, EOFException{
        GrafoMutavel grafoMut= new GrafoMutavel("grafoMut");

        grafoMut.carregar();
        assertTrue(grafoMut.dijkstra(15, 1).contains("- 15 - 60 - 65 - 26 - 37 - 43 - 9 - 111 - 30 - 62 - 73 - 94 - 44 - 82 - 87 - 90 - 42 - 28 - 1"));

    }
}
