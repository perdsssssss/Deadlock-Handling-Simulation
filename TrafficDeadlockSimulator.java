import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class TrafficDeadlockSimulator extends JFrame {
    private TrafficPanel trafficPanel;
    private JButton addTopBtn, addBottomBtn, addLeftBtn, addRightBtn;
    private JButton resetBtn, detectBtn, bankersBtn, toggleAllBtn;
    private JTextArea statusArea;
    private JCheckBox bankersCheckBox;

    public TrafficDeadlockSimulator() {
        setTitle("Traffic Deadlock Simulator - 2-Way Roads");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        trafficPanel = new TrafficPanel(this);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setPreferredSize(new Dimension(250, 0));
        controlPanel.setBackground(new Color(240, 240, 240));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Control Panel");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        controlPanel.add(titleLabel);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlPanel.add(new JSeparator());
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Banker's Algorithm Toggle
        bankersCheckBox = new JCheckBox("Banker's Algorithm (Deadlock Avoidance)");
        bankersCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        bankersCheckBox.setFont(new Font("Arial", Font.BOLD, 11));
        controlPanel.add(bankersCheckBox);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Add Car Buttons
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

        // Action Buttons
        toggleAllBtn = createButton("🚦 Toggle All Lights", new Color(255, 140, 0));
        detectBtn = createButton("🔍 Detect Deadlock (RAG)", new Color(220, 20, 60));
        bankersBtn = createButton("🛡️ Check Safe State", new Color(34, 139, 34));
        resetBtn = createButton("🔄 Reset", Color.GRAY);

        controlPanel.add(toggleAllBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(detectBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(bankersBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        controlPanel.add(resetBtn);
        controlPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Status Area
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

        // Button Actions
        addTopBtn.addActionListener(e -> trafficPanel.addCar("TOP"));
        addBottomBtn.addActionListener(e -> trafficPanel.addCar("BOTTOM"));
        addLeftBtn.addActionListener(e -> trafficPanel.addCar("LEFT"));
        addRightBtn.addActionListener(e -> trafficPanel.addCar("RIGHT"));
        resetBtn.addActionListener(e -> reset());
        detectBtn.addActionListener(e -> detectDeadlockRAG());
        bankersBtn.addActionListener(e -> checkBankersAlgorithm());
        toggleAllBtn.addActionListener(e -> trafficPanel.toggleAllLights());

        add(trafficPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        log("🚦 Traffic Simulator Ready!");
        log("━━━━━━━━━━━━━━━━━━━━━━━━");
        log("FEATURES:");
        log("• 2-way roads (vertical & horizontal)");
        log("• Banker's Algorithm (Avoidance)");
        log("• RAG Detection");
        log("• Click lights to control individually");
        log("• Toggle All Lights button\n");
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

    private void detectDeadlockRAG() {
        String result = trafficPanel.detectDeadlockRAG();
        log("\n" + result);
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

        private int centerX = 450;
        private int centerY = 350;
        private int roadWidth = 140; // Wider for clearer two lanes
        private int laneWidth = 40;
        private int intersectionSize = 120;

        private boolean deadlockDetected = false;
        private boolean deadlockShownDialog = false;
        private Random rand = new Random();

        public TrafficPanel(TrafficDeadlockSimulator parent) {
            this.parent = parent;
            setBackground(new Color(34, 139, 34));

            // Initialize traffic lights (moved further to the sides)
            // Positions chosen to be noticeably away from the intersection
            topLight = new TrafficLight(centerX - 0, centerY - intersectionSize/2 - 110, "TOP");
            bottomLight = new TrafficLight(centerX - 0, centerY + intersectionSize/2 + 110, "BOTTOM");
            leftLight = new TrafficLight(centerX - intersectionSize/2 - 110, centerY - 0, "LEFT");
            rightLight = new TrafficLight(centerX + intersectionSize/2 + 110, centerY - 0, "RIGHT");

            // Mouse listener for traffic lights
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

                    // If any light turns green, clear deadlock overlay
                    if (topLight.isGreen || bottomLight.isGreen || leftLight.isGreen || rightLight.isGreen) {
                        deadlockDetected = false;
                        deadlockShownDialog = false;
                    }
                    repaint();
                }
            });

            // Animation timer
            timer = new Timer(30, e -> {
                updateCars();
                detectCollisions();
                repaint();
            });
            timer.start();
        }

        public void toggleAllLights() {
            // Toggle all lights to opposite state
            boolean newState = !topLight.isGreen;
            topLight.isGreen = newState;
            bottomLight.isGreen = newState;
            leftLight.isGreen = newState;
            rightLight.isGreen = newState;

            parent.log("🚦 ALL LIGHTS: " + (newState ? "GREEN ✅" : "RED 🛑"));

            // if turned green, clear deadlock
            if (newState) {
                deadlockDetected = false;
                deadlockShownDialog = false;
            }
            repaint();
        }

        public void addCar(String direction) {
            // Banker's Algorithm Check
            if (parent.isBankersEnabled()) {
                if (!isSafeToAddCar(direction)) {
                    parent.log("🛑 BANKER'S: Unsafe to add car from " + direction);
                    JOptionPane.showMessageDialog(parent,
                            "Banker's Algorithm prevented deadlock!\n" +
                                    "Adding this car would create an unsafe state.",
                            "Deadlock Avoidance",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // choose random lane: -1 or +1
            int laneChoice = rand.nextBoolean() ? -1 : 1;
            cars.add(new Car(carIdCounter++, direction, laneChoice));
            parent.log("🚗 Car #" + (carIdCounter - 1) + " added from " + direction + " (lane " + (laneChoice == -1 ? "A" : "B") + ")");
        }

        private boolean isSafeToAddCar(String direction) {
            int topCount = 0, bottomCount = 0, leftCount = 0, rightCount = 0;

            for (Car car : cars) {
                if (car.direction.equals("TOP")) topCount++;
                if (car.direction.equals("BOTTOM")) bottomCount++;
                if (car.direction.equals("LEFT")) leftCount++;
                if (car.direction.equals("RIGHT")) rightCount++;
            }

            if (direction.equals("TOP")) topCount++;
            if (direction.equals("BOTTOM")) bottomCount++;
            if (direction.equals("LEFT")) leftCount++;
            if (direction.equals("RIGHT")) rightCount++;

            boolean topRed = !topLight.isGreen;
            boolean bottomRed = !bottomLight.isGreen;
            boolean leftRed = !leftLight.isGreen;
            boolean rightRed = !rightLight.isGreen;

            int dangerousDirections = 0;
            if (topCount >= 1 && topRed) dangerousDirections++;
            if (bottomCount >= 1 && bottomRed) dangerousDirections++;
            if (leftCount >= 1 && leftRed) dangerousDirections++;
            if (rightCount >= 1 && rightRed) dangerousDirections++;

            return dangerousDirections < 4;
        }

        public String checkBankersSafeState() {
            StringBuilder sb = new StringBuilder();
            sb.append("🛡️ BANKER'S ALGORITHM CHECK\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");

            int topCount = 0, bottomCount = 0, leftCount = 0, rightCount = 0;
            int topWaiting = 0, bottomWaiting = 0, leftWaiting = 0, rightWaiting = 0;

            for (Car car : cars) {
                if (car.direction.equals("TOP")) {
                    topCount++;
                    if (car.isWaitingAtLight()) topWaiting++;
                }
                if (car.direction.equals("BOTTOM")) {
                    bottomCount++;
                    if (car.isWaitingAtLight()) bottomWaiting++;
                }
                if (car.direction.equals("LEFT")) {
                    leftCount++;
                    if (car.isWaitingAtLight()) leftWaiting++;
                }
                if (car.direction.equals("RIGHT")) {
                    rightCount++;
                    if (car.isWaitingAtLight()) rightWaiting++;
                }
            }

            sb.append("Resource Status:\n");
            sb.append("TOP: " + topCount + " cars (" + topWaiting + " waiting)\n");
            sb.append("BOTTOM: " + bottomCount + " cars (" + bottomWaiting + " waiting)\n");
            sb.append("LEFT: " + leftCount + " cars (" + leftWaiting + " waiting)\n");
            sb.append("RIGHT: " + rightCount + " cars (" + rightWaiting + " waiting)\n\n");

            int waitingDirections = 0;
            if (topWaiting > 0 && !topLight.isGreen) waitingDirections++;
            if (bottomWaiting > 0 && !bottomLight.isGreen) waitingDirections++;
            if (leftWaiting > 0 && !leftLight.isGreen) waitingDirections++;
            if (rightWaiting > 0 && !rightLight.isGreen) waitingDirections++;

            if (waitingDirections >= 4) {
                sb.append("⚠️ STATE: UNSAFE\n");
                sb.append("System is in potential deadlock state!\n");
            } else {
                sb.append("✅ STATE: SAFE\n");
                sb.append("System can proceed safely.\n");
            }

            return sb.toString();
        }

        public String detectDeadlockRAG() {
            StringBuilder sb = new StringBuilder();
            sb.append("🔍 RAG DEADLOCK DETECTION\n");
            sb.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");

            Map<String, List<Car>> waitingCars = new HashMap<>();
            waitingCars.put("TOP", new ArrayList<>());
            waitingCars.put("BOTTOM", new ArrayList<>());
            waitingCars.put("LEFT", new ArrayList<>());
            waitingCars.put("RIGHT", new ArrayList<>());

            for (Car car : cars) {
                if (car.isWaitingAtLight()) {
                    waitingCars.get(car.direction).add(car);
                }
            }

            sb.append("Resource Allocation Graph:\n");
            for (String dir : new String[]{"TOP", "BOTTOM", "LEFT", "RIGHT"}) {
                List<Car> waiting = waitingCars.get(dir);
                if (!waiting.isEmpty()) {
                    sb.append(dir + " → [");
                    for (int i = 0; i < waiting.size(); i++) {
                        sb.append("Car#" + waiting.get(i).id);
                        if (i < waiting.size() - 1) sb.append(", ");
                    }
                    sb.append("]\n");
                }
            }
            sb.append("\n");

            boolean allRed = !topLight.isGreen && !bottomLight.isGreen &&
                    !leftLight.isGreen && !rightLight.isGreen;

            int directionsWithWaiting = 0;
            for (List<Car> waiting : waitingCars.values()) {
                if (!waiting.isEmpty()) directionsWithWaiting++;
            }

            if (allRed && directionsWithWaiting == 4) {
                sb.append("🚨 DEADLOCK DETECTED!\n");
                sb.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");
                sb.append("Circular wait detected:\n");
                sb.append("• All 4 directions have waiting cars\n");
                sb.append("• All traffic lights are RED\n");
                sb.append("• No car can proceed\n\n");
                sb.append("SOLUTION:\n");
                sb.append("Turn at least one light GREEN!\n");

                JOptionPane.showMessageDialog(parent,
                        "🚨 DEADLOCK DETECTED!\n\n" +
                                "RAG Analysis shows circular wait:\n" +
                                "All cars are blocked waiting for resources.\n\n" +
                                "Solution: Turn one light GREEN to break the cycle!",
                        "Deadlock Found - RAG Detection",
                        JOptionPane.ERROR_MESSAGE);
                deadlockDetected = true;
            } else {
                sb.append("✅ NO DEADLOCK\n");
                sb.append("System is progressing normally.\n");
            }

            return sb.toString();
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

        // collision detection between car rectangles => set deadlock overlay
        private void detectCollisions() {
            // If already detected, keep overlay unless a light becomes green
            if (deadlockDetected) {
                return;
            }

            for (int i = 0; i < cars.size(); i++) {
                Car a = cars.get(i);
                Rectangle ra = a.getBounds();
                for (int j = i + 1; j < cars.size(); j++) {
                    Car b = cars.get(j);
                    Rectangle rb = b.getBounds();
                    if (ra.intersects(rb)) {
                        // Collision/overlap detected — show deadlock overlay
                        deadlockDetected = true;
                        parent.log("🚨 COLLISION / DEADLOCK: Car#" + a.id + " & Car#" + b.id);
                        // show a dialog once too
                        if (!deadlockShownDialog) {
                            JOptionPane.showMessageDialog(parent,
                                    "🚨 DEADLOCK / COLLISION DETECTED!\n\n" +
                                            "Cars overlapped. Overlay showing \"DEADLOCK\".",
                                    "Deadlock Detected",
                                    JOptionPane.ERROR_MESSAGE);
                            deadlockShownDialog = true;
                        }
                        return;
                    }
                }
            }

            // also set deadlock if all four directions waiting and all lights red
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
                            "🚨 DEADLOCK DETECTED (circular wait)!\n\n" +
                                    "All directions waiting and all lights RED.",
                            "Deadlock Detected",
                            JOptionPane.ERROR_MESSAGE);
                    deadlockShownDialog = true;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw roads
            g2.setColor(new Color(60, 60, 60));
            // Vertical road (2-way)
            g2.fillRect(centerX - roadWidth / 2, 0, roadWidth, getHeight());
            // Horizontal road (2-way)
            g2.fillRect(0, centerY - roadWidth / 2, getWidth(), roadWidth);

            // Draw intersection
            g2.setColor(new Color(70, 70, 70));
            g2.fillRect(centerX - intersectionSize / 2, centerY - intersectionSize / 2,
                    intersectionSize, intersectionSize);

            // Draw road markings (center yellow line)
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{20, 20}, 0));

            // Vertical center line
            g2.drawLine(centerX, 0, centerX, centerY - intersectionSize / 2 - 5);
            g2.drawLine(centerX, centerY + intersectionSize / 2 + 5, centerX, getHeight());

            // Horizontal center line
            g2.drawLine(0, centerY, centerX - intersectionSize / 2 - 5, centerY);
            g2.drawLine(centerX + intersectionSize / 2 + 5, centerY, getWidth(), centerY);

            // Draw traffic lights (now moved to sides)
            topLight.draw(g2);
            bottomLight.draw(g2);
            leftLight.draw(g2);
            rightLight.draw(g2);

            // Draw cars
            for (Car car : cars) {
                car.draw(g2);
            }

            // Draw info
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString("Cars: " + cars.size(), 10, 20);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString("Banker's: " + (parent.isBankersEnabled() ? "ON" : "OFF"), 10, 40);
            g2.drawString("2-Way Roads", 10, 60);

            // Deadlock overlay panel
            if (deadlockDetected) {
                Composite old = g2.getComposite();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
                g2.setColor(Color.RED);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setComposite(old);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 72));
                String text = "DEADLOCK";
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = (getHeight() / 2) - fm.getHeight() / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);

                g2.setFont(new Font("Arial", Font.BOLD, 20));
                String sub = "Turn a light GREEN to resolve";
                FontMetrics fm2 = g2.getFontMetrics();
                int sx = (getWidth() - fm2.stringWidth(sub)) / 2;
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
                // Black box background
                g2.setColor(Color.BLACK);
                g2.fillRoundRect(x - size / 2, y - size / 2, size, size, 8, 8);

                // Light
                g2.setColor(isGreen ? new Color(0, 255, 0) : new Color(255, 0, 0));
                g2.fillOval(x - size / 2 + 6, y - size / 2 + 6, size - 12, size - 12);

                // Border
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(x - size / 2, y - size / 2, size, size, 8, 8);

                // Label
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
            String allocatedResource = null;
            int lane; // -1 or +1

            public Car(int id, String direction, int lane) {
                this.id = id;
                this.direction = direction;
                this.lane = lane;

                color = new Color(
                        100 + rand.nextInt(155),
                        100 + rand.nextInt(155),
                        100 + rand.nextInt(155)
                );

                // Starting position (2-way roads), choose lane offset
                int offset = lane * (laneWidth / 2 + 8); // offset from center lane
                switch (direction) {
                    case "TOP":
                        x = centerX + offset; // two lanes horizontally
                        y = -30;
                        break;
                    case "BOTTOM":
                        x = centerX - offset; // mirror lane so cars face up
                        y = getHeight() + 30;
                        break;
                    case "LEFT":
                        x = -30;
                        y = centerY + offset; // two lanes vertically
                        break;
                    case "RIGHT":
                        x = getWidth() + 30;
                        y = centerY - offset; // mirror lane so cars face left
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

            public void update() {
                if (completed) return;

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

                switch (direction) {
                    case "TOP":
                        if (y < centerY - intersectionSize / 2 - 40) {
                            y += speed;
                        } else if (y < centerY - intersectionSize / 2 - 30) {
                            if (canGo) {
                                y += speed;
                                inIntersection = true;
                                waiting = false;
                            } else {
                                waiting = true;
                            }
                        } else if (y < getHeight()) {
                            y += speed;
                            // leave intersection after passing center
                            if (y > centerY + intersectionSize / 2 + 80) inIntersection = false;
                        } else {
                            completed = true;
                        }
                        break;

                    case "BOTTOM":
                        if (y > centerY + intersectionSize / 2 + 40) {
                            y -= speed;
                        } else if (y > centerY + intersectionSize / 2 + 30) {
                            if (canGo) {
                                y -= speed;
                                inIntersection = true;
                                waiting = false;
                            } else {
                                waiting = true;
                            }
                        } else if (y > 0) {
                            y -= speed;
                            if (y < centerY - intersectionSize / 2 - 80) inIntersection = false;
                        } else {
                            completed = true;
                        }
                        break;

                    case "LEFT":
                        if (x < centerX - intersectionSize / 2 - 40) {
                            x += speed;
                        } else if (x < centerX - intersectionSize / 2 - 30) {
                            if (canGo) {
                                x += speed;
                                inIntersection = true;
                                waiting = false;
                            } else {
                                waiting = true;
                            }
                        } else if (x < getWidth()) {
                            x += speed;
                            if (x > centerX + intersectionSize / 2 + 80) inIntersection = false;
                        } else {
                            completed = true;
                        }
                        break;

                    case "RIGHT":
                        if (x > centerX + intersectionSize / 2 + 40) {
                            x -= speed;
                        } else if (x > centerX + intersectionSize / 2 + 30) {
                            if (canGo) {
                                x -= speed;
                                inIntersection = true;
                                waiting = false;
                            } else {
                                waiting = true;
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
                    // Horizontal orientation
                    g2.setColor(color);
                    g2.fillRoundRect((int) x - 20, (int) y - 12, 40, 24, 8, 8);
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect((int) x - 20, (int) y - 12, 40, 24, 8, 8);

                    g2.setColor(new Color(100, 150, 200, 150));
                    g2.fillRect((int) x - 5, (int) y - 8, 10, 16);
                } else {
                    // Vertical orientation
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
