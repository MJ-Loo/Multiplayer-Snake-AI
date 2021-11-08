import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar {
    public int[][] closed;
    public int[] head;
    public int[][] apples;
    public PriorityQueue<Node> open;
    public AStar(){
    }

    public int pathfinder(int[][] grid,int[] head, int[] apple, boolean tail){
        open = new PriorityQueue<Node>(30,new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if(o1.f==o2.f)  
                return 0;  
                else if(o1.f>o2.f)  
                return 1;  
                else  
                return -1;  
            }
        });
        closed = grid.clone();
        open.add(new Node(head[0],head[1]));
        if (tail) {
            closed[apple[0]][apple[1]]=0;
        }
        
        closed[head[0]][head[1]]=0;

        while(open.size()!=0){
            Node current = open.poll();
            int x =current.x;
            int y =current.y;

            if(!isTraversible(x, y)){//if the current node has already been checked or is a snake tail, move to the next node in the queue
                continue;
            }
            closed[x][y]=2;
            if(apple[0]==x && apple[1]==y){ //if the current node is the destination
                while(current.parent.x!=head[0] || current.parent.y!=head[1]){
                    
                    grid[current.x][current.y]=5;
                    current = current.parent;
                }
                //System.out.println("log found");
                return getMove(head, current.x, current.y);
            }
            //Direction Checking
            //Up x, y-1
            if(isValid(x,y-1)){
                Node dir = new Node(x,y-1,current);
                dir.calcCost(current, apple[0],apple[1]);
                open.add(dir);
            }

            //Down x, y+1
            if(isValid(x,y+1)){
                Node dir = new Node(x,y+1,current);
                dir.calcCost(current, apple[0],apple[1]);
                open.add(dir);
            }

            //Left x-1, y
            if(isValid(x-1,y)){
                Node dir = new Node(x-1,y,current);
                dir.calcCost(current, apple[0],apple[1]);
                open.add(dir);
            }

            //Right x+1, y
            if(isValid(x+1,y)){
                Node dir = new Node(x+1,y,current);
                dir.calcCost(current, apple[0],apple[1]);
                open.add(dir);
            }
        }
        System.out.println("log Can't find path");
        return 5;

    }

    public boolean isValid(int row, int col){
        return (row>=0 &&row<50 &&col>=0 &&col<50);
    }

    public boolean isTraversible(int x, int y){
        return closed[x][y]==0;
    }

    public double distance(Node target, int x, int y){
        return (Math.abs(target.x-x)+Math.abs(target.y-y));
    }

    public int getMove(int[] head,int x , int y){
        int directionVals = 2*Integer.signum(head[0]-x) + (Integer.signum(head[1]-y)); //-1 =down 1=up -2=right 2=left
        directionVals +=2; //0=right 1=down 2=do nothing 3=up 4=left
        int[] directions = new int[]{3,1,5,0,2}; //hashmap to store directions
        return directions[directionVals];
    }
}
