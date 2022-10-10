package src;

import java.util.Iterator;

class InfIterator implements Iterator<Integer> {
    public Integer value = 0;

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        return ++value;
    }
}

class Inf implements Iterable<Integer> {
    @Override
    public Iterator<Integer> iterator() {
        return new InfIterator();
    }
}

public class LabExperiment {
    public static void main(String[] args) throws InterruptedException {
        var inf = new Inf();
        for(var i : inf){
            System.out.println(i);
            Thread.sleep(10);
        }
    }
}
