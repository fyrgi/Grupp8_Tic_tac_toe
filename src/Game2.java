import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Game2 implements ActionListener {

    private JFrame frame;
    private Random random = new Random();
    private JPanel panelKnappar;
    private ArrayList<JButton> buttons = new ArrayList<JButton>();
    private JButton button;
    private int buttonsClicked  = 0, gamesPlayed = 0;
    private boolean gameWinner = false ;
    private ArrayList<Player> players = new ArrayList<Player>();
    private static HashMap<String, ArrayList<String>> winConditions = new HashMap<>();


    Game2(){
        frame = new JFrame();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setTitle("Tic Tac Toe");

        createWinConditions();
        layoutCenter2();
        addPlayers();
        assignTurnOrder();

        frame.setVisible(true);
    }

    void layoutCenter2(){
        panelKnappar = new JPanel();
        panelKnappar.setLayout(new GridLayout(3,3,1,1));
        frame.add(panelKnappar,BorderLayout.CENTER);
        for(int i = 1; i < 4; i++){
            for(int j = 1; j < 4; j++){
                button = new JButton();
                button.setName(""+i+j);
                button.setActionCommand(""+i+j);
                button.addActionListener(this);
                buttons.add(button);
            }
        }
        for(JButton button : buttons){
            panelKnappar.add(button);
        }
    }

    @Override

    public void actionPerformed(ActionEvent e) {
        String playedButton = e.getActionCommand();
        String playerSign = currentPlayerChoice(playedButton);
        try {
            addClickSound();
        } catch (UnsupportedAudioFileException ex) {
            throw new RuntimeException(ex);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (LineUnavailableException ex) {
            throw new RuntimeException(ex);
        }
        for(JButton button : buttons){
            if(button.getActionCommand().equals(playedButton)) {
                buttonsClicked++;
                button.setText(playerSign);
                button.setEnabled(false);
                if(buttonsClicked == 9 || gameWinner){
                    //TODO show dialog ask if the player wants the game to be restarted and then call restartGame();
                    gamesPlayed++;
                    restartGame();
                }
            }
        }

    }


    //Show the window to ask for the player's names. TODO Can be designed
    public void addPlayer(int numberOfPlayer){
        // Create an object of class player. We will need 2 players in a Multiplayer game.
        // They will have their own symbol based on a randomized funtion to assign it.
        String message = "Player "+ (numberOfPlayer + 1) + " name";
        //TODO get the solution grom Game about empty value/Cancel
        String name = JOptionPane.showInputDialog(message);
        players.add(new Player(name));
    }


    // hardcoded amount of players. In case we want more players we can pass a variable telling us how many they will be.
    public void addPlayers(){
        for(int i = 0; i < 2; i++){
            addPlayer(i);
        }
    }


    // set a player order. Works for hardcoded 2 players. It will need a different way of thinking in case of more player.
    public void assignTurnOrder(){
        int decideFirst = random.nextInt(1, 101);
        if(decideFirst % 2 == 0){
            players.get(0).setPlayerOrder(1);
            players.get(0).setCurrent(true);
            players.get(1).setPlayerOrder(2);
        } else {
            players.get(0).setPlayerOrder(2);
            players.get(1).setPlayerOrder(1);
            players.get(1).setCurrent(true);
        }
    }


    public String currentPlayerChoice(String playedButton) {
        String playedSing = "Err";
        for (Player player : players) {
            if (player.isCurrent()) {
                player.makeChoice(playedButton);
                if (!player.checkForWin()) {
                    player.setCurrent(false);
                    if (players.getFirst().equals(player)) {
                        players.getLast().setCurrent(true);
                    } else {
                        players.getFirst().setCurrent(true);
                    }
                    return player.playerSign;
                } else {
                    player.setWonRounds();
                    //TODO try and catch the errors in the function as suggested from teacher.
                    try {
                        gameWinner = true;
                        addWinSound();
                    } catch (UnsupportedAudioFileException ex) {
                        throw new RuntimeException(ex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (LineUnavailableException ex) {
                        throw new RuntimeException(ex);
                    }
                    return player.playerSign;
                }
            }
        }
        return playedSing;
    }


    public void restartGame(){
        buttonsClicked=0;
        gamesPlayed++;
        gameWinner = false;
        for(JButton button : buttons){
            button.setText("");
            button.setEnabled(true);
        }
        assignTurnOrder();
    }


    public void createWinConditions(){
        ArrayList<String> row1 = new ArrayList<>();
        row1.add("11");
        row1.add("12");
        row1.add("13");
        ArrayList<String> row2 = new ArrayList<>();
        row2.add("21");
        row2.add("22");
        row2.add("23");
        ArrayList<String> row3 = new ArrayList<>();
        row3.add("31");
        row3.add("32");
        row3.add("33");
        ArrayList<String> col1 = new ArrayList<>();
        col1.add("11");
        col1.add("21");
        col1.add("31");
        ArrayList<String> col2 = new ArrayList<>();
        col2.add("12");
        col2.add("22");
        col2.add("32");
        ArrayList<String> col3 = new ArrayList<>();
        col3.add("13");
        col3.add("23");
        col3.add("33");
        ArrayList<String> diag1 = new ArrayList<>();
        diag1.add("11");
        diag1.add("22");
        diag1.add("33");
        ArrayList<String> diag2 = new ArrayList<>();
        diag2.add("13");
        diag2.add("22");
        diag2.add("31");
        winConditions.put("row 1", row1);
        winConditions.put("row 2", row2);
        winConditions.put("row 3", row3);
        winConditions.put("col 1", col1);
        winConditions.put("col 2", col2);
        winConditions.put("col 3", col3);
        winConditions.put("diag 1", diag1);
        winConditions.put("diag 2", diag2);
    }


    public static HashMap<String, ArrayList<String>> getWinConditions() {
        return winConditions;
    }


    public void addClickSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("Pen Clicking (online-audio-converter.com).wav");
        AudioInputStream clickAudio = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(clickAudio);
        clip.start();
    }


    public void addWinSound() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file = new File("Slapping Three Faces.wav");
        AudioInputStream winSound = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(winSound);
        clip.start();
    }
}

