public class Node {
    public int x;
    public int y;
    public double f;
    public double h;
    public double g;
    public Node parent;

    public Node(int _x,int _y){
        x =_x;
        y =_y;
    }
    public Node(int _x, int _y, Node _parent){
        x =_x;
        y =_y;
        parent = _parent;
    }

    public double manhattanDistance(int _x,int _y){
        return Math.abs(this.x-_x)+Math.abs(this.y-_y);
    }

    public void calcCost(Node curr, int xGoal, int yGoal){
        double g = manhattanDistance(curr.x, curr.y);
        double h = manhattanDistance(xGoal, yGoal);
        f= g+h;
    }
}
