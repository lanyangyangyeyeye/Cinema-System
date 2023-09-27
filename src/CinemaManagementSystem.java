import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

// 电影类
class Movie {
    public static String showtime;
    private final int id;
    private final String name;
    private final double price;
    private final int duration;
    private final int hallnumber;

    public Movie(int id, String name, double price, int duration, int hallnumber, String showtime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.duration = duration;
        Movie.showtime = showtime;
        this.hallnumber = hallnumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public int getHallNumber() {
        return hallnumber;
    }

    public String getShowTime() {
        return showtime;
    }

    public int getAvailableSeats() {
        return 50;
    }

    public void setAvailableSeats() {
    }

    public char[][] getSeatMap() {
        char[][] seatMap = new char[5][10];
        for (char[] chars : seatMap) {
            Arrays.fill(chars, 'O');
        }
        // 填充座位状态，根据实际情况设置 'O' 和 'X'
        // 例如，'O' 表示座位可用，'X' 表示座位已被预订或占用
        // 这里根据实际情况设置座位状态
        return seatMap;
    }

    public int getTotalSeats() {
        return 50;
    }
}

class Order {
    private final String name;
    private final String purchaseTime;
    private final double price;
    private final String showtime;

    public Order(String name, String showtime, double price, String purchaseTime) {
        this.name = name;
        this.showtime = showtime;
        this.price = price;
        // 获取当前时间作为购票时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.purchaseTime = dateFormat.format(new Date());
    }

    @Override
    public String toString() {
        return "电影名：" + name + "\n场次：" + showtime + "\n价格：" + price + "\n购票时间：" + purchaseTime;
    }

    public Object getName() {
        return name;
    }

    public Object getShowtime() {
        return showtime;
    }
}

// 主程序
public class CinemaManagementSystem {
    // 获取当前日期时间
    static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private static final ArrayList<Order> orders = new ArrayList<>();
    private static final ArrayList<Movie> movies = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("欢迎使用影城管理系统");
            System.out.println("1. 用户登录");
            System.out.println("2. 用户注册");
            System.out.println("3. 退出");
            System.out.print("请选择操作：");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("感谢使用影城管理系统，再见！");
                    break;
                default:
                    System.out.println("无效的选择，请重新选择。");
            }
        } while (choice != 3);
    }

    private static void login() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入用户名：");
            String username = scanner.nextLine();
            System.out.print("请输入密码：");
            String password = scanner.nextLine();

            if (username.equals("admin") && password.equals("ynuinfo#777")) {
                System.out.println("管理员登录成功！");
                adminMenu();
            } else if (validateUser(username, password)) {
                System.out.println("用户登录成功！");
                userMenu();
            } else {
                System.out.println("登录失败：用户名或密码不正确。");
            }
        } catch (IOException e) {
            System.out.println("登录失败：" + e.getMessage());
        }
    }

    private static boolean validateUser(String username, String password) throws IOException {
        // 打开文件以读取用户信息
        FileReader fileReader = new FileReader("users.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;

        if ((line = bufferedReader.readLine()) != null) {
            do {
                String[] userInfo = line.split(" ");
                if (userInfo.length == 2 && userInfo[0].equals(username) && userInfo[1].equals(password)) {
                    bufferedReader.close();
                    return true;
                }
            } while ((line = bufferedReader.readLine()) != null);
        }

        bufferedReader.close();
        return false;
    }

    private static void register() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入用户名：");
            String username = scanner.nextLine();
            System.out.print("请输入密码：");
            String password = scanner.nextLine();

            // 打开文件以追加方式写入新用户信息
            FileWriter fileWriter = new FileWriter("users.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // 将新用户信息写入文件
            bufferedWriter.write(username + " " + password);
            bufferedWriter.newLine();
            bufferedWriter.close();

            System.out.println("注册成功！");
        } catch (IOException e) {
            System.out.println("注册失败：" + e.getMessage());
        }
    }

    private static void userMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("用户菜单");
            System.out.println("1. 查看电影");
            System.out.println("2. 订票选座");
            System.out.println("3. 退票");
            System.out.println("4. 返回上级菜单");
            System.out.print("请选择操作：");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    alreadyrelaseMovies();
                    break;
                case 2:
                    bookTicket();
                    break;
                case 3:
                    cancelTicket();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("无效的选择，请重新选择。");
            }
        } while (true);
    }

    private static void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("管理员菜单");
            System.out.println("1. 上架影片");
            System.out.println("2. 下架影片");
            System.out.println("3. 查询影片");
            System.out.println("4. 查询订单");
            System.out.println("5. 返回上级菜单");
            System.out.print("请选择操作：");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    removeMovie();
                    break;
                case 3:
                    displayMovies();
                    break;
                case 4:
                    searchorder();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("无效的选择，请重新选择。");
            }
        } while (true);
    }

    private static void searchorder() {
        System.out.println("订单列表：");
        for (Order order : orders) {
            System.out.println(order.toString());
            System.out.println("------------------");
        }
    }

    private static void displayMovies() {
        System.out.println("电影列表：");
        for (Movie movie : movies) {
            System.out.println("ID: " + movie.getId());
            System.out.println("片名: " + movie.getName());
            System.out.println("票价: " + movie.getPrice());
            System.out.println("时长: " + movie.getDuration() + "分钟");
            System.out.println("------------------");
        }
    }

    private static void alreadyrelaseMovies() {
        System.out.println("已安排放映的电影列表：");
        for (Movie movie : movies) {
            System.out.println("ID: " + movie.getId());
            System.out.println("片名: " + movie.getName());
            System.out.println("票价: " + movie.getPrice());
            System.out.println("时长: " + movie.getDuration() + "分钟");
            System.out.println("演播厅号: " + movie.getHallNumber());
            System.out.println("播放时间: " + movie.getShowTime());
            System.out.println("------------------");
        }
    }

    private static void bookTicket() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入电影片名：");
        String movieName = scanner.nextLine();
        System.out.print("请输入场次：");
        String showTime = scanner.nextLine();

        // 查找电影
        Movie selectedMovie = findMovieByNameAndShowTime(movieName, showTime);

        if (selectedMovie != null) {
            int totalSeats = selectedMovie.getTotalSeats();
            int availableSeats = selectedMovie.getAvailableSeats();
            char[][] seatMap = selectedMovie.getSeatMap();

            // 显示座位信息
            System.out.println("座位信息：");
            System.out.println("总座位数：" + totalSeats);
            System.out.println("空闲座位数：" + availableSeats);
            System.out.println("座位状态：");

            for (char[] chars : seatMap) {
                for (int col = 0; col < chars.length; col++) {
                    System.out.print(chars[col] + " ");
                }
                System.out.println(); // 换行
            }

            // 让用户选择座位
            System.out.print("请选择座位（行 列）或输入 '取消' 取消预订：");
            String input = scanner.nextLine();
            if (!input.equalsIgnoreCase("取消")) {
                String[] parts = input.split(" ");
                if (parts.length == 2) {
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    // 检查座位是否可用
                    if (row >= 0 && row < seatMap.length && col >= 0 && col < seatMap[row].length &&
                            seatMap[row][col] == 'O') {
                        // 预订座位
                        seatMap[row][col] = 'X';
                        selectedMovie.setAvailableSeats();
                        System.out.println("座位预订成功！");
                        Order order = new Order(movieName, showTime, selectedMovie.getPrice(), getCurrentDateTime());
                        orders.add(order);
                    } else {
                        System.out.println("无效的座位选择或座位已被预订。");
                    }
                } else {
                    System.out.println("无效的输入格式。");
                }
            }
        } else {
            System.out.println("未找到匹配的电影信息。");
        }
    }

    private static Movie findMovieByNameAndShowTime(String movieName, String showTime) {
        // 在电影列表中查找匹配的电影
        for (Movie movie : movies) {
            if (movie.getName().equals(movieName) && movie.getShowTime().equals(showTime)) {
                return movie;
            }
        }
        return null;
    }

    private static void cancelTicket() {
        // 显示用户已购买的电影票信息
        System.out.println("您已购买的电影票信息：");
        for (Order order : orders) {
            System.out.println(order.toString());
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要退的电影名字：");
        String movieName = scanner.nextLine();
        System.out.print("请输入要退的电影时间：");
        String showTime = scanner.nextLine();

        // 遍历订单列表查找匹配的订单
        Order foundOrder = null;
        for (Order order : orders) {
            if (order.getName().equals(movieName) && order.getShowtime().equals(showTime)) {
                foundOrder = order;
                break;
            }
        }

        if (foundOrder != null) {
            // 找到订单，执行退票操作
            orders.remove(foundOrder);
            System.out.println("退票成功！");
            // 还需要将座位状态恢复为可用，你可以根据订单信息找到对应的电影和座位进行处理
        } else {
            System.out.println("未找到匹配的订单，退票失败。");
        }
    }

    private static void addMovie() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入电影ID: ");
        int id = scanner.nextInt();
        System.out.print("请输入电影片名: ");
        String name = scanner.next();
        System.out.print("请输入票价: ");
        double price = scanner.nextDouble();
        System.out.print("请输入时长（分钟）: ");
        int duration = scanner.nextInt();
        System.out.print("请输入演播厅号: ");
        int hallnumber = scanner.nextInt();
        System.out.print("请输入播放时间（例如：2023-09-15-19:00）: ");
        String showtime = scanner.next();
        Movie movie = new Movie(id, name, price, duration, hallnumber, showtime);
        movies.add(movie);
        System.out.println("电影上架成功！");
    }

    private static void removeMovie() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入要下架的电影名: ");
        String movieName = scanner.nextLine();

        // 寻找电影名字匹配的电影并删除
        boolean removed = false;
        for (Movie movie : movies) {
            if (movie.getName().equalsIgnoreCase(movieName)) {
                movies.remove(movie);
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("电影下架成功！");
        } else {
            System.out.println("未找到匹配的电影，下架失败。");
        }
    }
}
