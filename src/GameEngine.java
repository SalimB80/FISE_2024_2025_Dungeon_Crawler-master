import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class GameEngine implements Engine, KeyListener {
    DynamicSprite hero;
    private Set<Integer> pressedKeys = new HashSet<>();

    public GameEngine(DynamicSprite hero) {
        this.hero = hero;
    }

    @Override
    public void update() {
        // Gérer les mises à jour du jeu, comme les mouvements
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Gérer les actions de type KeyTyped si nécessaire (non utilisé ici)
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        updateDirection();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        if (pressedKeys.isEmpty()) {
            hero.setDirection(Direction.STOP);
        } else {
            updateDirection();
        }
    }

    private void updateDirection() {
        boolean up = pressedKeys.contains(KeyEvent.VK_UP);
        boolean down = pressedKeys.contains(KeyEvent.VK_DOWN);
        boolean left = pressedKeys.contains(KeyEvent.VK_LEFT);
        boolean right = pressedKeys.contains(KeyEvent.VK_RIGHT);

        if (up && right) {
            hero.setDirection(Direction.NORTH_EAST);
        } else if (up && left) {
            hero.setDirection(Direction.NORTH_WEST);
        } else if (down && right) {
            hero.setDirection(Direction.SOUTH_EAST);
        } else if (down && left) {
            hero.setDirection(Direction.SOUTH_WEST);
        } else if (up) {
            hero.setDirection(Direction.NORTH);
        } else if (down) {
            hero.setDirection(Direction.SOUTH);
        } else if (left) {
            hero.setDirection(Direction.WEST);
        } else if (right) {
            hero.setDirection(Direction.EAST);
        }
    }
}