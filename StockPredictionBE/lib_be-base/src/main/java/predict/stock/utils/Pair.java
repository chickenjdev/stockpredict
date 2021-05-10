package predict.stock.utils;

public class Pair<K, V> {
    public K fst;
    public V snd;

    public Pair(K first, V second) {
        this.fst = first;
        this.snd = second;
    }

    public Pair() {

    }
}
