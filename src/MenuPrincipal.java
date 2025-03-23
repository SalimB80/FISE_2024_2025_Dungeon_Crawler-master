import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class MenuPrincipal extends JFrame {
    private Image imageFond;
    private Image imageTexte;
    private float scale = 0.4f;
    private boolean scalingUp = true;

    public MenuPrincipal() {
        // Charger les images
        try {
            imageFond = ImageIO.read(new File("img/accueil.png"));
            imageTexte = ImageIO.read(new File("img/text_accueil.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Configurer la fenêtre
        setTitle("Écran d'accueil animé");
        setSize(imageFond.getWidth(null), imageFond.getHeight(null));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Ajouter le panneau personnalisé
        setContentPane(new PanneauAnimation());

        // Ajouter un écouteur de clavier
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    lancerJeu();
                }
            }
        });

        // Permet de capter les événements clavier même si aucun composant n'a le focus
        setFocusable(true);
        requestFocusInWindow();

        setVisible(true);
    }

    private void lancerJeu() {
        System.out.println("Jeu lancé !");
        dispose(); // Ferme le menu
        try {
            new Main(); // Lance le jeu
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PanneauAnimation extends JPanel {
        private Timer timer;

        public PanneauAnimation() {
            // Initialiser le timer pour l'animation
            timer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Mettre à jour l'échelle
                    if (scalingUp) {
                        scale += 0.01f;
                        if (scale >= 0.4f) {
                            scalingUp = false;
                        }
                    } else {
                        scale -= 0.01f;
                        if (scale <= 0.3f) {
                            scalingUp = true;
                        }
                    }
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Dessiner l'image de fond
            g2d.drawImage(imageFond, 0, 0, getWidth(), getHeight(), this);

            // Calculer la taille et la position de l'image de texte
            int texteWidth = (int) (imageTexte.getWidth(null) * scale);
            int texteHeight = (int) (imageTexte.getHeight(null) * scale);
            int x = (getWidth() - texteWidth) / 2;
            int y = (getHeight() - texteHeight) / 2 + 120;

            // Activer le rendu de haute qualité
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Dessiner l'image de texte avec la transformation d'échelle
            g2d.drawImage(imageTexte, x, y, texteWidth, texteHeight, this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPrincipal::new);
    }
}
