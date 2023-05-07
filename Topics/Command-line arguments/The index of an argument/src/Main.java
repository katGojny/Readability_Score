class Problem {
    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++) {
            if (args[i].contains("test")) {
                System.out.println(i);
                break;
            } else if (!args[args.length - 1].contains("test")) {
                System.out.println(-1);
                break;
            }
        }
    }
}