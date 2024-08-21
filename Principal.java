import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

class ArvoreAVL {
    private class No {
        int chave, altura;
        No esquerdo, direito;

        No(int d) {
            chave = d;
            altura = 1;
        }
    }

    private No raiz;

    private int altura(No N) {
        if (N == null)
            return 0;
        return N.altura;
    }

    private No rotacaoDireita(No y) {
        No x = y.esquerdo;
        No T2 = x.direito;

        x.direito = y;
        y.esquerdo = T2;

        y.altura = Math.max(altura(y.esquerdo), altura(y.direito)) + 1;
        x.altura = Math.max(altura(x.esquerdo), altura(x.direito)) + 1;

        return x;
    }

    private No rotacaoEsquerda(No x) {
        No y = x.direito;
        No T2 = y.esquerdo;

        y.esquerdo = x;
        x.direito = T2;

        x.altura = Math.max(altura(x.esquerdo), altura(x.direito)) + 1;
        y.altura = Math.max(altura(y.esquerdo), altura(y.direito)) + 1;

        return y;
    }

    private int obterBalanceamento(No N) {
        if (N == null)
            return 0;
        return altura(N.esquerdo) - altura(N.direito);
    }

    public void inserir(int chave) {
        raiz = inserir(raiz, chave);
    }

    private No inserir(No no, int chave) {
        if (no == null)
            return (new No(chave));

        if (chave < no.chave)
            no.esquerdo = inserir(no.esquerdo, chave);
        else if (chave > no.chave)
            no.direito = inserir(no.direito, chave);
        else
            return no;

        no.altura = 1 + Math.max(altura(no.esquerdo), altura(no.direito));

        int balanceamento = obterBalanceamento(no);

        if (balanceamento > 1 && chave < no.esquerdo.chave)
            return rotacaoDireita(no);

        if (balanceamento < -1 && chave > no.direito.chave)
            return rotacaoEsquerda(no);

        if (balanceamento > 1 && chave > no.esquerdo.chave) {
            no.esquerdo = rotacaoEsquerda(no.esquerdo);
            return rotacaoDireita(no);
        }

        if (balanceamento < -1 && chave < no.direito.chave) {
            no.direito = rotacaoDireita(no.direito);
            return rotacaoEsquerda(no);
        }

        return no;
    }

    public void deletar(int chave) {
        raiz = deletar(raiz, chave);
    }

    private No deletar(No raiz, int chave) {
        if (raiz == null)
            return raiz;

        if (chave < raiz.chave)
            raiz.esquerdo = deletar(raiz.esquerdo, chave);
        else if (chave > raiz.chave)
            raiz.direito = deletar(raiz.direito, chave);
        else {
            if ((raiz.esquerdo == null) || (raiz.direito == null)) {
                No temp = null;
                if (temp == raiz.esquerdo)
                    temp = raiz.direito;
                else
                    temp = raiz.esquerdo;

                if (temp == null) {
                    temp = raiz;
                    raiz = null;
                } else
                    raiz = temp;
            } else {
                No temp = noValorMinimo(raiz.direito);
                raiz.chave = temp.chave;
                raiz.direito = deletar(raiz.direito, temp.chave);
            }
        }

        if (raiz == null)
            return raiz;

        raiz.altura = Math.max(altura(raiz.esquerdo), altura(raiz.direito)) + 1;

        int balanceamento = obterBalanceamento(raiz);

        if (balanceamento > 1 && obterBalanceamento(raiz.esquerdo) >= 0)
            return rotacaoDireita(raiz);

        if (balanceamento > 1 && obterBalanceamento(raiz.esquerdo) < 0) {
            raiz.esquerdo = rotacaoEsquerda(raiz.esquerdo);
            return rotacaoDireita(raiz);
        }

        if (balanceamento < -1 && obterBalanceamento(raiz.direito) <= 0)
            return rotacaoEsquerda(raiz);

        if (balanceamento < -1 && obterBalanceamento(raiz.direito) > 0) {
            raiz.direito = rotacaoDireita(raiz.direito);
            return rotacaoEsquerda(raiz);
        }

        return raiz;
    }

    private No noValorMinimo(No no) {
        No atual = no;

        while (atual.esquerdo != null)
            atual = atual.esquerdo;

        return atual;
    }

    public int contarOcorrencias(int chave) {
        return contarOcorrencias(raiz, chave);
    }

    private int contarOcorrencias(No no, int chave) {
        if (no == null)
            return 0;

        if (no.chave == chave)
            return 1 + contarOcorrencias(no.esquerdo, chave) + contarOcorrencias(no.direito, chave);

        if (chave < no.chave)
            return contarOcorrencias(no.esquerdo, chave);
        else
            return contarOcorrencias(no.direito, chave);
    }
}

class ArvoreRubroNegra {
    private class No {
        int dado;
        No pai;
        No esquerdo;
        No direito;
        int cor;

        No(int dado) {
            this.dado = dado;
            this.cor = 1;
        }
    }

    private No raiz = null;
    private No NULO = new No(0);

    private void rotacaoEsquerda(No x) {
        No y = x.direito;
        x.direito = y.esquerdo;
        if (y.esquerdo != NULO) {
            y.esquerdo.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            this.raiz = y;
        } else if (x == x.pai.esquerdo) {
            x.pai.esquerdo = y;
        } else {
            x.pai.direito = y;
        }
        y.esquerdo = x;
        x.pai = y;
    }

    private void rotacaoDireita(No x) {
        No y = x.esquerdo;
        x.esquerdo = y.direito;
        if (y.direito != NULO) {
            y.direito.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == null) {
            this.raiz = y;
        } else if (x == x.pai.direito) {
            x.pai.direito = y;
        } else {
            x.pai.esquerdo = y;
        }
        y.direito = x;
        x.pai = y;
    }

    private void corrigirInsercao(No k) {
        No u;
        while (k.pai.cor == 1) {
            if (k.pai == k.pai.pai.direito) {
                u = k.pai.pai.esquerdo;
                if (u.cor == 1) {
                    u.cor = 0;
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    k = k.pai.pai;
                } else {
                    if (k == k.pai.esquerdo) {
                        k = k.pai;
                        rotacaoDireita(k);
                    }
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    rotacaoEsquerda(k.pai.pai);
                }
            } else {
                u = k.pai.pai.direito;

                if (u.cor == 1) {
                    u.cor = 0;
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    k = k.pai.pai;
                } else {
                    if (k == k.pai.direito) {
                        k = k.pai;
                        rotacaoEsquerda(k);
                    }
                    k.pai.cor = 0;
                    k.pai.pai.cor = 1;
                    rotacaoDireita(k.pai.pai);
                }
            }
            if (k == raiz) {
                break;
            }
        }
        raiz.cor = 0;
    }

    public void inserir(int chave) {
        No no = new No(chave);
        no.pai = null;
        no.dado = chave;
        no.esquerdo = NULO;
        no.direito = NULO;
        no.cor = 1;

        No y = null;
        No x = this.raiz;

        while (x != NULO) {
            y = x;
            if (no.dado < x.dado) {
                x = x.esquerdo;
            } else {
                x = x.direito;
            }
        }

        no.pai = y;
        if (y == null) {
            raiz = no;
        } else if (no.dado < y.dado) {
            y.esquerdo = no;
        } else {
            y.direito = no;
        }

        if (no.pai == null) {
            no.cor = 0;
            return;
        }

        if (no.pai.pai == null) {
            return;
        }

        corrigirInsercao(no);
    }

    public void deletar(int dado) {
        deletarNoAuxiliar(this.raiz, dado);
    }

    private void deletarNoAuxiliar(No no, int chave) {
        No z = NULO;
        No x, y;
        while (no != NULO) {
            if (no.dado == chave) {
                z = no;
            }

            if (no.dado <= chave) {
                no = no.direito;
            } else {
                no = no.esquerdo;
            }
        }

        if (z == NULO) {
            return;
        }

        y = z;
        int yOriginalCor = y.cor;
        if (z.esquerdo == NULO) {
            x = z.direito;
            transplantar(z, z.direito);
        } else if (z.direito == NULO) {
            x = z.esquerdo;
            transplantar(z, z.esquerdo);
        } else {
            y = minimo(z.direito);
            yOriginalCor = y.cor;
            x = y.direito;
            if (y.pai == z) {
                x.pai = y;
            } else {
                transplantar(y, y.direito);
                y.direito = z.direito;
                y.direito.pai = y;
            }

            transplantar(z, y);
            y.esquerdo = z.esquerdo;
            y.esquerdo.pai = y;
            y.cor = z.cor;
        }

        if (yOriginalCor == 0) {
            corrigirDelecao(x);
        }
    }

    private void transplantar(No u, No v) {
        if (u.pai == null) {
            raiz = v;
        } else if (u == u.pai.esquerdo) {
            u.pai.esquerdo = v;
        } else {
            u.pai.direito = v;
        }
        v.pai = u.pai;
    }

    private void corrigirDelecao(No x) {
        No s;
        while (x != raiz && x.cor == 0) {
            if (x == x.pai.esquerdo) {
                s = x.pai.direito;
                if (s.cor == 1) {
                    s.cor = 0;
                    x.pai.cor = 1;
                    rotacaoEsquerda(x.pai);
                    s = x.pai.direito;
                }

                if (s.esquerdo.cor == 0 && s.direito.cor == 0) {
                    s.cor = 1;
                    x = x.pai;
                } else {
                    if (s.direito.cor == 0) {
                        s.esquerdo.cor = 0;
                        s.cor = 1;
                        rotacaoDireita(s);
                        s = x.pai.direito;
                    }

                    s.cor = x.pai.cor;
                    x.pai.cor = 0;
                    s.direito.cor = 0;
                    rotacaoEsquerda(x.pai);
                    x = raiz;
                }
            } else {
                s = x.pai.esquerdo;
                if (s.cor == 1) {
                    s.cor = 0;
                    x.pai.cor = 1;
                    rotacaoDireita(x.pai);
                    s = x.pai.esquerdo;
                }

                if (s.direito.cor == 0 && s.direito.cor == 0) {
                    s.cor = 1;
                    x = x.pai;
                } else {
                    if (s.esquerdo.cor == 0) {
                        s.direito.cor = 0;
                        s.cor = 1;
                        rotacaoEsquerda(s);
                        s = x.pai.esquerdo;
                    }

                    s.cor = x.pai.cor;
                    x.pai.cor = 0;
                    s.esquerdo.cor = 0;
                    rotacaoDireita(x.pai);
                    x = raiz;
                }
            }
        }
        x.cor = 0;
    }

    private No minimo(No no) {
        while (no.esquerdo != NULO) {
            no = no.esquerdo;
        }
        return no;
    }

    public int contarOcorrencias(int chave) {
        return contarOcorrencias(raiz, chave);
    }

    private int contarOcorrencias(No no, int chave) {
        if (no == null || no == NULO)
            return 0;

        if (no.dado == chave)
            return 1 + contarOcorrencias(no.esquerdo, chave) + contarOcorrencias(no.direito, chave);

        if (chave < no.dado)
            return contarOcorrencias(no.esquerdo, chave);
        else
            return contarOcorrencias(no.direito, chave);
    }
}

public class Principal {
    public static void main(String[] args) throws IOException {
        ArvoreAVL avl = new ArvoreAVL();
        ArvoreRubroNegra rb = new ArvoreRubroNegra();

        BufferedReader leitor = new BufferedReader(new FileReader("dados100_mil.txt"));
        String linha;

        long tempoInicio, tempoFim;

        tempoInicio = System.nanoTime();
        while ((linha = leitor.readLine()) != null) {
            int num = Integer.parseInt(linha);
            avl.inserir(num);
        }
        tempoFim = System.nanoTime();
        long tempoInsercaoAVL = tempoFim - tempoInicio;

        leitor.close();

        leitor = new BufferedReader(new FileReader("dados100_mil.txt"));

        tempoInicio = System.nanoTime();
        while ((linha = leitor.readLine()) != null) {
            int num = Integer.parseInt(linha);
            rb.inserir(num);
        }
        tempoFim = System.nanoTime();
        long tempoInsercaoRB = tempoFim - tempoInicio;

        leitor.close();

        Random rand = new Random();

        long tempoOperacoesAVL = 0;
        long tempoOperacoesRB = 0;

        for (int i = 0; i < 50000; i++) {
            int numeroAleatorio = rand.nextInt(19999) - 9999;

            if (numeroAleatorio % 3 == 0) {
                tempoInicio = System.nanoTime();
                avl.inserir(numeroAleatorio);
                tempoFim = System.nanoTime();
                tempoOperacoesAVL += (tempoFim - tempoInicio);

                tempoInicio = System.nanoTime();
                rb.inserir(numeroAleatorio);
                tempoFim = System.nanoTime();
                tempoOperacoesRB += (tempoFim - tempoInicio);
            } else if (numeroAleatorio % 5 == 0) {
                tempoInicio = System.nanoTime();
                avl.deletar(numeroAleatorio);
                tempoFim = System.nanoTime();
                tempoOperacoesAVL += (tempoFim - tempoInicio);

                tempoInicio = System.nanoTime();
                rb.deletar(numeroAleatorio);
                tempoFim = System.nanoTime();
                tempoOperacoesRB += (tempoFim - tempoInicio);
            } else {
                tempoInicio = System.nanoTime();
                avl.contarOcorrencias(numeroAleatorio);
                tempoFim = System.nanoTime();
                tempoOperacoesAVL += (tempoFim - tempoInicio);

                tempoInicio = System.nanoTime();
                rb.contarOcorrencias(numeroAleatorio);
                tempoFim = System.nanoTime();
                tempoOperacoesRB += (tempoFim - tempoInicio);
            }
        }

        System.out.println("Tempo de inserção AVL: " + tempoInsercaoAVL + " ns");
        System.out.println("Tempo de inserção Rubro-Negra: " + tempoInsercaoRB + " ns");
        System.out.println("Tempo de operações AVL: " + tempoOperacoesAVL + " ns");
        System.out.println("Tempo de operações Rubro-Negra: " + tempoOperacoesRB + " ns");
    }
}
