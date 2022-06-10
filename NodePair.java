package nitin.codebind;



import java.util.Objects;

public class NodePair {
    private  int source;
    private  int destination;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "NodePair{" +
                "source=" + source +
                ", destination=" + destination +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodePair nodePair = (NodePair) o;
        return source == nodePair.source &&
                destination == nodePair.destination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, destination);
    }
}