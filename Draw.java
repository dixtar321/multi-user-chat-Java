//import java.io.IOException;

public class Draw {
    private char[][] board = new char[3][3];

    public Draw(){
        super();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                board[i][j] = ' ';
            }
        }
    }

    public String get_board(){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                res.append("| ").append(board[i][j]).append(" ");
            }
            res.append("|\n");
            if (i < 2) {
                res.append("|---|---|---|\n");
            }
        }
        return res.toString();
    }
    

    public synchronized boolean win_check() {
        // Проверка строк, столбцов и диагоналей на наличие выигрышной комбинации
    
        for (int i = 0; i < 3; i++) {
            // Проверка строк
            if (checkEqual(board[i][0], board[i][1], board[i][2])) {
                if (board[i][0] != ' ') {
                    return true;
                }
            }
    
            // Проверка столбцов
            if (checkEqual(board[0][i], board[1][i], board[2][i])) {
                if (board[0][i] != ' ') {
                    return true;
                }
            }
        }
    
        // Проверка диагоналей
        if (checkEqual(board[0][0], board[1][1], board[2][2])) {
            if (board[0][0] != ' ') {
                return true;
            }
        }
    
        if (checkEqual(board[0][2], board[1][1], board[2][0])) {
            if (board[0][2] != ' ') {
                return true;
            }
        }
    
        return false;
    }
    
    private boolean checkEqual(char a, char b, char c) {
        return (a != ' ' && a == b && b == c);
    }
    

    public synchronized boolean board_insert(int arg1, int arg2, char symb){
        if(board[arg1][arg2] == ' '){
            board[arg1][arg2] = symb;

            // try {
            //             ProcessBuilder processBuilder = new ProcessBuilder("clear");
            //             Process process = processBuilder.inheritIO().start();
            //             process.waitFor();
            //         } catch (IOException | InterruptedException e) {
            //             e.printStackTrace();
            //         }
                    System.out.println(get_board());
            return true;
        }
        else{return false;}

        
    }
}
