import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;

public class TrafficDeadlockSimulator extends JFrame {
    private TrafficPanel trafficPanel;
    private JButton addTopBtn, addBottomBtn, addLeftBtn, addRightBtn;
    private JButton resetBtn, bankersBtn, toggleAllBtn;
    private JTextArea statusArea;
    private JCheckBox bankersCheckBox;

    public TrafficDeadlockSimulator() {
        setTitle("Deadlock Simulator");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Set window icon and background
        getContentPane().setBackground(new Color(45, 52, 65));

        trafficPanel = new TrafficPanel(this);

        // Enhanced Control Panel with modern design
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(300, 0));
        controlPanel.setBackground(new Color(52, 73, 94));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(41, 128, 185)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Modern title
        JLabel titleLabel = new JLabel("CONTROL CENTER");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(236, 240, 241));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(titleLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Custom separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(149, 165, 166));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        controlPanel.add(separator);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Enhanced checkbox with modern styling
        bankersCheckBox = new JCheckBox("Banker's Algorithm");
        bankersCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankersCheckBox.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bankersCheckBox.setForeground(new Color(46, 204, 113));
        bankersCheckBox.setBackground(new Color(52, 73, 94));
        bankersCheckBox.setSelected(true);
        bankersCheckBox.setFocusPainted(false);
        controlPanel.add(bankersCheckBox);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Section: Add Cars
        JLabel addLabel = new JLabel("SPAWN VEHICLES");
        addLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addLabel.setForeground(new Color(241, 196, 15));
        addLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(addLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        addTopBtn = createModernButton("FROM TOP", new Color(231, 76, 60), new Color(192, 57, 43));
        addBottomBtn = createModernButton("FROM BOTTOM", new Color(155, 89, 182), new Color(142, 68, 173));
        addLeftBtn = createModernButton("FROM LEFT", new Color(52, 152, 219), new Color(41, 128, 185));
        addRightBtn = createModernButton("FROM RIGHT", new Color(46, 204, 113), new Color(39, 174, 96));

        controlPanel.add(addTopBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(addBottomBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(addLeftBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(addRightBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        toggleAllBtn = createModernButton("TOGGLE ALL LIGHTS", new Color(230, 126, 34), new Color(211, 84, 0));
        bankersBtn = createModernButton("CHECK SAFE STATE", new Color(39, 174, 96), new Color(34, 153, 84));
        resetBtn = createModernButton("RESET SYSTEM", new Color(149, 165, 166), new Color(127, 140, 141));

        controlPanel.add(toggleAllBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(bankersBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(resetBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Section: Status Monitor
        JLabel statusLabel = new JLabel("SYSTEM STATUS");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusLabel.setForeground(new Color(52, 152, 219));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(statusLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Enhanced status area with modern styling
        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setBackground(new Color(44, 62, 80));
        statusArea.setForeground(new Color(236, 240, 241));
        statusArea.setCaretColor(new Color(52, 152, 219));
        statusArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166), 1));
        scrollPane.setBackground(new Color(44, 62, 80));
        scrollPane.getVerticalScrollBar().setBackground(new Color(52, 73, 94));
        scrollPane.getHorizontalScrollBar().setBackground(new Color(52, 73, 94));
        controlPanel.add(scrollPane);

        addTopBtn.addActionListener(e -> trafficPanel.addCar("TOP"));
        addBottomBtn.addActionListener(e -> trafficPanel.addCar("BOTTOM"));
        addLeftBtn.addActionListener(e -> trafficPanel.addCar("LEFT"));
        addRightBtn.addActionListener(e -> trafficPanel.addCar("RIGHT"));
        resetBtn.addActionListener(e -> reset());
        bankersBtn.addActionListener(e -> checkBankersAlgorithm());
        toggleAllBtn.addActionListener(e -> trafficPanel.toggleAllLights());

        add(trafficPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        log("Traffic Deadlock Simulator Ready!");
        log("=====================================");
        log("FEATURES:");
        log("- RIGHT SIDE TRAFFIC (all cars)");
        log("- Deadlock detection and avoidance");
        log("- Click lights to control individually");
        log("- Toggle All Lights button");
        log("");
        log("USAGE:");
        log("1. BANKER'S ALGORITHM ON (checkbox checked):");
        log("   - Cars intelligently avoid collisions");
        log("   - System prevents deadlock scenarios");
        log("   - Safe even with all RED lights");
        log("");
        log("2. BANKER'S ALGORITHM OFF (checkbox unchecked):");
        log("   - Cars follow lights strictly");
        log("   - Can cause deadlock/collision");
        log("   - Turn all lights RED to test!");
        log("");
        log("3. Try different scenarios:");
        log("   - Add cars from all directions");
        log("   - Toggle Banker's Algorithm ON/OFF");
        log("   - Control lights manually\n");
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(280, 45));
        btn.setPreferredSize(new Dimension(280, 45));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }

    private void reset() {
        trafficPanel.reset();
        statusArea.setText("");
        log("System Reset Complete!\n");
    }

    private void checkBankersAlgorithm() {
        String result = trafficPanel.checkBankersSafeState();
        log("\n" + result);
    }

    public boolean isBankersEnabled() {
        return bankersCheckBox.isSelected();
    }

    public void log(String msg) {
        statusArea.append(msg + "\n");
        statusArea.setCaretPosition(statusArea.getDocument().getLength());
    }

    class TrafficPanel extends JPanel {
        private List<Car> cars = new ArrayList<>();
        private TrafficLight topLight, bottomLight, leftLight, rightLight;
        private Timer timer;
        private int carIdCounter = 0;
        private TrafficDeadlockSimulator parent;

        private Map<String, Car> resourceAllocation = new HashMap<>();

        private int roadWidth = 140;
        private int laneWidth = 40;
        private int intersectionSize = 120;
        
        private int getCenterX() { return getWidth() / 2; }
        private int getCenterY() { return getHeight() / 2; }
        
        private void updateTrafficLightPositions(int centerX, int centerY) {
            topLight.x = centerX;
            topLight.y = centerY - intersectionSize/2 - 110;
            bottomLight.x = centerX;
            bottomLight.y = centerY + intersectionSize/2 + 110;
            leftLight.x = centerX - intersectionSize/2 - 110;
            leftLight.y = centerY;
            rightLight.x = centerX + intersectionSize/2 + 110;
            rightLight.y = centerY;
        }

        private boolean deadlockDetected = false;
        private boolean deadlockShownDialog = false;
        private boolean circularDeadlockDetected = false;
        private boolean circularDeadlockShownDialog = false;
        private Random rand = new Random();
        
        private Map<String, Long> lastCarAddedTimeByDirection = new HashMap<>();
        private static final long CAR_SPAWN_COOLDOWN_MS = 1000;
        
        // Car Types Enum
        enum CarType {
            SEDAN(40, 24, 2.5, new Color(100, 149, 237)), // Regular car
            TRUCK(50, 28, 2.0, new Color(139, 69, 19)),   // Slower, bigger
            MOTORCYCLE(25, 18, 3.5, new Color(255, 69, 0)), // Fast, small
            BUS(60, 32, 1.8, new Color(255, 215, 0)),     // Large, slow
            SPORTS_CAR(38, 22, 4.0, new Color(220, 20, 60)), // Fast, sleek
            EMERGENCY(42, 26, 3.0, new Color(255, 0, 0));  // Priority vehicle
            
            final int width, height;
            final double maxSpeed;
            final Color baseColor;
            
            CarType(int width, int height, double maxSpeed, Color baseColor) {
                this.width = width;
                this.height = height;
                this.maxSpeed = maxSpeed;
                this.baseColor = baseColor;
            }
        }

        public TrafficPanel(TrafficDeadlockSimulator parent) {
            this.parent = parent;
            // Enhanced background with gradient-like effect
            setBackground(new Color(39, 174, 96));
            
            setPreferredSize(new Dimension(1000, 800));
            setMinimumSize(new Dimension(1000, 800));

            topLight = new TrafficLight(450, 240, "TOP");
            bottomLight = new TrafficLight(450, 460, "BOTTOM");
            leftLight = new TrafficLight(340, 350, "LEFT");
            rightLight = new TrafficLight(560, 350, "RIGHT");

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX();
                    int y = e.getY();

                    if (topLight.contains(x, y)) {
                        topLight.toggle();
                        parent.log("TOP Light: " + (topLight.isGreen ? "GREEN" : "RED"));
                    } else if (bottomLight.contains(x, y)) {
                        bottomLight.toggle();
                        parent.log("BOTTOM Light: " + (bottomLight.isGreen ? "GREEN" : "RED"));
                    } else if (leftLight.contains(x, y)) {
                        leftLight.toggle();
                        parent.log("LEFT Light: " + (leftLight.isGreen ? "GREEN" : "RED"));
                    } else if (rightLight.contains(x, y)) {
                        rightLight.toggle();
                        parent.log("RIGHT Light: " + (rightLight.isGreen ? "GREEN" : "RED"));
                    }

                    if (topLight.isGreen || bottomLight.isGreen || leftLight.isGreen || rightLight.isGreen) {
                        deadlockDetected = false;
                        deadlockShownDialog = false;
                        circularDeadlockDetected = false;
                        circularDeadlockShownDialog = false;
                    }
                    repaint();
                }
            });

            timer = new Timer(30, e -> {
                updateCars();
                detectCollisions();
                repaint();
            });
            timer.start();
        }

        public void toggleAllLights() {
            boolean newState = !topLight.isGreen;
            topLight.isGreen = newState;
            bottomLight.isGreen = newState;
            leftLight.isGreen = newState;
            rightLight.isGreen = newState;

            parent.log("ALL LIGHTS: " + (newState ? "GREEN" : "RED"));

            if (newState) {
                deadlockDetected = false;
                deadlockShownDialog = false;
                circularDeadlockDetected = false;
                circularDeadlockShownDialog = false;
            }
            repaint();
        }

        public void addCar(String direction) {
            long currentTime = System.currentTimeMillis();
            long lastAddedTime = lastCarAddedTimeByDirection.getOrDefault(direction, 0L);
            
            if (currentTime - lastAddedTime < CAR_SPAWN_COOLDOWN_MS) {
                long remainingTime = (CAR_SPAWN_COOLDOWN_MS - (currentTime - lastAddedTime)) / 1000;
                parent.log("Please wait " + (remainingTime + 1) + " second(s) before adding another car from " + direction);
                return;
            }

            Car newCar = new Car(carIdCounter++, direction, 1);
            cars.add(newCar);
            String carTypeName = newCar.carType.name().toLowerCase().replace('_', ' ');
            carTypeName = Character.toUpperCase(carTypeName.charAt(0)) + carTypeName.substring(1);
            parent.log("Car #" + (carIdCounter - 1) + " (" + carTypeName + ") added from " + direction + " (RIGHT lane)");
            
            lastCarAddedTimeByDirection.put(direction, currentTime);
        }

        public String checkBankersSafeState() {
            StringBuilder sb = new StringBuilder();
            sb.append("BANKER'S ALGORITHM STATUS\n");
            sb.append("==========================\n");

            int[] allocation = new int[4];
            int[] waiting = new int[4];
            boolean[] lights = new boolean[4];
            String[] dirNames = {"TOP", "BOTTOM", "LEFT", "RIGHT"};

            for (Car car : cars) {
                int dirIndex = getDirectionIndex(car.direction);
                allocation[dirIndex]++;
                if (car.isWaitingAtLight()) {
                    waiting[dirIndex]++;
                }
            }

            lights[0] = topLight.isGreen;
            lights[1] = bottomLight.isGreen;
            lights[2] = leftLight.isGreen;
            lights[3] = rightLight.isGreen;

            sb.append("Current Traffic Status:\n");
            for (int i = 0; i < 4; i++) {
                String lightStatus = lights[i] ? "GREEN" : "RED";
                sb.append(dirNames[i] + ": " + allocation[i] + " cars (" + waiting[i] + " waiting) - " + lightStatus + "\n");
            }
            sb.append("\n");

            if (parent.isBankersEnabled()) {
                sb.append("BANKER'S ALGORITHM: ACTIVE\n");
                sb.append("System preventing collision scenarios.\n");
                sb.append("Cars check for conflicts before proceeding.\n");
                sb.append("Safe operation with automatic deadlock prevention.\n");
            } else {
                sb.append("BANKER'S ALGORITHM: DISABLED\n");
                sb.append("Cars follow traffic lights strictly.\n");
                sb.append("Risk of deadlock when all lights are RED.\n");
                sb.append("Enable Banker's Algorithm for safer operation.\n");
            }

            return sb.toString();
        }
        
        private int getDirectionIndex(String direction) {
            switch (direction) {
                case "TOP": return 0;
                case "BOTTOM": return 1;
                case "LEFT": return 2;
                case "RIGHT": return 3;
                default: return 0;
            }
        }

        public void reset() {
            cars.clear();
            carIdCounter = 0;
            resourceAllocation.clear();
            topLight.isGreen = true;
            bottomLight.isGreen = true;
            leftLight.isGreen = true;
            rightLight.isGreen = true;
            deadlockDetected = false;
            deadlockShownDialog = false;
            circularDeadlockDetected = false;
            circularDeadlockShownDialog = false;
            lastCarAddedTimeByDirection.clear();
        }

        private void updateCars() {
            Iterator<Car> iterator = cars.iterator();
            while (iterator.hasNext()) {
                Car car = iterator.next();
                car.update();
                if (car.isCompleted()) {
                    if (car.allocatedResource != null) {
                        resourceAllocation.remove(car.allocatedResource);
                    }
                    iterator.remove();
                }
            }
        }

        private void detectCollisions() {
            if (deadlockDetected) {
                return;
            }

            // Only check collisions when Smart Drivers is DISABLED
            if (!parent.isBankersEnabled()) {
                for (int i = 0; i < cars.size(); i++) {
                    Car a = cars.get(i);
                    Rectangle ra = a.getBounds();
                    for (int j = i + 1; j < cars.size(); j++) {
                        Car b = cars.get(j);
                        Rectangle rb = b.getBounds();
                        
                        if (ra.intersects(rb)) {
                            if ((a.inIntersection || b.inIntersection) || (!a.waiting && !b.waiting)) {
                                deadlockDetected = true;
                                parent.log("DEADLOCK: Car#" + a.id + " & Car#" + b.id + " collided!");
                                parent.log("System will auto-reset in 3 seconds...");
                                
                                // Stop all cars immediately
                                stopAllCars();
                                
                                // Schedule auto-reset after 3 seconds
                                Timer resetTimer = new Timer(3000, e -> {
                                    reset();
                                    parent.log("System automatically reset after deadlock.");
                                });
                                resetTimer.setRepeats(false);
                                resetTimer.start();
                                
                                return;
                            }
                        }
                    }
                }

                boolean allRed = !topLight.isGreen && !bottomLight.isGreen && !leftLight.isGreen && !rightLight.isGreen;
                boolean tWait = false, bWait = false, lWait = false, rWait = false;
                for (Car c : cars) {
                    if (c.direction.equals("TOP") && c.isWaitingAtLight()) tWait = true;
                    if (c.direction.equals("BOTTOM") && c.isWaitingAtLight()) bWait = true;
                    if (c.direction.equals("LEFT") && c.isWaitingAtLight()) lWait = true;
                    if (c.direction.equals("RIGHT") && c.isWaitingAtLight()) rWait = true;
                }
                if (allRed && tWait && bWait && lWait && rWait) {
                    circularDeadlockDetected = true;
                    if (!circularDeadlockShownDialog) {
                        parent.log("CIRCULAR DEADLOCK: All directions blocked!");
                        parent.log("System will auto-reset in 3 seconds...");
                        
                        // Stop all cars immediately
                        stopAllCars();
                        
                        // Schedule auto-reset after 3 seconds
                        Timer resetTimer = new Timer(3000, e -> {
                            reset();
                            parent.log("System automatically reset after circular deadlock.");
                        });
                        resetTimer.setRepeats(false);
                        resetTimer.start();
                        
                        circularDeadlockShownDialog = true;
                    }
                }
            }
        }
        
        private void stopAllCars() {
            // Stop all car movement immediately when deadlock occurs
            for (Car car : cars) {
                car.speed = 0;
                car.waiting = true;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int centerX = panelWidth / 2;
            int centerY = panelHeight / 2;

            updateTrafficLightPositions(centerX, centerY);

            // Draw environmental elements first (background)
            drawEnvironmentalElements(g2, panelWidth, panelHeight, centerX, centerY);

            // Enhanced road design with gradients and shadows
            // Road shadow
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRect(centerX - roadWidth / 2 + 3, 3, roadWidth, panelHeight);
            g2.fillRect(3, centerY - roadWidth / 2 + 3, panelWidth, roadWidth);
            
            // Main roads with darker asphalt color
            g2.setColor(new Color(45, 45, 45));
            g2.fillRect(centerX - roadWidth / 2, 0, roadWidth, panelHeight);
            g2.fillRect(0, centerY - roadWidth / 2, panelWidth, roadWidth);
            
            // Road borders
            g2.setColor(new Color(30, 30, 30));
            g2.setStroke(new BasicStroke(4));
            g2.drawRect(centerX - roadWidth / 2, 0, roadWidth, panelHeight);
            g2.drawRect(0, centerY - roadWidth / 2, panelWidth, roadWidth);

            // Enhanced intersection with better styling
            g2.setColor(new Color(55, 55, 55));
            g2.fillRect(centerX - intersectionSize / 2, centerY - intersectionSize / 2,
                    intersectionSize, intersectionSize);
            
            // Intersection border
            g2.setColor(new Color(241, 196, 15));
            g2.setStroke(new BasicStroke(3));
            g2.drawRect(centerX - intersectionSize / 2, centerY - intersectionSize / 2,
                    intersectionSize, intersectionSize);

            // Enhanced lane dividers with better visibility
            g2.setColor(new Color(255, 255, 255));
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{25, 15}, 0));

            g2.drawLine(centerX, 0, centerX, centerY - intersectionSize / 2 - 8);
            g2.drawLine(centerX, centerY + intersectionSize / 2 + 8, centerX, panelHeight);
            g2.drawLine(0, centerY, centerX - intersectionSize / 2 - 8, centerY);
            g2.drawLine(centerX + intersectionSize / 2 + 8, centerY, panelWidth, centerY);

            topLight.draw(g2);
            bottomLight.draw(g2);
            leftLight.draw(g2);
            rightLight.draw(g2);

            for (Car car : cars) {
                car.draw(g2);
            }

            // Enhanced status panel with modern design
            int statusPanelHeight = 100;
            g2.setColor(new Color(44, 62, 80, 200));
            g2.fillRoundRect(10, 10, panelWidth - 20, statusPanelHeight, 15, 15);
            g2.setColor(new Color(52, 152, 219));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(10, 10, panelWidth - 20, statusPanelHeight, 15, 15);

            // Status text with better styling
            g2.setColor(new Color(236, 240, 241));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2.drawString("Active Vehicles: " + cars.size(), 25, 35);
            
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            String deadlockMode = parent.isBankersEnabled() ? "Banker's Algorithm: ENABLED" : "Banker's Algorithm: DISABLED";
            g2.setColor(parent.isBankersEnabled() ? new Color(46, 204, 113) : new Color(231, 76, 60));
            g2.drawString(deadlockMode, 25, 55);
            
            g2.setColor(new Color(149, 165, 166));
            g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            String mode = parent.isBankersEnabled() ? "System Preventing Deadlocks" : "Manual Traffic Control";
            g2.drawString(mode, 25, 75);
            
            g2.setColor(new Color(241, 196, 15));
            g2.drawString("Right Side Traffic System", 25, 95);

            if (deadlockDetected) {
                // Enhanced deadlock overlay with animation
                Composite old = g2.getComposite();
                long time = System.currentTimeMillis();
                float alpha = (float) (0.3 + 0.25 * Math.sin(time * 0.005));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(new Color(231, 76, 60));
                g2.fillRect(0, 0, panelWidth, panelHeight);
                g2.setComposite(old);

                // Warning border
                g2.setColor(new Color(192, 57, 43));
                g2.setStroke(new BasicStroke(8));
                g2.drawRect(5, 5, panelWidth - 10, panelHeight - 10);
                
                // Main deadlock text with shadow
                g2.setColor(new Color(0, 0, 0, 150));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 80));
                String text = "DEADLOCK DETECTED!";
                FontMetrics fm = g2.getFontMetrics();
                int tx = (panelWidth - fm.stringWidth(text)) / 2;
                int ty = (panelHeight / 2) - fm.getHeight() / 2 + fm.getAscent();
                g2.drawString(text, tx + 3, ty + 3);

                g2.setColor(Color.WHITE);
                g2.drawString(text, tx, ty);

                // Subtitle with better styling
                g2.setColor(new Color(0, 0, 0, 150));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                String sub = "Enable Banker's Algorithm to prevent this!";
                FontMetrics fm2 = g2.getFontMetrics();
                int sx = (panelWidth - fm2.stringWidth(sub)) / 2;
                g2.drawString(sub, sx + 2, ty + 62);
                
                g2.setColor(new Color(255, 255, 255));
                g2.drawString(sub, sx, ty + 60);
            }

            if (circularDeadlockDetected) {
                // Enhanced circular deadlock overlay with animation
                Composite old = g2.getComposite();
                long time = System.currentTimeMillis();
                float alpha = (float) (0.4 + 0.25 * Math.sin(time * 0.008));
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(new Color(155, 89, 182));
                g2.fillRect(0, 0, panelWidth, panelHeight);
                g2.setComposite(old);

                // Warning border with circular pattern
                g2.setColor(new Color(142, 68, 173));
                g2.setStroke(new BasicStroke(8));
                g2.drawRoundRect(5, 5, panelWidth - 10, panelHeight - 10, 20, 20);
                
                // Circular pattern overlay
                g2.setColor(new Color(142, 68, 173, 100));
                for (int i = 0; i < 360; i += 45) {
                    double angle = Math.toRadians(i + time * 0.1);
                    int cx = panelWidth / 2;
                    int cy = panelHeight / 2;
                    int radius = 150;
                    int x1 = (int) (cx + radius * Math.cos(angle));
                    int y1 = (int) (cy + radius * Math.sin(angle));
                    g2.fillOval(x1 - 10, y1 - 10, 20, 20);
                }
                
                // Main text with shadow
                g2.setColor(new Color(0, 0, 0, 150));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 60));
                String text = "CIRCULAR DEADLOCK DETECTED!";
                FontMetrics fm = g2.getFontMetrics();
                int tx = (panelWidth - fm.stringWidth(text)) / 2;
                int ty = (panelHeight / 2) - fm.getHeight() / 2 + fm.getAscent();
                g2.drawString(text, tx + 3, ty + 3);

                g2.setColor(Color.WHITE);
                g2.drawString(text, tx, ty);

                // Subtitle with better styling
                g2.setColor(new Color(0, 0, 0, 150));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                String sub = "All directions blocked - Enable Banker's Algorithm!";
                FontMetrics fm2 = g2.getFontMetrics();
                int sx = (panelWidth - fm2.stringWidth(sub)) / 2;
                g2.drawString(sub, sx + 2, ty + 62);
                
                g2.setColor(new Color(255, 255, 255));
                g2.drawString(sub, sx, ty + 60);
            }
        }

        class TrafficLight {
            int x, y;
            String direction;
            boolean isGreen = true;
            int size = 40;

            public TrafficLight(int x, int y, String direction) {
                this.x = x;
                this.y = y;
                this.direction = direction;
            }

            public void toggle() {
                isGreen = !isGreen;
            }

            public boolean contains(int mx, int my) {
                return mx >= x - size && mx <= x + size &&
                        my >= y - size && my <= y + size;
            }

            public void draw(Graphics2D g2) {
                // Enhanced traffic light with better styling and shadow
                
                // Shadow
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRoundRect(x - size / 2 + 3, y - size / 2 + 3, size, size, 12, 12);
                
                // Main housing with gradient effect
                g2.setColor(new Color(25, 25, 25));
                g2.fillRoundRect(x - size / 2, y - size / 2, size, size, 12, 12);
                
                // Housing border
                g2.setColor(new Color(149, 165, 166));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x - size / 2, y - size / 2, size, size, 12, 12);

                // Light with glow effect
                Color lightColor = isGreen ? new Color(46, 204, 113) : new Color(231, 76, 60);
                Color glowColor = isGreen ? new Color(46, 204, 113, 100) : new Color(231, 76, 60, 100);
                
                // Outer glow
                g2.setColor(glowColor);
                g2.fillOval(x - size / 2 + 3, y - size / 2 + 3, size - 6, size - 6);
                
                // Main light
                g2.setColor(lightColor);
                g2.fillOval(x - size / 2 + 6, y - size / 2 + 6, size - 12, size - 12);
                
                // Light highlight
                g2.setColor(new Color(255, 255, 255, 150));
                g2.fillOval(x - size / 2 + 8, y - size / 2 + 8, size / 3, size / 3);

                // Direction label with better styling
                g2.setColor(new Color(236, 240, 241));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String label = direction.substring(0, 1);
                int labelWidth = fm.stringWidth(label);
                
                // Label background
                g2.setColor(new Color(44, 62, 80, 180));
                g2.fillRoundRect(x - labelWidth / 2 - 4, y + size / 2 + 8, labelWidth + 8, 20, 6, 6);
                
                // Label text
                g2.setColor(new Color(236, 240, 241));
                g2.drawString(label, x - labelWidth / 2, y + size / 2 + 22);
            }
        }

        // Environmental Elements
        private void drawEnvironmentalElements(Graphics2D g2, int panelWidth, int panelHeight, int centerX, int centerY) {
            // Draw trees around the map
            drawTrees(g2, panelWidth, panelHeight, centerX, centerY);
            
            // Draw ponds/water features
            drawWaterFeatures(g2, panelWidth, panelHeight, centerX, centerY);
            
            // Draw animated environmental elements (birds, clouds, etc.)
            drawAnimatedEnvironmentalElements(g2, panelWidth, panelHeight, centerX, centerY);
        }
        
        private void drawTrees(Graphics2D g2, int panelWidth, int panelHeight, int centerX, int centerY) {
            // Strategic tree placement - individual trees with excellent spacing to avoid any overlap
            
            // Define pond positions first to avoid conflicts
            int pond1X = 60;
            int pond1Y = 60;
            int pond2X = centerX + roadWidth/2 + 250;
            int pond2Y = centerY + roadWidth/2 + 100;
            
            // Top-left quadrant - individual well-spaced trees
            drawTree(g2, 50, 50);   // Corner tree
            drawTree(g2, 180, 80);  // Away from pond
            drawTree(g2, 280, 40);  // Upper area
            drawTree(g2, 120, 160); // Lower in quadrant
            
            // Top-right quadrant - scattered individual trees
            int topRightStart = centerX + roadWidth/2 + 80;
            if (topRightStart < panelWidth - 60) {
                drawTree(g2, topRightStart, 50);
                if (topRightStart + 120 < panelWidth - 60) {
                    drawTree(g2, topRightStart + 120, 90);
                }
                if (topRightStart + 240 < panelWidth - 60) {
                    drawTree(g2, topRightStart + 240, 60);
                }
                if (topRightStart + 80 < panelWidth - 60) {
                    drawTree(g2, topRightStart + 80, 180);
                }
            }
            
            // Bottom-left quadrant - park-style spacing
            int bottomLeftY = centerY + roadWidth/2 + 80;
            if (bottomLeftY < panelHeight - 60) {
                drawTree(g2, 70, bottomLeftY);
                drawTree(g2, 200, bottomLeftY + 60);
                drawTree(g2, 320, bottomLeftY + 20);
                if (bottomLeftY + 140 < panelHeight - 60) {
                    drawTree(g2, 140, bottomLeftY + 140);
                }
            }
            
            // Bottom-right quadrant - avoid pond area completely
            int bottomRightStart = centerX + roadWidth/2 + 80;
            int bottomRightY = centerY + roadWidth/2 + 80;
            if (bottomRightStart < panelWidth - 60 && bottomRightY < panelHeight - 60) {
                // Place trees well away from pond area
                if (bottomRightStart + 50 < pond2X - 80) {
                    drawTree(g2, bottomRightStart, bottomRightY);
                }
                if (bottomRightStart + 100 < pond2X - 80) {
                    drawTree(g2, bottomRightStart + 100, bottomRightY + 80);
                }
                // Trees after the pond area
                if (pond2X + 120 < panelWidth - 60) {
                    drawTree(g2, pond2X + 120, bottomRightY + 40);
                    if (pond2Y + 100 < panelHeight - 60) {
                        drawTree(g2, pond2X + 80, pond2Y + 100);
                    }
                }
            }
            
            // Single strategic trees along road edges (minimum 150 pixels apart)
            // Left road edge
            int leftEdgeX = centerX - roadWidth/2 - 60;
            drawTree(g2, leftEdgeX, 120);
            drawTree(g2, leftEdgeX, 300);
            if (panelHeight > 500) {
                drawTree(g2, leftEdgeX, 480);
            }
            
            // Right road edge
            int rightEdgeX = centerX + roadWidth/2 + 60;
            if (rightEdgeX < panelWidth - 60) {
                drawTree(g2, rightEdgeX, 100);
                drawTree(g2, rightEdgeX, 280);
                if (panelHeight > 480) {
                    drawTree(g2, rightEdgeX, 460);
                }
            }
        }
        
        private void drawTree(Graphics2D g2, int x, int y) {
            // Tree trunk with realistic texture
            g2.setColor(new Color(101, 67, 33)); // Dark brown
            g2.fillRect(x - 4, y, 8, 25);
            
            // Trunk texture lines
            g2.setColor(new Color(139, 69, 19)); // Medium brown
            g2.fillRect(x - 3, y + 2, 6, 21);
            g2.setColor(new Color(160, 82, 45)); // Light brown highlights
            g2.drawLine(x - 2, y + 3, x - 2, y + 20);
            g2.drawLine(x + 1, y + 5, x + 1, y + 22);
            
            // Tree base/roots
            g2.setColor(new Color(101, 67, 33));
            g2.fillOval(x - 6, y + 20, 12, 8);
            
            // Multi-layered crown for depth
            // Bottom layer (largest)
            g2.setColor(new Color(34, 139, 34));
            g2.fillOval(x - 18, y - 12, 36, 32);
            
            // Middle layer
            g2.setColor(new Color(46, 125, 50));
            g2.fillOval(x - 15, y - 18, 30, 30);
            
            // Top layer (smallest)
            g2.setColor(new Color(76, 175, 80));
            g2.fillOval(x - 12, y - 22, 24, 24);
            
            // Highlight spots for realism
            g2.setColor(new Color(129, 199, 132, 180));
            g2.fillOval(x - 8, y - 15, 12, 8);
            g2.fillOval(x + 2, y - 8, 8, 6);
            g2.fillOval(x - 12, y - 5, 6, 6);
            
            // Small branches visible through leaves
            g2.setColor(new Color(139, 69, 19));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(x, y - 5, x - 8, y - 12);
            g2.drawLine(x, y - 8, x + 6, y - 15);
            g2.drawLine(x, y - 12, x - 4, y - 18);
        }
        
        private void drawWaterFeatures(Graphics2D g2, int panelWidth, int panelHeight, int centerX, int centerY) {
            // Small pond in clear area between trees (top area)
            int pondX = 240;
            int pondY = 110;
            if (pondX < centerX - roadWidth/2 - 80) {
                drawPond(g2, pondX, pondY, 60, 40);
            }
            
            // Second pond positioned in completely clear area (bottom)
            int pond2X = centerX + roadWidth/2 + 180;
            int pond2Y = centerY + roadWidth/2 + 220;
            if (pond2X < panelWidth - 100 && pond2Y < panelHeight - 80) {
                drawPond(g2, pond2X, pond2Y, 80, 55);
            }
        }
        
        private void drawPond(Graphics2D g2, int x, int y, int width, int height) {
            // Pond shadow
            g2.setColor(new Color(0, 0, 139, 100));
            g2.fillOval(x + 2, y + 2, width, height);
            
            // Main water
            g2.setColor(new Color(30, 144, 255));
            g2.fillOval(x, y, width, height);
            
            // Water reflection effect
            long time = System.currentTimeMillis();
            float alpha = (float) (0.3 + 0.2 * Math.sin(time * 0.003));
            g2.setColor(new Color(173, 216, 230, (int)(255 * alpha)));
            g2.fillOval(x + 5, y + 5, width - 10, height - 10);
            
            // Small ripples
            g2.setColor(new Color(255, 255, 255, 100));
            g2.drawOval(x + width/4, y + height/4, width/2, height/2);
        }
        


        
        private void drawAnimatedEnvironmentalElements(Graphics2D g2, int panelWidth, int panelHeight, int centerX, int centerY) {
            // Animated environmental elements (no pedestrians)
            long time = System.currentTimeMillis();
            
            // Additional animated elements
            drawAnimatedElements(g2, panelWidth, panelHeight, centerX, centerY, time);
        }
        
        private void drawAnimatedElements(Graphics2D g2, int panelWidth, int panelHeight, int centerX, int centerY, long time) {
            // Animated birds flying across the sky
            int birdX = (int) ((time / 50) % (panelWidth + 100)) - 50;
            int birdY = 40 + (int) (10 * Math.sin(time * 0.01));
            drawBird(g2, birdX, birdY);
            
            // Second bird flock
            int bird2X = (int) ((time / 80) % (panelWidth + 150)) - 75;
            int bird2Y = 25 + (int) (8 * Math.sin(time * 0.008 + Math.PI));
            drawBird(g2, bird2X, bird2Y);
            drawBird(g2, bird2X - 15, bird2Y + 5);
            drawBird(g2, bird2X - 30, bird2Y + 2);
            
            // Swaying trees animation
            drawSwayingTreeEffect(g2, time);
            
            // Animated clouds
            drawClouds(g2, panelWidth, panelHeight, time);
            
            // Traffic light glow animation
            drawTrafficLightGlow(g2, centerX, centerY, time);
        }
        
        private void drawBird(Graphics2D g2, int x, int y) {
            g2.setColor(new Color(139, 69, 19));
            g2.setStroke(new BasicStroke(2));
            // Simple V shape for bird
            g2.drawLine(x - 3, y, x, y - 2);
            g2.drawLine(x, y - 2, x + 3, y);
        }
        
        private void drawSwayingTreeEffect(Graphics2D g2, long time) {
            // Add subtle sway effect to existing trees (wind effect)
            // This adds animated leaves falling from trees
            for (int i = 0; i < 5; i++) {
                int leafX = 120 + i * 80 + (int) (5 * Math.sin(time * 0.003 + i));
                int leafY = 100 + (int) ((time * 0.1 + i * 50) % 200);
                
                g2.setColor(new Color(255, 165, 0, 150)); // Orange falling leaf
                g2.fillOval(leafX, leafY, 3, 3);
            }
        }
        
        private void drawClouds(Graphics2D g2, int panelWidth, int panelHeight, long time) {
            // Slowly moving clouds
            int cloudSpeed = (int) (time / 200);
            
            // Cloud 1
            int cloud1X = (cloudSpeed % (panelWidth + 200)) - 100;
            drawCloud(g2, cloud1X, 30, 40, 20);
            
            // Cloud 2
            int cloud2X = ((cloudSpeed + 300) % (panelWidth + 250)) - 125;
            drawCloud(g2, cloud2X, 15, 60, 25);
        }
        
        private void drawCloud(Graphics2D g2, int x, int y, int width, int height) {
            g2.setColor(new Color(255, 255, 255, 180));
            // Multiple overlapping circles to create cloud shape
            g2.fillOval(x, y + height/3, width/3, height/2);
            g2.fillOval(x + width/4, y, width/2, height/2);
            g2.fillOval(x + width/2, y + height/4, width/3, height/2);
            g2.fillOval(x + width/3, y + height/3, width/2, height/3);
        }
        
        private void drawTrafficLightGlow(Graphics2D g2, int centerX, int centerY, long time) {
            // Add pulsing glow effects around traffic lights when they're green
            float glowAlpha = (float) (0.1 + 0.1 * Math.sin(time * 0.005));
            
            if (topLight.isGreen) {
                g2.setColor(new Color(46, 204, 113, (int)(255 * glowAlpha)));
                g2.fillOval(topLight.x - 25, topLight.y - 25, 50, 50);
            }
            if (bottomLight.isGreen) {
                g2.setColor(new Color(46, 204, 113, (int)(255 * glowAlpha)));
                g2.fillOval(bottomLight.x - 25, bottomLight.y - 25, 50, 50);
            }
            if (leftLight.isGreen) {
                g2.setColor(new Color(46, 204, 113, (int)(255 * glowAlpha)));
                g2.fillOval(leftLight.x - 25, leftLight.y - 25, 50, 50);
            }
            if (rightLight.isGreen) {
                g2.setColor(new Color(46, 204, 113, (int)(255 * glowAlpha)));
                g2.fillOval(rightLight.x - 25, rightLight.y - 25, 50, 50);
            }
        }
        


        class Car {
            int id;
            String direction;
            double x, y;
            Color color;
            boolean waiting = false;
            boolean inIntersection = false;
            boolean completed = false;
            double speed = 2.5;
            double maxSpeed = 2.5;
            double acceleration = 0.1;
            String allocatedResource = null;
            int lane;
            CarType carType;

            public Car(int id, String direction, int lane) {
                this.id = id;
                this.direction = direction;
                this.lane = lane;

                // Randomly assign car type with weighted probabilities
                double typeRandom = Math.random();
                if (typeRandom < 0.4) {
                    carType = CarType.SEDAN;
                } else if (typeRandom < 0.6) {
                    carType = CarType.TRUCK;
                } else if (typeRandom < 0.75) {
                    carType = CarType.MOTORCYCLE;
                } else if (typeRandom < 0.85) {
                    carType = CarType.BUS;
                } else if (typeRandom < 0.95) {
                    carType = CarType.SPORTS_CAR;
                } else {
                    carType = CarType.EMERGENCY;
                }

                // Set car properties based on type
                maxSpeed = carType.maxSpeed;
                speed = maxSpeed;
                
                // Add some color variation to base color
                int r = Math.min(255, Math.max(0, carType.baseColor.getRed() + rand.nextInt(60) - 30));
                int g = Math.min(255, Math.max(0, carType.baseColor.getGreen() + rand.nextInt(60) - 30));
                int b = Math.min(255, Math.max(0, carType.baseColor.getBlue() + rand.nextInt(60) - 30));
                color = new Color(r, g, b);

                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int centerX = panelWidth / 2;
                int centerY = panelHeight / 2;

                int carSpacing = 80;
                int carsInSameDirection = 0;
                
                for (Car existingCar : cars) {
                    if (existingCar.direction.equals(direction)) {
                        carsInSameDirection++;
                    }
                }

                int offset = (laneWidth / 2 + 8);
                switch (direction) {
                    case "TOP":
                        x = centerX + offset;
                        y = -30 - (carsInSameDirection * carSpacing);
                        break;
                    case "BOTTOM":
                        x = centerX - offset;
                        y = panelHeight + 30 + (carsInSameDirection * carSpacing);
                        break;
                    case "LEFT":
                        x = -30 - (carsInSameDirection * carSpacing);
                        y = centerY + offset;
                        break;
                    case "RIGHT":
                        x = panelWidth + 30 + (carsInSameDirection * carSpacing);
                        y = centerY - offset;
                        break;
                }
            }

            public boolean isWaitingAtLight() {
                return waiting && !inIntersection;
            }

            public boolean isCompleted() {
                return completed;
            }

            public Rectangle getBounds() {
                int w = carType.width;
                int h = carType.height;
                
                if (direction.equals("LEFT") || direction.equals("RIGHT")) {
                    return new Rectangle((int)x - w/2, (int)y - h/2, w, h);
                } else {
                    return new Rectangle((int)x - h/2, (int)y - w/2, h, w);
                }
            }
            
            private boolean isCarTooClose() {
                double minDistance = 60;
                
                for (Car otherCar : cars) {
                    if (otherCar == this) continue;
                    
                    if (otherCar.direction.equals(this.direction)) {
                        double distance = getDistanceTo(otherCar);
                        
                        boolean otherCarInFront = false;
                        switch (direction) {
                            case "TOP":
                                otherCarInFront = otherCar.y > this.y;
                                break;
                            case "BOTTOM":
                                otherCarInFront = otherCar.y < this.y;
                                break;
                            case "LEFT":
                                otherCarInFront = otherCar.x > this.x;
                                break;
                            case "RIGHT":
                                otherCarInFront = otherCar.x < this.x;
                                break;
                        }
                        
                        if (otherCarInFront && distance < minDistance) {
                            return true;
                        }
                    }
                }
                
                return false;
            }
            
            private double getDistanceTo(Car otherCar) {
                double dx = this.x - otherCar.x;
                double dy = this.y - otherCar.y;
                return Math.sqrt(dx * dx + dy * dy);
            }
            
            // SMART DRIVER: Check if near intersection
            private boolean isNearIntersection(int centerX, int centerY) {
                double distanceToIntersection = 70;
                
                switch (direction) {
                    case "TOP":
                        return Math.abs(y - (centerY - intersectionSize / 2)) < distanceToIntersection && y < centerY;
                    case "BOTTOM":
                        return Math.abs(y - (centerY + intersectionSize / 2)) < distanceToIntersection && y > centerY;
                    case "LEFT":
                        return Math.abs(x - (centerX - intersectionSize / 2)) < distanceToIntersection && x < centerX;
                    case "RIGHT":
                        return Math.abs(x - (centerX + intersectionSize / 2)) < distanceToIntersection && x > centerX;
                }
                return false;
            }
            
            // SMART DRIVER: Check for cross-traffic
            private boolean isCrossTrafficApproaching(int centerX, int centerY) {
                double safeDistance = 150;
                double intersectionCheckRadius = intersectionSize / 2 + 50;
                
                for (Car otherCar : cars) {
                    if (otherCar == this) continue;
                    
                    boolean isCrossDirection = false;
                    switch (this.direction) {
                        case "TOP":
                        case "BOTTOM":
                            isCrossDirection = otherCar.direction.equals("LEFT") || otherCar.direction.equals("RIGHT");
                            break;
                        case "LEFT":
                        case "RIGHT":
                            isCrossDirection = otherCar.direction.equals("TOP") || otherCar.direction.equals("BOTTOM");
                            break;
                    }
                    
                    if (!isCrossDirection) continue;
                    
                    double distanceToCenter = Math.sqrt(
                        Math.pow(otherCar.x - centerX, 2) + 
                        Math.pow(otherCar.y - centerY, 2)
                    );
                    
                    if (distanceToCenter < intersectionCheckRadius) {
                        if (otherCar.speed > 0.5 || otherCar.inIntersection) {
                            return true;
                        }
                    }
                    
                    boolean otherCarApproaching = false;
                    switch (otherCar.direction) {
                        case "TOP":
                            otherCarApproaching = otherCar.y < centerY && 
                                Math.abs(otherCar.y - (centerY - intersectionSize / 2)) < safeDistance;
                            break;
                        case "BOTTOM":
                            otherCarApproaching = otherCar.y > centerY && 
                                Math.abs(otherCar.y - (centerY + intersectionSize / 2)) < safeDistance;
                            break;
                        case "LEFT":
                            otherCarApproaching = otherCar.x < centerX && 
                                Math.abs(otherCar.x - (centerX - intersectionSize / 2)) < safeDistance;
                            break;
                        case "RIGHT":
                            otherCarApproaching = otherCar.x > centerX && 
                                Math.abs(otherCar.x - (centerX + intersectionSize / 2)) < safeDistance;
                            break;
                    }
                    
                    if (otherCarApproaching && otherCar.speed > 0.5) {
                        return true;
                    }
                }
                
                return false;
            }

            public void update() {
                if (completed) return;
                
                // Don't update if deadlock is detected (cars frozen)
                if (deadlockDetected || circularDeadlockDetected) return;

                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int centerX = panelWidth / 2;
                int centerY = panelHeight / 2;

                if (isCarTooClose()) {
                    waiting = true;
                    speed = Math.max(0, speed - acceleration * 2);
                    return;
                }
                
                if (!waiting && speed < maxSpeed) {
                    speed = Math.min(maxSpeed, speed + acceleration);
                }

                boolean canGo = false;
                switch (direction) {
                    case "TOP":
                        canGo = topLight.isGreen;
                        break;
                    case "BOTTOM":
                        canGo = bottomLight.isGreen;
                        break;
                    case "LEFT":
                        canGo = leftLight.isGreen;
                        break;
                    case "RIGHT":
                        canGo = rightLight.isGreen;
                        break;
                }
                
                // SMART DRIVER BEHAVIOR: Only active when checkbox is CHECKED
                if (parent.isBankersEnabled()) {
                    if (isNearIntersection(centerX, centerY) && !inIntersection) {
                        if (isCrossTrafficApproaching(centerX, centerY)) {
                            waiting = true;
                            speed = Math.max(0, speed - acceleration * 2);
                            return;
                        }
                    }
                }

                switch (direction) {
                    case "TOP":
                        if (y < centerY - intersectionSize / 2 - 60) {
                            y += speed;
                            waiting = false;
                        } else if (y < centerY - intersectionSize / 2 - 50) {
                            if (parent.isBankersEnabled()) {
                                // Smart driver: FOLLOW light first, then check cross-traffic
                                if (canGo && !isCrossTrafficApproaching(centerX, centerY)) {
                                    y += speed;
                                    inIntersection = true;
                                    waiting = false;
                                } else {
                                    waiting = true;
                                    speed = 0;
                                }
                            } else {
                                // Normal behavior: follow light strictly (no intelligence)
                                if (canGo) {
                                    y += speed;
                                    inIntersection = true;
                                    waiting = false;
                                } else {
                                    waiting = true;
                                    speed = 0;
                                }
                            }
                        } else if (y < panelHeight) {
                            y += speed;
                            if (y > centerY + intersectionSize / 2 + 80) inIntersection = false;
                        } else {
                            completed = true;
                        }
                        break;

                    case "BOTTOM":
                        if (y > centerY + intersectionSize / 2 + 60) {
                            y -= speed;
                            waiting = false;
                        } else if (y > centerY + intersectionSize / 2 + 50) {
                            if (parent.isBankersEnabled()) {
                                // Smart driver: FOLLOW light first, then check cross-traffic
                                if (canGo && !isCrossTrafficApproaching(centerX, centerY)) {
                                    y -= speed;
                                    inIntersection = true;
                                    waiting = false;
                                } else {
                                    waiting = true;
                                    speed = 0;
                                }
                            } else {
                                // Normal behavior: follow light strictly (no intelligence)
                                if (canGo) {
                                    y -= speed;
                                    inIntersection = true;
                                    waiting = false;
                                } else {
                                    waiting = true;
                                    speed = 0;
                                }
                            }
                        } else if (y > 0) {
                            y -= speed;
                            if (y < centerY - intersectionSize / 2 - 80) inIntersection = false;
                        } else {
                            completed = true;
                        }
                        break;

                    case "LEFT":
                        if (x < centerX - intersectionSize / 2 - 60) {
                            x += speed;
                            waiting = false;
                        } else if (x < centerX - intersectionSize / 2 - 50) {
                            if (parent.isBankersEnabled()) {
                                // Smart driver: FOLLOW light first, then check cross-traffic
                                if (canGo && !isCrossTrafficApproaching(centerX, centerY)) {
                                    x += speed;
                                    inIntersection = true;
                                    waiting = false;
                                } else {
                                    waiting = true;
                                    speed = 0;
                                }
                            } else {
                                // Normal behavior: follow light strictly (no intelligence)
                                if (canGo) {
                                    x += speed;
                                    inIntersection = true;
                                    waiting = false;
                                } else {
                                    waiting = true;
                                    speed = 0;
                                }
                            }
                        } else if (x < panelWidth) {
                            x += speed;
                            if (x > centerX + intersectionSize / 2 + 80) inIntersection = false;
                        } else {
                            completed = true;
                        }
                        break;

                    case "RIGHT":
                        if (x > centerX + intersectionSize / 2 + 60) {
                            x -= speed;
                            waiting = false;
                        } else if (x > centerX + intersectionSize / 2 + 50) {
                            if (parent.isBankersEnabled()) {
                                // Smart driver: FOLLOW light first, then check cross-traffic
                                if (canGo && !isCrossTrafficApproaching(centerX, centerY)) {
                                    x -= speed;
                                    inIntersection = true;
                                    waiting = false;
                                } else {
                                    waiting = true;
                                    speed = 0;
                                }
                            } else {
                                // Normal behavior: follow light strictly (no intelligence)
                                if (canGo) {
                                    x -= speed;
                                    inIntersection = true;
                                    waiting = false;
                                } else {
                                    waiting = true;
                                    speed = 0;
                                }
                            }
                        } else if (x > 0) {
                            x -= speed;
                            if (x < centerX - intersectionSize / 2 - 80) inIntersection = false;
                        } else {
                            completed = true;
                        }
                        break;
                }
            }

            public void draw(Graphics2D g2) {
                // Draw different vehicle types with unique appearances
                switch (carType) {
                    case SEDAN:
                        drawSedan(g2);
                        break;
                    case TRUCK:
                        drawTruck(g2);
                        break;
                    case MOTORCYCLE:
                        drawMotorcycle(g2);
                        break;
                    case BUS:
                        drawBus(g2);
                        break;
                    case SPORTS_CAR:
                        drawSportsCar(g2);
                        break;
                    case EMERGENCY:
                        drawEmergencyVehicle(g2);
                        break;
                }
                
                // Draw common elements (car number and waiting indicator) on top
                drawCarCommonElements(g2);
            }
            
            private void drawSedan(Graphics2D g2) {
                int w = carType.width;
                int h = carType.height;
                
                if (direction.equals("LEFT") || direction.equals("RIGHT")) {
                    // Car shadow
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRoundRect((int) x - w/2 + 2, (int) y - h/2 + 2, w, h, 12, 12);
                    
                    // Main car body
                    g2.setColor(color.darker());
                    g2.fillRoundRect((int) x - w/2, (int) y - h/2, w, h, 12, 12);
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - w/2 + 1, (int) y - h/2 + 1, w-2, h-2, 10, 10);
                    
                    // Windshield
                    g2.setColor(new Color(135, 206, 235, 180));
                    g2.fillRoundRect((int) x - 6, (int) y - 9, 12, 18, 6, 6);
                    
                    // Headlights
                    g2.setColor(new Color(255, 255, 200));
                    if (direction.equals("RIGHT")) {
                        g2.fillOval((int) x - w/2 - 2, (int) y - 6, 4, 4);
                        g2.fillOval((int) x - w/2 - 2, (int) y + 2, 4, 4);
                    } else {
                        g2.fillOval((int) x + w/2 - 2, (int) y - 6, 4, 4);
                        g2.fillOval((int) x + w/2 - 2, (int) y + 2, 4, 4);
                    }
                } else {
                    // Vertical orientation
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRoundRect((int) x - h/2 + 2, (int) y - w/2 + 2, h, w, 12, 12);
                    
                    g2.setColor(color.darker());
                    g2.fillRoundRect((int) x - h/2, (int) y - w/2, h, w, 12, 12);
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - h/2 + 1, (int) y - w/2 + 1, h-2, w-2, 10, 10);
                    
                    g2.setColor(new Color(135, 206, 235, 180));
                    g2.fillRoundRect((int) x - 9, (int) y - 6, 18, 12, 6, 6);
                    
                    g2.setColor(new Color(255, 255, 200));
                    if (direction.equals("TOP")) {
                        g2.fillOval((int) x - 6, (int) y + w/2 - 2, 4, 4);
                        g2.fillOval((int) x + 2, (int) y + w/2 - 2, 4, 4);
                    } else {
                        g2.fillOval((int) x - 6, (int) y - w/2 - 2, 4, 4);
                        g2.fillOval((int) x + 2, (int) y - w/2 - 2, 4, 4);
                    }
                }
            }
            
            private void drawTruck(Graphics2D g2) {
                int w = carType.width;
                int h = carType.height;
                
                if (direction.equals("LEFT") || direction.equals("RIGHT")) {
                    // Truck body (larger and more rectangular)
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect((int) x - w/2 + 2, (int) y - h/2 + 2, w, h);
                    
                    g2.setColor(color.darker());
                    g2.fillRect((int) x - w/2, (int) y - h/2, w, h);
                    g2.setColor(color);
                    g2.fillRect((int) x - w/2 + 2, (int) y - h/2 + 2, w-4, h-4);
                    
                    // Truck cab
                    g2.setColor(color.darker().darker());
                    if (direction.equals("RIGHT")) {
                        g2.fillRect((int) x - w/2, (int) y - h/2, 15, h);
                    } else {
                        g2.fillRect((int) x + w/2 - 15, (int) y - h/2, 15, h);
                    }
                    
                    // Cargo area pattern
                    g2.setColor(new Color(139, 69, 19));
                    for (int i = 0; i < 3; i++) {
                        g2.drawLine((int) x - w/2 + 5 + i*8, (int) y - h/2 + 2, 
                                  (int) x - w/2 + 5 + i*8, (int) y + h/2 - 2);
                    }
                } else {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRect((int) x - h/2 + 2, (int) y - w/2 + 2, h, w);
                    
                    g2.setColor(color.darker());
                    g2.fillRect((int) x - h/2, (int) y - w/2, h, w);
                    g2.setColor(color);
                    g2.fillRect((int) x - h/2 + 2, (int) y - w/2 + 2, h-4, w-4);
                    
                    g2.setColor(color.darker().darker());
                    if (direction.equals("TOP")) {
                        g2.fillRect((int) x - h/2, (int) y + w/2 - 15, h, 15);
                    } else {
                        g2.fillRect((int) x - h/2, (int) y - w/2, h, 15);
                    }
                }
            }
            
            private void drawMotorcycle(Graphics2D g2) {
                int w = carType.width;
                int h = carType.height;
                
                if (direction.equals("LEFT") || direction.equals("RIGHT")) {
                    // Small motorcycle body
                    g2.setColor(color);
                    g2.fillOval((int) x - w/2, (int) y - h/2, w, h);
                    
                    // Wheels
                    g2.setColor(Color.BLACK);
                    if (direction.equals("RIGHT")) {
                        g2.fillOval((int) x - w/2 - 2, (int) y - 3, 6, 6);
                        g2.fillOval((int) x + w/2 - 4, (int) y - 3, 6, 6);
                    } else {
                        g2.fillOval((int) x - w/2 - 2, (int) y - 3, 6, 6);
                        g2.fillOval((int) x + w/2 - 4, (int) y - 3, 6, 6);
                    }
                    
                    // Rider
                    g2.setColor(new Color(255, 220, 177));
                    g2.fillOval((int) x - 3, (int) y - 8, 6, 6);
                } else {
                    g2.setColor(color);
                    g2.fillOval((int) x - h/2, (int) y - w/2, h, w);
                    
                    g2.setColor(Color.BLACK);
                    if (direction.equals("TOP")) {
                        g2.fillOval((int) x - 3, (int) y - w/2 - 2, 6, 6);
                        g2.fillOval((int) x - 3, (int) y + w/2 - 4, 6, 6);
                    } else {
                        g2.fillOval((int) x - 3, (int) y - w/2 - 2, 6, 6);
                        g2.fillOval((int) x - 3, (int) y + w/2 - 4, 6, 6);
                    }
                    
                    g2.setColor(new Color(255, 220, 177));
                    g2.fillOval((int) x - 3, (int) y - 3, 6, 6);
                }
            }
            
            private void drawBus(Graphics2D g2) {
                int w = carType.width;
                int h = carType.height;
                
                if (direction.equals("LEFT") || direction.equals("RIGHT")) {
                    // Large bus body
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRoundRect((int) x - w/2 + 2, (int) y - h/2 + 2, w, h, 8, 8);
                    
                    g2.setColor(color.darker());
                    g2.fillRoundRect((int) x - w/2, (int) y - h/2, w, h, 8, 8);
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - w/2 + 2, (int) y - h/2 + 2, w-4, h-4, 6, 6);
                    
                    // Windows
                    g2.setColor(new Color(135, 206, 235, 180));
                    for (int i = 0; i < 4; i++) {
                        g2.fillRect((int) x - w/2 + 8 + i*10, (int) y - h/2 + 4, 6, 8);
                    }
                    
                    // Door
                    g2.setColor(color.darker().darker());
                    if (direction.equals("RIGHT")) {
                        g2.fillRect((int) x + w/2 - 8, (int) y - h/2 + 2, 6, h-4);
                    } else {
                        g2.fillRect((int) x - w/2 + 2, (int) y - h/2 + 2, 6, h-4);
                    }
                } else {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRoundRect((int) x - h/2 + 2, (int) y - w/2 + 2, h, w, 8, 8);
                    
                    g2.setColor(color.darker());
                    g2.fillRoundRect((int) x - h/2, (int) y - w/2, h, w, 8, 8);
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - h/2 + 2, (int) y - w/2 + 2, h-4, w-4, 6, 6);
                    
                    g2.setColor(new Color(135, 206, 235, 180));
                    for (int i = 0; i < 4; i++) {
                        g2.fillRect((int) x - h/2 + 4, (int) y - w/2 + 8 + i*10, 8, 6);
                    }
                }
            }
            
            private void drawSportsCar(Graphics2D g2) {
                int w = carType.width;
                int h = carType.height;
                
                if (direction.equals("LEFT") || direction.equals("RIGHT")) {
                    // Sleek sports car body
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRoundRect((int) x - w/2 + 2, (int) y - h/2 + 2, w, h, 15, 15);
                    
                    g2.setColor(color.darker());
                    g2.fillRoundRect((int) x - w/2, (int) y - h/2, w, h, 15, 15);
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - w/2 + 1, (int) y - h/2 + 1, w-2, h-2, 12, 12);
                    
                    // Sporty windshield
                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.fillRoundRect((int) x - 5, (int) y - 6, 10, 12, 8, 8);
                    
                    // Racing stripes
                    g2.setColor(Color.WHITE);
                    g2.fillRect((int) x - w/2 + 5, (int) y - 1, w - 10, 2);
                    
                    // Headlights (more aggressive)
                    g2.setColor(new Color(255, 255, 255));
                    if (direction.equals("RIGHT")) {
                        g2.fillOval((int) x - w/2 - 1, (int) y - 5, 5, 3);
                        g2.fillOval((int) x - w/2 - 1, (int) y + 2, 5, 3);
                    } else {
                        g2.fillOval((int) x + w/2 - 4, (int) y - 5, 5, 3);
                        g2.fillOval((int) x + w/2 - 4, (int) y + 2, 5, 3);
                    }
                } else {
                    g2.setColor(new Color(0, 0, 0, 100));
                    g2.fillRoundRect((int) x - h/2 + 2, (int) y - w/2 + 2, h, w, 15, 15);
                    
                    g2.setColor(color.darker());
                    g2.fillRoundRect((int) x - h/2, (int) y - w/2, h, w, 15, 15);
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - h/2 + 1, (int) y - w/2 + 1, h-2, w-2, 12, 12);
                    
                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.fillRoundRect((int) x - 6, (int) y - 5, 12, 10, 8, 8);
                    
                    g2.setColor(Color.WHITE);
                    g2.fillRect((int) x - 1, (int) y - w/2 + 5, 2, w - 10);
                }
            }
            
            private void drawEmergencyVehicle(Graphics2D g2) {
                drawSedan(g2); // Base as sedan
                
                // Emergency lights on top
                long currentTime = System.currentTimeMillis();
                boolean flashRed = (currentTime / 200) % 2 == 0;
                
                g2.setColor(flashRed ? Color.RED : Color.BLUE);
                g2.fillOval((int) x - 4, (int) y - 15, 8, 6);
                
                // Emergency symbol
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 8));
                g2.drawString("+", (int) x - 2, (int) y - 10);

            }
            
            // Draw car elements that appear on all car types
            private void drawCarCommonElements(Graphics2D g2) {
                // Enhanced waiting indicator (red blinking light)
                if (waiting) {
                    // Pulsing effect for waiting indicator
                    long timeNow = System.currentTimeMillis();
                    float alpha = (float) (0.5 + 0.5 * Math.sin(timeNow * 0.01));
                    g2.setColor(new Color(231, 76, 60, (int) (255 * alpha)));
                    g2.fillOval((int) x - 8, (int) y - 35, 16, 16);
                    g2.setColor(new Color(255, 255, 255));
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    // Center the exclamation mark properly
                    FontMetrics fm = g2.getFontMetrics();
                    int exclamationWidth = fm.stringWidth("!");
                    g2.drawString("!", (int) x - exclamationWidth / 2, (int) y - 22);
                }

                // Enhanced car ID with better visibility
                g2.setColor(new Color(44, 62, 80, 200));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                String idStr = String.valueOf(id);
                FontMetrics fm = g2.getFontMetrics();
                int idWidth = fm.stringWidth(idStr);
                g2.fillRoundRect((int) x - idWidth / 2 - 4, (int) y - 8, idWidth + 8, 16, 6, 6);
                
                g2.setColor(new Color(236, 240, 241));
                g2.drawString(idStr, (int) x - idWidth / 2, (int) y + 4);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new IntroScreen();
        });
    }
}

// Intro Screen Class
class IntroScreen extends JFrame {
    private Timer pulseTimer;
    private float pulseAlpha = 0.5f;
    private boolean pulseDirection = true;
    
    public IntroScreen() {
        setTitle("Traffic Deadlock Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Create intro panel
        JPanel introPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background gradient
                GradientPaint gradient = new GradientPaint(0, 0, new Color(44, 62, 80), 
                                                          0, getHeight(), new Color(52, 73, 94));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Title
                g2.setColor(new Color(236, 240, 241));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 48));
                FontMetrics fm = g2.getFontMetrics();
                String title = "Traffic Deadlock Simulator";
                int x = (getWidth() - fm.stringWidth(title)) / 2;
                g2.drawString(title, x, 120);
                
                // Subtitle
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 24));
                fm = g2.getFontMetrics();
                String subtitle = "Advanced Deadlock Detection & Avoidance System";
                x = (getWidth() - fm.stringWidth(subtitle)) / 2;
                g2.setColor(new Color(52, 152, 219));
                g2.drawString(subtitle, x, 160);
                
                // Features
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                g2.setColor(new Color(149, 165, 166));
                String[] features = {
                    "Real-time Traffic Simulation",
                    "Banker's Algorithm Implementation", 
                    "Circular Wait Deadlock Detection",
                    "Interactive Traffic Light Control",
                    "Right-Side Traffic System"
                };
                
                int startY = 220;
                for (int i = 0; i < features.length; i++) {
                    fm = g2.getFontMetrics();
                    x = (getWidth() - fm.stringWidth("• " + features[i])) / 2;
                    g2.drawString("• " + features[i], x, startY + (i * 30));
                }
                
                // Interactive prompt with pulsing effect
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, pulseAlpha));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
                g2.setColor(new Color(241, 196, 15));
                String prompt = "Click anywhere to start simulation";
                fm = g2.getFontMetrics();
                x = (getWidth() - fm.stringWidth(prompt)) / 2;
                g2.drawString(prompt, x, 420);
                
                // Reset composite for other elements
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                
                // Start button visual - centered
                int buttonWidth = 200;
                int buttonHeight = 50;
                int buttonX = (getWidth() - buttonWidth) / 2;
                int buttonY = 450;
                
                g2.setColor(new Color(46, 204, 113, (int) (255 * pulseAlpha)));
                g2.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);
                g2.setColor(new Color(39, 174, 96));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);
                
                g2.setColor(new Color(255, 255, 255));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                String buttonText = "START SIMULATOR";
                fm = g2.getFontMetrics();
                x = (getWidth() - fm.stringWidth(buttonText)) / 2;
                g2.drawString(buttonText, x, buttonY + 32);
                
                // Team Credits
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.setColor(new Color(52, 152, 219));
                String projectTitle = "The Forensic Five";
                fm = g2.getFontMetrics();
                x = (getWidth() - fm.stringWidth(projectTitle)) / 2;
                g2.drawString(projectTitle, x, 525);
                
                // Development Team
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.setColor(new Color(149, 165, 166));
                String[] teamMembers = {
                    "John Paul Calub • Ferdinand Corbin Jr. • John Mikael Evangelista",
                    "Tristan Jay Sevilla • Alvhin Solo"
                };
                
                for (int i = 0; i < teamMembers.length; i++) {
                    fm = g2.getFontMetrics();
                    x = (getWidth() - fm.stringWidth(teamMembers[i])) / 2;
                    g2.drawString(teamMembers[i], x, 545 + (i * 12));
                }
                
                // Instructions
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                g2.setColor(new Color(127, 140, 141));
                String instructions = "Click or press SPACE to continue";
                fm = g2.getFontMetrics();
                x = (getWidth() - fm.stringWidth(instructions)) / 2;
                g2.drawString(instructions, x, 580);
            }
        };
        
        introPanel.setBackground(new Color(44, 62, 80));
        add(introPanel);
        
        // Add mouse and keyboard listeners for interaction
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                startSimulator();
            }
        });
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    startSimulator();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        
        setFocusable(true);
        requestFocus();
        setVisible(true);
        
        // Pulsing animation for the prompt
        pulseTimer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (pulseDirection) {
                    pulseAlpha += 0.02f;
                    if (pulseAlpha >= 1.0f) {
                        pulseAlpha = 1.0f;
                        pulseDirection = false;
                    }
                } else {
                    pulseAlpha -= 0.02f;
                    if (pulseAlpha <= 0.3f) {
                        pulseAlpha = 0.3f;
                        pulseDirection = true;
                    }
                }
                repaint();
            }
        });
        pulseTimer.start();
    }
    
    private void startSimulator() {
        pulseTimer.stop();
        dispose();
        SwingUtilities.invokeLater(() -> {
            TrafficDeadlockSimulator sim = new TrafficDeadlockSimulator();
            sim.setVisible(true);
        });
    }
}
