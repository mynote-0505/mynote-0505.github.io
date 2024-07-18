package org.poi;

import java.util.*;

class User {
    protected String username;
    protected String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}

class Customer extends User {
    private List<String> shoppingCart = new ArrayList<>();
    private List<String> shoppingHistory = new ArrayList<>();

    public Customer(String username, String password) {
        super(username, password);
    }

    public List<String> getShoppingCart() {
        return shoppingCart;
    }

    public List<String> getShoppingHistory() {
        return shoppingHistory;
    }

    public void addToCart(String product) {
        shoppingCart.add(product);
    }

    public void removeFromCart(String product) {
        shoppingCart.remove(product);
    }

    public void checkout() {
        shoppingHistory.addAll(shoppingCart);
        shoppingCart.clear();
    }
}

class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }
}

class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " - $" + price;
    }
}

public class ShoppingSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Customer> customers = new HashMap<>();
    private static Map<String, Admin> admins = new HashMap<>();
    private static List<Product> products = new ArrayList<>();
    private static User loggedInUser = null;

    public static void main(String[] args) {
        initializeData();
        while (true) {
            if (loggedInUser == null) {
                showMainMenu();
            } else if (loggedInUser instanceof Admin) {
                showAdminMenu();
            } else if (loggedInUser instanceof Customer) {
                showCustomerMenu();
            }
        }
    }

    private static void initializeData() {
        admins.put("admin", new Admin("admin", "admin123"));
        products.add(new Product("Apple", 1.0));
        products.add(new Product("Banana", 0.5));
    }

    private static void showMainMenu() {
        System.out.println("1. 注册");
        System.out.println("2. 登录");
        System.out.print("请选择操作: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            default:
                System.out.println("无效的选择");
        }
    }

    private static void register() {
        System.out.print("输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("输入密码: ");
        String password = scanner.nextLine();

        if (customers.containsKey(username)) {
            System.out.println("用户名已存在");
        } else {
            customers.put(username, new Customer(username, password));
            System.out.println("注册成功");
        }
    }

    private static void login() {
        System.out.print("输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("输入密码: ");
        String password = scanner.nextLine();

        if (admins.containsKey(username) && admins.get(username).checkPassword(password)) {
            loggedInUser = admins.get(username);
            System.out.println("管理员登录成功");
        } else if (customers.containsKey(username) && customers.get(username).checkPassword(password)) {
            loggedInUser = customers.get(username);
            System.out.println("用户登录成功");
        } else {
            System.out.println("用户名或密码错误");
        }
    }

    private static void showAdminMenu() {
        System.out.println("1. 用户密码管理");
        System.out.println("2. 客户管理");
        System.out.println("3. 商品管理");
        System.out.println("4. 退出登录");
        System.out.print("请选择操作: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                manageUserPasswords();
                break;
            case 2:
                manageCustomers();
                break;
            case 3:
                manageProducts();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("无效的选择");
        }
    }

    private static void manageUserPasswords() {
        System.out.println("1. 重置普通用户密码");
        System.out.println("2. 修改自身密码");
        System.out.print("请选择操作: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                resetCustomerPassword();
                break;
            case 2:
                changeAdminPassword();
                break;
            default:
                System.out.println("无效的选择");
        }
    }

    private static void resetCustomerPassword() {
        System.out.print("输入要重置密码的客户用户名: ");
        String username = scanner.nextLine();

        if (customers.containsKey(username)) {
            System.out.print("输入新密码: ");
            String newPassword = scanner.nextLine();
            customers.get(username).setPassword(newPassword);
            System.out.println("密码重置成功");
        } else {
            System.out.println("客户不存在");
        }
    }

    private static void changeAdminPassword() {
        System.out.print("输入新密码: ");
        String newPassword = scanner.nextLine();
        loggedInUser.setPassword(newPassword);
        System.out.println("密码修改成功");
    }

    private static void manageCustomers() {
        System.out.println("1. 列出所有客户信息");
        System.out.println("2. 删除客户信息");
        System.out.println("3. 查询客户信息");
        System.out.print("请选择操作: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                listAllCustomers();
                break;
            case 2:
                deleteCustomer();
                break;
            case 3:
                queryCustomer();
                break;
            default:
                System.out.println("无效的选择");
        }
    }

    private static void listAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("没有客户信息");
        } else {
            customers.forEach((username, customer) -> System.out.println(username));
        }
    }

    private static void deleteCustomer() {
        System.out.print("输入要删除的客户用户名: ");
        String username = scanner.nextLine();

        if (customers.containsKey(username)) {
            customers.remove(username);
            System.out.println("客户删除成功");
        } else {
            System.out.println("客户不存在");
        }
    }

    private static void queryCustomer() {
        System.out.print("输入要查询的客户用户名: ");
        String username = scanner.nextLine();

        if (customers.containsKey(username)) {
            System.out.println("客户存在: " + username);
        } else {
            System.out.println("客户不存在");
        }
    }

    private static void manageProducts() {
        System.out.println("1. 列出所有商品信息");
        System.out.println("2. 添加商品信息");
        System.out.println("3. 修改商品信息");
        System.out.println("4. 删除商品信息");
        System.out.println("5. 查询商品信息");
        System.out.print("请选择操作: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                listAllProducts();
                break;
            case 2:
                addProduct();
                break;
            case 3:
                editProduct();
                break;
            case 4:
                deleteProduct();
                break;
            case 5:
                queryProduct();
                break;
            default:
                System.out.println("无效的选择");
        }
    }

    private static void listAllProducts() {
        if (products.isEmpty()) {
            System.out.println("没有商品信息");
        } else {
            products.forEach(product -> System.out.println(product));
        }
    }

    private static void addProduct() {
        System.out.print("输入商品名称: ");
        String name = scanner.nextLine();
        System.out.print("输入商品价格: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        products.add(new Product(name, price));
        System.out.println("商品添加成功");
    }

    private static void editProduct() {
        System.out.print("输入要修改的商品名称: ");
        String name = scanner.nextLine();

        for (Product product : products) {
            if (product.getName().equals(name)) {
                System.out.print("输入新的商品名称: ");
                String newName = scanner.nextLine();
                System.out.print("输入新的商品价格: ");
                double newPrice = scanner.nextDouble();
                scanner.nextLine(); // consume newline

                product.setName(newName);
                product.setPrice(newPrice);
                System.out.println("商品修改成功");
                return;
            }
        }
        System.out.println("商品不存在");
    }

    private static void deleteProduct() {
        System.out.print("输入要删除的商品名称: ");
        String name = scanner.nextLine();

        for (Iterator<Product> iterator = products.iterator(); iterator.hasNext(); ) {
            Product product = iterator.next();
            if (product.getName().equals(name)) {
                iterator.remove();
                System.out.println("商品删除成功");
                return;
            }
        }
        System.out.println("商品不存在");
    }

    private static void queryProduct() {
        System.out.print("输入要查询的商品名称: ");
        String name = scanner.nextLine();

        for (Product product : products) {
            if (product.getName().equals(name)) {
                System.out.println("商品存在: " + product);
                return;
            }
        }
        System.out.println("商品不存在");
    }

    private static void showCustomerMenu() {
        System.out.println("1. 修改密码");
        System.out.println("2. 重置密码");
        System.out.println("3. 购物");
        System.out.println("4. 退出登录");
        System.out.print("请选择操作: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        switch (choice) {
            case 1:
                changeCustomerPassword();
                break;
            case 2:
                resetPassword();
                break;
            case 3:
                shopping();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("无效的选择");
        }
    }

    private static void changeCustomerPassword() {
        System.out.print("输入新密码: ");
        String newPassword = scanner.nextLine();
        loggedInUser.setPassword(newPassword);
        System.out.println("密码修改成功");
    }

    private static void resetPassword() {
        System.out.print("输入用户名: ");
        String username = scanner.nextLine();

        if (customers.containsKey(username)) {
            System.out.print("输入新密码: ");
            String newPassword = scanner.nextLine();
            customers.get(username).setPassword(newPassword);
            System.out.println("密码重置成功");
        } else {
            System.out.println("用户不存在");
        }
    }

    private static void shopping() {
        Customer customer = (Customer) loggedInUser;
        while (true) {
            System.out.println("1. 添加商品到购物车");
            System.out.println("2. 从购物车移除商品");
            System.out.println("3. 查看购物车");
            System.out.println("4. 结账");
            System.out.println("5. 查看购物历史");
            System.out.println("6. 返回");
            System.out.print("请选择操作: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addToCart(customer);
                    break;
                case 2:
                    removeFromCart(customer);
                    break;
                case 3:
                    viewCart(customer);
                    break;
                case 4:
                    checkout(customer);
                    break;
                case 5:
                    viewHistory(customer);
                    break;
                case 6:
                    return;
                default:
                    System.out.println("无效的选择");
            }
        }
    }

    private static void addToCart(Customer customer) {
        System.out.print("输入要添加到购物车的商品名称: ");
        String product = scanner.nextLine();
        customer.addToCart(product);
        System.out.println("商品已添加到购物车");
    }

    private static void removeFromCart(Customer customer) {
        System.out.print("输入要从购物车移除的商品名称: ");
        String product = scanner.nextLine();
        customer.removeFromCart(product);
        System.out.println("商品已从购物车移除");
    }

    private static void viewCart(Customer customer) {
        List<String> cart = customer.getShoppingCart();
        if (cart.isEmpty()) {
            System.out.println("购物车是空的");
        } else {
            System.out.println("购物车中的商品:");
            cart.forEach(System.out::println);
        }
    }

    private static void checkout(Customer customer) {
        customer.checkout();
        System.out.println("结账完成");
    }

    private static void viewHistory(Customer customer) {
        List<String> history = customer.getShoppingHistory();
        if (history.isEmpty()) {
            System.out.println("没有购物历史");
        } else {
            System.out.println("购物历史:");
            history.forEach(System.out::println);
        }
    }

    private static void logout() {
        loggedInUser = null;
        System.out.println("已退出登录");
    }
}
