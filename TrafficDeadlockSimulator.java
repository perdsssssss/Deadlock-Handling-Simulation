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
        setTitle("Traffic Deadlock Simulator - Right Side Traffic");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        setResizable(false);
        setLocationRelativeTo(null);

        trafficPanel = new TrafficPanel(this);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(250, 0));
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Control Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(titleLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        bankersCheckBox = new JCheckBox("Banker's Algorithm (Deadlock Avoidance)");
        bankersCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankersCheckBox.setFont(new Font("Arial", Font.BOLD, 11));
        bankersCheckBox.setSelected(true);
        controlPanel.add(bankersCheckBox);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel addLabel = new JLabel("Add Car:");
        addLabel.setFont(new Font("Arial", Font.BOLD, 14));
        addLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(addLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        addTopBtn = createButton("🚗 From TOP", new Color(255, 105, 180));
        addBottomBtn = createButton("🚗 From BOTTOM", new Color(255, 105, 180));
        addLeftBtn = createButton("🚗 From LEFT", new Color(255, 105, 180));
        addRightBtn = createButton("🚗 From RIGHT", new Color(255, 105, 180));

        controlPanel.add(addTopBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(addBottomBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(addLeftBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(addRightBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        toggleAllBtn = createButton("🚦 Toggle All Lights", new Color(255, 140, 0));
        bankersBtn = createButton("🛡️ Check Safe State", new Color(34, 139, 34));
        resetBtn = createButton("🔄 Reset", Color.GRAY);

        controlPanel.add(toggleAllBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(bankersBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(resetBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(statusLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(statusArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        log("🚦 Traffic Simulator Ready!");
        log("━━━━━━━━━━━━━━━━━━━━━━━━");
        log("FEATURES:");
        log("• RIGHT SIDE TRAFFIC (all cars)");
        log("• Smart Drivers with collision avoidance");
        log("• Click lights to control individually");
        log("• Toggle All Lights button");
        log("");
        log("💡 USAGE:");
        log("1. SMART DRIVERS ON (checkbox checked):");
        log("   - Cars intelligently avoid collisions");
        log("   - Drivers check for cross-traffic");
        log("   - Safe even with all RED lights");
        log("");
        log("2. SMART DRIVERS OFF (checkbox unchecked):");
        log("   - Cars follow lights strictly");
        log("   - Can cause deadlock/collision");
        log("   - Turn all lights RED to test!");
        log("");
        log("3. Try different scenarios:");
        log("   - Add cars from all directions");
        log("   - Toggle smart drivers ON/OFF");
        log("   - Control lights manually\n");
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(230, 35));
        return btn;
    }

    private void reset() {
        trafficPanel.reset();
        statusArea.setText("");
        log("🔄 System Reset Complete!\n");
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
        private Random rand = new Random();
        
        private Map<String, Long> lastCarAddedTimeByDirection = new HashMap<>();
        private static final long CAR_SPAWN_COOLDOWN_MS = 1000;

        public TrafficPanel(TrafficDeadlockSimulator parent) {
            this.parent = parent;
            setBackground(new Color(34, 139, 34));
            
            setPreferredSize(new Dimension(900, 700));
            setMinimumSize(new Dimension(900, 700));

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
                        parent.log("🚦 TOP Light: " + (topLight.isGreen ? "GREEN ✅" : "RED 🛑"));
                    } else if (bottomLight.contains(x, y)) {
                        bottomLight.toggle();
                        parent.log("🚦 BOTTOM Light: " + (bottomLight.isGreen ? "GREEN ✅" : "RED 🛑"));
                    } else if (leftLight.contains(x, y)) {
                        leftLight.toggle();
                        parent.log("🚦 LEFT Light: " + (leftLight.isGreen ? "GREEN ✅" : "RED 🛑"));
                    } else if (rightLight.contains(x, y)) {
                        rightLight.toggle();
                        parent.log("🚦 RIGHT Light: " + (rightLight.isGreen ? "GREEN ✅" : "RED 🛑"));
                    }

                    if (topLight.isGreen || bottomLight.isGreen || leftLight.isGreen || rightLight.isGreen) {
                        deadlockDetected = false;
                        deadlockShownDialog = false;
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

            parent.log("🚦 ALL LIGHTS: " + (newState ? "GREEN ✅" : "RED 🛑"));

            if (newState) {
                deadlockDetected = false;
                deadlockShownDialog = false;
            }
            repaint();
        }

        public void addCar(String direction) {
            long currentTime = System.currentTimeMillis();
            long lastAddedTime = lastCarAddedTimeByDirection.getOrDefault(direction, 0L);
            
            if (currentTime - lastAddedTime < CAR_SPAWN_COOLDOWN_MS) {
                long remainingTime = (CAR_SPAWN_COOLDOWN_MS - (currentTime - lastAddedTime)) / 1000;
                parent.log("⏳ Please wait " + (remainingTime + 1) + " second(s) before adding another car from " + direction);
                return;
            }

            cars.add(new Car(carIdCounter++, direction, 1));
            parent.log("🚗 Car #" + (carIdCounter - 1) + " added from " + direction + " (RIGHT lane)");
            
            lastCarAddedTimeByDirection.put(direction, currentTime);
        }

        public String checkBankersSafeState() {
            StringBuilder sb = new StringBuilder();
            sb.append("🧠 SMART DRIVER STATUS\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");

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
                String lightStatus = lights[i] ? "🟢 GREEN" : "🔴 RED";
                sb.append(dirNames[i] + ": " + allocation[i] + " cars (" + waiting[i] + " waiting) - " + lightStatus + "\n");
            }
            sb.append("\n");

            if (parent.isBankersEnabled()) {
                sb.append("✅ BANKER'S ALGORITHM: ACTIVE\n");
                sb.append("Cars are intelligently avoiding collisions.\n");
                sb.append("Drivers check for cross-traffic before entering.\n");
                sb.append("Safe operation even with manual light control.\n");
            } else {
                sb.append("⚠️ BANKER'S ALGORITHM: DISABLED\n");
                sb.append("Cars follow traffic lights strictly.\n");
                sb.append("Risk of deadlock/collision when all lights are RED.\n");
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
                                parent.log("🚨 DEADLOCK: Car#" + a.id + " & Car#" + b.id + " collided!");
                                if (!deadlockShownDialog) {
                                    JOptionPane.showMessageDialog(parent,
                                            "🚨 DEADLOCK DETECTED!\n\n" +
                                                    "Car#" + a.id + " and Car#" + b.id + " collided!\n" +
                                                    "This happened because Banker's Algorithm is disabled.\n\n" +
                                                    "Enable Banker's Algorithm to prevent DEADLOCK!",
                                            "DEADLOCK",
                                            JOptionPane.ERROR_MESSAGE);
                                    deadlockShownDialog = true;
                                }
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
                    deadlockDetected = true;
                    if (!deadlockShownDialog) {
                        JOptionPane.showMessageDialog(parent,
                                "🚨 DEADLOCK DETECTED!\n\n" +
                                        "All directions waiting and all lights RED.\n" +
                                        "This is a classic circular wait deadlock.\n\n" +
                                        "Enable Banker's Algorithm to prevent this!",
                                "Deadlock Detected",
                                JOptionPane.ERROR_MESSAGE);
                        deadlockShownDialog = true;
                    }
                }
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

            g2.setColor(new Color(60, 60, 60));
            g2.fillRect(centerX - roadWidth / 2, 0, roadWidth, panelHeight);
            g2.fillRect(0, centerY - roadWidth / 2, panelWidth, roadWidth);

            g2.setColor(new Color(70, 70, 70));
            g2.fillRect(centerX - intersectionSize / 2, centerY - intersectionSize / 2,
                    intersectionSize, intersectionSize);

            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{20, 20}, 0));

            g2.drawLine(centerX, 0, centerX, centerY - intersectionSize / 2 - 5);
            g2.drawLine(centerX, centerY + intersectionSize / 2 + 5, centerX, panelHeight);
            g2.drawLine(0, centerY, centerX - intersectionSize / 2 - 5, centerY);
            g2.drawLine(centerX + intersectionSize / 2 + 5, centerY, panelWidth, centerY);

            topLight.draw(g2);
            bottomLight.draw(g2);
            leftLight.draw(g2);
            rightLight.draw(g2);

            for (Car car : cars) {
                car.draw(g2);
            }

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Cars: " + cars.size(), 10, 20);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Banker's Algorithm: " + (parent.isBankersEnabled() ? "ON 🧠" : "OFF"), 10, 40);
            g2.drawString(parent.isBankersEnabled() ? "Mode: Deadlock Avoidance ✅" : "Mode: Strict Light Following ⚠️", 10, 60);
            g2.drawString("Right Side Traffic", 10, 80);

            if (deadlockDetected) {
                Composite old = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
                g2.setColor(Color.RED);
                g2.fillRect(0, 0, panelWidth, panelHeight);
                g2.setComposite(old);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 72));
                String text = "DEADLOCK!";
                FontMetrics fm = g2.getFontMetrics();
                int tx = (panelWidth - fm.stringWidth(text)) / 2;
                int ty = (panelHeight / 2) - fm.getHeight() / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);

                g2.setFont(new Font("Arial", Font.BOLD, 20));
                String sub = "Enable Banker's Algorithm to prevent this";
                FontMetrics fm2 = g2.getFontMetrics();
                int sx = (panelWidth - fm2.stringWidth(sub)) / 2;
                g2.drawString(sub, sx, ty + 50);
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
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(x - size / 2, y - size / 2, size, size, 8, 8);

                g2.setColor(isGreen ? new Color(0, 255, 0) : new Color(255, 0, 0));
                g2.fillOval(x - size / 2 + 6, y - size / 2 + 6, size - 12, size - 12);

                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x - size / 2, y - size / 2, size, size, 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                String label = direction.substring(0, 1);
                g2.drawString(label, x - fm.stringWidth(label) / 2, y + size / 2 + 18);
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

            public Car(int id, String direction, int lane) {
                this.id = id;
                this.direction = direction;
                this.lane = lane;

                color = new Color(
                        100 + rand.nextInt(155),
                        100 + rand.nextInt(155),
                        100 + rand.nextInt(155)
                );

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
                if (direction.equals("LEFT") || direction.equals("RIGHT")) {
                    return new Rectangle((int)x - 20, (int)y - 12, 40, 24);
                } else {
                    return new Rectangle((int)x - 12, (int)y - 20, 24, 40);
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
                if (direction.equals("LEFT") || direction.equals("RIGHT")) {
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - 20, (int) y - 12, 40, 24, 8, 8);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect((int) x - 20, (int) y - 12, 40, 24, 8, 8);

                    g2.setColor(new Color(100, 150, 200, 150));
                    g2.fillRect((int) x - 5, (int) y - 8, 10, 16);
                } else {
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - 12, (int) y - 20, 24, 40, 8, 8);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect((int) x - 12, (int) y - 20, 24, 40, 8, 8);

                    g2.setColor(new Color(100, 150, 200, 150));
                    g2.fillRect((int) x - 8, (int) y - 5, 16, 10);
                }

                if (waiting) {
                    g2.setColor(Color.RED);
                    g2.fillOval((int) x - 6, (int) y - 30, 12, 12);
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.BOLD, 10));
                    g2.drawString("!", (int) x - 3, (int) y - 21);
                }

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 9));
                String idStr = String.valueOf(id);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(idStr, (int) x - fm.stringWidth(idStr) / 2, (int) y + 4);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TrafficDeadlockSimulator sim = new TrafficDeadlockSimulator();
            sim.setVisible(true);
        });
    }
}
