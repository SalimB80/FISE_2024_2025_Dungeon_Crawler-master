import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class DynamicSprite extends SolidSprite {
    private Direction direction = Direction.STOP;
    private double speed = 10;
    private double timeBetweenFrame = 250;
    private boolean isWalking = true;
    private final int spriteSheetNumberOfColumn = 10;
    private double health = 100;
    private double saturation = 100;
    private double damage = 10;
    boolean isMoving = false;
    private int lastFrameIndex = 0;

    public DynamicSprite(double x, double y, Image image, double width, double height) {
        super(x, y, image, width, height);
        startSaturationDrain();
    }


    private void startSaturationDrain() {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100000); // Attendre 1 seconde
                    if (speed == 5) {
                        saturation -= 5;
                        if (saturation < 50) {
                            speed = 3; // Réduire la vitesse si la saturation est trop basse
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true); // Permet au thread de s'arrêter quand le programme se termine
        thread.start();
    }

    private boolean isMovingPossible(ArrayList<Sprite> environment) {
        Rectangle2D.Double moved = new Rectangle2D.Double();

        // Mettre à jour la vitesse selon la saturation
        if (saturation < 50) {
            speed = 3;
        } else {
            speed = 5;
        }

        switch (direction) {
            case EAST -> moved.setRect(super.getHitBox().getX() + speed, super.getHitBox().getY(),
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
            case WEST -> moved.setRect(super.getHitBox().getX() - speed, super.getHitBox().getY(),
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
            case NORTH -> moved.setRect(super.getHitBox().getX(), super.getHitBox().getY() - speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
            case SOUTH -> moved.setRect(super.getHitBox().getX(), super.getHitBox().getY() + speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
            case SOUTH_EAST -> moved.setRect(super.getHitBox().getX() + speed, super.getHitBox().getY() + speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
            case NORTH_EAST -> moved.setRect(super.getHitBox().getX() + speed, super.getHitBox().getY() - speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
            case SOUTH_WEST -> moved.setRect(super.getHitBox().getX() - speed, super.getHitBox().getY() + speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
            case NORTH_WEST -> moved.setRect(super.getHitBox().getX() - speed, super.getHitBox().getY() - speed,
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
            case STOP -> moved.setRect(super.getHitBox().getX(), super.getHitBox().getY(),
                    super.getHitBox().getWidth(), super.getHitBox().getHeight());
        }

        for (Sprite s : environment) {
            if ((s instanceof SolidSprite) && (s != this)) {
                if (((SolidSprite) s).intersect(moved)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        isMoving = true;  // Le personnage est en mouvement quand la direction change
    }
    public void stopMoving() {
        isMoving = false;  // Le personnage s'arrête
    }

    private void move() {
        if (direction != Direction.STOP) {  // Si la direction est STOP, ne pas bouger
            switch (direction) {
                case NORTH:
                    this.y -= speed;
                    break;
                case SOUTH:
                    this.y += speed;
                    break;
                case EAST:
                    this.x += speed;
                    break;
                case WEST:
                    this.x -= speed;
                    break;
                case NORTH_EAST:
                    this.x += speed / Math.sqrt(2);  // Normalise pour ne pas se déplacer plus vite en diagonale
                    this.y -= speed / Math.sqrt(2);
                    break;
                case NORTH_WEST:
                    this.x -= speed / Math.sqrt(2);
                    this.y -= speed / Math.sqrt(2);
                    break;
                case SOUTH_EAST:
                    this.x += speed / Math.sqrt(2);
                    this.y += speed / Math.sqrt(2);
                    break;
                case SOUTH_WEST:
                    this.x -= speed / Math.sqrt(2);
                    this.y += speed / Math.sqrt(2);
                    break;
            }
            isMoving = true;
        }
        else{
            isMoving = false;
        }
    }

    public void moveIfPossible(ArrayList<Sprite> environment) {
        if (isMovingPossible(environment)) {
            move();
            isMoving = true;  // Le personnage est en mouvement
        } else {
            isMoving = false;  // Le personnage ne bouge pas, il est arrêté
        }
    }

    @Override
    public void draw(Graphics g) {
        int index = isMoving ? (int) (System.currentTimeMillis() / timeBetweenFrame % spriteSheetNumberOfColumn) : lastFrameIndex;

        // Si le personnage est en mouvement, mets à jour la frame
        if (isMoving) {
            lastFrameIndex = index;
        }

        g.drawImage(image, (int) x, (int) y, (int) (x + width), (int) (y + height),
                (int) (index * this.width), (int) (direction.getFrameLineNumber() * height),
                (int) ((index + 1) * this.width), (int) ((direction.getFrameLineNumber() + 1) * this.height), null);
    }
}