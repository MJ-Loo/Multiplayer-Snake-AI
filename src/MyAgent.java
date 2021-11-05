import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import za.ac.wits.snake.DevelopmentAgent;

public class MyAgent extends DevelopmentAgent {

    public static void main(String args[]) {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            int[][] grid;
            boolean invis;
            
            while (true) {
                // final long startTime = System.currentTimeMillis();
                AStar finder = new AStar();
                invis = false;
                grid = new int[50][50];
                int[] head =new int[2];
                int[] tail = new int[2];
                int snakeLength=0;
                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }

                int apples[][] = processApples(new String[]{line,br.readLine()});
                //do stuff with apples
                int mySnakeNum = Integer.parseInt(br.readLine());
                ArrayList<int[]> enemyHeads = new ArrayList<>();
                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();
                    
                    if (i == mySnakeNum) {
                        int[][] coo = getCoords(snakeLine);
                        head = coo[0];
                        tail = coo[coo.length-1];
                        snakeLength = Integer.parseInt(snakeLine.split(" ")[1]);
                        //if (snakeLine.charAt(0)=='i') {System.out.println("log my snake is invis with head t getCoords "+ head[0]+","+head[1]);
                        //System.out.println("log Given coords "+snakeLine);}
                    }
                    else if (snakeLine.charAt(0)=='i') {
                        invis = true;
                    }
                    if (mySnakeNum!=i && snakeLine.charAt(0)=='a') {
                        enemyHeads.add(getCoords(snakeLine)[0]);
                    }
                    drawSnake(grid, snakeLine);
                }
                //finished reading, calculate move;
                int[] apple = apples[getClosestApple(apples, head)];
                int move = decisionMaker(finder, grid, invis, enemyHeads, head, tail, snakeLength, apple);
                
                // for (int[] i : enemyHeads) {
                //     System.out.println("log "+Arrays.toString(i));
                // }
                // final long endTime = System.currentTimeMillis();
                // System.out.println("log "+(endTime-startTime));
                // printGrid(grid);
                System.out.println(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean isValid(int x, int y){
        return (x>=0 &&x<50 &&y>=0 &&y<50);
    }
    public int[][] processApples(String[] aplString){
        int[][] ans = new int[2][2];
        for (int i=0;i<aplString.length;i++) {
            String[] coords = aplString[i].split(" ");
            ans[i][0] = Integer.parseInt(coords[0]);
            ans[i][1] = Integer.parseInt(coords[1]);
        }
        return ans;
    }
    private int[][] getCoords(String snakeLine){
        String[] line = snakeLine.split(" ");
        int index = 3;
        if (line[0].equals("invisible")) {
            index+=2;
        }
        line = Arrays.copyOfRange(line, index, line.length);
        int[][] coords = new int[line.length][2];
        for (int i = 0; i < line.length; i++) {
            String[] coordString = line[i].split(",");
            coords[i][0] = Integer.parseInt(coordString[0]);
            coords[i][1] = Integer.parseInt(coordString[1]);
        }
        return coords;
    }
    public void drawSnake(int[][] grid, String snakeLine){
        int[][] coordArr = getCoords(snakeLine);
        for (int i = 0; i < coordArr.length-1; i++) {
            int[] point1 = coordArr[i];
            int[] point2 = coordArr[i+1];
            drawLine(grid,point1, point2);
        }
    }
    public void drawLine(int[][] grid, int[] coord1,int[] coord2){
        int x1 = coord1[0];
        int x2 = coord2[0];
        int y1 = coord1[1];
        int y2 = coord2[1];
        if (y1!=y2) {
            
            int multi = Integer.signum(y2-y1);
            for (int i = 0; i < Math.abs(y2-y1)+1; i++) {
                grid[x1][y1+(i*multi)] = 1;
            }
        }
        else if(x1!=x2){
            int multi = Integer.signum(x2-x1);
            for (int i = 0; i < Math.abs(x2-x1)+1; i++) {
                grid[x1+(i*multi)][y1] = 1;
            }
        }
    }
    public static void drawBoundaries(int[][] grid, ArrayList<int[]> enemyHeads){
        for (int i = 0; i < enemyHeads.size(); i++) {
            int[] currHead = enemyHeads.get(i);
            if (isValid(currHead[0], currHead[1]-1)) {//up
                grid[currHead[0]][currHead[1]-1]=1;
            }
            if (isValid(currHead[0], currHead[1]+1)) {//down
                grid[currHead[0]][currHead[1]+1]=1;
            }
            if (isValid(currHead[0]-1, currHead[1])) {//left
                grid[currHead[0]-1][currHead[1]]=1;
            }
            if (isValid(currHead[0]+1, currHead[1])) {//right
                grid[currHead[0]+1][currHead[1]]=1;
            }
        }
    }

    
    public static int decisionMaker(AStar finder, int[][] grid, boolean invis, ArrayList<int[]> enemyHeads, int[] head, int[] tail,int length, int[] apple){
        int move =5;
        drawBoundaries(grid,enemyHeads);
        if (length<30) {
            if (invis) {
                move = finder.pathfinder(grid, head, tail);
                //System.out.println("log invis snake: chasing tail");
            }
            else {
                move = finder.pathfinder(grid, head, apple);
                if (move==5) {
                    move = finder.pathfinder(grid, head, tail);
                    //System.out.println("log chasing tail");
                }
            }
        }
        else{
            double headToGoal = manhattanDistance(head[0], head[1], apple[0], apple[1]);
            boolean closest = true;
            for (int[] i : enemyHeads) {
                double enemyToGoal = manhattanDistance(i[0], i[1], apple[0], apple[1]);
                if (headToGoal>enemyToGoal) {
                    closest = false;
                }
            }
            if (closest) {
                move = finder.pathfinder(grid, head, apple);
            }
            else{
                move = finder.pathfinder(grid, head, tail);
            }
        }
        return move;
    }



    private int getClosestApple(int[][] apples, int head[]){
        int bestDist = 9999;
        int index = -1;
        if(apples[0][0]==-1 && apples[1][0]!=-1){
            return 1;
        }
        else if(apples[0][0]!=-1&& apples[1][0]==-1){
            return 0;
        }
        else{
            for (int i = 0; i < apples.length; i++) {
                int appleX = apples[i][0];
                int appleY = apples[i][1];
                int dist = Math.abs(head[0]-appleX) +Math.abs(head[1]-appleY);
                if (dist<bestDist) {
                    index = i;
                    bestDist = dist;
                }
            }
        }
        
        return index;
    }

    public static double manhattanDistance(int x1, int y1, int x2, int y2){
        return Math.abs(x1-x2)+Math.abs(y1-y2);
    }

    public static void printGrid(int[][] grid) throws IOException{
        Path fileName = Path.of("log.txt");
        String ans = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (j==0) {
                    ans+=i+1+"\t";
                }
                ans+=grid[j][i]+" ";
            }
            ans+="\n";
        }
        Files.writeString(fileName,ans);
    }
}