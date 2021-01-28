package com.it_intensive.eli3;

import java.util.*;


public class Navigation {
    private static final int mapScale = 15;
    private static final int cartDirections = 4;
    private static final int INF = 987654321;

    private static Vector<Status>[][][] adj = new Vector[mapScale][mapScale][cartDirections];
    private static int[] dx = {0, 1, 0, -1};
    private static int[] dy = {1, 0, -1, 0};

    private static Navigation instance;
    private Navigation(){
        // generate movement graph
        for(int x = 0; x < mapScale; x++)
            for(int y = 0; y < mapScale; y++) {
                if((x / 3) % 2 == 1 && (y / 3) % 2 == 1) continue;
                for (int d = 0; d < cartDirections; d++){
                    adj[x][y][d] = new Vector<Status>();
                    for(int nd = 0; nd < cartDirections; nd++){
                        if(nd == d) {
                            int nx = x + dx[nd], ny = y + dy[nd];
                            if(nx < 0 || ny < 0|| nx >= mapScale || ny >= mapScale)
                                continue;
                            if((nx / 3) % 2 != 1 || (ny / 3) % 2 != 1)
                                adj[x][y][d].add(new Status(nx, ny, d));
                        }
                        else adj[x][y][d].add(new Status(x, y, nd));
                    }
                }
            }
    }

    public static Navigation getInstance(){
        if(instance == null)
            instance = new Navigation();
        return instance;
    }

    public Point[] findShortestPath(Point locSource, Point locDestination){
        int[][][] minDistance = new int[mapScale][mapScale][cartDirections];
        Status[][][] statusPast = new Status[mapScale][mapScale][cartDirections];

        for(int i = 0; i < mapScale; i++)
            for(int j = 0; j < mapScale; j++)
                Arrays.fill(minDistance[i][j], INF);

        Queue<Status> q = new ArrayDeque<>();
        for(int i = 0; i < cartDirections; i++) {
            minDistance[locSource.x][locSource.y][i] = 0;
            statusPast[locSource.x][locSource.y][i] = new Status(-1, -1, -1);
            q.add(new Status(locSource.x, locSource.y, i));
        }

        while(!q.isEmpty()){
            Status cur = q.poll();
            for(int i = 0; i < cartDirections; i++)
                if(cur.x + dx[i] == locDestination.x && cur.y + dy[i] == locDestination.y){
                    statusPast[locDestination.x][locDestination.y][i] = cur;
                    return restorePath(locSource, new Status(locDestination.x, locDestination.y, i), statusPast);
                }

            for(Status next : adj[cur.x][cur.y][cur.d]){
                if(minDistance[next.x][next.y][next.d] <= minDistance[cur.x][cur.y][cur.d] + 1) continue;
                minDistance[next.x][next.y][next.d] = minDistance[cur.x][cur.y][cur.d] + 1;
                statusPast[next.x][next.y][next.d] = cur;
                q.add(next);
            }
        }
        return null;
    }

    private Point[] restorePath(Point locSource, Status statusEnd, Status[][][] statusPast){
        List<Status> pathStatus = new LinkedList<>();
        while(statusEnd.x >= 0 && statusEnd.y >= 0 && statusEnd.d >= 0){
            pathStatus.add(statusEnd);
            statusEnd = statusPast[statusEnd.x][statusEnd.y][statusEnd.d];
        }

        int pathIdx = pathStatus.size() - 1;
        Point[] locPath = new Point[pathStatus.size()];
        for(Status st : pathStatus)
            locPath[pathIdx--] = new Point(st.x, st.y);

        return locPath;
    }
}


class Point{
    static final int lengthLoad = 58;
    static final int lengthDisplay = 88;

    int x, y;
    public Point(){
        this.x = 0;
        this.y = 0;
    }
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    private static int convertGridToLocation(int x){
        int ret = (x / 6) * (lengthLoad + lengthDisplay);
        if(x / 3 % 2 == 0) ret += ((x % 3) * lengthLoad) / 3 - (x % 3) * 6;
        else ret += lengthLoad + ((x % 3) * lengthDisplay) / 3;
        return ret;
    }

    public static Point convertCartGridToLocation(Point loc){
        return new Point(convertGridToLocation(loc.x), convertGridToLocation(loc.y));
    }

    public static Point convertTargetGridToLocation(Point loc){
        int dx = ((loc.x % 3) - 1) * 7;
        int dy = ((loc.y % 3) - 2) * 5;
        return new Point(convertGridToLocation(loc.x) + dx, convertGridToLocation(loc.y) + dy);
    }

    public static Point convertPathGridToLocation(Point loc){
        return new Point(convertGridToLocation(loc.x) + 15, convertGridToLocation(loc.y) + 15);
    }

    public static Point convertLocationToCoordinate(Point loc, float dp){
        return new Point((int)(loc.x * dp), (int)(loc.y * dp));
    }
}


class Status{
    int x, y, d;
    Status(){}
    Status(int x, int y, int d){
        this.x = x;
        this.y = y;
        this.d = d;
    }
}