package com.pl.pizzastore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PizzaStoreApp extends JFrame {
    private List<Pizza> menuList;
    private Map<String, String> imageMap;
    private Order currentOrder = new Order();
    private DefaultTableModel cartTableModel;
    private JTable cartTable;
    private JLabel lblTotal;
    private JPanel productPanel;

    public PizzaStoreApp() {
        // Khởi tạo menu và map hình ảnh
        menuList = new ArrayList<>(Arrays.asList(getMenuItems()));
        imageMap = new HashMap<>();
        imageMap.put("Margherita", "62838e56acf96.png");
        imageMap.put("Pepperoni", "682bf0329f556.png");
        imageMap.put("Hawaiian", "pizza.png");
        imageMap.put("Vegetarian", "—Pngtree—pizza_1466901.png");
        imageMap.put("BBQ Chicken", "pizza-1344720_1280.jpg");
        imageMap.put("Four Cheese", "cheese-1869708_1280.jpg");
        imageMap.put("Seafood Deluxe", "pizza-2589575_1280.jpg");
        imageMap.put("Mexican Spicy", "pizza-3010062_1280.jpg");
        imageMap.put("Veggie Supreme", "pizza-8294340_1280.png");
        imageMap.put("Meat Lovers", "salami-pizza-6593465_1280.jpg");

        setTitle("P&L Pizza Store");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Giao diện Nimbus
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ignored) {}

        ((JComponent) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout(10, 10));

        // Header banner nổi bật
        JLabel lblBanner = new JLabel();
        ImageIcon bannerIcon = loadIcon("/images/poster.png");
        if (bannerIcon != null) {
            Image img = bannerIcon.getImage().getScaledInstance(getWidth() - 20, 180, Image.SCALE_SMOOTH);
            lblBanner.setIcon(new ImageIcon(img));
        } else {
            lblBanner.setText("P&L Pizza Store");
            lblBanner.setHorizontalAlignment(SwingConstants.CENTER);
            lblBanner.setFont(new Font("SansSerif", Font.BOLD, 32));
            lblBanner.setOpaque(true);
            lblBanner.setBackground(new Color(200, 20, 20));
            lblBanner.setForeground(Color.WHITE);
            lblBanner.setPreferredSize(new Dimension(getWidth(), 180));
        }

        // Thanh công cụ: tìm kiếm + thêm/xóa menu
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(new JLabel("Tìm kiếm: "));
        JTextField tfSearch = new JTextField(20);
        toolBar.add(tfSearch);
        JButton btnSearch = new JButton("Tìm");
        toolBar.add(btnSearch);
        toolBar.addSeparator();
        JButton btnAddMenu = new JButton("Thêm loại");
        toolBar.add(btnAddMenu);
        JButton btnRemoveMenu = new JButton("Xóa loại");
        toolBar.add(btnRemoveMenu);

        // Panel trên chứa banner và toolbar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblBanner, BorderLayout.NORTH);
        topPanel.add(toolBar, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Split pane chia menu và giỏ hàng
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        add(splitPane, BorderLayout.CENTER);

        // Panel menu: 3 sản phẩm mỗi hàng
        productPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productPanel.setBorder(new TitledBorder("Menu"));
        refreshMenu();
        splitPane.setLeftComponent(new JScrollPane(productPanel));

        // Panel giỏ hàng
        cartTableModel = new DefaultTableModel(new String[]{"Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(cartTableModel);
        JPanel cartPanel = new JPanel(new BorderLayout(5, 5));
        cartPanel.setBorder(new TitledBorder("Giỏ hàng"));
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel cartBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        JButton btnDelete = new JButton("Xóa");
        btnDelete.addActionListener(e -> deleteSelectedItem());
        cartBottom.add(btnDelete);
        lblTotal = new JLabel("Tổng: 0 đ");
        cartBottom.add(lblTotal);
        JButton btnCheckout = new JButton("Thanh toán");
        btnCheckout.addActionListener(e -> checkout());
        cartBottom.add(btnCheckout);
        cartPanel.add(cartBottom, BorderLayout.SOUTH);
        splitPane.setRightComponent(cartPanel);

        // Sự kiện nút
        btnSearch.addActionListener(e -> searchMenu(tfSearch.getText()));
        btnAddMenu.addActionListener(e -> addMenuItem());
        btnRemoveMenu.addActionListener(e -> removeMenuItem());
    }

    private void searchMenu(String kw) {
        productPanel.removeAll();
        for (Pizza p : menuList) {
            if (p.getName().toLowerCase().contains(kw.toLowerCase())) {
                productPanel.add(createProductCard(p));
            }
        }
        productPanel.revalidate(); productPanel.repaint();
    }

    private void refreshMenu() {
        productPanel.removeAll();
        for (Pizza p : menuList) productPanel.add(createProductCard(p));
        productPanel.revalidate(); productPanel.repaint();
    }

    private void addMenuItem() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Tên món:")); panel.add(nameField);
        panel.add(new JLabel("Giá (đ):")); panel.add(priceField);
        int res = JOptionPane.showConfirmDialog(this, panel, "Thêm loại pizza", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                menuList.add(new Pizza(name, price));
                imageMap.put(name, name.toLowerCase() + ".png");
                refreshMenu();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Giá không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removeMenuItem() {
        String[] names = menuList.stream().map(Pizza::getName).toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(this, "Chọn loại xóa:", "Xóa loại pizza",
                JOptionPane.PLAIN_MESSAGE, null, names, names.length > 0 ? names[0] : null);
        if (sel != null) {
            menuList.removeIf(p -> p.getName().equals(sel));
            imageMap.remove(sel);
            refreshMenu();
        }
    }

    private void deleteSelectedItem() {
        int row = cartTable.getSelectedRow();
        if (row >= 0) {
            currentOrder.getItems().remove(row);
            refreshCart();
        } else {
            JOptionPane.showMessageDialog(this, "Chọn mục để xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JPanel createProductCard(Pizza pizza) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(180, 300));
        card.setBorder(new TitledBorder(pizza.getName()));

        String fname = imageMap.getOrDefault(pizza.getName(), pizza.getName().toLowerCase() + ".png");
        ImageIcon icon = loadIcon("/images/" + fname);
        JLabel lblIcon = new JLabel(); lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        if (icon != null) {
            lblIcon.setIcon(new ImageIcon(icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH)));
        } else {
            lblIcon.setText("[No image]");
        }
        card.add(lblIcon, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new EmptyBorder(5, 5, 5, 5));
        JLabel lblPrice = new JLabel("Giá: " + pizza.getPrice() + " đ");
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT); bottom.add(lblPrice);
        bottom.add(Box.createVerticalStrut(5));
        JSpinner sp = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        sp.setMaximumSize(new Dimension(60, 25)); sp.setAlignmentX(Component.CENTER_ALIGNMENT); bottom.add(sp);
        bottom.add(Box.createVerticalStrut(5));
        JButton btnAdd = new JButton("Thêm vào giỏ"); btnAdd.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAdd.addActionListener(e -> { currentOrder.addItem(pizza, (int) sp.getValue()); refreshCart(); });
        bottom.add(btnAdd);

        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private ImageIcon loadIcon(String path) {
        try { return new ImageIcon(getClass().getResource(path)); } catch (Exception e) { return null; }
    }

    private void refreshCart() {
        cartTableModel.setRowCount(0);
        for (OrderItem it : currentOrder.getItems()) {
            cartTableModel.addRow(new Object[]{it.getPizza().getName(), it.getPizza().getPrice(), it.getQuantity(), it.getSubtotal()});
        }
        lblTotal.setText("Tổng: " + currentOrder.getTotal() + " đ");
    }

    private void checkout() {
        if (currentOrder.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!\nTổng: " + currentOrder.getTotal() + " đ");
            currentOrder.clear(); refreshCart();
        }
    }

    private Pizza[] getMenuItems() {
        return new Pizza[]{
            new Pizza("Margherita", 80000),
            new Pizza("Pepperoni", 90000),
            new Pizza("Hawaiian", 95000),
            new Pizza("Vegetarian", 85000),
            new Pizza("BBQ Chicken", 88000),
            new Pizza("Four Cheese", 100000),
            new Pizza("Seafood Deluxe", 120000),
            new Pizza("Mexican Spicy", 92000),
            new Pizza("Veggie Supreme", 87000),
            new Pizza("Meat Lovers", 115000)
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PizzaStoreApp().setVisible(true));
    }
}