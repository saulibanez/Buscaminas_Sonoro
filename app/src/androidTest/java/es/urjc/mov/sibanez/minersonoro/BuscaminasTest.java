package es.urjc.mov.sibanez.minersonoro;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.ImageButton;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;


@RunWith(AndroidJUnit4.class)
public class BuscaminasTest {
    private Buscaminas miner;
    int contador = 16;


    @Rule
    public ActivityTestRule<Buscaminas> mActivityRule = new ActivityTestRule<>(Buscaminas.class);

    @Test
    public void minerTest() throws Exception {
        miner = mActivityRule.getActivity();
        Board[][] board;
        BoardOperations init_game;
        BoardOperations game = miner.getInitGame();

        init_game = new BoardOperations(4, 4, 3, false);
        board = init_game.initGame();
        String row = "";
        String col = "";

        for (int i = 0 ; i < game.getRows() ; i++) {
            for (int j = 0; j < game.getCols(); j++) {
                if (!board[i][j].isMine()){
                    row = String.valueOf(i);
                    col = String.valueOf(j);
                    onView(withContentDescription("position button xy:"+row+col)).perform(click());
                }else{
                    System.err.println("MINA ENCONTRADA, HAS PERDIDO");
                    fail();
                }

                if (result()){
                    System.out.println("HAS GANADO");
                    break;
                }
            }
        }
    }

    private boolean result(){

        contador--;
        if(contador == 3){
            return true;
        }
        return false;
    }
}
