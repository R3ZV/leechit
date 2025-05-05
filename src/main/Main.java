import  bencode.Decoder;

public  class Main {
    public static void main(String[] args) throws Exception {
        String command = args[0];

        if (command.equals("decode")) {
            Decoder decoder = new Decoder(args[1]);
            String decoded = decoder.next();

            System.out.println(decoded);
        } else {
            System.out.println("Command '{}' is not supported");
        }
    }
}
