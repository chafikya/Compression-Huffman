package huffman;



public class Node implements Comparable<Node>  {
	char ch;
    int freq;
    Node left;
    Node right;

    Node(char ch, int freq) {
        this.ch = ch;
        this.freq = freq;
    }

    Node(int freq, Node left, Node right) {
        this.freq = freq;
        this.left = left;
        this.right = right;
    }

    boolean isLeaf() {
        return (left == null && right == null);
    }

    public int compareTo(Node other) {
        int freqComp = Integer.compare(this.freq, other.freq);
        if (freqComp != 0) {
            return freqComp;
        } else {
            return Character.compare(this.ch, other.ch);
        }
    }

}
