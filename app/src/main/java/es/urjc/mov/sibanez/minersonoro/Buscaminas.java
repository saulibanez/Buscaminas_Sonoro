package es.urjc.mov.sibanez.minersonoro;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Buscaminas extends AppCompatActivity {
    private static final String EASY = "EASY";
    private static final int EASY_GAME_ROWS = 4;
    private static final int EASY_GAME_COLUMS = 4;
    private static final int EASY_MINES = 3;

    private static final String MEDIUM = "MEDIUM";
    private static final int MEDIUM_GAME_ROWS = 5;
    private static final int MEDIUM_GAME_COLUMS = 6;
    private static final int MEDIUM_MINES = 10;

    private static final String HARD = "HARD";
    private static final int HARD_GAME_ROWS = 6;
    private static final int HARD_GAME_COLUMS = 7;
    private static final int HARD_MINES = 15;

    private static final int WHITE = 0x0000FF00;

    private Board[][] board;
    private BoardOperations init_game;
    private Level level;
    private TableLayout box;
    private ImageButton newGameButton;
    private ImageButton ib[][];
    private int imgs_num[] = {R.mipmap.zero, R.mipmap.one, R.mipmap.two, R.mipmap.three,
            R.mipmap.four, R.mipmap.five, R.mipmap.six, R.mipmap.seven, R.mipmap.eight};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscaminas);

        levelSelect(EASY);

        newGameButton = (ImageButton) findViewById(R.id.reset);
        newGameButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Buscaminas.this.drawSmile();
            }
        });

        initGame();
        drawBoard();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.easy:
                Toast.makeText(getApplicationContext(), "Easy Selected", Toast.LENGTH_SHORT).show();
                levelSelect(EASY);
                break;
            case R.id.medium:
                Toast.makeText(getApplicationContext(), "Medium Selected", Toast.LENGTH_SHORT).show();
                levelSelect(MEDIUM);
                break;
            case R.id.hard:
                Toast.makeText(getApplicationContext(), "Hard Selected", Toast.LENGTH_SHORT).show();
                levelSelect(HARD);
                break;
            case R.id.help:
                Toast.makeText(getApplicationContext(), "Andeandaran las minas", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.playMachine:
                drawPlayMachine();
                Toast.makeText(getApplicationContext(), "La maquina juega sola", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        box.removeAllViews();
        initGame();
        drawBoard();
        return true;
    }

    private void levelSelect(String level) {
        if (level.equals(EASY)) {
            this.level = new Level(EASY_GAME_ROWS, EASY_GAME_COLUMS, EASY_MINES, EASY_GAME_ROWS * EASY_GAME_COLUMS);
        } else if (level.equals(MEDIUM)) {
            this.level = new Level(MEDIUM_GAME_ROWS, MEDIUM_GAME_COLUMS, MEDIUM_MINES, MEDIUM_GAME_ROWS * MEDIUM_GAME_COLUMS);
        } else if (level.equals(HARD)) {
            this.level = new Level(HARD_GAME_ROWS, HARD_GAME_COLUMS, HARD_MINES, HARD_GAME_ROWS * HARD_GAME_COLUMS);
        }
    }

    private void drawSmile() {
        box.removeAllViews();
        this.newGameButton.setImageResource(R.drawable.playing);
        initGame();
        drawBoard();
    }

    private void initGame() {
        boolean push_click = false;
        init_game = new BoardOperations(level.getRow(), level.getCol(), level.getMines(), push_click);
        board = init_game.initGame();
    }

    public BoardOperations getInitGame(){
        return init_game;
    }

    private void initImageBut(){
        ib = new ImageButton[level.getRow()][level.getCol()];
        for (int row = 0; row < level.getRow(); row++) {
            for (int col = 0; col < level.getCol(); col++) {
                ib[row][col] = new ImageButton(this);
            }
        }
    }

    private int sizeWindows(String type) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (type.equals("width")) {
            return size.x / level.getCol();
        } else {
            return size.y / level.getRow();
        }
    }

    private TableRow initTableRow() {
        int width = sizeWindows("width") * level.getCol();
        int height = sizeWindows("height");

        TableRow table_row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(width, height);
        table_row.setLayoutParams(lp);

        return table_row;
    }

    private void drawMines() {
        if (box != null) {
            box.removeAllViews();
        }
        box = (TableLayout) findViewById(R.id.board);
        boolean with_sound = false;
        for (int row = 0; row < level.getRow(); row++) {
            TableRow table_row = initTableRow();

            for (int col = 0; col < level.getCol(); col++) {
                ImageButton imgbut = new ImageButton(this);
                imgbut.setBackgroundColor(WHITE);
                if (board[row][col].isMine()) {
                    imgbut.setImageResource(R.mipmap.mine);
                } else {
                    if (board[row][col].getNeighborMinesCount() != 0) {
                        drawNeighborMinesCount(imgbut, row, col, with_sound);
                        board[row][col].setNeighborMinesCount(0);
                    } else {
                        imgbut.setImageResource(R.mipmap.block);
                    }
                }
                table_row.addView(imgbut);
            }
            table_row.setGravity(Gravity.CENTER);
            box.addView(table_row);
        }
    }

    private void drawBoard() {
        box = (TableLayout) findViewById(R.id.board);
        initImageBut();
        for (int row = 0; row < level.getRow(); row++) {
            TableRow table_row = initTableRow();

            for (int col = 0; col < level.getCol(); col++) {
                ImageButton imgbut = new ImageButton(this);
                ib[row][col] = imgbut;
                imgbut.setBackgroundColor(WHITE);
                imgbut.setImageResource(R.mipmap.block);
                imgbut.setContentDescription("position button xy:" + String.valueOf(row) + String.valueOf(col));
                imgbut.setOnClickListener(new Box(new Board(row, col, board[row][col].isMine(), board[row][col].isClick(), board[row][col].isCovered()), level));
                table_row.addView(imgbut);
            }
            table_row.setGravity(Gravity.CENTER);

            box.addView(table_row);
        }
    }

    private void drawNeighborMinesCount(ImageButton imgbut, int row, int col, boolean with_sound) {
        int num_bomb = imgs_num[board[row][col].getNeighborMinesCount()];
        if(with_sound)
            freqTone((board[row][col].getNeighborMinesCount()+10)*10);
        imgbut.setImageResource(num_bomb);
    }

    private void drawPlayMachine() {
        boolean win = true;

        if (box != null) {
            box.removeAllViews();
        }
        box = (TableLayout) findViewById(R.id.board);
        boolean with_sound = false;

        for (int row = 0; row < level.getRow(); row++) {
            TableRow table_row = initTableRow();

            for (int col = 0; col < level.getCol(); col++) {
                ImageButton imgbut = new ImageButton(this);
                imgbut.setBackgroundColor(WHITE);
                if (board[row][col].isMine()) {
                    imgbut.setImageResource(R.mipmap.mine);
                } else {
                    board[row][col].setNeighborMinesCount(0);
                    board[row][col].setNeighborMinesCount(init_game.getNeighbor(row, col));
                    drawNeighborMinesCount(imgbut, row, col, with_sound);
                }
                table_row.addView(imgbut);
            }
            table_row.setGravity(Gravity.CENTER);
            box.addView(table_row);
        }
        winner(win);
    }

    private synchronized void freqTone(int freqOfTone){
        new GenerateSound().play(freqOfTone);
    }

    private void winner(boolean win) {
        int freq_tone_win = 300;
        int freq_tone_lose = 600;

        if (win) {
            freqTone(freq_tone_win);
            Intent winner = new Intent(Buscaminas.this, Winner.class);
            winner.addFlags(winner.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(winner);
            Toast.makeText(getApplicationContext(), "YOU WIN", Toast.LENGTH_SHORT).show();
        }else {
            freqTone(freq_tone_lose);
            Intent loser = new Intent(Buscaminas.this, Loser.class);
            loser.addFlags(loser.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loser);
            Toast.makeText(getApplicationContext(), "YOU LOSE", Toast.LENGTH_SHORT).show();
        }
    }

    private class Box implements OnClickListener {
        private Board tablero;
        private int flag_count;
        private Board flag[][];
        private Level level;
        private boolean is_winner = false;

        public Box(Board board, Level level) {
            this.tablero = board;
            this.level = level;
        }

        @Override
        public void onClick(View v) {
            ImageButton imgbut = (ImageButton) v;
            imgbut.setBackgroundColor(WHITE);

            //imgbut.setContentDescription("position button xy:" + String.valueOf(tablero.getRow()) + String.valueOf(tablero.getColumn()));

            //Log.v("getcontent: ", ""+imgbut.getContentDescription());
            uncover(imgbut);
        }

        private void uncover(ImageButton imgbut) {

            if (tablero.isMine()) {
                imgbut.setImageResource(R.mipmap.mine);
                winner(is_winner);
                drawMines();
                finish();
            } else {
                boolean with_sound = true;
                recursiveUncover(tablero.getRow(), tablero.getColumn(), imgbut, with_sound);
            }
        }

        private void recursiveUncover(int row, int col, ImageButton imgbut, boolean with_sound) {

            if (!tablero.isMine()) {
                board[row][col].setNeighborMinesCount(0);
                tablero.setNeighborMinesCount(init_game.getNeighbor(row, col));
                drawNeighborMinesCount(imgbut, row, col, with_sound);
            }

            board[row][col].setCovered(false);

            if (!board[row][col].isClick())
                level.setCovered_blocks_count(level.getCovered_blocks_count() - 1);

            board[row][col].setClick(true);

            if (level.getCovered_blocks_count() == level.getMines()) {
                is_winner = true;
                winner(is_winner);
                finish();
            }

            if (tablero.isEmpty()) {
                for (BoardOperations loc : this.getNeighbors(row, col)) {
                    if (loc.getRows() <= level.getRow()-1 && loc.getRows() >= 0 &&
                            loc.getCols() <= level.getCol()-1 && loc.getCols() >= 0) {
                        if (board[loc.getRows()][loc.getCols()].isCovered()) {
                            imgbut = ib[loc.getRows()][loc.getCols()];
                            with_sound = false;
                            recursiveUncover(loc.getRows(), loc.getCols(), imgbut, with_sound);
                        }
                    }
                }
            }
        }

        private BoardOperations[] getNeighbors(int row, int col) {
            return new BoardOperations[]{
                    new BoardOperations(row - 1, col - 1),
                    new BoardOperations(row - 1, col),
                    new BoardOperations(row - 1, col + 1),
                    new BoardOperations(row, col - 1),
                    new BoardOperations(row, col + 1),
                    new BoardOperations(row + 1, col - 1),
                    new BoardOperations(row + 1, col),
                    new BoardOperations(row + 1, col + 1),
            };
        }

        private void flag(int row, int col) {
            if (!flag[row][col].isFlagged()) {
                this.flag_count++;
                flag[row][col].setFlagged(true);
            }
        }

        private void unflag(int row, int col) {
            if (this.flag_count >= 0 && flag[row][col].isFlagged()) {
                this.flag_count--;
                flag[row][col].setFlagged(false);
            }
        }
    }
}